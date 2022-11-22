package com.easymco.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.easymco.models.AccountDataSet;
import com.easymco.models.Response;
import com.easymco.network_checker.NetworkConnection;

import java.util.ArrayList;

public class AddressBook extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, API_Result {

    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<AccountDataSet> profileList = new ArrayList<>();
    Toolbar toolbar;
    Button btn_new_address;
    int country_id;
    int zone_id;
    Boolean value;
    API_Result api_result;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_address_list_activity);
        api_result = this;
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_new_address = (Button) findViewById(R.id.btn_new_adds);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_adds_book);

        btn_new_address.setOnClickListener(v -> {
            Intent ii = new Intent(AddressBook.this, AddsBook_EditAdds.class);
            startActivity(ii);
            finish();
        });
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
                    "ar".equals(AppLanguageSupport.getLanguage(AddressBook.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            cart();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            cart();
        }
        return false;
    }

    public void cart() {
        Intent intent = new Intent(AddressBook.this, Cart.class);
        startActivity(intent);
    }

    public void url() {
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);
            String[] url = {URL_Class.mURL + URL_Class.mURL_Get_Customer_Profile +
                    URL_Class.mCustomer_id + DataBaseHandlerAccount.
                    getInstance(getApplicationContext()).get_customer_id()
                    + "&" + Methods.current_language()};
            new API_Get().get_method(url, api_result, "",
                    JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "CustomerProfile");
        } else {
            Intent intent = new Intent(AddressBook.this, NoInternetConnection.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        url();

        super.onResume();
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
            Intent intent = new Intent(AddressBook.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            Intent intent = new Intent(AddressBook.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(AddressBook.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            Intent intent = new Intent(AddressBook.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(AddressBook.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AddressBook.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(AddressBook.this, OrderHistory.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "AddressBook", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            if (data[0] != null) {
                if (source.equals("CustomerProfile")) {
                    profileList = GetJSONData.getCustomerAddress(data[0]);
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(AddressBook.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new RecyclerViewAdapter(profileList);
                    mRecyclerView.setAdapter(mAdapter);
                } else if (source.equals("AddressDelete")) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(getResources().getString(R.string.adds_successfully_deleted));
                        } else {
                            Methods.toast(getResources().getString(R.string.error));
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

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataObjectHolder> {

        private ArrayList<AccountDataSet> mDataSet;

        RecyclerViewAdapter(ArrayList<AccountDataSet> myDataSet) {
            mDataSet = myDataSet;
        }

        @NonNull
        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_account_address_list_row, parent, false);
            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(final DataObjectHolder holder, int position) {
            final int current_position = position;

            value = mDataSet.get(position).getmAddress_Id().equals(mDataSet.get(position).getmDefaultAddressId());
            holder.hFirstName.setText(mDataSet.get(position).getmFirstName());
            holder.hLastName.setText(mDataSet.get(position).getmLastName());
            if (mDataSet.get(position).getmCompany() != null) {
                if (!mDataSet.get(position).getmCompany().isEmpty()) {
                    holder.hCompany.setText(mDataSet.get(position).getmCompany());
                } else {
                    holder.hCompany.setVisibility(View.GONE);
                }
            } else {
                holder.hCompany.setVisibility(View.GONE);
            }
            holder.hAddress1.setText(mDataSet.get(position).getmAddress_1());
            holder.hCity.setText(mDataSet.get(position).getmCity());
            holder.hPostcode.setText(mDataSet.get(position).getmPostcode());
            holder.hCountry.setText(mDataSet.get(position).getmCountry());
            holder.hRegionstate.setText(mDataSet.get(position).getmState());

            if (mDataSet.get(position).getmCountry_id() != null) {
                country_id = Integer.parseInt(mDataSet.get(position).getmCountry_id());
            } else {
                country_id = 0;
            }
            if (mDataSet.get(position).getmZone_id() != null) {
                zone_id = Integer.parseInt(mDataSet.get(position).getmZone_id());
            } else {
                zone_id = 0;
            }

            holder.editItem.setOnClickListener(v -> {

                Intent i = new Intent(v.getContext(), AddsBook_EditAdds.class);
                i.putExtra(JSON_Names.KEY_ADDRESS_ID, mDataSet.get(current_position).getmAddress_Id());
                i.putExtra(JSON_Names.KEY_CUSTOMER_ID, mDataSet.get(current_position).getmCustomerId());
                i.putExtra(JSON_Names.KEY_FIRST_NAME, holder.hFirstName.getText().toString());
                i.putExtra(JSON_Names.KEY_LAST_NAME, holder.hLastName.getText().toString());
                i.putExtra(JSON_Names.KEY_COMPANY, holder.hCompany.getText().toString());
                i.putExtra(JSON_Names.KEY_ADDRESS_1, holder.hAddress1.getText().toString());
                i.putExtra(JSON_Names.KEY_CITY, holder.hCity.getText().toString());
                i.putExtra(JSON_Names.KEY_POSTCODE, holder.hPostcode.getText().toString());
                i.putExtra(JSON_Names.KEY_COUNTRY_ID, country_id);
                i.putExtra(JSON_Names.KEY_ZONE_ID, zone_id);
                i.putExtra(JSON_Names.KEY_COUNTRY, holder.hCountry.getText().toString());
                i.putExtra(JSON_Names.KEY_STATE, holder.hRegionstate.getText().toString());
                i.putExtra(JSON_Names.KEY_DEFAULT_ADDRESS_ID, value);
                v.getContext().startActivity(i);
                finish();

            });

            holder.deleteItem.setOnClickListener(v -> deleteItem(current_position));
        }

        int deleteItem(int index) {
            String tempAddsId = mDataSet.get(index).getmAddress_Id();
            String tempCustomerId = mDataSet.get(index).getmCustomerId();
            String tempDefAddsID = mDataSet.get(index).getmDefaultAddressId();

            if (!tempAddsId.equals(tempDefAddsID)) {
                if (mDataSet.size() > 1) {
                    mDataSet.remove(index);
                    notifyItemRemoved(index);
                    notifyDataSetChanged();
                    if (NetworkConnection.connectionChecking(getApplicationContext())) {
                        progressBar.setVisibility(View.VISIBLE);
                        String[] url = {URL_Class.mURL + URL_Class.mURL_Delete_Address +
                                URL_Class.mCustomer_id + tempCustomerId + URL_Class.mAddress_Id + tempAddsId};
                        new API_Get().get_method(url, api_result, "",
                                JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "AddressDelete");
                    } else {
                        Intent intent = new Intent(AddressBook.this, NoInternetConnection.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Methods.toast(getResources().getString(R.string.address_warning));
                }
            } else {
                Methods.toast(getResources().getString(R.string.default_address_warning));
            }
            return index;
        }

        @Override
        public int getItemCount() {
            return this.mDataSet.size();
        }

        class DataObjectHolder extends RecyclerView.ViewHolder {

            TextView hFirstName, hLastName, hCompany, hAddress1, hCity, hPostcode, hCountry, hRegionstate;
            Button editItem, deleteItem;

            DataObjectHolder(View view) {
                super(view);
                hFirstName = view.findViewById(R.id.tv_card_first_name);
                hLastName = view.findViewById(R.id.tv_card_last_name);
                hCompany = view.findViewById(R.id.tv_card_company);
                hAddress1 = view.findViewById(R.id.tv_card_address1);
                hCity = view.findViewById(R.id.tv_card_city);
                hPostcode = view.findViewById(R.id.tv_card_post_code);
                hCountry = view.findViewById(R.id.tv_card_country);
                hRegionstate = view.findViewById(R.id.tv_card_state);

                editItem = view.findViewById(R.id.img_btn_edit);
                deleteItem = view.findViewById(R.id.img_btn_delete);
            }
        }
    }
}
