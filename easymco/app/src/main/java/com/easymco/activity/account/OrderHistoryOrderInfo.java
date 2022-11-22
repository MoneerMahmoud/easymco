package com.easymco.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.Cart;
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
import com.easymco.models.OrderHistoryData;

import java.util.ArrayList;

public class OrderHistoryOrderInfo extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Toolbar toolbar;
    private ArrayList<OrderHistoryData> ROrdersProductList = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<OrderHistoryData>>> ROrdersPOptionsList = new ArrayList<>();
    private ArrayList<OrderHistoryData> ROrdersOTotalsList = new ArrayList<>();
    LinearLayout mItemsContainer,mShippingContainer,mActionContainer;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_order_history_order_info);

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActionContainer = findViewById(R.id.layout_l_order_his_o_info_actions_container);
        mItemsContainer = findViewById(R.id.layout_l_order_his_o_info_action_items);
        mShippingContainer = findViewById(R.id.layout_l_order_his_o_info_action_shipping);
        mShippingContainer.setVisibility(View.GONE);

        TextView txtShippingTitle = (TextView) findViewById(R.id.order_detail_product_shipping_title);
        TextView txtShippingValue = (TextView) findViewById(R.id.order_detail_product_shipping_price);

        TextView txtFinalTotalValue = (TextView) findViewById(R.id.order_detail_product_total_price);
        TextView txtFinalTotalCount = (TextView) findViewById(R.id.order_detail_product_total_count);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String count = bundle.getString("orderProductQuantity") + " "+getResources().getString(R.string.items);
            txtFinalTotalCount.setText(count);
            txtFinalTotalValue.setText(bundle.getString("orderTotal"));
        }

        if (ROrdersProductList != null) {
            ROrdersProductList = (ArrayList<OrderHistoryData>) getIntent().getSerializableExtra("OrdersProductList");
        }
        if (ROrdersPOptionsList != null) {
            ROrdersPOptionsList = (ArrayList<ArrayList<ArrayList<OrderHistoryData>>>) getIntent().getSerializableExtra("OrdersPOptionsList");
        }
        if (ROrdersOTotalsList != null) {
            ROrdersOTotalsList = (ArrayList<OrderHistoryData>) getIntent().getSerializableExtra("OrdersOTotalsList");


            for(int s=0;s<ROrdersOTotalsList.size();s++){
                if(ROrdersOTotalsList.get(s).getmCode().equals("shipping")){
                    txtShippingTitle.setText(ROrdersOTotalsList.get(s).getmTitle());
                    txtShippingValue.setText(ROrdersOTotalsList.get(s).getmText());

                }
            }

            if(!txtShippingTitle.getText().toString().isEmpty() &&
                    !txtShippingValue.getText().toString().isEmpty() ){
                mShippingContainer.setVisibility(View.VISIBLE);
                mActionContainer.setGravity(Gravity.CENTER);
            }else {
                mShippingContainer.setVisibility(View.GONE);
                mActionContainer.setGravity(Gravity.CENTER);
            }


        }


        RecyclerView mOrderHistoryHolder = (RecyclerView) findViewById(R.id.layout_l_dynamic_content);
        mOrderHistoryHolder.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        mOrderHistoryHolder.setAdapter(new OrderHistoryAdapter(ROrdersProductList));

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
                    "ar".equals(AppLanguageSupport.getLanguage(OrderHistoryOrderInfo.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
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
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Search.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            Intent intent = new Intent(OrderHistoryOrderInfo.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(OrderHistoryOrderInfo.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(OrderHistoryOrderInfo.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            onBackPressed();
            finish();
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "OrderHistoryOrderInfo", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Cart.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            Intent intent = new Intent(OrderHistoryOrderInfo.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    private class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<OrderHistoryData> mOrderHistoryProductList;

        OrderHistoryAdapter(ArrayList<OrderHistoryData> mOrderProductList) {
            this.mOrderHistoryProductList = mOrderProductList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_account_order_history_order_product_info_row, parent, false);
            return new OrderProductViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            OrderProductViewHolder orderProductViewHolder = (OrderProductViewHolder) holder;
            Methods.glide_image_loader(mOrderHistoryProductList.get(position).getmImageUrl(), orderProductViewHolder.imgProductList);


            String model = ROrdersProductList.get(position).getmModel();
            String quantity = ROrdersProductList.get(position).getmQuantity();

            String connectQuantity = getResources().getString(R.string.tv_quantity) + " : " + quantity;
            String connectModel = getResources().getString(R.string.tv_model) + " : " + model;

            orderProductViewHolder.txtProductName.setText(mOrderHistoryProductList.get(position).getmName());
            if (ROrdersProductList.get(position).getmModel() != null) {
                if (!ROrdersProductList.get(position).getmModel().isEmpty()) {
                    orderProductViewHolder.txtModel.setVisibility(View.VISIBLE);
                    orderProductViewHolder.txtModel.setText(connectModel);
                } else {
                    orderProductViewHolder.txtModel.setVisibility(View.GONE);
                }
            } else {
                orderProductViewHolder.txtModel.setVisibility(View.GONE);
            }

            if (ROrdersProductList.get(position).getmQuantity() != null) {
                if (!ROrdersProductList.get(position).getmQuantity().isEmpty()) {
                    orderProductViewHolder.txtQuantity.setVisibility(View.VISIBLE);
                    orderProductViewHolder.txtQuantity.setText(connectQuantity);
                } else {
                    orderProductViewHolder.txtQuantity.setVisibility(View.GONE);
                }
            } else {
                orderProductViewHolder.txtQuantity.setVisibility(View.GONE);
            }

            orderProductViewHolder.txtPrice.setText(ROrdersProductList.get(position).getmPrice());
            orderProductViewHolder.txtTotal.setText(mOrderHistoryProductList.get(position).getmProductsTotal());


            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            if (!(ROrdersPOptionsList.size() == 0)) {
                int p = 0;
                for (int q = 0; q < ROrdersPOptionsList.get(p).size(); q++)  // product x
                {
                    if (ROrdersPOptionsList.get(p).get(q).size() != 0) {
                        for (int r = 0; r < ROrdersPOptionsList.get(p).get(q).size(); r++)  // product x option r
                        {
                            if ((ROrdersProductList.get(position).getmOrderProductId()).equals(ROrdersPOptionsList.get(p).get(q).get(r).getmOptionsOrderProductId())) {

                                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.cart_option_holder, null, false);
                                TextView mOptionTitle = view.findViewById(R.id.lblListItem);

                                String optionDataString = ROrdersPOptionsList.get(p).get(q).get(r).getmOptionsName() + " : "
                                        + ROrdersPOptionsList.get(p).get(q).get(r).getmOptionsValue();
                                mOptionTitle.setText(optionDataString);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                                params.setMargins(0, 4, 0, 4);
                                view.setLayoutParams(params);
                                orderProductViewHolder.option_holder.addView(view);
                            }
                        }
                    }
                }
            }

        }

        @Override
        public int getItemCount() {
            return this.mOrderHistoryProductList.size();
        }

        class OrderProductViewHolder extends RecyclerView.ViewHolder {

            LinearLayout option_holder;
            TextView txtProductName, txtModel, txtQuantity, txtPrice, txtTotal;
            ImageView imgProductList;

            OrderProductViewHolder(View vw) {
                super(vw);
                imgProductList = vw.findViewById(R.id.imageview_product_list);
                option_holder = vw.findViewById(R.id.layout_l_order_his_options);
                txtProductName = vw.findViewById(R.id.tv_order_his_recycle_title);
                txtModel = vw.findViewById(R.id.tv_order_his_recycle_model_data);
                txtQuantity = vw.findViewById(R.id.tv_order_his_recycle_quantity_data);
                txtPrice = vw.findViewById(R.id.tv_order_his_recycle_price_data);
                txtTotal = vw.findViewById(R.id.tv_order_his_recycle_total_data);
            }
        }

    }

}

