package com.easymco.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.product.Product_Details;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.interfaces.WishListAPIRequest;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductDataSet;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.net.URLEncoder;
import java.util.ArrayList;

public class Wish_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<ProductDataSet> mDataSet;
    private int CUSTOM_VIEW = 1;
    private WishListAPIRequest wishListAPIRequest;

    public Wish_List_Adapter(Context context, ArrayList<ProductDataSet> dataSets, WishListAPIRequest wishListAPIRequest) {
        this.mContext = context;
        this.mDataSet = dataSets;
        this.wishListAPIRequest = wishListAPIRequest;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == CUSTOM_VIEW) {
            View view = inflater.inflate(R.layout.wish_list_row_view, parent, false);
            holder = new ViewHolder_WishList(view);
        } else {
            View view = inflater.inflate(R.layout.no_data, parent, false);
            holder = new ViewHolderEmpty_View(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == CUSTOM_VIEW) {
            ViewHolder_WishList holder_wishList = (ViewHolder_WishList) holder;
            Methods.glide_image_loader_fixed_size(mDataSet.get(position).getImage(), holder_wishList.img_wish_list_product_image);
            holder_wishList.txt_wish_list_product_price_quantity.setText(mDataSet.get(position).getPrice());
            holder_wishList.txt_wish_list_product_title.setText(mDataSet.get(position).getTitle());

        } else {
            ViewHolderEmpty_View empty_view = (ViewHolderEmpty_View) holder;
            empty_view.empty_view.setText(R.string.empty_wish_list);
        }
    }

    public void delete(int position) {
        mDataSet.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int EMPTY_VIEW = 0;
        if (mDataSet != null) {
            if (mDataSet.size() > 0) {
                return CUSTOM_VIEW;
            } else {
                return EMPTY_VIEW;
            }
        } else {
            return EMPTY_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet != null) {
            if (mDataSet.size() > 0) {
                return mDataSet.size();
            } else {
                return 1;
            }

        } else {
            return 1;
        }
    }

    private void storeProductData(int position, View v) {
        Intent intent = new Intent(v.getContext(), Product_Details.class);
        intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, mDataSet.get(position).getProduct_id());
        intent.putExtra(JSON_Names.KEY_IMAGE, mDataSet.get(position).getImage());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(intent);
    }

    private class ViewHolderEmpty_View extends RecyclerView.ViewHolder {
        TextView empty_view;

        ViewHolderEmpty_View(View view) {
            super(view);
            empty_view = view.findViewById(R.id.empty_view);
        }
    }

    private class ViewHolder_WishList extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_wish_list_product_image;
        TextView txt_wish_list_product_title;
        TextView txt_wish_list_product_price_quantity,img_btn_wish_list_product_remove;

        ViewHolder_WishList(View view) {
            super(view);
            img_wish_list_product_image = view.findViewById(R.id.wish_list_product_image);
            txt_wish_list_product_title = view.findViewById(R.id.wish_list_product_title);
            txt_wish_list_product_price_quantity = view.findViewById(R.id.wish_list_product_price);
            img_btn_wish_list_product_remove = view.findViewById(R.id.wish_list_product_remove);
            img_wish_list_product_image.setOnClickListener(this);
            txt_wish_list_product_title.setOnClickListener(this);
            txt_wish_list_product_price_quantity.setOnClickListener(this);
            img_btn_wish_list_product_remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.wish_list_product_price:
                    storeProductData(getAdapterPosition(), v);
                    break;
                case R.id.wish_list_product_title:
                    storeProductData(getAdapterPosition(), v);
                    break;
                case R.id.wish_list_product_remove:
                    DataStorage.mStoreSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mDataSet.get(getAdapterPosition()).getProduct_id());
                    DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).remove_from_wish_list(mDataSet.get(getAdapterPosition()).getProduct_id());
                    String url[] = {URL_Class.mURL + URL_Class.mURL_Remove_WishList};
                    wishListAPIRequest.wish_list_api_request(get_wish_list_post_data(), url,false);
                    delete(getAdapterPosition());
                    break;
                case R.id.wish_list_product_image:
                    storeProductData(getAdapterPosition(), v);
                    break;
            }
        }

        private String get_wish_list_post_data() {

            String mProduct_id;
            String mCustomer_id;
            try {
                mProduct_id = DataStorage.mRetrieveSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
                mCustomer_id = String.valueOf(DataBaseHandlerAccount.getInstance(mContext.getApplicationContext()).get_customer_id());
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
    }
}
