package com.easymco.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.account.MyAccountMainMenu;
import com.easymco.activity.account.OrderHistory;
import com.easymco.activity.product.Product_Details;
import com.easymco.activity.user.Login;
import com.easymco.activity.user.SignUp;
import com.easymco.adapter.Product_Listing_Gird_And_List;
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
import com.easymco.interfaces.SearchListLoadMore;
import com.easymco.interfaces.WishListAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductDataSet;
import com.easymco.models.Response;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements View.OnClickListener,
        API_Result, WishListAPIRequest, SearchListLoadMore {
    public ArrayList<ProductDataSet> mSearch_Details;
    Toolbar toolbar;
    RecyclerView search_result_recycler_view;
    SearchView search;
    TextView search_not_found_result;
    String mQuery;
    int i = 0;
    boolean condition = false;
    Product_Listing_Gird_And_List adapter;
    ProgressBar progressBar;
    FloatingActionButton view_changer_seller;
    API_Result api_result;
    Boolean initialSearchLoading = true;
    SearchListLoadMore mSearchListLoadMore;
    WishListAPIRequest wishListAPIRequest;
    int mPageCount = 1;
    Boolean mIsGrid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        api_result = this;
        wishListAPIRequest = this;

        mSearchListLoadMore = this;

        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

        search_result_recycler_view = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        search = (SearchView) findViewById(R.id.test_search_view);
        search_not_found_result = (TextView) findViewById(R.id.search_result_not_found);
        view_changer_seller = (FloatingActionButton) findViewById(R.id.fab);
        search_result_recycler_view.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        search_not_found_result.setText(R.string.empty_text);
        progressBar.setVisibility(View.GONE);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.clearFocus();


                //The mPageCount should be reset at each search :-
                mPageCount = 1;
                mSearch_Details = new ArrayList<>();
                initialSearchLoading = true;

                String url[] = {URL_Class.mURL + URL_Class.mURL_Search_Product + Methods.current_language()
                        + URL_Class.mURL_Search + query.replace(" ", "%20") + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount};
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    progressBar.setVisibility(View.VISIBLE);
                    new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "Search");
                } else {
                    Intent intent = new Intent(Search.this, NoInternetConnection.class);
                    startActivity(intent);
                    finish();
                }
                mQuery = query.replace(" ", "%20");
                Methods.hideKeyboard(Search.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPageCount = 1;
                return false;
            }
        });

        /*search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                sendEmpty();
            }
        });*/
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
                    "ar".equals(AppLanguageSupport.getLanguage(Search.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Methods.hideKeyboard(Search.this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);

        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(false);
            menu.findItem(R.id.my_order).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.search).setVisible(false);
        } else {

            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.search).setVisible(false);
        }
        return true;
    }

    public void update(Menu menu) {
        TextView textView;
        ImageView imageView;
        View view = menu.findItem(R.id.cart_count).getActionView();
        textView = view.findViewById(R.id.cart_count_value);
        imageView = view.findViewById(R.id.cart_image_view);
        final String tempData = DataBaseHandlerCart.getInstance(getApplicationContext()).get_whole_list_count();
        if (!tempData.equals("0")) {
            textView.setText(String.valueOf(tempData));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentTransfer();
                }
            });
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    intentTransfer();
                    return false;
                }
            });
        } else {
            textView.setVisibility(View.INVISIBLE);
        }

    }

    public void intentTransfer() {
        Intent intent = new Intent(Search.this, Cart.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(Search.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            Intent intent = new Intent(Search.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(Search.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            Intent intent = new Intent(Search.this, SplashScreen.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(Search.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Search.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(Search.this, OrderHistory.class);
            startActivity(intent);
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "Search", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }

    public void gridRecyclerView() {

        mIsGrid = true;

        search_result_recycler_view.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        sendType(true);
    }

    public void listRecyclerView() {

        mIsGrid = false;

        search_result_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        sendType(false);
    }

    public void sendEmpty() {
        search_result_recycler_view.setAdapter(null);
    }

    public void sendType(boolean type) {
        search_not_found_result.setVisibility(View.GONE);
        view_changer_seller.setVisibility(View.VISIBLE);
        view_changer_seller.setOnClickListener(this);

        if (initialSearchLoading ) {
            String current_count = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_COUNT);
            if(current_count != null && Integer.valueOf(current_count) > 6){
                mSearch_Details.add(null);
            }
            initialSearchLoading = false;

        }

        adapter = new Product_Listing_Gird_And_List(Search.this, mSearch_Details, type,
                wishListAPIRequest, search_result_recycler_view,true,false);
        search_result_recycler_view.setAdapter(adapter);

       /* adapter.setOnLoadMoreListener(new Product_Listing_Gird_And_List.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageCount++;
                String current_count = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_COUNT);
                if (current_count != null)
                    if (mPageCount <= ((Integer.valueOf(current_count) / 6) + 1)) {
                        String url[] = new String[1];

                        url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mURL_Search
                                + mQuery.replace(" ", "%20") + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;

                        mSearch_Details.add(null);
                        adapter.notifyItemInserted(mSearch_Details.size() - 1);

                        new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "PageCalling");
                    }
            }
        });*/
    }

    public void check() {
        condition = true;
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();

      /*  mSearch_Details = new ArrayList<>();
        if (adapter != null && search_result_recycler_view != null) {
            adapter = new Product_Listing_Gird_And_List(Search.this, mSearch_Details,
                    true, wishListAPIRequest,  *//*refresher,*//*search_result_recycler_view, true, true);
            search_result_recycler_view.setAdapter(adapter);
        }*/

        if (mSearch_Details != null && mSearch_Details.size() > 0) {

            if(mIsGrid){
                search_result_recycler_view.setLayoutManager(new GridLayoutManager(Search.this, 2));
                adapter = new Product_Listing_Gird_And_List(Search.this, mSearch_Details,
                        mIsGrid, wishListAPIRequest,  /*refresher,*/search_result_recycler_view, true, true);
                search_result_recycler_view.setAdapter(adapter);
            }else {
                search_result_recycler_view.setLayoutManager(new LinearLayoutManager(Search.this, LinearLayoutManager.VERTICAL, false));
                adapter = new Product_Listing_Gird_And_List(Search.this, mSearch_Details,
                        mIsGrid, wishListAPIRequest,  /*refresher,*/search_result_recycler_view, true, true);
                search_result_recycler_view.setAdapter(adapter);
            }

        }

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                if (condition) {
                    if (i == 0) {
                        view_changer_seller.setImageResource(R.drawable.ic_view_list_white_18dp);
                        listRecyclerView();
                        i = 1;
                    } else {
                        view_changer_seller.setImageResource(R.drawable.ic_view_module_white_18dp);
                        gridRecyclerView();
                        i = 0;
                    }
                }
                break;
        }
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "Search":
                        mSearch_Details = GetJSONData.getProductDetails(data[0]);
                        if (mSearch_Details != null) {
                            if (mSearch_Details.size() > 0) {
                                check();
                                if (i == 0) {
                                    mIsGrid = true;
                                    sendType(true);
                                } else {
                                    mIsGrid = false;
                                    sendType(false);
                                }
                            }else {
                                if (getApplicationContext() != null) {
                                    mSearch_Details = new ArrayList<>();
                                    if (adapter != null && search_result_recycler_view != null) {
                                        adapter = new Product_Listing_Gird_And_List(Search.this, mSearch_Details,
                                                true, wishListAPIRequest, search_result_recycler_view, true, true);
                                        search_result_recycler_view.setAdapter(adapter);
                                    }
                                    Methods.toast(getApplicationContext().getResources().getString(R.string.try_using_another_keyword));

                                }

                            }
                        }

                        break;
                    case "PageCalling":
                        if (!mSearch_Details.isEmpty()) {

                            if(mSearch_Details.get(mSearch_Details.size() - 1) == null){
                                mSearch_Details.remove(mSearch_Details.size() - 1);
                                adapter.notifyItemRemoved(mSearch_Details.size());
                            }
                            if (GetJSONData.getProductDetails(data[0]) != null) {
                                ArrayList<ProductDataSet> mTemp = GetJSONData.getProductDetails(data[0]);
                                if (mTemp != null) {
                                    for (int i = 0; i < mTemp.size(); i++) {
                                        mSearch_Details.add(mTemp.get(i));
                                    }

                                    String current_count = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_COUNT);
                                    if(current_count!= null){
                                        if(mSearch_Details.size() < Integer.valueOf(current_count)){
                                            mSearch_Details.add(null);
                                        }
                                    }

                                }
                            }
                            adapter.notifyItemInserted(mSearch_Details.size());
                            adapter.setLoaded();


                        }
                        break;
                    case "WishListAdd":
                        Response responseAdd = GetJSONData.getResponse(data[0]);
                        if (responseAdd != null) {
                            if (responseAdd.getmStatus() == 200) {
                                //  Methods.toast(responseAdd.getmMessage());
                                Methods.toast(getResources().getString(R.string.successfully_added));
                            } else {
                                Methods.toast(responseAdd.getmMessage());
                            }
                        }
                        break;
                    case "WishListRemove":
                        Response responseRemove = GetJSONData.getResponse(data[0]);
                        if (responseRemove != null) {
                            if (responseRemove.getmStatus() == 200) {
                                //  Methods.toast(responseRemove.getmMessage());
                                Methods.toast(getResources().getString(R.string.successfully_removed));
                            } else {
                                Methods.toast(responseRemove.getmMessage());
                            }
                        }
                        break;
                }
            } else {
                mIsGrid = false;
                sendType(false);
                search_result_recycler_view.setVisibility(View.INVISIBLE);
                String result = getResources().getString(R.string.result_not_found);
                search_not_found_result.setText(result);
            }
        }
    }

    @Override
    public void wish_list_api_request(String data, String[] url,Boolean isAdd) {
        if(isAdd){
            new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE, true,
                    getBaseContext(), "WishListAdd");
        }else {
            new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE, true,
                    getBaseContext(), "WishListRemove");
        }

    }

    @Override
    public void transaction(String product_id, ImageView transaction_view, String image_url) {
        Intent intent = new Intent(Search.this, Product_Details.class);
        intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, product_id);
        intent.putExtra(JSON_Names.KEY_IMAGE, image_url);
        startActivity(intent);
    }

    @Override
    public void toLoadSearchList() {

        if (adapter != null) {
            adapter.setLoaded();
            adapter.setOnLoadMoreListener(new Product_Listing_Gird_And_List.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPageCount++;
                    String current_count = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_COUNT);
                    if (current_count != null)
                        if (mPageCount <= ((Integer.valueOf(current_count) / 6) + 1)) {
                            String url[] = new String[1];

                            url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() + URL_Class.mURL_Search
                                    + mQuery.replace(" ", "%20") + URL_Class.mURL_Limit + URL_Class.mURL_Page + mPageCount;


                            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "PageCalling");
                        }
                }
            });

        }


    }
}
