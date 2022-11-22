package com.easymco.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.user.Login;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.interfaces.SearchListLoadMore;
import com.easymco.interfaces.WishListAPIRequest;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductDataSet;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.net.URLEncoder;
import java.util.ArrayList;

public class Product_Listing_Gird_And_List extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ProductDataSet> mCategoryList;
    private boolean mType;
    private int GRID = 1, LIST = 2, EMPTY = 3;
    private WishListAPIRequest wishListAPIRequest;
    private String add[] = {URL_Class.mURL + URL_Class.mURL_Add_To_WishList}, remove[] = {URL_Class.mURL + URL_Class.mURL_Remove_WishList};

    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 6;
    private int lastVisibleItem, totalItemCount;
    private boolean loading,mFromSearch,mFromSearchOnResume;
    private SearchListLoadMore mSearchListLoadMore;


    public Product_Listing_Gird_And_List(Context context, ArrayList<ProductDataSet> list, boolean type,
                                         WishListAPIRequest wishListAPIRequest, RecyclerView recyclerView,
                                         Boolean fromSearch,Boolean fromSearchOnResume) {
        this.mContext = context;
        this.mCategoryList = list;
        this.mType = type;
        this.wishListAPIRequest = wishListAPIRequest;
        this.mFromSearch = fromSearch;
        if(mFromSearch){
            this.mSearchListLoadMore = (SearchListLoadMore)mContext;
        }
        this.mFromSearchOnResume = fromSearchOnResume;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder parentHolder;
        View view;
        if (viewType == GRID) {
            view = LayoutInflater.from(mContext).inflate(R.layout.category_grid_column, parent, false);
            parentHolder = new Grid_Holder(view);
        } else if (viewType == LIST) {
            view = LayoutInflater.from(mContext).inflate(R.layout.category_list_column, parent, false);
            parentHolder = new List_Holder(view);
        } else if (viewType == EMPTY) {
            view = LayoutInflater.from(mContext).inflate(R.layout.no_data, parent, false);
            parentHolder = new ViewHolderEmpty_View(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.progress_bar_layout, parent, false);
            parentHolder = new ProgressViewHolder(view);
        }
        return parentHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == LIST) {

            if (mCategoryList != null && mCategoryList.size() > 0) {
                List_Holder list = (List_Holder) holder;
                list.mCategory_special_p.setVisibility(View.VISIBLE);
                list.mCategory_special_sp.setVisibility(View.VISIBLE);
                list.mCategory_price.setVisibility(View.VISIBLE);

                if (mCategoryList.get(position) != null) {
                    image_caller(mCategoryList.get(position).getImage(), list.mCategory_image);


                    if(mCategoryList.get(position).getTitle().length() > 60){

                        String title = mCategoryList.get(position).getTitle().substring(0,60)+"...";
                        list.mCategory_title.setText(title);

                    }else {

                        list.mCategory_title.setText(mCategoryList.get(position).getTitle());

                    }

                    if (mCategoryList.get(position).getSpecial_price().isEmpty()) {
                        list.mCategory_price.setText(mCategoryList.get(position).getPrice());
                        list.mCategory_special_p.setVisibility(View.GONE);
                        list.mCategory_special_sp.setVisibility(View.GONE);
                    } else {
                        list.mCategory_price.setVisibility(View.GONE);
                        list.mCategory_special_p.setText(mCategoryList.get(position).getPrice());
                        list.mCategory_special_p.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        list.mCategory_special_sp.setText(mCategoryList.get(position).getSpecial_price());
                    }
                    if (DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).checking_wish_list(mCategoryList.get(position).getProduct_id())) {
                        list.mCategory_fav.setImageResource(R.drawable.ic_favorite_color_accent_24dp);
                    } else {
                        list.mCategory_fav.setImageResource(R.drawable.ic_favorite_grey_500_24dp);
                    }
                }
            }
        } else if (holder.getItemViewType() == GRID) {
            if (mCategoryList != null && mCategoryList.size() > 0) {
                Grid_Holder grid = (Grid_Holder) holder;
                grid.mCategory_special_p.setVisibility(View.VISIBLE);
                grid.mCategory_special_sp.setVisibility(View.VISIBLE);

                if (mCategoryList.get(position) != null) {
                    image_caller(mCategoryList.get(position).getImage(), grid.mCategory_image);
                    if (mCategoryList.get(position).getTitle().length() > 30) {
                       // grid.mCategory_title.setLines(2);
                        String title = mCategoryList.get(position).getTitle().substring(0, 28) + "...";
                        grid.mCategory_title.setText(title);
                    } else if (mCategoryList.get(position).getTitle().length() > 17 && mCategoryList.get(position).getTitle().length() < 24) {
                       // grid.mCategory_title.setLines(2);
                        grid.mCategory_title.setText(mCategoryList.get(position).getTitle());
                    } else {
                       // grid.mCategory_title.setLines(1);
                        grid.mCategory_title.setText(mCategoryList.get(position).getTitle());
                    }

                    if (mCategoryList.get(position).getSpecial_price().isEmpty()) {
                        grid.mCategory_special_p.setVisibility(View.GONE);
                        grid.mCategory_special_sp.setText(mCategoryList.get(position).getPrice());
                    } else {
                        grid.mCategory_special_p.setText(mCategoryList.get(position).getPrice());
                        grid.mCategory_special_p.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        grid.mCategory_special_sp.setText(mCategoryList.get(position).getSpecial_price());
                    }
                    if (DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).checking_wish_list(mCategoryList.get(position).getProduct_id())) {
                        grid.mCategory_fav.setImageResource(R.drawable.ic_favorite_color_accent_24dp);
                    } else {
                        grid.mCategory_fav.setImageResource(R.drawable.ic_favorite_grey_500_24dp);
                    }
                }
            }
        } else if (holder.getItemViewType() == EMPTY) {
            ViewHolderEmpty_View empty_view = (ViewHolderEmpty_View) holder;
            if(mFromSearch){
                empty_view.empty_view.setText(R.string.empty_text);
            }else {
                empty_view.empty_view.setText(R.string.empty_category);
            }

        } else {
            ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
            progressViewHolder.progressBar.setIndeterminate(true);
        }


        //For product search process only :-
        if(mFromSearch){
            if(mCategoryList !=null) {
                if (position == mCategoryList.size() - 1) {
                   if(mContext!=null) {
                       mSearchListLoadMore.toLoadSearchList();
                    }
                }
            }
        }


    }

    private void image_caller(String url, ImageView imageView) {
        Methods.glide_image_loader_fixed_size(url, imageView);
    }

    @Override
    public int getItemCount() {
        if (mCategoryList != null) {
            if (mCategoryList.size() > 0) {
                return mCategoryList.size();
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mCategoryList != null && !mCategoryList.isEmpty()) {
            if (mCategoryList.get(position) != null) {
                if (mType) {
                    return GRID;
                } else {
                    return LIST;
                }
            } else {
                return 0;
            }
        } else {
            return EMPTY;
        }
    }

    private void toProductDetail(int position, ImageView transactionView) {
        wishListAPIRequest.transaction(mCategoryList.get(position).getProduct_id(), transactionView, mCategoryList.get(position).getImage());
    }

    private void addToWishList(ImageButton mCategory_fav, View v, int position) {
        if (DataBaseHandlerAccount.getInstance(mContext.getApplicationContext()).check_login()) {
            if (!DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).checking_wish_list(mCategoryList.get(position).getProduct_id())) {
                DataStorage.mStoreSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mCategoryList.get(position).getProduct_id());
                DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).add_to_wish_list(mCategoryList.get(position).getProduct_id(), mCategoryList.get(position).getProduct_string());
                if (get_wish_list_post_data() != null) {
                    wishListAPIRequest.wish_list_api_request(get_wish_list_post_data(), add,true);
                }
                mCategory_fav.setImageResource(R.drawable.ic_favorite_color_accent_24dp);
            } else {
                DataStorage.mStoreSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID, mCategoryList.get(position).getProduct_id());
                DataBaseHandlerWishList.getInstance(mContext.getApplicationContext()).remove_from_wish_list(mCategoryList.get(position).getProduct_id());
                mCategory_fav.setImageResource(R.drawable.ic_favorite_grey_500_24dp);
                if (get_wish_list_post_data() != null) {
                    wishListAPIRequest.wish_list_api_request(get_wish_list_post_data(), remove,false);
                }
            }
        } else {
            Intent intent = new Intent(v.getContext(), Login.class);
            DataStorage.mStoreSharedPreferenceString(mContext.getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID,
                    mCategoryList.get(position).getProduct_id());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }
    }

    private class Grid_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategory_image;
        TextView mCategory_title;
        TextView mCategory_special_p;
        TextView mCategory_special_sp;
        ImageButton mCategory_fav;

        Grid_Holder(View view) {
            super(view);
            mCategory_image = view.findViewById(R.id.category_image);
            mCategory_title = view.findViewById(R.id.category_title);
            mCategory_special_p = view.findViewById(R.id.category_special_price_p);
            mCategory_special_sp = view.findViewById(R.id.category_special_price_sp);
            mCategory_fav = view.findViewById(R.id.category_wishList);
            mCategory_fav.setOnClickListener(this);
            mCategory_image.setOnClickListener(this);
            mCategory_title.setOnClickListener(this);
            mCategory_special_p.setOnClickListener(this);
            mCategory_special_sp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.category_wishList:
                    addToWishList(mCategory_fav, v, getAdapterPosition());
                    break;
                case R.id.category_image:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
                case R.id.category_title:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
                case R.id.category_price:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
                case R.id.category_special_price_p:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
                case R.id.category_special_price_sp:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
            }
        }
    }

    private class List_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategory_image;
        TextView mCategory_title;
        TextView mCategory_price;
        TextView mCategory_special_p;
        TextView mCategory_special_sp;
        ImageButton mCategory_fav;

        List_Holder(View view) {
            super(view);
            mCategory_image = view.findViewById(R.id.category_image);
            mCategory_title = view.findViewById(R.id.category_title);
            mCategory_price = view.findViewById(R.id.category_price);
            mCategory_special_p = view.findViewById(R.id.category_special_price_p);
            mCategory_special_sp = view.findViewById(R.id.category_special_price_sp);
            mCategory_fav = view.findViewById(R.id.category_wishList);
            mCategory_fav.setOnClickListener(this);
            mCategory_image.setOnClickListener(this);
            mCategory_title.setOnClickListener(this);
            mCategory_price.setOnClickListener(this);
            mCategory_special_p.setOnClickListener(this);
            mCategory_special_sp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.category_wishList:
                    addToWishList(mCategory_fav, v, getAdapterPosition());
                    break;
                case R.id.category_image:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
                case R.id.category_title:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
                case R.id.category_price:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
                case R.id.category_special_price_p:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
                case R.id.category_special_price_sp:
                    toProductDetail(getAdapterPosition(), mCategory_image);
                    break;
            }
        }
    }

    private class ViewHolderEmpty_View extends RecyclerView.ViewHolder {
        TextView empty_view;

        ViewHolderEmpty_View(View view) {
            super(view);
            empty_view = view.findViewById(R.id.empty_view);
        }
    }


    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
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

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoaded() {
        loading = false;
    }
}
