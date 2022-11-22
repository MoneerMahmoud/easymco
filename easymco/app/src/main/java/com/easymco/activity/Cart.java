package com.easymco.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.easymco.R;
import com.easymco.activity.user.Login;
import com.easymco.adapter.CartAdapter;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.CartHandler;
import com.easymco.interfaces.Refresher;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.CartDataSet;
import com.easymco.models.FilterDataSet;
import com.easymco.models.ProductOptionDataSet;
import com.easymco.models.Response;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart extends AppCompatActivity implements Refresher, API_Result, CartHandler {

    private RecyclerView mCartProduct;
    private Button mBtnCheckOut;
    private ArrayList<CartDataSet> mDataSet = null;
    private ProgressBar progressBar;
    private HashMap<String, Object> product_data = new HashMap<>();
    private ArrayList<ProductOptionDataSet> mOptionList = new ArrayList<>();
    private HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChildHolder = new HashMap<>();
    private ArrayList<ProductOptionDataSet> temp = new ArrayList<>();
    private ArrayList<Integer> list;
    Refresher refresher;
    private Boolean checker = true;
    API_Result api_result;
    private String mCouponCode, mRewardPoints, mGiftVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        api_result = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        refresher = this;

        if (DataBaseHandlerDiscount.getInstance().get_coupon_code() != null)
            mCouponCode = DataBaseHandlerDiscount.getInstance().get_coupon_code();

        if (DataBaseHandlerDiscount.getInstance().get_reward_points() != null)
            mRewardPoints = DataBaseHandlerDiscount.getInstance().get_reward_points();

        if (DataBaseHandlerDiscount.getInstance().get_gift_voucher() != null)
            mGiftVoucher = DataBaseHandlerDiscount.getInstance().get_gift_voucher();

        list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (list != null) {
                post_calling();
            }
        } else {
            Intent intent = new Intent(Cart.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }

        mCartProduct = (RecyclerView) findViewById(R.id.cart_product_items);
        mBtnCheckOut = (Button) findViewById(R.id.cart_check_out);

        mBtnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataSet != null) {
                    if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                        if (!mBtnCheckOut.getText().toString().equals(getResources().getString(R.string._continue))) {
                            Intent intent = new Intent(Cart.this, Check_Out.class);
                            startActivity(intent);
                        } else {
                            Methods.toast(getResources().getString(R.string.quantity_error_message));
                        }
                    } else {
                        Intent intent = new Intent(Cart.this, Login.class);
                        startActivity(intent);
                    }
                } else {
                    onBackPressed();
                    finish();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base));
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(
                    "ar".equals(AppLanguageSupport.getLanguage(Cart.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public void post_calling() {
        progressBar.setVisibility(View.VISIBLE);
        String[] data = {URL_Class.mURL + URL_Class.mURL_Get_Cart_Product};
        new API_Get().get_method(data, api_result, getCartPostData(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "Cart");
    }

    public void dataSetting(String result) {
        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CART_DATA, result);
        mDataSet = GetJSONData.get_cart_detail(result);
        if (!stock_status()) {
            mBtnCheckOut.setText(R.string._continue);
        }
        if (mDataSet != null) {

            ArrayList<FilterDataSet> list = GetJSONData.get_cart_total_new(result);
            if (list != null) {
                mCartProduct.setLayoutManager(new LinearLayoutManager(this));
                mCartProduct.setAdapter(new CartAdapter(Cart.this, mDataSet, refresher, result, list));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CART_DATA);
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    public boolean check_choice(int index, Integer data[]) {
        boolean result = false;
        String product_detail = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductString(index);
        product_data = GetJSONData.getSeparateProductDetail(product_detail);
        if (product_data != null) {
            mOptionList = (ArrayList<ProductOptionDataSet>) product_data.get("Option");
            mOptionListChildHolder = (HashMap<Integer, ArrayList<ProductOptionDataSet>>) product_data.get("Option Child");
            for (int i = 0; i < mOptionList.size(); i++) {
                if (mOptionList.get(i).getProduct_option_type().equals("checkbox")) {
                    for (int j = 0; j < mOptionListChildHolder.get(i).size(); j++) {
                        temp = mOptionListChildHolder.get(i);
                        if (temp.get(j).getProduct_option_value_id().equals(String.valueOf(data[1])) && mOptionList.get(i).getProduct_option_id().equals(String.valueOf(data[0]))) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    public void refresh() {
        button_text_changer();
        list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();
        if (list != null) {
            if (list.size() != 0) {
                post_calling();
            } else {
                mCartProduct.setAdapter(new CartAdapter(Cart.this, null, refresher, null, null));
                mDataSet = null;
                checker = false;
                button_text_changer();
            }
        } else {
            mCartProduct.setAdapter(new CartAdapter(Cart.this, null, refresher, null, null));
            mDataSet = null;
            checker = false;
            button_text_changer();
        }
    }

    public void button_text_changer() {
        if (!checker) {
            mBtnCheckOut.setText(R.string._continue);
        } else {
            mBtnCheckOut.setText(R.string.checkout);
        }
    }

    @Override
    public void refresher() {
        refresh();
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        Methods.hideKeyboard(Cart.this);
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "Cart":
                        String response = GetJSONData.getResponseStatus(data[0]);
                        if (response != null) {
                            if (response.equals("success") || response.equals("200")) {
                                dataSetting(data[0]);
                            } else {
                                Response error_response = GetJSONData.getResponse(data[0]);
                                if (error_response != null) {
                                    Methods.toast(error_response.getmMessage());
                                    mCartProduct.setAdapter(new CartAdapter(Cart.this, null, refresher, null, null));
                                    mDataSet = null;
                                    checker = false;
                                    button_text_changer();
                                }
                            }
                        }
                        break;
                    case "Coupon":
                        Response couponResponse = GetJSONData.getResponse(data[0]);
                        if (couponResponse != null) {
                            if (couponResponse.getmStatus() == 200) {
                                if (DataBaseHandlerDiscount.getInstance().get_coupon_code() != null) {
                                    DataBaseHandlerDiscount.getInstance().update_coupon_code(mCouponCode);
                                } else {
                                    DataBaseHandlerDiscount.getInstance().insert_coupon_code(mCouponCode);
                                }
                                refresh();
                            } else {
                                Methods.dialog(Cart.this, "coupon", null);
                            }
                        } else {
                            String dataR = getResources().getString(R.string.error);
                            Methods.toast(dataR);
                        }
                        break;
                    case "GiftVoucher":
                        Response giftVoucherResponse = GetJSONData.getResponse(data[0]);
                        if (giftVoucherResponse != null) {
                            if (giftVoucherResponse.getmStatus() == 200) {
                                if (DataBaseHandlerDiscount.getInstance().get_gift_voucher() != null) {
                                    DataBaseHandlerDiscount.getInstance().update_gift_voucher(mGiftVoucher);
                                } else {
                                    DataBaseHandlerDiscount.getInstance().insert_gift_voucher(mGiftVoucher);
                                }
                                refresh();
                            } else {
                                Methods.toast(giftVoucherResponse.getmMessage());
                                Methods.GiftVoucher(Cart.this);
                            }
                        } else {
                            String dataR = getResources().getString(R.string.error);
                            Methods.toast(dataR);
                        }
                        break;
                }
            } else {
                mCartProduct.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                mCartProduct.setAdapter(new CartAdapter(Cart.this, mDataSet, refresher, null, null));
            }
        } else {
            mCartProduct.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            mCartProduct.setAdapter(new CartAdapter(Cart.this, mDataSet, refresher, null, null));
        }
    }

    public String getCartPostData() {
        try {
            JSONArray array = new JSONArray();
            list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();
            for (int i = 0; i < DataBaseHandlerCart.getInstance(getApplicationContext()).get_Size_cart(); i++) {
                JSONObject product_object = new JSONObject();
                JSONObject object1 = new JSONObject();
                if (DataBaseHandlerCartOptions.getInstance(getApplicationContext()).check_option_index(list.get(i))) {
                    String id = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductId(list.get(i));
                    int count = DataBaseHandlerCart.getInstance(getApplicationContext()).get_product_count(list.get(i));
                    product_object.put(JSON_Names.KEY_PRODUCT_ID, id);
                    product_object.put(JSON_Names.KEY_QUANTITY, count);
                    ArrayList<Integer[]> value = DataBaseHandlerCartOptions.getInstance(getApplicationContext()).option_checking(list.get(i));
                    for (int j = 0; j < value.size(); j++) {
                        if (check_choice(list.get(i), value.get(j))) {
                            JSONArray option_id = new JSONArray();
                            Integer data[] = value.get(j);
                            option_id.put(data[1]);
                            object1.put(String.valueOf(data[0]), option_id);
                        } else {
                            Integer data[] = value.get(j);
                            object1.put(String.valueOf(data[0]), data[1]);
                        }
                    }
                    product_object.put(JSON_Names.KEY_OPTION, object1);
                } else {
                    String id = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductId(list.get(i));
                    int count = DataBaseHandlerCart.getInstance(getApplicationContext()).get_product_count(list.get(i));
                    product_object.put(JSON_Names.KEY_PRODUCT_ID, id);
                    product_object.put(JSON_Names.KEY_QUANTITY, count);
                }
                array.put(i, product_object);
            }

            JSONObject testHomeObject = new JSONObject();
            testHomeObject.put(JSON_Names.KEY_PRODUCTS, array);
            testHomeObject.put(JSON_Names.KEY_LANGUAGE_ID, Methods.current_language_id());

            if (mGiftVoucher != null)
                testHomeObject.put("voucher", mGiftVoucher);

            if (mCouponCode != null)
                testHomeObject.put("coupon", mCouponCode);

            if (DataBaseHandlerAccount.getInstance(getBaseContext()).check_login()) {
                if (mRewardPoints != null)
                    testHomeObject.put("reward", mRewardPoints);

                if(DataBaseHandlerAccount.getInstance(Cart.this).getAddressId()!=null){
                    testHomeObject.put("address_id", DataBaseHandlerAccount.getInstance(Cart.this).getAddressId());
                }else {
                    testHomeObject.put("address_id", "0");
                }

                testHomeObject.put("customer_id", DataBaseHandlerAccount.getInstance(getBaseContext()).get_customer_id() + "");
            }

            return testHomeObject.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresher();
    }

    /*Disable check out btn if stock not available*/
    private boolean stock_status() {
        boolean temp = true;
        if (mDataSet != null) {
            for (int i = 0; i < mDataSet.size(); i++) {
                if (!mDataSet.get(i).getStock_status()) {
                    temp = false;
                }
            }
        } else {
            temp = false;
        }

        return temp;
    }

    @Override
    public void CartTransferHandler(String data, String which) {
        switch (which) {
            case "Coupon":
                progressBar.setVisibility(View.VISIBLE);
                mCouponCode = data;
                String[] urlCouponCode = {URL_Class.mURL_Coupon};
                new API_Get().get_method(urlCouponCode, api_result, getCoupon(mCouponCode), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "Coupon");
                Methods.hideKeyboard(Cart.this);
                break;
            case "GiftVoucher":
                progressBar.setVisibility(View.VISIBLE);
                mGiftVoucher = data;
                String[] urlGiftVoucher = {URL_Class.mURL_Gift_Voucher};
                new API_Get().get_method(urlGiftVoucher, api_result, getGiftVoucher(mGiftVoucher), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "GiftVoucher");
                Methods.hideKeyboard(Cart.this);
                break;
            case "RewardPoints":
                mRewardPoints = data;
                if (DataBaseHandlerDiscount.getInstance().get_reward_points() != null) {
                    DataBaseHandlerDiscount.getInstance().update_reward_point(mRewardPoints);
                } else {
                    DataBaseHandlerDiscount.getInstance().insert_reward_point(mRewardPoints);
                }
                refresh();
                break;
        }
    }

    private String getCoupon(String mCoupon) {
        HashMap<String, String> couponList = new HashMap<>();
        couponList.put("coupon", mCoupon);
        couponList.put("language_id", Methods.current_language_id());
        return getCouponOrGiftVoucherString(couponList);
    }

    private String getGiftVoucher(String mCoupon) {
        HashMap<String, String> couponList = new HashMap<>();
        couponList.put("voucher", mCoupon);
        couponList.put("language_id", Methods.current_language_id());
        return getCouponOrGiftVoucherString(couponList);
    }

    private String getCouponOrGiftVoucherString(HashMap<String, String> couponList) {
        try {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : couponList.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append(URL_Class.mAnd_Symbol);

                result.append(URLEncoder.encode(entry.getKey(), URL_Class.mConvertType));
                result.append(URL_Class.mEqual_Symbol);
                result.append(URLEncoder.encode(entry.getValue(), URL_Class.mConvertType));
            }
            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }
}


