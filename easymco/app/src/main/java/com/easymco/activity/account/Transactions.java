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
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.Search;
import com.easymco.activity.SplashScreen;
import com.easymco.activity.Wish_List;
import com.easymco.activity.user.Login;
import com.easymco.activity.user.SignUp;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.mechanism.Methods;
import com.easymco.models.TransactionData;

import java.util.ArrayList;

public class Transactions extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Toolbar toolbar;
    Button btn_transac_continue;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<TransactionData> mTransactionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_transactions);

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_transac_continue = (Button) findViewById(R.id.btn_transac_continue);

        btn_transac_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_transactions);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(Transactions.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        for (int i = 0; i < 8; i++) {
            TransactionData transactionData = new TransactionData("26/01/2016", "The Description Details!", "5");
            mTransactionsList.add(transactionData);
        }

        mAdapter = new TransactionsAdapter(mTransactionsList);
        mRecyclerView.setAdapter(mAdapter);
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
                    "ar".equals(AppLanguageSupport.getLanguage(Transactions.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

        private ArrayList<TransactionData> mDatasetTransactions;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtDateAdded;
            TextView txtDiscriptions;
            TextView txtAmtUSD;


            public ViewHolder(View v) {
                super(v);
                txtDateAdded = v.findViewById(R.id.tv_transactions_date_add_data);
                txtDiscriptions = v.findViewById(R.id.tv_transactions_description_data);
                txtAmtUSD = v.findViewById(R.id.tv_transactions_amt_usd_data);
            }
        }

        TransactionsAdapter(ArrayList<TransactionData> mytransactions) {
            this.mDatasetTransactions = mytransactions;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_account_transactions_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.txtDateAdded.setText(mDatasetTransactions.get(position).getmDateAdded());
            holder.txtDiscriptions.setText(mDatasetTransactions.get(position).getmDiscription());
            holder.txtAmtUSD.setText(mDatasetTransactions.get(position).getmAmtUSD());
        }

        @Override
        public int getItemCount() {
            return this.mDatasetTransactions.size();
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
            Intent intent = new Intent(Transactions.this, Login.class);
            startActivity(intent);
            //finish();
        } else if (id == R.id.search) {
            Intent intent = new Intent(Transactions.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(Transactions.this, SignUp.class);
            startActivity(intent);
            //finish();
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            Intent intent = new Intent(Transactions.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(Transactions.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Transactions.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(Transactions.this, OrderHistory.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "Transactions", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }

}
