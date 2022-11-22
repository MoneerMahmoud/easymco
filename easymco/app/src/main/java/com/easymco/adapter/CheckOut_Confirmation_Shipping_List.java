package com.easymco.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.models.ConfirmResponseDataSet;

import java.util.ArrayList;

public class CheckOut_Confirmation_Shipping_List extends RecyclerView.Adapter<CheckOut_Confirmation_Shipping_List.ViewHolder> {
    Context mContext;
    private ArrayList<ConfirmResponseDataSet> mList = new ArrayList<>();

    public CheckOut_Confirmation_Shipping_List(Context context, ArrayList<ConfirmResponseDataSet> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.checkout_confirm_order_total_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList != null) {
            holder.title.setText(mList.get(position).getmTotalsTitle());
            holder.value.setText(mList.get(position).getmTotalsValue());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, value;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.confirmation_shipping_title);
            value = view.findViewById(R.id.confirmation_shipping_value);
        }
    }
}
