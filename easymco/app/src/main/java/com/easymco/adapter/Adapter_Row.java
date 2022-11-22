package com.easymco.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.interfaces.WishListAPIRequest;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductDataSet;

import java.util.ArrayList;

class Adapter_Row extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ProductDataSet> itemsData;
    private Context mContext;
    private int PRODUCT = 0/*, FULL_VIEW = 1*/;
    private WishListAPIRequest wishListAPIRequest;

    Adapter_Row(ArrayList<ProductDataSet> itemsData, Context context, WishListAPIRequest wishListAPIRequest) {
        this.itemsData = itemsData;
        this.mContext = context;
        this.wishListAPIRequest = wishListAPIRequest;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v2 = inflater.inflate(R.layout.home_row, parent, false);
        viewHolder = new ViewHolder(v2);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = viewHolder.getItemViewType();

        if (type == PRODUCT) {
            ViewHolder itemViewHolder = (ViewHolder) viewHolder;
            itemViewHolder.special_price_p.setVisibility(View.VISIBLE);
            itemViewHolder.special_price_sp.setVisibility(View.VISIBLE);

            if (itemsData.get(position).getTitle().length() > 17) {
                String title = itemsData.get(position).getTitle().substring(0, 14) + "...";
                itemViewHolder.title.setText(title);
            } else {
                itemViewHolder.title.setText(itemsData.get(position).getTitle());
            }

            Methods.glide_image_loader_fixed_size(itemsData.get(position).getImage(), itemViewHolder.image);

            if (itemsData.get(position).getSpecial_price().isEmpty()) {//Check the data
                itemViewHolder.special_price_p.setVisibility(View.GONE);
                itemViewHolder.special_price_sp.setText(itemsData.get(position).getPrice());
            } else {
                itemViewHolder.special_price_p.setText(itemsData.get(position).getPrice());
                itemViewHolder.special_price_p.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.special_price_sp.setText(itemsData.get(position).getSpecial_price());
            }
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        public ImageView image;
        TextView special_price_p;
        TextView special_price_sp;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.column_title);
            image = view.findViewById(R.id.column_image);
            special_price_p = view.findViewById(R.id.column_special_price_p);
            special_price_sp = view.findViewById(R.id.column_special_price_sp);

            title.setOnClickListener(this);
            image.setOnClickListener(this);
            special_price_p.setOnClickListener(this);
            special_price_sp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.column_title:
                    transfer(getAdapterPosition(), image);
                    break;
                case R.id.column_image:
                    transfer(getAdapterPosition(), image);
                    break;
                case R.id.column_special_price_p:
                    transfer(getAdapterPosition(), image);
                    break;
                case R.id.column_special_price_sp:
                    transfer(getAdapterPosition(), image);
                    break;
            }
        }
    }

    private void transfer(int position, ImageView transactionView) {
        wishListAPIRequest.transaction(itemsData.get(position).getProduct_id(), transactionView, itemsData.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        if (itemsData.size() >= 4) {
            return 4;
        } else {
            return itemsData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return PRODUCT;
    }
}
