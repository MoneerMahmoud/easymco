package com.easymco.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.account.MyAccountMainMenu;
import com.easymco.activity.account.OrderHistory;
import com.easymco.activity.product.Product_Details;
import com.easymco.activity.user.Login;
import com.easymco.activity.user.SignUp;
import com.easymco.adapter.Adapter_SlideView;
import com.easymco.adapter.HomeNavigationAdapter;
import com.easymco.adapter.Home_Adapter;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.db_handler.DataBaseStorageData;
import com.easymco.fragments.NavigationDrawerFragment;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.BannerHandler;
import com.easymco.interfaces.WishListAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.CategoryDataSet;
import com.easymco.models.ProductDataSet;
import com.easymco.network_checker.NetworkConnection;

import java.util.ArrayList;
import java.util.HashMap;


public class Home extends AppCompatActivity implements
        View.OnClickListener, View.OnTouchListener, API_Result, WishListAPIRequest, BannerHandler {

    Context mContext;

    final String url[] = {URL_Class.mURL + URL_Class.mURL_LatestProduct + Methods.current_language() + URL_Class.mURL_Limit_Special + URL_Class.mURL_Page + "1",
            URL_Class.mURL + URL_Class.mURL_SpecialProduct + Methods.current_language() + "&limit=3" + URL_Class.mURL_Page + "1"/*,
            URL_Class.mURL + URL_Class.mURL_FeaturedProduct + Methods.current_language()*/};

    Toolbar toolbar;
    ProgressBar progressBar;
    RecyclerView homeViewHolder, homeNavigation;
    API_Result result;
    TextView mHomeNewSearchBar;
    String slide1[], mNavigationData, mBannerData;
    ArrayList<ProductDataSet> list = new ArrayList<>();
    HashMap<Integer, ArrayList<ProductDataSet>> whole_list = new HashMap<>();
    WishListAPIRequest wishListAPIRequest;
    ViewPager slide_view;
    BannerHandler bannerHandler;
    LinearLayout banner_btn_holder;
    private Handler handler;
    private int delay = 4000;
    Adapter_SlideView adapter_slideView;
    private int page = 0;
    Runnable runnable = new Runnable() {
        public void run() {
            if (adapter_slideView != null) {
                if (adapter_slideView.getCount() == page) {
                    page = 0;
                } else {
                    page++;
                }
            } else {
                page = 0;
            }
            if (slide_view != null)
                slide_view.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);


        if (DataBaseStorageData.getInstance(getApplicationContext()).check_storage_data_presented()) {

            mNavigationData = DataBaseStorageData.getInstance(getApplicationContext()).get_navi_data();
            mBannerData = DataBaseStorageData.getInstance(getApplicationContext()).get_banner_data();
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        homeViewHolder = (RecyclerView) findViewById(R.id.home_list_recycler_view);
        homeNavigation = (RecyclerView) findViewById(R.id.home_navigation_holder);
        mHomeNewSearchBar = (TextView) findViewById(R.id.home_fk_like_search_bar);

        result = this;
        wishListAPIRequest = this;
        bannerHandler = this;

        handler = new Handler();

        if (mBannerData != null) {
            slide1 = GetJSONData.get_slide(mBannerData);
        }

        NavigationDrawerFragment navigationDrawerFragment =
                (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        mContext = Home.this;

        homeViewHolder.setLayoutManager(new LinearLayoutManager(this));
        homeNavigation.setLayoutManager(new LinearLayoutManager(mContext));
        mHomeNewSearchBar.setOnClickListener(this);

        action_NavigationData();

        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (NetworkConnection.connectionType(getApplicationContext())) {
                progressBar.setVisibility(View.VISIBLE);
                new API_Get().get_method(url, result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "Home");
            } else {
                two_g_transfer();
            }
        } else {
            Intent intent = new Intent(Home.this, NoInternetConnection.class);
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
                    "ar".equals(AppLanguageSupport.getLanguage(Home.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public void action_NavigationData() {
        if (mNavigationData != null)
            homeNavigation.setAdapter(new HomeNavigationAdapter(mContext, mNavigationData, Home.this));
    }

    public void navigation_reset() {
        if (NavigationDrawerFragment.mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            NavigationDrawerFragment.mdrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);

        menu.findItem(R.id.search).setVisible(false);

        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(false);
            menu.findItem(R.id.my_order).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else {
            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.login) {
            login_transfer();
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(Home.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(Home.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(Home.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            invalidateOptionsMenu();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                login_transfer();
            } else {
                Intent intent = new Intent(Home.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(Home.this, OrderHistory.class);
            startActivity(intent);
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "Home", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            cart_transfer();
        } else if (id == R.id.home_fk_like_search_bar) {
            Intent intent = new Intent(Home.this, Search.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            cart_transfer();
        }
        return false;
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        navigation_reset();
        super.onResume();
        if (slide1 != null) {
            if (slide1.length > 0) {
                handler.postDelayed(runnable, delay);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (slide1 != null) {
            if (slide1.length > 0) {
                handler.removeCallbacks(runnable);
            }
        }
    }

    public void login_transfer() {
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
    }

    public void cart_transfer() {
        Intent intent = new Intent(Home.this, Cart.class);
        startActivity(intent);
    }

    public void two_g_transfer() {
        Intent intent = new Intent(Home.this, Network_Error.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (source.equals("Home")) {
            if (data != null) {
                setting(data);
            } else {
                two_g_transfer();
            }
        }
    }

    private void setting(String[] data) {
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] != null) {
                    list = GetJSONData.getHomeData(data[i]);
                    if (list != null) {
                        if (!list.isEmpty()) {
                            if (i == 0) {
                                list.get(0).setHeading(getResources().getString(R.string.latest));
                            } else if (i == 1) {
                                list.get(0).setHeading(getResources().getString(R.string.special));
                            } else if (i == 2) {
                                list.get(0).setHeading(getResources().getString(R.string.featured));
                            }
                            whole_list.put(i, list);
                        }
                    }
                }
            }
            final ArrayList<Object> mHomeViewOrder = new ArrayList<>();

            if (slide1 != null) {
                if (slide1.length > 0)
                    mHomeViewOrder.add(slide1);
            }

            ArrayList<CategoryDataSet> mHomeCategoryListForHomeDesign;
            mHomeCategoryListForHomeDesign = GetJSONData.getHomeCategoryData(mNavigationData);
            if (mHomeCategoryListForHomeDesign != null) {
                if (mHomeCategoryListForHomeDesign.size() > 1) {
                    mHomeViewOrder.add(true);
                }
            }
            if (whole_list != null) {
                for (int i = 0; i <= whole_list.size(); i++) {
                    if (whole_list.get(i) != null) {
                        mHomeViewOrder.add(whole_list.get(i));
                    }
                }
            }
            mHomeViewOrder.add(1);

            homeViewHolder.setAdapter(new Home_Adapter(mContext, mHomeViewOrder, mNavigationData, wishListAPIRequest, bannerHandler));
            homeViewHolder.setItemAnimator(new DefaultItemAnimator());

        } else {
            Methods.toast(getResources().getString(R.string.error));
            two_g_transfer();
        }
    }

    @Override
    public void wish_list_api_request(String data, String[] url,Boolean isAdd) {

    }

    @Override
    public void transaction(String product_id, ImageView transaction_view, String image_url) {
        Intent intent = new Intent(Home.this, Product_Details.class);
        intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, product_id);
        intent.putExtra(JSON_Names.KEY_IMAGE, image_url);
        startActivity(intent);
    }

    @Override
    public void bannerTransfer(ViewPager viewPager, LinearLayout button_holder) {
        slide_view = viewPager;
        banner_btn_holder = button_holder;
        adapter_slideView = new Adapter_SlideView(mContext, slide1);
        slide_view.setAdapter(adapter_slideView);
        int pos = slide_view.getCurrentItem();
        for (int i = 0; i < slide1.length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_slide_button, null, false);
            final Button button = view.findViewById(R.id.btn_one);
            button.setPadding(100, 100, 100, 100);
            int size = (int) mContext.getResources().getDisplayMetrics().density;
            if (size == 3) {
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(30,30);
                buttonParams.setMargins(5,0,5,0);
                button.setLayoutParams(buttonParams);
            } else if (size == 2) {
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(22,22);
                buttonParams.setMargins(5,0,5,0);
                button.setLayoutParams(buttonParams)    ;
            } else {
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(18,18);
                buttonParams.setMargins(5,0,5,0);
                button.setLayoutParams(buttonParams);
            }
            if (pos == i) {
                button.setBackgroundResource(R.drawable.btn_slider_bg);
            } else {
                button.setBackgroundResource(R.drawable.round_button);
            }
            button.setId(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slide_view.setCurrentItem(button.getId());
                }
            });
            banner_btn_holder.addView(button);
        }


        slide_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                banner_btn_holder.removeAllViews();
                if (slide1 != null) {
                    if (slide1.length > 0) {
                        for (int i = 0; i < slide1.length; i++) {
                            View view = LayoutInflater.from(mContext).inflate(R.layout.home_slide_button, null, false);
                            final Button button = view.findViewById(R.id.btn_one);
                            int size = (int) mContext.getResources().getDisplayMetrics().density;
                            if (size == 3) {
                                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(30,30);
                                buttonParams.setMargins(5,0,5,0);
                                button.setLayoutParams(buttonParams);
                            } else if (size == 2) {
                                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(22,22);
                                buttonParams.setMargins(5,0,5,0);
                                button.setLayoutParams(buttonParams);
                            } else {
                                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(18,18);
                                buttonParams.setMargins(5,0,5,0);
                                button.setLayoutParams(buttonParams);
                            }
                            if (position == i) {
                                button.setBackgroundResource(R.drawable.btn_slider_bg);
                            } else {
                                button.setBackgroundResource(R.drawable.round_button);
                            }
                            button.setId(i);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    slide_view.setCurrentItem(button.getId());
                                }
                            });
                            banner_btn_holder.addView(button);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
