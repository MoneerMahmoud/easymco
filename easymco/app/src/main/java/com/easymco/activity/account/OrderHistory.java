package com.easymco.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.Cart;
import com.easymco.activity.NoInternetConnection;
import com.easymco.activity.Search;
import com.easymco.activity.SplashScreen;
import com.easymco.activity.Wish_List;
import com.easymco.activity.user.Login;
import com.easymco.activity.user.SignUp;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.interfaces.API_Result;
import com.easymco.mechanism.Methods;
import com.easymco.models.OrderHistoryData;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.recycler_view_click_event.RecyclerViewClickEventHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, API_Result {

    public ArrayList<OrderHistoryData> tempOProductList = new ArrayList<>();
    public ArrayList<OrderHistoryData> tempOOtotalsList = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<OrderHistoryData>>> tempOOptionsList = new ArrayList<>();
    public Integer mOH_JArrayLength = 0;
    Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<OrderHistoryData> mOrderHListMain = new ArrayList<>();
    private ArrayList<ArrayList<OrderHistoryData>> mOrdersProductList = new ArrayList<>();
    private ArrayList<ArrayList<OrderHistoryData>> mOrdersOTotalsList = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<OrderHistoryData>>> mOrdersPOptionsList = new ArrayList<>();
    private ArrayList<Integer> nopCout = new ArrayList<>();
    ProgressBar progressBar;
    API_Result api_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_order_list);

        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        api_result = this;

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        send_request();

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
                    "ar".equals(AppLanguageSupport.getLanguage(OrderHistory.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public void send_request() {
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);
            String url[] = {URL_Class.mURL + URL_Class.mGet_Customer_Order + URL_Class.mCustomer_id
                    + String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id())
                    +"&"+Methods.current_language() };
            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "CustomerProfile");
        } else {
            Intent intent = new Intent(OrderHistory.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(OrderHistory.this, Cart.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            Intent intent = new Intent(OrderHistory.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    public void noOfProducts() {
        for (int i = 0; i < mOrdersProductList.size(); i++) {
            int count = 0;
            for (int j = 0; j < mOrdersProductList.get(i).size(); j++) {
                count += Integer.valueOf(mOrdersProductList.get(i).get(j).getmQuantity());
            }
            nopCout.add(count);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);

        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(false);
            menu.findItem(R.id.my_order).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else {
            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.my_order).setVisible(false);
        }
        return true;
    }

    public void update(Menu menu) {
        View view = menu.findItem(R.id.cart_count).getActionView();
        ImageView t1 = view.findViewById(R.id.cart_image_view);
        TextView t2 = view.findViewById(R.id.cart_count_value);
        String tempData = DataBaseHandlerCart.getInstance(getApplicationContext()).get_whole_list_count();
        if (!tempData.equals("0")) {
            t2.setText(tempData);
            t2.setOnClickListener(this);
            t1.setOnTouchListener(this);
        } else {
            t2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        } else if (id == R.id.user_name) {
            onBackPressed();
            finish();
        } else if (id == R.id.login) {
            Intent intent = new Intent(OrderHistory.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(OrderHistory.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(OrderHistory.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            Intent intent = new Intent(OrderHistory.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(OrderHistory.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(OrderHistory.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "OrderHistory", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (source.equals("CustomerProfile")) {
            if (data != null) {
                try {
                    JSONObject jobject = new JSONObject(data[0]);
                    JSONArray jarray = jobject.getJSONArray("orders");
                    mOH_JArrayLength = jarray.length();

                    if (mOH_JArrayLength != 0) {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jgetobject = jarray.getJSONObject(i);

                            OrderHistoryData basemydataObject = new OrderHistoryData(jgetobject.optString("order_id"), jgetobject.optString("invoice_no"), jgetobject.optString("invoice_prefix"),
                                    jgetobject.optString("store_id"), jgetobject.optString("store_name"), jgetobject.optString("store_url"), jgetobject.optString("customer_id"),
                                    jgetobject.optString("firstname"), jgetobject.optString("lastname"), jgetobject.optString("telephone"), jgetobject.optString("fax"),
                                    jgetobject.optString("email"), jgetobject.optString("payment_firstname"), jgetobject.optString("payment_lastname"), jgetobject.optString("payment_company"),
                                    jgetobject.optString("payment_address_1"), jgetobject.optString("payment_address_2"), jgetobject.optString("payment_postcode"), jgetobject.optString("payment_city"),
                                    jgetobject.optString("payment_zoneid"), jgetobject.optString("payment_zone"), jgetobject.optString("payment_zone_code"), jgetobject.optString("payment_countryid"),
                                    jgetobject.optString("payment_country"), jgetobject.optString("payment_iso_code_2"), jgetobject.optString("payment_iso_code_3"), jgetobject.optString("payment_address_format"),
                                    jgetobject.optString("payment_method"),
                                    jgetobject.optString("shipping_firstname"), jgetobject.optString("shipping_lastname"), jgetobject.optString("shipping_company"),
                                    jgetobject.optString("shipping_address_1"), jgetobject.optString("shipping_address_2"), jgetobject.optString("shipping_postcode"), jgetobject.optString("shipping_city"),
                                    jgetobject.optString("shipping_zoneid"), jgetobject.optString("shipping_zone"), jgetobject.optString("shipping_zone_code"), jgetobject.optString("shipping_countryid"),
                                    jgetobject.optString("shipping_country"), jgetobject.optString("shipping_iso_code_2"), jgetobject.optString("shipping_iso_code_3"), jgetobject.optString("shipping_address_format"),
                                    jgetobject.optString("shipping_method"),
                                    jgetobject.optString("comment"), jgetobject.optString("total"), jgetobject.optString("order_status_id"),
                                    jgetobject.optString("order_status"), jgetobject.optString("language_id"), jgetobject.optString("currency_id"), jgetobject.optString("currency_code"),
                                    jgetobject.optString("currency_value"), jgetobject.optString("date_modified"), jgetobject.optString("date_added"), jgetobject.optString("ip"));

                            mOrderHListMain.add(basemydataObject);

                            JSONArray jsonArray1 = jgetobject.getJSONArray("products");
                            ArrayList<OrderHistoryData> subtemplist1 = new ArrayList<>();
                            ArrayList<ArrayList<OrderHistoryData>> subtempListInner = new ArrayList<>();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                OrderHistoryData submydataObject1 = new OrderHistoryData(jsonObject1.optString("order_product_id"), jsonObject1.optString("order_id"), jsonObject1.optString("product_id"), jsonObject1.optString("name"),
                                        jsonObject1.optString("model"), jsonObject1.optString("quantity"), jsonObject1.optString("price"), jsonObject1.optString("total"),
                                        jsonObject1.optString("tax"), jsonObject1.optString("reward"), jsonObject1.optString("image"));

                                subtemplist1.add(submydataObject1);

                                JSONArray jsonArray1Sub = jsonObject1.getJSONArray("options");
                                ArrayList<OrderHistoryData> subtemplist1Sub = new ArrayList<>();
                                for (int jj = 0; jj < jsonArray1Sub.length(); jj++) {
                                    JSONObject jsonObject1Sub = jsonArray1Sub.getJSONObject(jj);

                                    OrderHistoryData submydataObject1Sub = new OrderHistoryData(jsonObject1Sub.optString("order_option_id"), jsonObject1Sub.optString("order_id"), jsonObject1Sub.optString("order_product_id"), jsonObject1Sub.optString("product_option_id"),
                                            jsonObject1Sub.optString("product_option_value_id"), jsonObject1Sub.optString("name"), jsonObject1Sub.optString("value"), jsonObject1Sub.optString("type"));

                                    subtemplist1Sub.add(submydataObject1Sub);
                                }
                                subtempListInner.add(subtemplist1Sub);
                            }
                            mOrdersPOptionsList.add(subtempListInner);
                            mOrdersProductList.add(subtemplist1);

                            JSONArray jsonArray2 = jgetobject.getJSONArray("order_totals");
                            ArrayList<OrderHistoryData> subtemplist2 = new ArrayList<>();
                            for (int k = 0; k < jsonArray2.length(); k++) {
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(k);

                                OrderHistoryData submydataObject2 = new OrderHistoryData(jsonObject2.optString("order_total_id"), jsonObject2.optString("order_id"), jsonObject2.optString("code"),
                                        jsonObject2.optString("title"), jsonObject2.optString("value"), jsonObject2.optString("sort_order"), jsonObject2.optString("text"));

                                subtemplist2.add(submydataObject2);
                            }
                            mOrdersOTotalsList.add(subtemplist2);
                        }
                    }

                } catch (Exception e) {
                    mOrderHListMain = null;
                    mOrdersPOptionsList = null;
                    mOrdersProductList = null;
                    mOrdersOTotalsList = null;
                }

                if (mOH_JArrayLength != 0) {
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(OrderHistory.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    noOfProducts();
                    if (mOrderHListMain != null) {
                        mAdapter = new OrderHistoryAdapter(mOrderHListMain, nopCout);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickEventHandler(getBaseContext(), new RecyclerViewClickEventHandler.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (mOrderHListMain.get(position) != null) {
                                    Intent i = new Intent(OrderHistory.this, OrderHistoryOrderInfo.class);
                                    i.putExtra("orderTotal", mOrderHListMain.get(position).getmTotal());
                                    i.putExtra("orderProductQuantity", String.valueOf(nopCout.get(position)));
                                    tempOProductList = mOrdersProductList.get(position);
                                    i.putExtra("OrdersProductList", tempOProductList);
                                    tempOOptionsList.add(mOrdersPOptionsList.get(position));
                                    i.putExtra("OrdersPOptionsList", tempOOptionsList);
                                    tempOOtotalsList = mOrdersOTotalsList.get(position);
                                    i.putExtra("OrdersOTotalsList", tempOOtotalsList);
                                    i.putExtra("total", mOrderHListMain.get(position).getmTotal());
                                    startActivity(i);
                                    //finish();
                                }
                            }
                        }));
                    }
                } else {
                    Methods.dialog(OrderHistory.this, "dismiss", getString(R.string.empty_order_history));
                }
            }
        }
    }


    class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

        private ArrayList<OrderHistoryData> mDatasetMain;
        private ArrayList<Integer> mNOPCount;

        OrderHistoryAdapter(ArrayList<OrderHistoryData> myDatasetMain, ArrayList<Integer> nopcount) {
            this.mDatasetMain = myDatasetMain;
            this.mNOPCount = nopcount;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_account_order_list_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            String mOrderDate = getResources().getString(R.string.tv_date_added) + " " + mDatasetMain.get(position).getmDateAdded(),
                    mOrderQuantity = getResources().getString(R.string.account_quantity) + " " + mNOPCount.get(position).toString(),
                    mOrderTotal = getResources().getString(R.string.tv_total) + " " + mDatasetMain.get(position).getmTotal();

            holder.txtOrderStatus.setText(mDatasetMain.get(position).getmOrderStatus());
            holder.txtOrderId.setText(mDatasetMain.get(position).getmOrderId());
            holder.txtDateAdded.setText(mOrderDate);
            holder.txtNoOfPro.setText(mOrderQuantity);
            holder.txtTotal.setText(mOrderTotal);
        }

        @Override
        public int getItemCount() {
            return this.mDatasetMain.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtOrderId, txtOrderStatus;
            TextView txtDateAdded;
            TextView txtNoOfPro;
            TextView txtTotal;

            public ViewHolder(View v) {
                super(v);
                txtOrderId = v.findViewById(R.id.tv_my_order_recycle_order_id_data);
                txtDateAdded = v.findViewById(R.id.tv_my_order_recycle_date_add_data);
                txtNoOfPro = v.findViewById(R.id.tv_my_order_recycle_no_of_pro_data);
                txtTotal = v.findViewById(R.id.tv_my_order_recycle_total_data);
                txtOrderStatus = v.findViewById(R.id.tv_my_order_recycle_order_status_data);
            }

        }

    }
}