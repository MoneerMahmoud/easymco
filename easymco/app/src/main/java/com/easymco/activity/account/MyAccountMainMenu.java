package com.easymco.activity.account;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
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
import com.easymco.db_handler.DataBaseLanguageDetails;
import com.easymco.fragments.account.AccountChangePassword;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.AccountListener;
import com.easymco.interfaces.EnquiryPost;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.ItemForMultipleSelection;
import com.easymco.models.Response;
import com.easymco.network_checker.NetworkConnection;

import java.util.ArrayList;

public class MyAccountMainMenu extends AppCompatActivity implements View.OnTouchListener,
        View.OnClickListener, EnquiryPost, API_Result, AccountListener {

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    API_Result api_result;
    ProgressBar progressBar;
    private ArrayList<ItemForMultipleSelection> mMyAcMainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_activity);
        api_result = this;
        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.fragment_multiple_selection_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MyAccountMainMenu.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        progressBar = findViewById(R.id.progressBar_my_account_main);
        progressBar.setVisibility(View.GONE);

        mAdapter = new MyACMainMenuAdapter(getMyAMMenuList());
        mRecyclerView.setAdapter(mAdapter);
    }

    public ArrayList<ItemForMultipleSelection> getMyAMMenuList() {

        for (int i = 0; i < 3; i++) {

            ItemForMultipleSelection itemForMSelection = new ItemForMultipleSelection();

            if (i == 0) {
                itemForMSelection.setmMyAccount(getResources().getString(R.string.menu_account));
                itemForMSelection.setmEditAcInfo(getResources().getString(R.string.menu_edit_ac));
                itemForMSelection.setmChangePwd(getResources().getString(R.string.menu_change_pwd));
                itemForMSelection.setmModifyAddsBook(getResources().getString(R.string.menu_modify_adds));
                itemForMSelection.setId(i);
            }
            if (i == 1) {
                itemForMSelection.setmMyOrders(getResources().getString(R.string.menu_order));
                itemForMSelection.setmViewOrderH(getResources().getString(R.string.menu_order_history));
                itemForMSelection.setmRewardPoints(getResources().getString(R.string.menu_reward_points));
                //itemForMSelection.setmTransactions(getResources().getString(R.string.menu_transactions));
                itemForMSelection.setId(i);
            }
            if (i == 2) {
                itemForMSelection.setmExtra(getResources().getString(R.string.menu_extra));
                itemForMSelection.setmContactUS(getResources().getString(R.string.menu_contact_us));
                itemForMSelection.setmAboutUS(getResources().getString(R.string.menu_about_us));
                itemForMSelection.setId(i);
            }
            mMyAcMainList.add(itemForMSelection);

        }
        return mMyAcMainList;
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
                    "ar".equals(AppLanguageSupport.getLanguage(MyAccountMainMenu.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(MyAccountMainMenu.this, Cart.class);
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
            Intent intent = new Intent(MyAccountMainMenu.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void loadChangePassword() {
        AccountChangePassword accountChangePassword = new AccountChangePassword();
        accountChangePassword.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        accountChangePassword.show(getFragmentManager(), "ChangePassword");
    }

    @Override
    public void postChangePassword(String data) {
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            if (data != null) {
                progressBar.setVisibility(View.VISIBLE);
                String[] url = {URL_Class.mURL + URL_Class.mURL_Change_Password};
                new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE,
                        true, getBaseContext(), "ChangePassword");
            }
        } else {
            Intent intent = new Intent(MyAccountMainMenu.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    class MyACMainMenuAdapter extends RecyclerView.Adapter<MyACMainMenuAdapter.ViewHolder> {

        private ArrayList<ItemForMultipleSelection> mDatasetMyAcMMenu;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mPrimaryTextView, mText1, mText2, mText3, mText4;

            public ViewHolder(final View itemView) {
                super(itemView);

                mPrimaryTextView = itemView.findViewById(R.id.list_item_primary_text);
                mText1 = itemView.findViewById(R.id.tv_text_1);
                mText2 = itemView.findViewById(R.id.tv_text_2);
                mText3 = itemView.findViewById(R.id.tv_text_3);
                mText4 = itemView.findViewById(R.id.tv_text_4);

                if(DataBaseLanguageDetails.getInstance(getApplicationContext()).check_language_selected()){
                    String languageId = DataBaseLanguageDetails.getInstance(getApplicationContext()).get_language_id();
                    if(languageId.equals("1")){
                        mText1.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                ContextCompat.getDrawable(getApplicationContext(),
                                        R.drawable.ic_keyboard_arrow_right_grey_500_24dp), null);

                        mText2.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                ContextCompat.getDrawable(getApplicationContext(),
                                        R.drawable.ic_keyboard_arrow_right_grey_500_24dp), null);

                        mText3.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                ContextCompat.getDrawable(getApplicationContext(),
                                        R.drawable.ic_keyboard_arrow_right_grey_500_24dp), null);

                        mText3.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                ContextCompat.getDrawable(getApplicationContext(),
                                        R.drawable.ic_keyboard_arrow_right_grey_500_24dp), null);

                    }else {

                        mText1.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.ic_keyboard_arrow_left_grey_500_24dp), null,
                                null, null);

                        mText2.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.ic_keyboard_arrow_left_grey_500_24dp), null,
                                null, null);

                        mText3.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.ic_keyboard_arrow_left_grey_500_24dp), null,
                                null, null);

                        mText4.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.ic_keyboard_arrow_left_grey_500_24dp), null,
                                null, null);


                    }

                }

            }
        }

        MyACMainMenuAdapter(ArrayList<ItemForMultipleSelection> mytransactions) {
            this.mDatasetMyAcMMenu = mytransactions;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_account_list_row, parent, false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {

            if (i == 0) {
                viewHolder.mPrimaryTextView.setText(mDatasetMyAcMMenu.get(i).getmMyAccount());
                viewHolder.mText1.setText(mDatasetMyAcMMenu.get(i).getmEditAcInfo());
                viewHolder.mText2.setText(mDatasetMyAcMMenu.get(i).getmChangePwd());
                viewHolder.mText3.setText(mDatasetMyAcMMenu.get(i).getmModifyAddsBook());

                viewHolder.mText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), MyAccountInfo.class);
                        startActivity(ie);
                    }
                });

                viewHolder.mText2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadChangePassword();
                    }
                });

                viewHolder.mText3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), AddressBook.class);
                        startActivity(ie);
                    }
                });

                viewHolder.mText4.setVisibility(View.GONE);
            }
            if (i == 1) {
                viewHolder.mPrimaryTextView.setText(mDatasetMyAcMMenu.get(i).getmMyOrders());
                viewHolder.mText1.setText(mDatasetMyAcMMenu.get(i).getmViewOrderH());
                viewHolder.mText2.setText(mDatasetMyAcMMenu.get(i).getmRewardPoints());
                //viewHolder.mText3.setText(mDatasetMyAcMMenu.get(i).getmTransactions());


                viewHolder.mText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), OrderHistory.class);
                        startActivity(ie);
                    }
                });


                viewHolder.mText2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), RewardPoints.class);
                        startActivity(ie);
                    }
                });


                /*viewHolder.mText3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ie = new Intent(getApplicationContext(), Transactions.class);
                        startActivity(ie);
                    }
                });*/

                viewHolder.mText3.setVisibility(View.GONE);
                viewHolder.mText4.setVisibility(View.GONE);
            }
            if (i == 2) {
                viewHolder.mPrimaryTextView.setText(mDatasetMyAcMMenu.get(i).getmExtra());
                viewHolder.mText1.setText(mDatasetMyAcMMenu.get(i).getmAboutUS());
                viewHolder.mText2.setText(mDatasetMyAcMMenu.get(i).getmContactUS());

                viewHolder.mText1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Methods.AboutUs(getFragmentManager());
                    }
                });

                viewHolder.mText2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Methods.ContactUs(getFragmentManager());
                    }
                });

                viewHolder.mText3.setVisibility(View.GONE);
                viewHolder.mText4.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return this.mDatasetMyAcMMenu.size();
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
            Methods.toast(getResources().getString(R.string.already_in_your_account));
        } else if (id == R.id.login) {
            Intent intent = new Intent(MyAccountMainMenu.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(MyAccountMainMenu.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(MyAccountMainMenu.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            Intent intent = new Intent(MyAccountMainMenu.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(MyAccountMainMenu.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MyAccountMainMenu.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(MyAccountMainMenu.this, OrderHistory.class);
            startActivity(intent);
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "MyAccountMainMenu", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enquiryPost(String data) {
        if (NetworkConnection.connectionChecking(getBaseContext())) {
            if (NetworkConnection.connectionType(getBaseContext())) {
                progressBar.setVisibility(View.VISIBLE);
                String[] url = {URL_Class.mURL + URL_Class.mContact_Us};
                new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE, false, getBaseContext(), "EnquiryPost");
            } else {
                internetErrorCaller();
            }
        } else {
            internetErrorCaller();
        }
    }

    private void internetErrorCaller() {
        Intent intent = new Intent(MyAccountMainMenu.this, NoInternetConnection.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void result(String[] data, String source) {
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "EnquiryPost": {
                        Response responseEnquiryPost = GetJSONData.getResponse(data[0]);
                        if (responseEnquiryPost != null) {
                            if (responseEnquiryPost.getmStatus() == 200) {
                               // Methods.toast(responseEnquiryPost.getmMessage());
                                Methods.toast(getResources().getString(R.string.mail_successfully_send));
                            } else {
                                Methods.toast(responseEnquiryPost.getmMessage());
                            }
                        } else {
                            Methods.toast(getResources().getString(R.string.error));
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    break;
                    case "ChangePassword": {
                        Response response = GetJSONData.getResponse(data[0]);
                        if (response != null) {
                            if (response.getmStatus() == 200) {
                                Methods.toast(getResources().getString(R.string.change_password_success));
                            } else {
                                Methods.toast(response.getmMessage());
                            }
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    break;
                }
            }
        }
    }
}
