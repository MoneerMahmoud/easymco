package com.easymco.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.product.Product_Details;
import com.easymco.constant_class.JSON_Names;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductDataSet;

import java.util.ArrayList;

class HomeListImageDifferentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ProductDataSet> mFullProductList = new ArrayList<>();

    HomeListImageDifferentAdapter(Context context, ArrayList<ProductDataSet> productList) {

        this.mContext = context;
        this.mFullProductList = productList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        View rowType;

        if (viewType == 1) {
            rowType = LayoutInflater.from(mContext).inflate(R.layout.category_list_left_side_image_row, parent, false);
            viewHolder = new List_Left_Side_Image_Holder(rowType);
        } else {
            rowType = LayoutInflater.from(mContext).inflate(R.layout.category_list_right_side_image_row, parent, false);
            viewHolder = new List_Right_Side_Image_Holder(rowType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            List_Left_Side_Image_Holder list_left_side_image_holder = (List_Left_Side_Image_Holder) holder;
            list_left_side_image_holder.mCategory_special_p.setVisibility(View.VISIBLE);
            list_left_side_image_holder.mCategory_special_sp.setVisibility(View.VISIBLE);

            if (mFullProductList.get(position) != null) {
                image_caller(mFullProductList.get(position).getImage(), list_left_side_image_holder.mCategory_image);

                if(mFullProductList.get(position).getTitle().length() > 60){

                    String title = mFullProductList.get(position).getTitle().substring(0,60)+"...";
                    list_left_side_image_holder.mCategory_title.setText(title);

                }else {
                    list_left_side_image_holder.mCategory_title.setText(mFullProductList.get(position).getTitle());
                }


                if (mFullProductList.get(position).getSpecial_price().isEmpty()) {
                    list_left_side_image_holder.mCategory_special_p.setVisibility(View.GONE);
                    list_left_side_image_holder.mCategory_special_sp.setText(mFullProductList.get(position).getPrice());
                } else {
                    list_left_side_image_holder.mCategory_special_p.setText(mFullProductList.get(position).getPrice());
                    list_left_side_image_holder.mCategory_special_p.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    list_left_side_image_holder.mCategory_special_sp.setText(mFullProductList.get(position).getSpecial_price());
                }
            }
        } else {
            List_Right_Side_Image_Holder list_right_side_image_holder = (List_Right_Side_Image_Holder) holder;

            list_right_side_image_holder.mCategory_special_p.setVisibility(View.VISIBLE);
            list_right_side_image_holder.mCategory_special_sp.setVisibility(View.VISIBLE);

            if (mFullProductList.get(position) != null) {
                image_caller(mFullProductList.get(position).getImage(), list_right_side_image_holder.mCategory_image);

                list_right_side_image_holder.mCategory_title.setText(mFullProductList.get(position).getTitle());

                if (mFullProductList.get(position).getSpecial_price().isEmpty()) {
                    list_right_side_image_holder.mCategory_special_p.setVisibility(View.GONE);
                    list_right_side_image_holder.mCategory_special_sp.setText(mFullProductList.get(position).getPrice());
                } else {
                    list_right_side_image_holder.mCategory_special_p.setText(mFullProductList.get(position).getPrice());
                    list_right_side_image_holder.mCategory_special_p.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    list_right_side_image_holder.mCategory_special_sp.setText(mFullProductList.get(position).getSpecial_price());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFullProductList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    private class List_Right_Side_Image_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategory_image;
        TextView mCategory_title;
        TextView mCategory_special_p;
        TextView mCategory_special_sp;

        List_Right_Side_Image_Holder(View view) {
            super(view);
            mCategory_image = view.findViewById(R.id.category_image);
            mCategory_title = view.findViewById(R.id.category_title);
            mCategory_special_p = view.findViewById(R.id.category_special_price_p);
            mCategory_special_sp = view.findViewById(R.id.category_special_price_sp);
            mCategory_image.setOnClickListener(this);
            mCategory_title.setOnClickListener(this);
            mCategory_special_p.setOnClickListener(this);
            mCategory_special_sp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.category_image:
                    toProductDetail(getAdapterPosition());
                    break;
                case R.id.category_title:
                    toProductDetail(getAdapterPosition());
                    break;
                case R.id.category_special_price_p:
                    toProductDetail(getAdapterPosition());
                    break;
                case R.id.category_special_price_sp:
                    toProductDetail(getAdapterPosition());
                    break;
            }
        }
    }

    private class List_Left_Side_Image_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategory_image;
        TextView mCategory_title;
        TextView mCategory_special_p;
        TextView mCategory_special_sp;

        List_Left_Side_Image_Holder(View view) {
            super(view);
            mCategory_image = view.findViewById(R.id.category_image);
            mCategory_title = view.findViewById(R.id.category_title);
            mCategory_special_p = view.findViewById(R.id.category_special_price_p);
            mCategory_special_sp = view.findViewById(R.id.category_special_price_sp);
            mCategory_image.setOnClickListener(this);
            mCategory_title.setOnClickListener(this);
            mCategory_special_p.setOnClickListener(this);
            mCategory_special_sp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.category_image:
                    toProductDetail(getAdapterPosition());
                    break;
                case R.id.category_title:
                    toProductDetail(getAdapterPosition());
                    break;
                case R.id.category_special_price_p:
                    toProductDetail(getAdapterPosition());
                    break;
                case R.id.category_special_price_sp:
                    toProductDetail(getAdapterPosition());
                    break;
            }
        }
    }

    private void toProductDetail(int adapterPosition) {
        Intent intent = new Intent(mContext, Product_Details.class);
        intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, mFullProductList.get(adapterPosition).getProduct_id());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void image_caller(String url, ImageView imageView) {
        Methods.glide_image_loader_fixed_size(url, imageView);
    }
}
