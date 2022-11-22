package com.easymco.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.easymco.R;
import com.easymco.activity.user.Login;
import com.easymco.adapter.Wish_List_Adapter;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.WishListAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductDataSet;
import com.easymco.models.Response;
import com.easymco.network_checker.NetworkConnection;

import java.util.ArrayList;


public class Wish_List extends AppCompatActivity implements API_Result, WishListAPIRequest {
    Toolbar toolbar;
    RecyclerView mWish_List_recycler_view;
    ArrayList<ProductDataSet> mDataSet = new ArrayList<>();
    ProgressBar progressBar;
    API_Result api_result;
    WishListAPIRequest wishListAPIRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_list_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        api_result = this;
        wishListAPIRequest = this;

        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar.setVisibility(View.GONE);

        mWish_List_recycler_view = (RecyclerView) findViewById(R.id.wish_list_recycler_view);
        mWish_List_recycler_view.setLayoutManager(new LinearLayoutManager(this));

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
                    "ar".equals(AppLanguageSupport.getLanguage(Wish_List.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                String temp_url[] = {URL_Class.mURL + URL_Class.mURL_Get_WishList + Methods.current_language() +
                        URL_Class.mUser_Id + DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id()};
                progressBar.setVisibility(View.VISIBLE);
                new API_Get().get_method(temp_url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "WishList");
            } else {
                Intent intent = new Intent(Wish_List.this, Login.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(Wish_List.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            if (data[0] != null) {
                if (source.equals("WishList")) {
                    setting(data[0]);
                } else if (source.equals("WishListAdd")) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            // Methods.toast(response.getmMessage());
                            Methods.toast(getResources().getString(R.string.successfully_added));
                        } else {
                            Methods.toast(response.getmMessage());
                        }
                    }
                }else if (source.equals("WishListRemove")) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            // Methods.toast(response.getmMessage());
                            Methods.toast(getResources().getString(R.string.successfully_removed));
                        } else {
                            Methods.toast(response.getmMessage());
                        }
                    }
                }
            } else {
                Methods.toast(getResources().getString(R.string.error));
            }
        } else {
            Methods.toast(getResources().getString(R.string.error));
        }
    }

    @Override
    public void wish_list_api_request(String data, String[] url,Boolean isAdd) {
        progressBar.setVisibility(View.VISIBLE);
        if(isAdd){
            new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE,
                    true, getBaseContext(), "WishListAdd");
        }else {

            new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE,
                    true, getBaseContext(), "WishListRemove");
        }
    }

    @Override
    public void transaction(String product_id, ImageView transaction_view, String image_url) {

    }

    public void setting(String data) {
        ArrayList<ProductDataSet> mDataSetTemp;
        if (data != null) {
            mDataSetTemp = GetJSONData.getProductDetails(data);
            if (mDataSetTemp != null) {
                DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
                for (int i = 0; i < mDataSetTemp.size(); i++) {
                    if (!DataBaseHandlerWishList.getInstance(getApplicationContext()).
                            checking_wish_list(mDataSetTemp.get(i).getProduct_id())) {
                        DataBaseHandlerWishList.getInstance(getApplicationContext()).add_to_wish_list
                                (mDataSetTemp.get(i).getProduct_id(), mDataSetTemp.get(i).getProduct_string());
                    }
                }
                mDataSet = mDataSetTemp;
            }
        }
        mWish_List_recycler_view.setAdapter(new Wish_List_Adapter(Wish_List.this, mDataSet, wishListAPIRequest));
    }
}
