package com.easymco.activity.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.Cart;
import com.easymco.activity.NoInternetConnection;
import com.easymco.activity.Search;
import com.easymco.activity.Wish_List;
import com.easymco.activity.account.MyAccountMainMenu;
import com.easymco.activity.account.OrderHistory;
import com.easymco.activity.product.Product_Details;
import com.easymco.activity.user.Login;
import com.easymco.activity.user.SignUp;
import com.easymco.adapter.Category_Sub_List_Adapter;
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
import com.easymco.interfaces.WishListAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.CategoryDataSet;
import com.easymco.models.ProductDataSet;
import com.easymco.models.Response;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.recycler_view_click_event.RecyclerViewClickEventHandler;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.util.ArrayList;

public class Category_Details extends AppCompatActivity implements
        View.OnClickListener, API_Result, WishListAPIRequest {
    public static ArrayList<ProductDataSet> mCategory_Details;
    RecyclerView mCategory_recycler;
    TextView mCategory_filter;
    TextView mCategory_sort_by;
    ImageView mCategory_viewType;
    ImageView mCategory_banner;
    RecyclerView mListView;
    int i = 0;
    Context mContext;
    ArrayList<CategoryDataSet> mList;
    Product_Listing_Gird_And_List adapter;
    int mType;
    int mCategory_Id;
    Toolbar toolbar;
    String title, image_url, filter, mCurrentURL, baseURLProductList, baseURLCategory;
    ProgressBar progressBar;
    Boolean current_value = true, mFeatured = false;
    PopupWindow mSortingList;
    TextView sort_default, sort_name_AZ, sort_name_ZA, sort_price_LH, sort_price_HL,
            sort_rating_H, sort_rating_L, sort_model_AZ, sort_model_ZA, mSubCategoryTitle;
    API_Result api_resultGet;
    WishListAPIRequest wishListAPIRequest;
    LinearLayout filter_and_sorting_holder;
    int mPageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_coordinator_layout);
        mContext = getApplicationContext();
        api_resultGet = this;
        wishListAPIRequest = this;

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (RecyclerView) findViewById(R.id.sub_category_list_view);
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

        mSubCategoryTitle = (TextView) findViewById(R.id.sub_category_list_title);
        mCategory_recycler = (RecyclerView) findViewById(R.id.category_recycler_view);
        mCategory_filter = (TextView) findViewById(R.id.category_filter);
        mCategory_sort_by = (TextView) findViewById(R.id.category_sorting);
        mCategory_viewType = (ImageView) findViewById(R.id.category_type_of_view);
        mCategory_banner = (ImageView) findViewById(R.id.category_banner);
        filter_and_sorting_holder = (LinearLayout) findViewById(R.id.filter_and_sorting_holder);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE) != 0) {
                mCategory_Id = bundle.getInt(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE);
            }
            if (bundle.getInt(JSON_Names.KEY_TYPE_SHARED_PREFERENCE) != 0) {
                mType = bundle.getInt(JSON_Names.KEY_TYPE_SHARED_PREFERENCE);
            }
            if (bundle.getString(JSON_Names.KEY_TITLE_SHARED_PREFERENCE) != null) {
                title = bundle.getString(JSON_Names.KEY_TITLE_SHARED_PREFERENCE);
            }
            if (bundle.getString(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE) != null) {
                image_url = bundle.getString(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE);
            }
            if (bundle.getString("Filter") != null) {
                filter = bundle.getString("Filter");
            }
        }


        baseURLProductList = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_Page;

        baseURLCategory = URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language() + URL_Class.mCategory_id + mCategory_Id;

        mListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, JSON_Names.KEY_FALSE_BOOLEAN));
        mCategory_recycler.setLayoutManager(new GridLayoutManager(mContext, 2));

        String url[];
        if (mType == (R.string.sort_category)) {

            url = new String[2];
            url[0] = baseURLProductList + mPageCount;
            url[1] = baseURLCategory;
            storeCurrentUrl(baseURLProductList);
            loadData();

        } else if (mType == (R.string.sort_default)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_Default;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_Default + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.sort_name_AZ)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_ByName_ASC;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_ByName_ASC + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.sort_name_ZA)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_ByName_DESC;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_ByName_DESC + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.sort_price_LH)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_ByPrice_ASC;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_ByPrice_ASC + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.sort_price_HL)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_ByPrice_DESC;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_ByPrice_DESC + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.sort_rating_H)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_ByRating_DESC;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_ByRating_DESC + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.sort_rating_L)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_ByRating_ASC;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_ByRating_ASC + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.sort_model_AZ)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_ByModel_ASC;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_ByModel_ASC + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.sort_model_ZA)) {

            url = new String[2];
            mPageCount = 1;
            url[0] = baseURLProductList + mPageCount + URL_Class.mURL_GetCategory_SortList_ByModel_DESC;
            url[1] = baseURLCategory;
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                    URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetCategory_SortList_ByModel_DESC + URL_Class.mURL_Page);
            loadData();

        } else if (mType == (R.string.filter)) {

            url = new String[2];
            mPageCount = 1;
            if (filter != null) {
                url[0] = URL_Class.mURL + URL_Class.mURL_GetCategory_List + Methods.current_language() +
                        URL_Class.mCategory_id + mCategory_Id + URL_Class.mURL_Limit + URL_Class.mURL_GetFilter_For_Filter_Id + filter + URL_Class.mURL_Page + mPageCount;
            } else {
                url[0] = baseURLProductList + mPageCount;
            }
            url[1] = baseURLCategory;
            storeCurrentUrl(baseURLProductList);
            loadData();

        } else if (mType == 11) {

            url = new String[1];
            mFeatured = true;
            mListView.setVisibility(View.GONE);
            filter_and_sorting_holder.setVisibility(View.GONE);
            url[0] = URL_Class.mURL + URL_Class.mURL_FeaturedProduct + Methods.current_language();
            mCategory_banner.setVisibility(View.GONE);

        } else if (mType == 22) {

            url = new String[1];
            mPageCount = 1;
            mListView.setVisibility(View.GONE);
            filter_and_sorting_holder.setVisibility(View.GONE);
            url[0] = URL_Class.mURL + URL_Class.mURL_LatestProduct + Methods.current_language();
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_LatestProduct + Methods.current_language());
            mCategory_banner.setVisibility(View.GONE);

        } else if (mType == 33) {

            url = new String[1];
            mPageCount = 1;
            mListView.setVisibility(View.GONE);
            filter_and_sorting_holder.setVisibility(View.GONE);
            url[0] = URL_Class.mURL + URL_Class.mURL_SpecialProduct + Methods.current_language();
            storeCurrentUrl(URL_Class.mURL + URL_Class.mURL_SpecialProduct + Methods.current_language());
            mCategory_banner.setVisibility(View.GONE);

        } else {
            mListView.setVisibility(View.GONE);
            filter_and_sorting_holder.setVisibility(View.GONE);
            url = new String[0];
        }


        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (!mFeatured) {
                progressBar.setVisibility(View.VISIBLE);
                new API_Get().get_method(url, api_resultGet, "", JSON_Names.KEY_GET_TYPE, true, mContext, "CategoryListing");
            } else {
                new API_Get().get_method(url, api_resultGet, "", JSON_Names.KEY_GET_TYPE, true, mContext, "SpecialFeaturedLatest");
            }
        } else {
            Intent intent = new Intent(Category_Details.this, NoInternetConnection.class);
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
                    "ar".equals(AppLanguageSupport.getLanguage(Category_Details.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public void storeCurrentUrl(String url) {
        mCurrentURL = url;
    }

    public void loadData() {
        if (image_url != null) {
            if (image_url.contains("no_image")) {
                mCategory_banner.setVisibility(View.GONE);
            } else {
                Methods.glide_image_loader(image_url, mCategory_banner);
            }
        } else {
            mCategory_banner.setVisibility(View.GONE);
        }
    }

    public void gridRecyclerView() {
        mCategory_recycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        sendType(true);
    }

    public void sendType(boolean type) {

        current_value = type;
        adapter = new Product_Listing_Gird_And_List(Category_Details.this, mCategory_Details, type, wishListAPIRequest, mCategory_recycler,false,false);
        mCategory_recycler.setAdapter(adapter);
        if (!mFeatured && mCategory_Details != null) {
            adapter.setOnLoadMoreListener(new Product_Listing_Gird_And_List.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPageCount++;
                    String current_count = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_COUNT);
                    if (current_count != null)
                        if (mPageCount <= ((Integer.valueOf(current_count) / 6) + 1)) {
                            String url[] = new String[1];
                            if (mCurrentURL != null)
                                if (filter != null) {
                                    url[0] = mCurrentURL + mPageCount + URL_Class.mURL_GetFilter_For_Filter_Id + filter;
                                } else {
                                    url[0] = mCurrentURL + mPageCount;
                                }
                            mCategory_Details.add(null);
                            adapter.notifyItemInserted(mCategory_Details.size() - 1);

                            new API_Get().get_method(url, api_resultGet, "", JSON_Names.KEY_GET_TYPE, true, mContext, "PageCalling");
                        }
                }
            });
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

    public void listRecyclerView() {
        mCategory_recycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, JSON_Names.KEY_FALSE_BOOLEAN));
        sendType(JSON_Names.KEY_FALSE_BOOLEAN);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.category_filter:
                Intent intent = new Intent(Category_Details.this, Filter_Activity.class);
                intent.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mCategory_Id);
                intent.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, image_url);
                intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, title);
                startActivity(intent);
                //finish();
                break;
            case R.id.category_sorting:
                sorting_popup_window(v);
                break;
            case R.id.category_type_of_view:
                if (i == 0) {
                    mCategory_viewType.setImageResource(R.drawable.ic_list_black_24dp);
                    listRecyclerView();
                    i = 1;
                } else {
                    mCategory_viewType.setImageResource(R.drawable.ic_view_module_black_24dp);
                    gridRecyclerView();
                    i = 0;
                }
                break;
            case R.id.cart_count_value:
                Intent i = new Intent(Category_Details.this, Cart.class);
                startActivity(i);
                break;
            case R.id.sorting_default:
                storeData(R.string.sort_default);
                break;
            case R.id.sorting_name_AZ:
                storeData(R.string.sort_name_AZ);
                break;
            case R.id.sorting_name_ZA:
                storeData(R.string.sort_name_ZA);
                break;
            case R.id.sorting_price_LH:
                storeData(R.string.sort_price_LH);
                break;
            case R.id.sorting_price_HL:
                storeData(R.string.sort_price_HL);
                break;
            case R.id.sorting_rating_H:
                storeData(R.string.sort_rating_H);
                break;
            case R.id.sorting_rating_L:
                storeData(R.string.sort_rating_L);
                break;
            case R.id.sorting_model_AZ:
                storeData(R.string.sort_model_AZ);
                break;
            case R.id.sorting_model_ZA:
                storeData(R.string.sort_model_ZA);
                break;
        }
    }

    public void storeData(int type) {
        mSortingList.dismiss();
        Intent sorting = new Intent(Category_Details.this, Category_Details.class);
        sorting.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mCategory_Id);
        sorting.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, type);
        sorting.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, image_url);
        sorting.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, title);
        startActivity(sorting);
        //finish();
    }

    public void sorting_popup_window(View v) {
        mSortingList = new PopupWindow(Category_Details.this);
        View view = getLayoutInflater().inflate(R.layout.category_sorting_list, null);
        mSortingList.setContentView(view);

        sort_default = view.findViewById(R.id.sorting_default);
        sort_name_AZ = view.findViewById(R.id.sorting_name_AZ);
        sort_name_ZA = view.findViewById(R.id.sorting_name_ZA);
        sort_price_LH = view.findViewById(R.id.sorting_price_LH);
        sort_price_HL = view.findViewById(R.id.sorting_price_HL);
        sort_rating_H = view.findViewById(R.id.sorting_rating_H);
        sort_rating_L = view.findViewById(R.id.sorting_rating_L);
        sort_model_AZ = view.findViewById(R.id.sorting_model_AZ);
        sort_model_ZA = view.findViewById(R.id.sorting_model_ZA);

        mSortingList.setHeight(LayoutParams.WRAP_CONTENT);
        mSortingList.setWidth(LayoutParams.WRAP_CONTENT);
        mSortingList.setOutsideTouchable(true);
        mSortingList.setFocusable(true);
        mSortingList.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mSortingList.showAtLocation(v, Gravity.CENTER, 0, 0);

        sort_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSortingList.dismiss();
            }
        });
        sort_name_AZ.setOnClickListener(this);
        sort_name_ZA.setOnClickListener(this);
        sort_price_LH.setOnClickListener(this);
        sort_price_HL.setOnClickListener(this);
        sort_rating_H.setOnClickListener(this);
        sort_rating_L.setOnClickListener(this);
        sort_model_AZ.setOnClickListener(this);
        sort_model_ZA.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);
        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
            menu.findItem(R.id.my_order).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
            menu.findItem(R.id.logout).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
        } else {
            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
            menu.findItem(R.id.login).setVisible(JSON_Names.KEY_FALSE_BOOLEAN);
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
            textView.setText(tempData);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Category_Details.this, Cart.class);
                    startActivity(intent);
                }
            });
            imageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Intent intent = new Intent(Category_Details.this, Cart.class);
                    startActivity(intent);
                    return false;
                }
            });
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        update(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        } else if (id == R.id.login) {
            Intent intent = new Intent(Category_Details.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(Category_Details.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            onBackPressed();
            finish();
        } else if (id == R.id.search) {
            Intent intent = new Intent(Category_Details.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_WISH_LIST);
                Intent intent = new Intent(Category_Details.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Category_Details.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(Category_Details.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(Category_Details.this, OrderHistory.class);
            startActivity(intent);
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "Category", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        super.onRestart();
    }

    private void setting(String[] data) {
        if (data != null) {
            if (/*mType == 11 || */mType == 22 || mType == 33) {
                mCategory_Details = GetJSONData.getProductDetailsThree(data[0]);
            } else {
                mCategory_Details = GetJSONData.getProductDetails(data[0]);
                mList = GetJSONData.getSubCategoryList(data[1]);
                if (mList != null) {
                    if (mList.size() > 0) {
                        String array[] = Methods.getArrayList(mList);
                        if (array != null) {
                            mSubCategoryTitle.setVisibility(View.VISIBLE);
                            Category_Sub_List_Adapter arrayAdapter = new Category_Sub_List_Adapter(array, mContext);

                            mListView.setAdapter(arrayAdapter);
                            mListView.addOnItemTouchListener(new RecyclerViewClickEventHandler(mContext, new RecyclerViewClickEventHandler.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_NAME + "sub_category" + mCategory_Id, "1");
                                    Intent intent = new Intent(Category_Details.this, Category_Details.class);
                                    intent.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mList.get(position).getCategory_id());
                                    intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, R.string.sort_category);
                                    intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, mList.get(position).getName());
                                    intent.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, mList.get(position).getImage());
                                    startActivity(intent);
                                    //finish();
                                }
                            }));
                        } else {
                            hide_title_and_list();
                        }
                    } else {
                        hide_title_and_list();
                    }
                } else {
                    hide_title_and_list();
                }
            }
            sendType(JSON_Names.KEY_TRUE_BOOLEAN);
            progressBar.setVisibility(View.GONE);
            mCategory_filter.setOnClickListener(this);
            mCategory_sort_by.setOnClickListener(this);
            mCategory_viewType.setOnClickListener(this);
        } else {
            sendType(false);
            Methods.toast(getResources().getString(R.string.error));
            finish();
        }
    }

    private void hide_title_and_list() {
        mSubCategoryTitle.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            switch (source) {
                case "CategoryListing":
                    setting(data);
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
                case "SpecialFeaturedLatest":
                    mCategory_Details = GetJSONData.getProductDetailsThree(data[0]);
                    sendType(JSON_Names.KEY_TRUE_BOOLEAN);
                    break;
                case "PageCalling":
                    if (!mCategory_Details.isEmpty()) {
                        mCategory_Details.remove(mCategory_Details.size() - 1);
                        adapter.notifyItemRemoved(mCategory_Details.size());

                        if (/*mType == 11 ||*/ mType == 22 || mType == 33) {
                            if (GetJSONData.getProductDetailsThree(data[0]) != null) {
                                ArrayList<ProductDataSet> mTemp = GetJSONData.getProductDetailsThree(data[0]);
                                if (mTemp != null)
                                    for (int i = 0; i < mTemp.size(); i++) {
                                        mCategory_Details.add(mTemp.get(i));
                                    }
                            }
                        } else {
                            if (GetJSONData.getProductDetails(data[0]) != null) {
                                ArrayList<ProductDataSet> mTemp = GetJSONData.getProductDetails(data[0]);
                                if (mTemp != null)
                                    for (int i = 0; i < mTemp.size(); i++) {
                                        mCategory_Details.add(mTemp.get(i));
                                    }
                            }
                        }
                        adapter.notifyItemInserted(mCategory_Details.size());
                        adapter.setLoaded();
                    }
                    break;
            }
        }
    }

    @Override
    public void wish_list_api_request(String data, String[] url,Boolean isAdd) {
        if(isAdd){
            new API_Get().get_method(url, api_resultGet, data, JSON_Names.KEY_POST_TYPE, true,
                    getBaseContext(), "WishListAdd");
        }else {
            new API_Get().get_method(url, api_resultGet, data, JSON_Names.KEY_POST_TYPE, true,
                    getBaseContext(), "WishListRemove");
        }

    }


    @Override
    public void transaction(String product_id, ImageView transaction_view, String image_url) {
        Intent intent = new Intent(Category_Details.this, Product_Details.class);
        intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, product_id);
        intent.putExtra(JSON_Names.KEY_IMAGE, image_url);
        startActivity(intent);
    }
}
