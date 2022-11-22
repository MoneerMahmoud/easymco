package com.easymco.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.mechanism.Methods;
import com.easymco.models.CartDataSet;

import java.util.ArrayList;

public class CheckOut_Adapter extends RecyclerView.Adapter<CheckOut_Adapter.ViewHolder> {
    Context mContext;
    private ArrayList<CartDataSet> mResultDataSet;

    public CheckOut_Adapter(Context context, ArrayList<CartDataSet> mDataSet) {
        this.mContext = context;
        this.mResultDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.checkout_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Methods.glide_image_loader(mResultDataSet.get(position).getImage(), holder.mCheckOutRowProductImage);
        String price = mResultDataSet.get(position).getQuantity() + " * " + mResultDataSet.get(position).getPrice();
        holder.mCheckOutRowProductPrice.setText(price);
        holder.mCheckOutRowProductTitle.setText(mResultDataSet.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mResultDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mCheckOutRowProductImage;
        TextView mCheckOutRowProductTitle, mCheckOutRowProductPrice;

        public ViewHolder(View view) {
            super(view);
            mCheckOutRowProductImage = view.findViewById(R.id.checkout_row_product_image);
            mCheckOutRowProductTitle = view.findViewById(R.id.checkout_product_title);
            mCheckOutRowProductPrice = view.findViewById(R.id.checkout_product_price);
        }
    }
}
