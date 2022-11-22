package com.easymco.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.easymco.R;
import com.easymco.activity.user.Login;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.db_handler.DataBaseLanguageDetails;
import com.easymco.db_handler.DataBaseStorageData;
import com.easymco.interfaces.API_Result;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

public class SplashScreen extends Activity implements API_Result {

    static int SPLASH_TIME_OUT = 500;
    ProgressBar progressBar;
    API_Result api_resultGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        clear();
        api_resultGet = this;

        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (NetworkConnection.connectionType(getApplicationContext())) {

                if (!DataBaseLanguageDetails.getInstance(getApplicationContext()).check_language_selected()) {
                    DataBaseLanguageDetails.getInstance(getApplicationContext()).insert_language_detail("1");
                    loadAPI();
                } else {
                    loadAPI();
                }


            } else {
                two_g_transfer();
            }


        } else {
            Intent intent = new Intent(SplashScreen.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
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
                    "ar".equals(AppLanguageSupport.getLanguage(SplashScreen.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public void clear() {
        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
    }

    public void two_g_transfer() {
        Intent intent = new Intent(SplashScreen.this, Network_Error.class);
        startActivity(intent);
        finish();
    }

    public void success(final String data[]) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getApplication() != null) {
                    if (DataBaseStorageData.getInstance(getApplication()).check_storage_data_presented()) {
                        DataBaseStorageData.getInstance(getApplication()).delete_storage_data();
                        DataBaseStorageData.getInstance(getApplication()).insert_storage_data(data[0], data[1]);
                    } else {
                        DataBaseStorageData.getInstance(getApplication()).insert_storage_data(data[0], data[1]);
                    }

                    Intent i = new Intent(SplashScreen.this, Home.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.INVISIBLE);
        if (source.equals("SplashScreen")) {
            if (data != null) {
                if (data.length == 2) {
                    if(DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()){
                        String mCustomerStatus = GetJSONData.getCustomerStatus(data[1]);
                        if(mCustomerStatus!=null && !mCustomerStatus.isEmpty()&&
                                mCustomerStatus.equals("0")){
                            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
                            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
                            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
                            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
                            success(data);
                        }else {
                            success(data);
                        }
                    }else {
                        success(data);
                    }
                } else {
                    two_g_transfer();
                }
            } else {
                two_g_transfer();
            }
        }
    }

    private void loadAPI() {

        if(DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()){
            int mCustomerId = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id();
            String list[] = {URL_Class.mURL + URL_Class.mURL_MainCategory +
                    Methods.current_language(), URL_Class.mURL + URL_Class.mBanner +
                    Methods.current_language()+"&"+URL_Class.mCustomer_id+mCustomerId};
            progressBar.setVisibility(View.VISIBLE);
            new API_Get().get_method(list, api_resultGet, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "SplashScreen");
        }else {
            String list[] = {URL_Class.mURL + URL_Class.mURL_MainCategory +
                    Methods.current_language(), URL_Class.mURL + URL_Class.mBanner + Methods.current_language()};
            progressBar.setVisibility(View.VISIBLE);
            new API_Get().get_method(list, api_resultGet, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "SplashScreen");
        }

    }
}
