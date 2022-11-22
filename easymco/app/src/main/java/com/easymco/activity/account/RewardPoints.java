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
import android.widget.Button;
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
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.RewardPointsData;
import com.easymco.network_checker.NetworkConnection;

import java.util.ArrayList;

public class RewardPoints extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, API_Result {

    Toolbar toolbar;
    Button btn_rwd_points_continue;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    API_Result api_result;
    ProgressBar progressBar;
    private TextView mRewardTotal;
    TextView listEmpty,total_reward_points_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_reward_points);

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        api_result = this;

        btn_rwd_points_continue = (Button) findViewById(R.id.btn_reward_pnt_continue);
        listEmpty = (TextView) findViewById(R.id.tv_reward_pnt_empty);
        listEmpty.setVisibility(View.GONE);
        total_reward_points_title = findViewById(R.id.tv_total_no_reward_points);
        total_reward_points_title.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        mRewardTotal = (TextView) findViewById(R.id.tv_total_no_reward_points_amt);
        btn_rwd_points_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_reward_points);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(RewardPoints.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);
            String url[] = {URL_Class.mURL + URL_Class.mGetCustomerRewardPoints+"&" + URL_Class.mCustomer_id
                    + String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id())
                    +"&"+Methods.current_language() };
            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "CustomerRewardPoints");
        } else {
            Intent intent = new Intent(RewardPoints.this, NoInternetConnection.class);
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
                    "ar".equals(AppLanguageSupport.getLanguage(RewardPoints.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(RewardPoints.this, Cart.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            Intent intent = new Intent(RewardPoints.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "CustomerRewardPoints":
                        setting(data[0]);
                        break;
                }
            }
        }

    }

    private void setting(String data) {
        ArrayList<RewardPointsData> mRewardPointsList = GetJSONData.getRewardPointData(data);

        if(mRewardPointsList != null && mRewardPointsList.size() > 0){
            mRecyclerView.setVisibility(View.VISIBLE);
            listEmpty.setVisibility(View.GONE);
            mRewardTotal.setVisibility(View.VISIBLE);
            total_reward_points_title.setVisibility(View.VISIBLE);
            mRewardTotal.setText(GetJSONData.getRewardPointTotal(data));

            mAdapter = new RewardPointsAdapter(mRewardPointsList);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mRewardTotal.setVisibility(View.GONE);
            total_reward_points_title.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            listEmpty.setVisibility(View.VISIBLE);

        }
    }

    class RewardPointsAdapter extends RecyclerView.Adapter<RewardPointsAdapter.ViewHolder> {

        private ArrayList<RewardPointsData> mDatasetrewardpoints;

        class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView txtDateAdded;
            TextView txtDiscriptions;
            TextView txtPoints;


            public ViewHolder(View v) {
                super(v);
                txtDateAdded = v.findViewById(R.id.tv_reward_points_date_add_data);
                txtDiscriptions = v.findViewById(R.id.tv_reward_points_description_data);
                txtPoints = v.findViewById(R.id.tv_reward_points_points_data);
            }
        }

        RewardPointsAdapter(ArrayList<RewardPointsData> myrewardpoints) {
            this.mDatasetrewardpoints = myrewardpoints;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_account_reward_points_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.txtDateAdded.setText(mDatasetrewardpoints.get(position).getmDateAdded());
            holder.txtDiscriptions.setText(mDatasetrewardpoints.get(position).getmDescription());
            holder.txtPoints.setText(mDatasetrewardpoints.get(position).getmPoints());
        }

        @Override
        public int getItemCount() {
            return this.mDatasetrewardpoints.size();
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
            onBackPressed();
            finish();
        } else if (id == R.id.login) {
            Intent intent = new Intent(RewardPoints.this, Login.class);
            startActivity(intent);
            //finish();
        } else if (id == R.id.register) {
            Intent intent = new Intent(RewardPoints.this, SignUp.class);
            startActivity(intent);
            //finish();
        } else if (id == R.id.search) {
            Intent intent = new Intent(RewardPoints.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            Intent intent = new Intent(RewardPoints.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(RewardPoints.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(RewardPoints.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(RewardPoints.this, OrderHistory.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "RewardPoints", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }
}
