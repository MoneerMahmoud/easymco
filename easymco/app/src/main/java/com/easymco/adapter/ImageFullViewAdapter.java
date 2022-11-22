package com.easymco.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.easymco.R;
import com.easymco.custom.ZoomImageView;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductImageDataSet;

import java.util.ArrayList;

public class ImageFullViewAdapter extends RecyclerView.Adapter<ImageFullViewAdapter.ViewHolder> {
    Context mContext;
    private ArrayList<ProductImageDataSet> mImageList;
    private ZoomImageView image_full_view;
    private int previousPosition = -1;

    public ImageFullViewAdapter(Context context, ArrayList<ProductImageDataSet> list, ZoomImageView image_full_view) {
        this.mContext = context;
        this.mImageList = list;
        this.image_full_view = image_full_view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_detail_image_row_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mImageList.get(position).isSelected()) {
            holder.mChildView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.view_all_background));
            previousPosition = position;
        } else {
            holder.mChildView.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.view_all_back_ground_un_selected));
        }

        final int dummyPosition = position;

        image_caller(mImageList.get(position).getChildImage(), holder.mChildView);

        holder.mChildView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousPosition != dummyPosition) {
                    mImageList.get(previousPosition).setSelected(false);
                    image_full_view.reset();
                    image_full_view.setMaxZoom(3.0f);
                    image_caller(mImageList.get(dummyPosition).getChildImage(), image_full_view);
                    mImageList.get(dummyPosition).setSelected(true);
                    previousPosition = dummyPosition;
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void image_caller(String url, ImageView imageView) {
        Methods.glide_image_loader_banner(url, imageView);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mChildView;

        public ViewHolder(View itemView) {
            super(itemView);
            mChildView = itemView.findViewById(R.id.image_full_view_row_image);
        }
    }
}
