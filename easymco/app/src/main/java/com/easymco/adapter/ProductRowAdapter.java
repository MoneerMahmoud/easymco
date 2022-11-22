package com.easymco.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.constant_class.JSON_Names;
import com.easymco.models.ProductOptionDataSet;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.util.ArrayList;

class ProductRowAdapter extends RecyclerView.Adapter<ProductRowAdapter.ViewHolder> {
    Context mContext;
    private ArrayList<ProductOptionDataSet> mOptionList;
    private String mOptionId, mProduct_ID;
    private int last_position = -1;

    ProductRowAdapter(Context context, ArrayList<ProductOptionDataSet> mOptionList, String option_id, String product_id) {
        this.mContext = context;
        this.mOptionList = mOptionList;
        this.mOptionId = option_id;
        this.mProduct_ID = product_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_detail_size_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mRowTextView.setText(mOptionList.get(position).getProduct_name());
        if (mOptionList.get(position).getSelected()) {
            holder.mRowTextView.setBackgroundResource(R.color.colorPrimary);
            holder.mRowTextView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.mRowTextView.setBackgroundResource(R.color.white);
            holder.mRowTextView.setTextColor(ContextCompat.getColor(mContext, R.color.text_color));
        }
    }


    @Override
    public int getItemCount() {
        return mOptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mRowTextView;
        LinearLayout mOptionRowHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            mRowTextView = itemView.findViewById(R.id.product_detail_row);
            mOptionRowHolder = itemView.findViewById(R.id.option_row_holder);
            mOptionRowHolder.setOnClickListener(this);
            mRowTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.product_detail_row) {
                if (last_position != -1) {
                    DataStorage.mStoreSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionId, mOptionId);
                    DataStorage.mStoreSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionId, mOptionList.get(getAdapterPosition()).getProduct_option_value_id());
                    mOptionList.get(last_position).setSelected(false);
                    mOptionList.get(getAdapterPosition()).setSelected(true);
                    last_position = getAdapterPosition();
                    notifyDataSetChanged();
                } else {
                    DataStorage.mStoreSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_OPTION_ID + mOptionId, mOptionId);
                    DataStorage.mStoreSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mOptionId, mOptionList.get(getAdapterPosition()).getProduct_option_value_id());
                    mOptionList.get(getAdapterPosition()).setSelected(true);
                    last_position = getAdapterPosition();
                    notifyDataSetChanged();
                }
            }
        }
    }
}

