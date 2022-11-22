package com.easymco.activity.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.Cart;
import com.easymco.activity.Network_Error;
import com.easymco.activity.NoInternetConnection;
import com.easymco.activity.Search;
import com.easymco.activity.SplashScreen;
import com.easymco.activity.Wish_List;
import com.easymco.activity.account.MyAccountMainMenu;
import com.easymco.activity.account.OrderHistory;
import com.easymco.activity.user.Login;
import com.easymco.activity.user.SignUp;
import com.easymco.adapter.ProductDetailImageSlider;
import com.easymco.adapter.ProductOptionAdapter;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.db_handler.DataBaseLanguageDetails;
import com.easymco.fragments.product.Product_Review_Post;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.EnquiryPost;
import com.easymco.interfaces.ReviewPost;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductDataSet;
import com.easymco.models.ProductImageDataSet;
import com.easymco.models.ProductOptionDataSet;
import com.easymco.models.Response;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Product_Details extends AppCompatActivity implements
        View.OnClickListener, API_Result, ReviewPost, EnquiryPost {

    Toolbar toolbar;
    Button btn_product_add_to_cart/*, btn_product_buy*/;
    ImageButton img_btn_add_wish_list, img_btn_share;
    TextView txt_product_title, txt_product_price, txt_product_special_price;
    TextView txt_product_color_title, txt_product_write_rating;
    TextView txt_product_detail_details, txt_product_details_no_of_reviews,
            txt_product_description_title;
    LinearLayout mReviewHolder, button_holder;
    RatingBar rating_bar_product;
    RecyclerView option_holder;
    ArrayList<ProductImageDataSet> mImageList = new ArrayList<>();
    ArrayList<ProductOptionDataSet> mOptionList = new ArrayList<>();
    HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChild = new HashMap<>();
    ProductDataSet mProductDataSet = new ProductDataSet();
    ProductOptionAdapter productOptionAdapter;
    String mProduct_String, product_id, mImage_URL;
    HashMap<String, Object> mDataSet = new HashMap<>();
    API_Result api_result;
    EditText quantitySpinner;
    String quantity = "0";
    WebView description;
    ProgressBar progressBar;
    ViewPager product_slide_view;
    Context mContext;
    EnquiryPost enquiryPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        api_result = this;
        enquiryPost = this;
        mContext = getBaseContext();

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_product_add_to_cart = (Button) findViewById(R.id.product_detail_add_to_cart);
        img_btn_add_wish_list = (ImageButton) findViewById(R.id.product_detail_add_to_fav);
        txt_product_title = (TextView) findViewById(R.id.product_detail_title);
        txt_product_price = (TextView) findViewById(R.id.product_detail_price);
        txt_product_special_price = (TextView) findViewById(R.id.product_detail_special_price);
        txt_product_color_title = (TextView) findViewById(R.id.product_detail_color_title);
        txt_product_description_title = (TextView) findViewById(R.id.product_description_title);
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        description = (WebView) findViewById(R.id.product_description);
        quantitySpinner = (EditText) findViewById(R.id.product_detail_quantity_valueT);
        product_slide_view = (ViewPager) findViewById(R.id.product_detail_image_viewer);
        //btn_product_buy = (Button) findViewById(R.id.product_detail_buy);
        img_btn_share = (ImageButton) findViewById(R.id.product_detail_share);

        txt_product_details_no_of_reviews = (TextView) findViewById(R.id.product_detail_no_of_rating_title);
        txt_product_detail_details = (TextView) findViewById(R.id.product_detail_details);
        txt_product_write_rating = (TextView) findViewById(R.id.product_detail_write_rating);

        option_holder = (RecyclerView) findViewById(R.id.product_detail_first_row);
        rating_bar_product = (RatingBar) findViewById(R.id.product_detail_rating_bar);

        mReviewHolder = (LinearLayout) findViewById(R.id.rating_holder);
        button_holder = (LinearLayout) findViewById(R.id.home_slider_view_view_pager_button_holder);


        product_id = getIntent().getExtras().getString(JSON_Names.KEY_PRODUCT_STRING);
        mImage_URL = getIntent().getExtras().getString(JSON_Names.KEY_IMAGE);

        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (NetworkConnection.connectionType(getApplicationContext())) {
                if (product_id != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    String url[] = {URL_Class.mURL + URL_Class.mProductDetail +
                            Methods.current_language() + URL_Class.mProduct_id + product_id};
                    new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true,
                            getBaseContext(), "ProductDetail");
                }
            } else {
                Intent intent = new Intent(Product_Details.this, Network_Error.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(Product_Details.this, NoInternetConnection.class);
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
                    "ar".equals(AppLanguageSupport.getLanguage(Product_Details.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public void option_reset(HashMap<Integer, ArrayList<ProductOptionDataSet>> mOptionListChild) {
        if (mOptionListChild != null) {
            for (int i = 0; i < mOptionListChild.size(); i++) {
                for (int j = 0; j < mOptionListChild.get(i).size(); j++) {
                    mOptionListChild.get(i).get(j).setSelected(false);
                }
            }
        }
    }

    public void clearData(ArrayList<ProductOptionDataSet> mOptionList) {
        if (mOptionList != null) {
            for (int i = 0; i < mOptionList.size(); i++) {
                DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID
                        + mOptionList.get(i).getProduct_option_id());
                DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID
                        + mOptionList.get(i).getProduct_option_id());
            }
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
            t1.setOnClickListener(this);
            t2.setOnClickListener(this);
        } else {
            t2.setVisibility(View.GONE);
        }
    }

    public void wish_list_updater() {
        if (product_id != null)
            if (DataBaseHandlerWishList.getInstance(getApplicationContext()).checking_wish_list(product_id)) {
                img_btn_add_wish_list.setImageResource(R.drawable.ic_favorite_color_accent_24dp);
            } else {
                img_btn_add_wish_list.setImageResource(R.drawable.ic_favorite_border_color_accent_24dp);
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
        update(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            /*onBackPressed();
            finish();*/
            supportFinishAfterTransition();
        } else if (id == R.id.search) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            Intent intent = new Intent(Product_Details.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(Product_Details.this, Login.class);
                startActivity(intent);
            } else {
                if (mOptionList != null) {
                    clearData(mOptionList);
                }
                Intent intent = new Intent(Product_Details.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(Product_Details.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.my_order) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, OrderHistory.class);
            startActivity(intent);
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "Product", "Language", product_id);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.product_detail_add_to_fav) {
            if (NetworkConnection.connectionChecking(getApplicationContext())) {
                if (mProductDataSet != null) {
                    if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                        if (!DataBaseHandlerWishList.getInstance(getApplicationContext()).checking_wish_list(mProductDataSet.getProduct_id())) {
                            current_product_id();
                            DataBaseHandlerWishList.getInstance(getApplicationContext()).add_to_wish_list(mProductDataSet.getProduct_id(), mProductDataSet.getProduct_string());
                            if (get_wish_list_post_data() != null) {
                                String url[] = {URL_Class.mURL + URL_Class.mURL_Add_To_WishList};
                                new API_Get().get_method(url, api_result, get_wish_list_post_data(), JSON_Names.KEY_POST_TYPE, true,
                                        getBaseContext(), "WishListAdd");
                            }
                            img_btn_add_wish_list.setImageResource(R.drawable.ic_favorite_color_accent_24dp);

                        } else {
                            DataBaseHandlerWishList.getInstance(getApplicationContext()).remove_from_wish_list(mProductDataSet.getProduct_id());
                            current_product_id();
                            if (get_wish_list_post_data() != null) {
                                String url[] = {URL_Class.mURL + URL_Class.mURL_Remove_WishList};
                                new API_Get().get_method(url, api_result, get_wish_list_post_data(), JSON_Names.KEY_POST_TYPE, true,
                                        getBaseContext(), "WishListRemove");
                            }
                            img_btn_add_wish_list.setImageResource(R.drawable.ic_favorite_border_color_accent_24dp);

                        }
                    } else {
                        Intent intent = new Intent(Product_Details.this, Login.class);
                        current_product_id();
                        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_PRODUCT_DETAIL);
                        startActivity(intent);

                    }
                }
            } else {
                Intent intent = new Intent(Product_Details.this, NoInternetConnection.class);
                startActivity(intent);
                finish();
            }

        } else if (id == R.id.product_detail_details) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Product_Detail_And_Description.class);
            if (mProduct_String != null)
                intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, mProduct_String);
            startActivity(intent);
        } else if (id == R.id.cart_count_value) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.product_detail_write_rating) {
            if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    Product_Review_Post product_review_post = Product_Review_Post.getInstant(mProductDataSet.getProduct_id());
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.product_detail_review_post_holder, product_review_post, "Review");
                    fragmentTransaction.addToBackStack("Review");
                    fragmentTransaction.commit();
                }
            } else {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(Product_Details.this, Login.class);
                startActivity(intent);
            }
        } else if (id == R.id.cart_image_view) {
            if (mOptionList != null) {
                clearData(mOptionList);
            }
            Intent intent = new Intent(Product_Details.this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.product_detail_no_of_rating_title) {
            intentTransfer("Review");
        }
    }

    public void current_product_id() {
        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mProductDataSet.getProduct_id());
    }

    public void intentTransfer(String from) {
        if (mOptionList != null) {
            clearData(mOptionList);
        }
        Intent intent = new Intent(Product_Details.this, ImageFullView.class);
        intent.putExtra(JSON_Names.KEY_FROM, from);
        if (mProduct_String != null)
            intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, mProduct_String);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
        supportFinishAfterTransition();
    }

    @Override
    protected void onResume() {
        wish_list_updater();
        invalidateOptionsMenu();
        super.onResume();
    }

    @SuppressWarnings("unchecked")
    public void setting(String data) {
        if (data != null) {
            mDataSet = GetJSONData.getSeparateProductDetail(data);
        }
        if (mDataSet != null) {
            mProductDataSet = (ProductDataSet) mDataSet.get(JSON_Names.KEY_PD_PRODUCT);
            mImageList = mProductDataSet.getProductImageDataSetsList();
            mOptionList = (ArrayList<ProductOptionDataSet>) mDataSet.get(JSON_Names.KEY_PD_OPTION);
            mOptionListChild = (HashMap<Integer, ArrayList<ProductOptionDataSet>>) mDataSet.get(JSON_Names.KEY_PD_OPTION_CHILD);
        }
        //Any process or calculation should start after this method call
        if (mOptionList != null) {
            clearData(mOptionList);
        }

        if (mProductDataSet != null) {

            // btn_product_buy.setOnClickListener(this);
            mProduct_String = mProductDataSet.getProduct_string();

            img_btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mProductDataSet != null) {
                        if (mProductDataSet.getProduct_id() != null) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, URL_Class.mShare + mProductDataSet.getProduct_id());
                            startActivity(Intent.createChooser(intent, "Share"));
                        }
                    }
                }
            });

            current_product_id();
            wish_list_updater();


            if (mProductDataSet.getMinimum() != null) {
                quantitySpinner.setText(mProductDataSet.getMinimum());
            } else {
                quantitySpinner.setText("1");
            }

            img_btn_add_wish_list.setOnClickListener(this);
            txt_product_title.setText(mProductDataSet.getTitle());

            if (mProductDataSet.getDescription() != null) {
                if (!mProductDataSet.getDescription().isEmpty()) {
                    try {

                        String url = URL_Class.mDescription + mProductDataSet.getProduct_id()
                                + "&" + Methods.current_language();
                        description.loadUrl(url);

                        WebSettings setting = description.getSettings();
                        setting.setJavaScriptEnabled(true);
                        setting.setDomStorageEnabled(true);
                        setting.setJavaScriptCanOpenWindowsAutomatically(true);
                        setting.setAllowContentAccess(true);

                        WebClientClass webViewClient = new WebClientClass();
                        description.setWebViewClient(webViewClient);
                        WebChromeClient webChromeClient = new WebChromeClient();
                        description.setWebChromeClient(webChromeClient);
                    } catch (Exception e) {
                        txt_product_description_title.setVisibility(View.GONE);
                        description.setVisibility(View.GONE);
                        Methods.toast(e.toString());
                    }
                } else {
                    txt_product_description_title.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
                }
            } else {
                txt_product_description_title.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
            }

            if (mProductDataSet.getSpecial_price() != null) {
                if (!mProductDataSet.getSpecial_price().isEmpty()) {
                    txt_product_price.setText(mProductDataSet.getPrice());
                    txt_product_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    txt_product_special_price.setText(mProductDataSet.getSpecial_price());
                } else {
                    txt_product_special_price.setText(mProductDataSet.getPrice());
                    txt_product_price.setVisibility(View.GONE);
                }
            } else {
                txt_product_special_price.setText(mProductDataSet.getPrice());
                txt_product_price.setVisibility(View.GONE);
            }
        }

        ProductDetailImageSlider adapter_slideView;
        if (mImageList != null && mProductDataSet != null) {
            if (mImageList.size() != 0) {


                final String image_list[] = new String[mImageList.size() + 1];
                if (mProductDataSet.getBig_image() != null){
                    image_list[0] = mProductDataSet.getBig_image();
                }else {
                    image_list[0] = mProductDataSet.getImage();
                }

                for (int i = 1; i <= mImageList.size(); i++) {
                    image_list[i] = mImageList.get(i - 1).getChildImage();
                }

                adapter_slideView = new ProductDetailImageSlider(mContext, image_list, enquiryPost);
                product_slide_view.setAdapter(adapter_slideView);

                int pos = product_slide_view.getCurrentItem();

                for (int i = 0; i < image_list.length; i++) {
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
                        button.setLayoutParams(buttonParams);
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
                            product_slide_view.setCurrentItem(button.getId());
                        }
                    });

                    LinearLayout.LayoutParams layoutParamsMargin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParamsMargin.setMargins(2, 0, 0, 0);
                    layoutParamsMargin.gravity = Gravity.BOTTOM;
                    button_holder.setLayoutParams(layoutParamsMargin);
                    button_holder.setGravity(Gravity.CENTER);
                    button_holder.addView(button);
                }


                product_slide_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        button_holder.removeAllViews();

                        if (image_list.length > 0) {
                            for (int i = 0; i < image_list.length; i++) {
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
                                        product_slide_view.setCurrentItem(button.getId());
                                    }
                                });
                                button_holder.addView(button);
                            }
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            } else {
                String image_list_1[] = new String[1];
                if (mProductDataSet.getBig_image() != null) {
                    image_list_1[0] = mProductDataSet.getBig_image();
                } else {
                    image_list_1[0] = mProductDataSet.getImage();
                }

                adapter_slideView = new ProductDetailImageSlider(mContext, image_list_1, enquiryPost);
                product_slide_view.setAdapter(adapter_slideView);
            }
        } else {
            product_slide_view.setVisibility(View.GONE);
        }

        if(DataBaseLanguageDetails.getInstance(getApplicationContext()).check_language_selected()){
            String languageId = DataBaseLanguageDetails.getInstance(getApplicationContext()).get_language_id();
            if(languageId.equals("1")){
                txt_product_detail_details.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.ic_keyboard_arrow_right_grey_500_24dp), null);
            }else {

                txt_product_detail_details.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_keyboard_arrow_left_grey_500_24dp), null,
                        null, null);
            }

        }

        txt_product_detail_details.setOnClickListener(this);
        txt_product_write_rating.setOnClickListener(this);

        if (mProductDataSet != null) {
            if (mProductDataSet.getRating() != null && !mProductDataSet.getRating().isEmpty()) {//Null pointer exception possible
                rating_bar_product.setRating(Float.valueOf(mProductDataSet.getRating()));
            } else {
                rating_bar_product.setVisibility(View.GONE);
            }
            if (mProductDataSet.getRating() != null) {
                if (!mProductDataSet.getRating().isEmpty()) {
                    rating_bar_product.setRating(Float.valueOf(mProductDataSet.getRating()));
                    if (mProductDataSet.getNo_of_review() != null) {
                        if (!mProductDataSet.getNo_of_review().isEmpty()) {
                            String review = "( " + mProductDataSet.getNo_of_review() + " " + getResources().getString(R.string.reviews) + " )";
                            txt_product_details_no_of_reviews.setText(review);
                            txt_product_details_no_of_reviews.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                            txt_product_details_no_of_reviews.setOnClickListener(this);
                        }
                    } else {
                        mReviewHolder.setVisibility(View.GONE);
                    }
                } else {
                    mReviewHolder.setVisibility(View.GONE);
                }
            } else {
                mReviewHolder.setVisibility(View.GONE);
            }

            productOptionAdapter = new ProductOptionAdapter(getApplicationContext(), mOptionList, mOptionListChild, mProductDataSet.getProduct_id(), Product_Details.this);
            option_holder.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, JSON_Names.KEY_FALSE_BOOLEAN));
            option_holder.setAdapter(productOptionAdapter);
        }

        btn_product_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut("Add to cart");
            }
        });

       /* btn_product_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOut("Buy");
            }
        });*/

    }

   /* private void directCheckOut() {
        Intent intent;
        if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            intent = new Intent(Product_Details.this, Check_Out.class);

        } else {
            intent = new Intent(Product_Details.this, Login.class);
        }
        startActivity(intent);
    }*/

    private void checkOut(String from) {
        if (!quantitySpinner.getText().toString().isEmpty()) {
            if (!quantitySpinner.getText().toString().equals("0")) {
                quantity = quantitySpinner.getText().toString();
            } else {
                quantity = "0";
            }
        } else {
            quantity = "0";
        }

        if (Integer.valueOf(quantity) > 0 || !quantity.isEmpty()) {

            String dummy = "";
            int i = 0, j = 0;
            if (mOptionList != null) {
                while (i < mOptionList.size()) {
                    //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                    String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                    String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                    if (temp1 == null && temp2 == null) {
                        if (j == 0) {
                            dummy += mOptionList.get(i).getProduct_option_name();
                            j++;
                        } else {
                            dummy += " " + getResources().getString(R.string.and) + " " + mOptionList.get(i).getProduct_option_name();
                        }
                    }
                    i++;
                    //}
                }
            }
            if (dummy.length() != 0) {
                Methods.toast(getResources().getString(R.string.please_select) + " " + dummy);
            } else {
                Integer minimum_value = Integer.valueOf(quantity);
                //Check with option quantity for add to cart
                int minimum_option_value = get_minimum_option_size();
                if (minimum_value <= minimum_option_value || minimum_option_value == -1) {
                    if (mProductDataSet != null) {
                        if (minimum_value < Integer.valueOf(mProductDataSet.getMinimum())) {
                            String minimum = getResources().getString(R.string.quantity_check_1) + " " + mProductDataSet.getTitle() +" " +
                                    getResources().getString(R.string.quantity_check_2) +" " + mProductDataSet.getMinimum();
                            Methods.toast(minimum);
                        } else {
                            if (DataBaseHandlerCart.getInstance(getApplicationContext()).get_Size_cart() == 0) {
                                Methods.toast(getResources().getString(R.string.add_to_cart_success));
                                DataBaseHandlerCart.getInstance(getApplicationContext()).add_to_cart(1, mProductDataSet.getProduct_id(), minimum_value, mProduct_String, minimum_option_value);
                                if (mOptionList != null) {
                                    i = 0;
                                    while (i < mOptionList.size()) {
                                        //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                        String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                                JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                        String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                                                JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                        DataBaseHandlerCartOptions.getInstance(getApplicationContext()).add_options(1, temp1, temp2,
                                                mProductDataSet.getProduct_id());
                                        i++;
                                        //}
                                    }
                                }
                               /* if (from.equals("Buy")) {
                                    directCheckOut();
                                }*/
                            } else {
                                if (DataBaseHandlerCart.getInstance(getApplicationContext()).checking_cart(mProductDataSet.getProduct_id())) {
                                    ArrayList<Integer> list = DataBaseHandlerCart.getInstance(getApplicationContext()).get_index(mProductDataSet.getProduct_id());
                                    int count = DataBaseHandlerCart.getInstance(getApplicationContext()).getProductCount(list);
                                    if (count <= Integer.valueOf(mProductDataSet.getQuantity())) {
                                        if (list != null) {
                                            int check = DataBaseHandlerCart.getInstance(getApplicationContext()).getLastIndex();
                                            if (check != 0) {
                                                int index = check + 1;
                                                ArrayList<Integer[]> list1 = new ArrayList<>();
                                                if (mOptionList != null) {
                                                    i = 0;
                                                    while (i < mOptionList.size()) {
                                                        //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                        String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                        String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                        Integer value[] = {Integer.valueOf(temp1), Integer.valueOf(temp2)};
                                                        list1.add(value);
                                                        i++;
                                                        //}
                                                    }
                                                }
                                                int reference = 0;
                                                for (int k = 0; k < list.size(); k++) {
                                                    if (DataBaseHandlerCartOptions.getInstance(getApplicationContext()).check_index(list.get(k), list1)) {
                                                        reference = list.get(k);
                                                    }
                                                }
                                                if (reference != 0) {
                                                    if (minimum_value + DataBaseHandlerCart.getInstance(getApplicationContext())
                                                            .get_product_count(reference) <= minimum_option_value) {
                                                        DataBaseHandlerCart.getInstance(getApplicationContext()).update_product_count(reference, minimum_value);
                                                        Methods.toast(getResources().getString(R.string.add_to_cart_success));
                                                       /* if (from.equals("Buy")) {
                                                            directCheckOut();
                                                        }*/
                                                    } else {
                                                        Methods.toast(getResources().getString(R.string.quantity_error_message));
                                                    }
                                                } else {
                                                    DataBaseHandlerCart.getInstance(getApplicationContext()).add_to_cart(index, mProductDataSet.getProduct_id(), minimum_value, mProduct_String, minimum_option_value);
                                                    Methods.toast(getResources().getString(R.string.add_to_cart_success));
                                                    if (mOptionList != null) {
                                                        i = 0;
                                                        while (i < mOptionList.size()) {
                                                            //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                            String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                            String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).add_options(index, temp1, temp2, mProductDataSet.getProduct_id());
                                                            i++;
                                                            // }
                                                        }
                                                    }
                                                  /*  if (from.equals("Buy")) {
                                                        directCheckOut();
                                                    }*/
                                                }
                                            }
                                        }
                                    } else {
                                        Methods.toast(getResources().getString(R.string.quantity_error_message));
                                    }

                                } else {
                                    int check = DataBaseHandlerCart.getInstance(getApplicationContext()).getLastIndex();
                                    if (check != 0) {
                                        int index = check + 1;
                                        DataBaseHandlerCart.getInstance(getApplicationContext()).add_to_cart(index, mProductDataSet.getProduct_id(), minimum_value, mProduct_String, minimum_option_value);
                                        Methods.toast(getResources().getString(R.string.add_to_cart_success));
                                        if (mOptionList != null) {
                                            i = 0;
                                            while (i < mOptionList.size()) {
                                                //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                                                String temp1 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionList.get(i).getProduct_option_id());
                                                String temp2 = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());
                                                DataBaseHandlerCartOptions.getInstance(getApplicationContext()).add_options(index, temp1, temp2, mProductDataSet.getProduct_id());
                                                i++;
                                                //}
                                            }
                                        }
                                       /* if (from.equals("Buy")) {
                                            directCheckOut();
                                        }*/
                                    }
                                }
                            }
                            option_reset(mOptionListChild);
                            if (mOptionList != null) {
                                clearData(mOptionList);
                            }
                            productOptionAdapter.notifyDataSetChanged();
                            invalidateOptionsMenu();
                        }
                    } else {
                        Methods.toast(getResources().getString(R.string.quantity_error_message));
                    }
                } else {
                    String minimum = getResources().getString(R.string.quantity_check_1) + " "
                            + mProductDataSet.getTitle() +" " +
                            getResources().getString(R.string.quantity_check_2) + " " + minimum_option_value;
                    Methods.toast(minimum);
                }
            }
        } else {
            String minimum = getResources().getString(R.string.quantity_check_1) + " "
                    + mProductDataSet.getTitle() +" " +
                    getResources().getString(R.string.quantity_check_2) + " " + get_minimum_option_size();
            Methods.toast(minimum);
        }
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "ProductDetail":
                        setting(data[0]);
                        break;
                    case "WishListAdd":
                        wish_list_setting(data[0],true);
                        break;
                    case "WishListRemove":
                        wish_list_setting(data[0],false);
                        break;
                    case "ProductReviewPost":
                        Response response = GetJSONData.getResponse(data[0]);
                        if (response != null) {
                            if (response.getmStatus() == 200) {
                                toast_call(getResources().getString(R.string.success));
                                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            } else {
                                toast_call(getResources().getString(R.string.failure));
                                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                        } else {
                            toast_call(getResources().getString(R.string.failure));
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        break;
                }
            }
        }

    }

    public void toast_call(String id) {
        Methods.toast(id);
    }

    public String get_wish_list_post_data() {

        String mProduct_id;
        String mCustomer_id;
        try {
            mProduct_id = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
            mCustomer_id = String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id());
            return URLEncoder.encode(JSON_Names.KEY_PRODUCT_ID, URL_Class.mConvertType) +
                    URL_Class.mEqual_Symbol +
                    URLEncoder.encode(mProduct_id, URL_Class.mConvertType) +
                    URL_Class.mAnd_Symbol +
                    URLEncoder.encode(JSON_Names.KEY_USER_ID, URL_Class.mConvertType) +
                    URL_Class.mEqual_Symbol +
                    URLEncoder.encode(mCustomer_id, URL_Class.mConvertType);
        } catch (Exception e) {
            return null;
        }
    }


    public void wish_list_setting(String data,Boolean isAdd) {
        if (data != null) {
            Response response = GetJSONData.getResponse(data);
            if (response != null) {
                if (response.getmStatus() == 200) {
                    DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
                    // Methods.toast(response.getmMessage());
                    if(isAdd){
                        Methods.toast(getResources().getString(R.string.successfully_added));
                    }else {
                        Methods.toast(getResources().getString(R.string.successfully_removed));
                    }
                } else {
                    Methods.toast(response.getmMessage());
                }
            }
        } else {
            Methods.toast(getResources().getString(R.string.error));
        }
    }

    @Override
    public void review_post(String data) {
        progressBar.setVisibility(View.VISIBLE);
        String url[] = {URL_Class.mURL + URL_Class.mURL_Review_Post};
        new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE, true,
                getBaseContext(), "ProductReviewPost");
    }

    /*Get Minimum option quantity for add to cart*/

    public int get_minimum_option_size() {
        int list[] = new int[mOptionList.size()];
        if (mOptionList != null && mOptionList.size() > 0) {
            for (int i = 0; i < mOptionList.size(); i++) {
                //if (mOptionList.get(i).getProduct_option_required().equals(JSON_Names.KEY_REQUIRED_VALUE)) {
                String option_id = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(),
                        JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionList.get(i).getProduct_option_id());

                for (int j = 0; j < mOptionListChild.get(i).size(); j++) {
                    if (option_id.equals(mOptionListChild.get(i).get(j).getProduct_option_value_id())) {
                        list[i] = Integer.valueOf(mOptionListChild.get(i).get(j).getProduct_option_quantity());
                    }
                }

                // }
            }
            if (list.length != 0) {
                Arrays.sort(list);
            }
            return list.length > 0 ? list[0] : -1;
        } else {
            return Integer.valueOf(mProductDataSet.getQuantity());
        }
    }

    @Override
    public void enquiryPost(String data) {
        intentTransfer("image");
    }

    private class WebClientClass extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }

}