package com.easymco.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.easymco.R;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerAccountAddress;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.db_handler.DataBaseHandlerConfirmOrder;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.fragments.check_out.CheckOut_Address_Edit;
import com.easymco.fragments.check_out.CheckOut_Confirmation;
import com.easymco.fragments.check_out.CheckOut_Delivery_Detail;
import com.easymco.fragments.check_out.CheckOut_Delivery_Type;
import com.easymco.fragments.check_out.CheckOut_Payment_Type;
import com.easymco.fragments.check_out.CheckOut_Success;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.CheckOutAPIRequest;
import com.easymco.interfaces.CheckOutTransaction;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.AccountDataSet;
import com.easymco.models.ProductOptionDataSet;
import com.easymco.models.Response;
import com.easymco.models.ShippingAndPayment_DataSet;
import com.easymco.shared_preferenc_estring.DataStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Check_Out extends AppCompatActivity implements API_Result, CheckOutTransaction, CheckOutAPIRequest {
    CheckOut_Delivery_Detail checkOut_delivery_detail;
    Toolbar toolbar;
    API_Result api_result;
    FragmentManager fragmentManager;
    ProgressBar progressBar;
    String  address_list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        api_result = this;

        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            checkout_delivery_details_request();
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
                    "ar".equals(AppLanguageSupport.getLanguage(Check_Out.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Methods.hideKeyboard(Check_Out.this);
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_DEFAULT_ADDRESS_ID);
            finish();
        }
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "CheckoutDeliveryDetail":
                        checkout_delivery_details(data[0]);
                        address_list = data[0];
                        break;
                    case "CheckoutDeliveryType":
                        String response = GetJSONData.getResponseStatus(data[0]);
                        if (response != null) {
                            if (response.equals("success") || response.equals("200")) {
                                checkout_delivery_type(data[0]);
                            } else {
                                Response error_response = GetJSONData.getResponse(data[0]);
                                if (error_response != null) {
                                    Methods.toast(error_response.getmMessage());
                                    finish();
                                }
                            }
                        }
                        break;
                    case "CheckoutPaymentType":
                        String CheckoutPaymentType = GetJSONData.getResponseStatus(data[0]);
                        if (CheckoutPaymentType != null) {
                            if (CheckoutPaymentType.equals("success") || CheckoutPaymentType.equals("200")) {
                                checkout_payment_type(data[0]);
                            } else {
                                Response error_response = GetJSONData.getResponse(data[0]);
                                if (error_response != null) {
                                    Methods.toast(error_response.getmMessage());
                                    finish();
                                }
                            }
                        }
                        break;
                    case "CheckoutConfirmation":
                        String CheckoutConfirmation = GetJSONData.getResponseStatus(data[0]);
                        if (CheckoutConfirmation != null) {
                            if (CheckoutConfirmation.equals("success") || CheckoutConfirmation.equals("200")) {
                                checkout_confirmation(data[0]);
                            } else {
                                Response error_response = GetJSONData.getResponse(data[0]);
                                if (error_response != null) {
                                    Methods.toast(error_response.getmMessage());
                                    getSupportFragmentManager().popBackStack();
                                }
                            }
                        }
                        break;
                    case "CheckoutPlaceOrder":
                        Response response_data = GetJSONData.getResponse(data[0]);
                        if (response_data != null) {
                            if (response_data.getmStatus() == 200) {
                                checkout_place_order();
                            } else {
                                Methods.toast(response_data.getmMessage());
                            }
                        }
                        break;
                    case "CheckoutAddressEdit":
                        checkout_address_edit(data[0]);
                        break;
                    case "CheckoutAddressEditPost":
                        Response responsePost = GetJSONData.getResponse(data[0]);
                        if (responsePost != null) {
                            if (responsePost.getmStatus() == 200) {
                                Methods.toast(getResources().getString(R.string.success_address));
                                finish();
                                startActivity(new Intent(Check_Out.this, Check_Out.class));
                            } else {
                                Methods.toast(responsePost.getmMessage());
                            }
                        }
                        break;
                }
            } else {
                Methods.toast(getResources().getString(R.string.error));
            }
        } else {
            Methods.toast(getResources().getString(R.string.error));
        }
    }

    public void checkout_delivery_details_request() {
        progressBar.setVisibility(View.VISIBLE);
        String url[] = {URL_Class.mURL
                + URL_Class.mURL_Get_Customer_Profile
                + URL_Class.mCustomer_id
                + DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id()
                +"&"+Methods.current_language() };
        new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE,
                true, getApplicationContext(), "CheckoutDeliveryDetail");
    }

    public void checkout_delivery_details(String data) {
        checkOut_delivery_detail = CheckOut_Delivery_Detail.getInstance(data);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.check_out_container, checkOut_delivery_detail, "First");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void checkout_address_edit(String data) {
        CheckOut_Address_Edit checkOut_address_edit = CheckOut_Address_Edit.getInstance(data, address_list);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.check_out_container, checkOut_address_edit, "Zero");
        fragmentTransaction.addToBackStack("Zero");
        fragmentTransaction.commit();
    }

    @Override
    public void checkout_delivery_type(String data) {
        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_DEFAULT_ADDRESS_ID);
        CheckOut_Delivery_Type checkOut_delivery_type = CheckOut_Delivery_Type.getInstance(data);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.check_out_container, checkOut_delivery_type, "Second");
        fragmentTransaction.addToBackStack("Second");
        fragmentTransaction.commit();
    }

    @Override
    public void checkout_payment_type(String data) {
        CheckOut_Payment_Type checkOut_payment_type = CheckOut_Payment_Type.getInstance(data);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.check_out_container, checkOut_payment_type, "Third");
        fragmentTransaction.addToBackStack("Third");
        fragmentTransaction.commit();
    }

    @Override
    public void checkout_confirmation(String data) {
        CheckOut_Confirmation checkOut_confirmation = CheckOut_Confirmation.getInstance(data);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.check_out_container, checkOut_confirmation, "Fourth");
        fragmentTransaction.addToBackStack("Fourth");
        fragmentTransaction.commit();
    }

    @Override
    public void checkout_cart_changer() {
        DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).delete_payment_type();
        DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).delete_shipping_type();
        DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).delete_account_address();
        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CART_DATA);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        finish();
    }

    @Override
    public void checkout_place_order() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        DataBaseHandlerDiscount.getInstance().delete_coupon_code();
        DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
        DataBaseHandlerDiscount.getInstance().delete_reward_points();
        DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).delete_account_address();
        DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).delete_shipping_type();
        DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).delete_payment_type();
        DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
        DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
        CheckOut_Success checkOut_success = new CheckOut_Success();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.check_out_container, checkOut_success);
        fragmentTransaction.commit();
    }

    @Override
    public void checkout_delivery_detail_request() {
        progressBar.setVisibility(View.VISIBLE);
        String temp = getCheckOutShippingPaymentPostData();
        if (temp != null) {
            String url[] = {URL_Class.mURL + URL_Class.mURL_Get_Shipping_Method};
            new API_Get().get_method(url, api_result, temp, JSON_Names.KEY_POST_TYPE,
                    true, getBaseContext(), "CheckoutDeliveryType");
        }
    }

    @Override
    public void checkout_payment_type_request() {
        progressBar.setVisibility(View.VISIBLE);
        String temp = getCheckOutShippingPaymentPostData();
        if (temp != null) {
            String url[] = {URL_Class.mURL + URL_Class.mURL_Get_Payment_Method};
            new API_Get().get_method(url, api_result, temp, JSON_Names.KEY_POST_TYPE,
                    true, getBaseContext(), "CheckoutPaymentType");
        }
    }

    @Override
    public void checkout_confirmation() {
        progressBar.setVisibility(View.VISIBLE);
        String temp = getCheckOutConfirmationPostData();
        if (temp != null) {
            String url[] = {URL_Class.mURL + URL_Class.mURL_Confirm_Order};
            new API_Get().get_method(url, api_result, temp, JSON_Names.KEY_POST_TYPE,
                    true, getBaseContext(), "CheckoutConfirmation");
        }
    }

    @Override
    public void checkout_place_order(String data) {
        progressBar.setVisibility(View.VISIBLE);
        String temp = getCheckOutPlaceOrder(data);
        if (temp != null) {
            String url[] = {URL_Class.mURL + URL_Class.mURL_Place_order};
            new API_Get().get_method(url, api_result, temp, JSON_Names.KEY_POST_TYPE,
                    true, getBaseContext(), "CheckoutPlaceOrder");
        }
    }

    @Override
    public void checkout_edit_address() {
        progressBar.setVisibility(View.VISIBLE);
        String url[] = {URL_Class.mURL + URL_Class.mURL_Country +"&"+Methods.current_language()};
        new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE,
                true, getBaseContext(), "CheckoutAddressEdit");
    }

    @Override
    public void checkout_edit_address_post(String data) {
        progressBar.setVisibility(View.VISIBLE);
        String url[] = {URL_Class.mURL + URL_Class.mURL_Edit_Address};
        new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE,
                true, getBaseContext(), "CheckoutAddressEditPost");
    }

    public String getCheckOutPlaceOrder(String order_id) {
        try {
            JSONObject object = new JSONObject();
            if (order_id != null) {
                object.put(JSON_Names.KEY_CONFIRMATION_ORDER_ID, order_id);
                object.put(JSON_Names.KEY_CONFIRMATION_ORDER_STATUS_ID, 1);
            } else {
                object.put(JSON_Names.KEY_CONFIRMATION_ORDER_ID, 0);
                object.put(JSON_Names.KEY_CONFIRMATION_ORDER_STATUS_ID, 0);
            }

            return object.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getCheckOutConfirmationPostData() {
        AccountDataSet accountDataSetShipping, accountDataSetPayment;
        ShippingAndPayment_DataSet shipping_dataSet, payment_dataSet;
        try {
            accountDataSetShipping = DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).get_account_payment_address();
            accountDataSetPayment = DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).get_account_shipping_address();
            shipping_dataSet = DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).get_shipping_type();
            payment_dataSet = DataBaseHandlerConfirmOrder.getInstance(getApplicationContext()).get_payment_type();
            JSONArray array = new JSONArray();
            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();

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
            testHomeObject.put(JSON_Names.KEY_LANGUAGE_ID, Methods.current_language_id());

            if (DataBaseHandlerDiscount.getInstance().get_coupon_code() != null) {
                testHomeObject.put(JSON_Names.KEY_RESPONSE_COUPON, DataBaseHandlerDiscount.getInstance().get_coupon_code());
            }

            if (DataBaseHandlerDiscount.getInstance().get_gift_voucher() != null) {
                testHomeObject.put(JSON_Names.KEY_RESPONSE_VOUCHER, DataBaseHandlerDiscount.getInstance().get_gift_voucher());
            }

            if (DataBaseHandlerDiscount.getInstance().get_reward_points() != null) {
                testHomeObject.put("reward", DataBaseHandlerDiscount.getInstance().get_reward_points());
            }

            testHomeObject.put(JSON_Names.KEY_CUSTOMER_ID, DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id());
            testHomeObject.put(JSON_Names.KEY_PRODUCTS, array);

            JSONObject payment = new JSONObject();
            payment.put(JSON_Names.KEY_ADDRESS_ID, DataBaseHandlerAccount.getInstance(getApplicationContext()).getAddressId());
            payment.put(JSON_Names.KEY_PAYMENT_FIRST_NAME, accountDataSetPayment.getmFirstName());
            payment.put(JSON_Names.KEY_PAYMENT_LAST_NAME, accountDataSetPayment.getmLastName());
            payment.put(JSON_Names.KEY_PAYMENT_COMPANY, accountDataSetPayment.getmCompany());
            payment.put(JSON_Names.KEY_PAYMENT_ADDRESS_1, accountDataSetPayment.getmAddress_1());
            payment.put(JSON_Names.KEY_PAYMENT_ADDRESS_2, "");
            payment.put(JSON_Names.KEY_PAYMENT_CITY, accountDataSetPayment.getmCity());
            payment.put(JSON_Names.KEY_PAYMENT_PIN_CODE, accountDataSetPayment.getmPostcode());
            payment.put(JSON_Names.KEY_PAYMENT_COUNTRY, accountDataSetPayment.getmCountry());
            payment.put(JSON_Names.KEY_PAYMENT_COUNTRY_ID, accountDataSetPayment.getmCountry_id());
            if (!accountDataSetPayment.getmState().equals("0")) {
                payment.put(JSON_Names.KEY_PAYMENT_ZONE, accountDataSetPayment.getmState());
            } else {
                payment.put(JSON_Names.KEY_PAYMENT_ZONE, "");
            }
            payment.put(JSON_Names.KEY_PAYMENT_ZONE_ID, accountDataSetPayment.getmState_Id());
            payment.put(JSON_Names.KEY_PAYMENT_PHONE_NUMBER, accountDataSetPayment.getmTelePhone());
            payment.put(JSON_Names.KEY_PAYMENT_EMAIL, accountDataSetPayment.getmEmailId());

            JSONObject shipping = new JSONObject();
            shipping.put(JSON_Names.KEY_ADDRESS_ID, DataBaseHandlerAccount.getInstance(getApplicationContext()).getAddressId());
            shipping.put(JSON_Names.KEY_SHIPPING_FIRST_NAME, accountDataSetShipping.getmFirstName());
            shipping.put(JSON_Names.KEY_SHIPPING_LAST_NAME, accountDataSetShipping.getmLastName());
            shipping.put(JSON_Names.KEY_SHIPPING_COMPANY, accountDataSetShipping.getmCompany());
            shipping.put(JSON_Names.KEY_SHIPPING_ADDRESS_1, accountDataSetShipping.getmAddress_1());
            shipping.put(JSON_Names.KEY_SHIPPING_ADDRESS_2, "");
            shipping.put(JSON_Names.KEY_SHIPPING_CITY, accountDataSetShipping.getmCity());
            shipping.put(JSON_Names.KEY_SHIPPING_PIN_CODE, accountDataSetShipping.getmPostcode());
            shipping.put(JSON_Names.KEY_SHIPPING_COUNTRY, accountDataSetShipping.getmCountry());
            shipping.put(JSON_Names.KEY_SHIPPING_COUNTRY_ID, accountDataSetShipping.getmCountry_id());
            if (!accountDataSetPayment.getmState().equals("0")) {
                shipping.put(JSON_Names.KEY_SHIPPING_ZONE, accountDataSetShipping.getmState());
            } else {
                shipping.put(JSON_Names.KEY_SHIPPING_ZONE, "");
            }
            shipping.put(JSON_Names.KEY_SHIPPING_ZONE_ID, accountDataSetShipping.getmState_Id());
            shipping.put(JSON_Names.KEY_SHIPPING_PHONE_NUMBER, accountDataSetShipping.getmTelePhone());
            shipping.put(JSON_Names.KEY_SHIPPING_EMAIL, accountDataSetShipping.getmEmailId());

            JSONObject shipping_object = new JSONObject();
            JSONObject payment_object = new JSONObject();
            shipping_object.put(JSON_Names.KEY_RESPONSE_TITLE, shipping_dataSet.getmTitle());
            shipping_object.put(JSON_Names.KEY_RESPONSE_CODE, shipping_dataSet.getmCode());
            shipping_object.put(JSON_Names.KEY_RESPONSE_COST, shipping_dataSet.getmCost());
            shipping_object.put(JSON_Names.KEY_RESPONSE_TAX_CLASS_ID, shipping_dataSet.getmTaxClassId());
            shipping_object.put(JSON_Names.KEY_RESPONSE_SORT_ORDER, shipping_dataSet.getmSortOrder());
            payment_object.put(JSON_Names.KEY_RESPONSE_TITLE, payment_dataSet.getmTitle());
            payment_object.put(JSON_Names.KEY_RESPONSE_CODE, payment_dataSet.getmCode());
            payment_object.put(JSON_Names.KEY_RESPONSE_TERMS, payment_dataSet.getmTerms());
            payment_object.put(JSON_Names.KEY_RESPONSE_SORT_ORDER, payment_dataSet.getmSortOrder());

            testHomeObject.put(JSON_Names.KEY_PAYMENT_ADDRESS, payment);
            testHomeObject.put(JSON_Names.KEY_SHIPPING_ADDRESS, shipping);
            testHomeObject.put(JSON_Names.KEY_RESPONSE_SHIPPING_METHOD, shipping_object);
            testHomeObject.put(JSON_Names.KEY_RESPONSE_PAYMENT_METHOD, payment_object);

            return testHomeObject.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getCheckOutShippingPaymentPostData() {
        AccountDataSet accountDataSetShipping, accountDataSetPayment;
        try {
            accountDataSetShipping = DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).get_account_payment_address();
            accountDataSetPayment = DataBaseHandlerAccountAddress.getInstance(getApplicationContext()).get_account_shipping_address();
            JSONArray array = new JSONArray();
            ArrayList<Integer> list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_all_index();

            for (int i = 0; i < DataBaseHandlerCart.getInstance(getApplicationContext()).get_Size_cart(); i++) {
                JSONObject product_object = new JSONObject();
                JSONObject object1 = new JSONObject();
                if (DataBaseHandlerCartOptions.getInstance(getApplicationContext()).check_option_index(list.get(i))) {
                    String id = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductId(list.get(i));
                    int count = DataBaseHandlerCart.getInstance(getApplicationContext()).get_product_count(list.get(i));
                    product_object.put(JSON_Names.KEY_PRODUCT_ID, id);
                    product_object.put(JSON_Names.KEY_QUANTITY, count);
                    ArrayList<Integer[]> value = DataBaseHandlerCartOptions.getInstance(getApplicationContext()).
                            option_checking(list.get(i));
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
            JSONObject payment = new JSONObject();
            payment.put(JSON_Names.KEY_ADDRESS_ID, accountDataSetPayment.getmAddress_Id());
            payment.put(JSON_Names.KEY_PAYMENT_FIRST_NAME, accountDataSetPayment.getmFirstName());
            payment.put(JSON_Names.KEY_PAYMENT_LAST_NAME, accountDataSetPayment.getmLastName());
            payment.put(JSON_Names.KEY_PAYMENT_COMPANY, accountDataSetPayment.getmCompany());
            payment.put(JSON_Names.KEY_PAYMENT_ADDRESS_1, accountDataSetPayment.getmAddress_1());
            payment.put(JSON_Names.KEY_PAYMENT_ADDRESS_2, "");
            payment.put(JSON_Names.KEY_PAYMENT_CITY, accountDataSetPayment.getmCity());
            payment.put(JSON_Names.KEY_PAYMENT_PIN_CODE, accountDataSetPayment.getmPostcode());
            payment.put(JSON_Names.KEY_PAYMENT_COUNTRY, accountDataSetPayment.getmCountry());
            payment.put(JSON_Names.KEY_PAYMENT_COUNTRY_ID, accountDataSetPayment.getmCountry_id());
            if (!accountDataSetPayment.getmState().equals("0")) {
                payment.put(JSON_Names.KEY_PAYMENT_ZONE, accountDataSetPayment.getmState());
            } else {
                payment.put(JSON_Names.KEY_PAYMENT_ZONE, "");
            }
            payment.put(JSON_Names.KEY_PAYMENT_ZONE_ID, accountDataSetPayment.getmState_Id());
            payment.put(JSON_Names.KEY_PAYMENT_PHONE_NUMBER, accountDataSetPayment.getmTelePhone());
            payment.put(JSON_Names.KEY_PAYMENT_EMAIL, accountDataSetPayment.getmEmailId());

            JSONObject shipping = new JSONObject();
            shipping.put(JSON_Names.KEY_ADDRESS_ID, accountDataSetPayment.getmAddress_Id());
            shipping.put(JSON_Names.KEY_SHIPPING_FIRST_NAME, accountDataSetShipping.getmFirstName());
            shipping.put(JSON_Names.KEY_SHIPPING_LAST_NAME, accountDataSetShipping.getmLastName());
            shipping.put(JSON_Names.KEY_SHIPPING_COMPANY, accountDataSetShipping.getmCompany());
            shipping.put(JSON_Names.KEY_SHIPPING_ADDRESS_1, accountDataSetShipping.getmAddress_1());
            shipping.put(JSON_Names.KEY_SHIPPING_ADDRESS_2, "");
            shipping.put(JSON_Names.KEY_SHIPPING_CITY, accountDataSetShipping.getmCity());
            shipping.put(JSON_Names.KEY_SHIPPING_PIN_CODE, accountDataSetShipping.getmPostcode());
            shipping.put(JSON_Names.KEY_SHIPPING_COUNTRY, accountDataSetShipping.getmCountry());
            shipping.put(JSON_Names.KEY_SHIPPING_COUNTRY_ID, accountDataSetShipping.getmCountry_id());
            if (!accountDataSetPayment.getmState().equals("0")) {
                shipping.put(JSON_Names.KEY_SHIPPING_ZONE, accountDataSetShipping.getmState());
            } else {
                shipping.put(JSON_Names.KEY_SHIPPING_ZONE, "");
            }
            shipping.put(JSON_Names.KEY_SHIPPING_ZONE_ID, accountDataSetShipping.getmState_Id());
            shipping.put(JSON_Names.KEY_SHIPPING_PHONE_NUMBER, accountDataSetShipping.getmTelePhone());
            shipping.put(JSON_Names.KEY_SHIPPING_EMAIL, accountDataSetShipping.getmEmailId());
            testHomeObject.put(JSON_Names.KEY_PAYMENT_ADDRESS, payment);
            testHomeObject.put(JSON_Names.KEY_SHIPPING_ADDRESS, shipping);
            return testHomeObject.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean check_choice(int index, Integer data[]) {
        HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChildHolder = new HashMap<>();
        ArrayList<ProductOptionDataSet> temp;
        HashMap<String, Object> product_data;
        ArrayList<ProductOptionDataSet> mOptionList = new ArrayList<>();
        boolean result = false;
        String product_detail = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductString(index);
        product_data = GetJSONData.getSeparateProductDetail(product_detail);
        if (product_data != null) {
            mOptionList = (ArrayList<ProductOptionDataSet>) product_data.get("Option");
            mOptionListChildHolder = (HashMap<Integer, ArrayList<ProductOptionDataSet>>) product_data.get("Option Child");
        }
        for (int i = 0; i < mOptionList.size(); i++) {
            if (mOptionList.get(i).getProduct_option_type().equals("checkbox")) {
                for (int j = 0; j < mOptionListChildHolder.get(i).size(); j++) {
                    temp = mOptionListChildHolder.get(i);
                    if (temp.get(j).getProduct_option_value_id().equals(String.valueOf(data[1])) &&
                            mOptionList.get(i).getProduct_option_id().equals(String.valueOf(data[0]))) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
}
