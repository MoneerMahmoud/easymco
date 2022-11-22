package com.easymco.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.interfaces.Refresher;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.CartDataSet;
import com.easymco.models.FilterDataSet;

import java.util.ArrayList;
import java.util.HashMap;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity mContext;
    private int CUSTOM_VIEW = 1, TOTAL_VIEW = 2, DISCOUNT_VIEW = 3;
    private Refresher refresher_refresher;
    private ArrayList<CartDataSet> mResultDataSet;
    private HashMap<String, ArrayList<String[]>> mCartOptionData;
    private ArrayList<FilterDataSet> mList = new ArrayList<>();
    private int mMaxRewardPoint = 0, mCustomerRewardPoints = 0;

    public CartAdapter(Activity context, ArrayList<CartDataSet> mDataSet, Refresher refresher, String result, ArrayList<FilterDataSet> list) {
        this.mContext = context;
        this.mResultDataSet = mDataSet;
        this.refresher_refresher = refresher;
        if (result != null) {
            this.mCartOptionData = GetJSONData.get_cart_options(result);
            this.mMaxRewardPoint = GetJSONData.get_cart_max_reward_point(result);
            this.mCustomerRewardPoints = GetJSONData.get_cart_customer_reward_point(result);
        }
        this.mList = list;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == CUSTOM_VIEW) {
            View view = inflater.inflate(R.layout.cart_row, parent, false);
            viewHolder = new ViewHolder_Cart_List(view);
        } else if (viewType == TOTAL_VIEW) {
            viewHolder = new TotalListHolder(LayoutInflater.from(mContext).inflate(R.layout.product_detail_specification_values, parent, false));
        } else if (viewType == DISCOUNT_VIEW) {
            viewHolder = new CartDiscountHolder(LayoutInflater.from(mContext).inflate(R.layout.discount_container, parent, false));
        } else {
            View view = inflater.inflate(R.layout.no_data, parent, false);
            viewHolder = new ViewHolderEmpty_View(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        int EMPTY_VIEW = 0;
        if (mResultDataSet != null) {
            if (mResultDataSet.size() > 0) {
                if (mResultDataSet.size() > position) {
                    return CUSTOM_VIEW;
                } else if (mResultDataSet.size() == position) {
                    return DISCOUNT_VIEW;
                } else {
                    return TOTAL_VIEW;
                }
            } else {
                return EMPTY_VIEW;
            }
        } else {
            return EMPTY_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        if (mResultDataSet != null) {
            if (mResultDataSet.size() > 0) {
                return mResultDataSet.size() + mList.size() + 1;
            } else {
                return 1;
            }

        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == CUSTOM_VIEW) {
            ViewHolder_Cart_List holder_cart_list = (ViewHolder_Cart_List) holder;
            Methods.glide_image_loader(mResultDataSet.get(position).getImage(), holder_cart_list.mCartImage);
            holder_cart_list.mCartTitle.setText(mResultDataSet.get(position).getTitle());
            String price = mResultDataSet.get(position).getQuantity() + " * " + mResultDataSet.get(position).getPrice();
            holder_cart_list.mCartPrice.setText(price);
            holder_cart_list.mCartProductCount.setText(mResultDataSet.get(position).getQuantity());
            if (!mResultDataSet.get(position).isLoad())
                if (mCartOptionData.get(mResultDataSet.get(position).getProduct_id() + position) != null) {

                    for (int i = 0; i < mCartOptionData.get(mResultDataSet.get(position).getProduct_id() + position).size(); i++) {
                        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_option_holder, null, false);
                        TextView mOptionTitle = view.findViewById(R.id.lblListItem);
                        String optionDataStringArray[] = mCartOptionData.get(mResultDataSet.get(position).getProduct_id() + position).get(i);
                        String optionDataString = optionDataStringArray[0] + " : " + optionDataStringArray[1];
                        mOptionTitle.setText(optionDataString);
                        holder_cart_list.mCartOptionHolder.addView(view);
                    }

                }

            if (!mResultDataSet.get(position).getStock_status()) {
                holder_cart_list.mCartErrorHolder.setVisibility(View.VISIBLE);
            } else {
                holder_cart_list.mCartErrorHolder.setVisibility(View.GONE);
            }

        } else if (holder.getItemViewType() == TOTAL_VIEW) {
            TotalListHolder totalListHolder = (TotalListHolder) holder;
            if (mList.get(position - (mResultDataSet.size() + 1)) != null) {
                totalListHolder.mListTitle.setText(mList.get(position - (mResultDataSet.size() + 1)).getmName());
                totalListHolder.mListValue.setText(mList.get(position - (mResultDataSet.size() + 1)).getmFilterId());
            }

          /*  int tempPosition = getItemCount()-position;
            //Log.e(position+" P "+tempPosition,"IS "+mList.get(position - (mResultDataSet.size())).getmName());
            if(mList.size() == 3){

                if(tempPosition == 3){

                    totalListHolder.mListTitle.setText(R.string.cart_sub_total);
                    totalListHolder.mListValue.setText(mList.get(position - (mResultDataSet.size())).getmFilterId());

                }else if(tempPosition == 2){

                    totalListHolder.mListTitle.setText(mList.get(position - (mResultDataSet.size())).getmName());
                    totalListHolder.mListValue.setText(mList.get(position - (mResultDataSet.size())).getmFilterId());

                }else if(tempPosition == 1){

                    totalListHolder.mListTitle.setText(R.string.cart_total);
                    totalListHolder.mListValue.setText(mList.get(position - (mResultDataSet.size())).getmFilterId());

                }

            }else if(mList.size() == 2){

                if(tempPosition == 2){

                    totalListHolder.mListTitle.setText(R.string.cart_sub_total);
                    totalListHolder.mListValue.setText(mList.get(position - (mResultDataSet.size())).getmFilterId());

                }else if(tempPosition == 1){

                    totalListHolder.mListTitle.setText(R.string.cart_total);
                    totalListHolder.mListValue.setText(mList.get(position - (mResultDataSet.size())).getmFilterId());

                }

            }*/

        } else if (holder.getItemViewType() == DISCOUNT_VIEW) {
            CartDiscountHolder cartDiscountHolder = (CartDiscountHolder) holder;
            cartDiscountHolder.mCouponHandler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Methods.CouponCode(mContext);
                }
            });
            cartDiscountHolder.mGiftVoucherHandler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Methods.GiftVoucher(mContext);
                }
            });
            if (DataBaseHandlerAccount.getInstance(mContext).check_login()) {

                if (mMaxRewardPoint != 0 && mCustomerRewardPoints != 0) {
                    cartDiscountHolder.mRewardPointHandler.setVisibility(View.VISIBLE);
                    cartDiscountHolder.mRewardPointHandler.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Methods.RewardPoints(mContext, mMaxRewardPoint, mCustomerRewardPoints);
                        }
                    });
                } else {
                    cartDiscountHolder.mRewardPointHandler.setVisibility(View.GONE);
                }
            } else {
                cartDiscountHolder.mRewardPointHandler.setVisibility(View.GONE);
            }
        } else {
            ViewHolderEmpty_View empty_view = (ViewHolderEmpty_View) holder;
            empty_view.empty_view.setText(R.string.empty_cart);
        }
    }

    public void delete(int index, int position) {
        DataBaseHandlerCartOptions.getInstance(mContext.getApplicationContext()).remove_from_options(index);
        DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).remove_cart(index);
        mResultDataSet.remove(position);
    }

    private void refresher() {
        refresher_refresher.refresher();
    }

    private class ViewHolderEmpty_View extends RecyclerView.ViewHolder {
        TextView empty_view;

        ViewHolderEmpty_View(View view) {
            super(view);
            empty_view = view.findViewById(R.id.empty_view);
        }
    }

    private class ViewHolder_Cart_List extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCartImage;
        TextView mCartTitle, mCartPrice, mCartProductCount, mCartErrorHolder;
        ImageButton mCartRemoveFromCart, mCartAddProductCount, mCartRemoveProductCount;
        LinearLayout mCartOptionHolder;

        ViewHolder_Cart_List(View view) {
            super(view);
            mCartImage = view.findViewById(R.id.cart_product_image);
            mCartTitle = view.findViewById(R.id.cart_product_title);
            mCartPrice = view.findViewById(R.id.cart_product_price);
            mCartProductCount = view.findViewById(R.id.cart_product_count);
            mCartRemoveFromCart = view.findViewById(R.id.cart_product_remove_item);
            mCartAddProductCount = view.findViewById(R.id.cart_product_count_add);
            mCartRemoveProductCount = view.findViewById(R.id.cart_product_count_remove);
            mCartOptionHolder = view.findViewById(R.id.cart_option_holder);
            mCartErrorHolder = view.findViewById(R.id.cart_out_of_stock);
            mCartRemoveFromCart.setOnClickListener(this);
            mCartAddProductCount.setOnClickListener(this);
            mCartRemoveProductCount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.cart_product_remove_item:
                    delete(mResultDataSet.get(getAdapterPosition()).getIndex(), getAdapterPosition());
                    refresher();
                    break;
                case R.id.cart_product_count_add:
                    if (mResultDataSet.get(getAdapterPosition()).getStock_status()) {
                        int value_add = mResultDataSet.get(getAdapterPosition()).getIndex();
                        DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).update_product_count(value_add, 1);
                        refresher();
                    } else {
                        Methods.toast(mContext.getResources().getString(R.string.quantity_error_message));
                    }
                    break;
                case R.id.cart_product_count_remove:
                    int value_remove = mResultDataSet.get(getAdapterPosition()).getIndex();
                    if (DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).get_product_count(value_remove) >
                            mResultDataSet.get(getAdapterPosition()).getMinimum()) {
                        DataBaseHandlerCart.getInstance(mContext.getApplicationContext()).update_product_count(value_remove, -1);
                        notifyDataSetChanged();
                        refresher();
                    } else {
                        String minimum = mContext.getResources().getString(R.string.select_minimum_1) + " " +
                                mResultDataSet.get(getAdapterPosition()).getTitle() +" " +
                                mContext.getResources().getString(R.string.select_minimum_2) + " " +
                                mResultDataSet.get(getAdapterPosition()).getMinimum();
                        Methods.toast(minimum);
                    }
                    break;
            }

        }
    }

    private class TotalListHolder extends RecyclerView.ViewHolder {
        TextView mListTitle, mListValue;

        TotalListHolder(View itemView) {
            super(itemView);
            mListTitle = itemView.findViewById(R.id.product_detail_value_name);
            mListValue = itemView.findViewById(R.id.product_detail_value_name_value);
        }
    }

    private class CartDiscountHolder extends RecyclerView.ViewHolder {
        TextView mCouponHandler, mGiftVoucherHandler, mRewardPointHandler;

        CartDiscountHolder(View itemView) {
            super(itemView);
            mCouponHandler = itemView.findViewById(R.id.discount_coupon);
            mGiftVoucherHandler = itemView.findViewById(R.id.discount_gift_voucher);
            mRewardPointHandler = itemView.findViewById(R.id.discount_reward_points);
        }
    }
}
