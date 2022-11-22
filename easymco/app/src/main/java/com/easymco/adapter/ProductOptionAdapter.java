package com.easymco.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.constant_class.JSON_Names;
import com.easymco.models.ProductOptionDataSet;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductOptionAdapter extends RecyclerView.Adapter<ProductOptionAdapter.ViewHolder> {
    private ArrayList<ProductOptionDataSet> mList;
    private HashMap<Integer, ArrayList<ProductOptionDataSet>> mChildList;
    Context mContext;
    private Activity activity;
    private String mID;

    public ProductOptionAdapter(Context context, ArrayList<ProductOptionDataSet> list,
                                HashMap<Integer, ArrayList<ProductOptionDataSet>> childList, String product_id,
                                Activity activity) {
        this.mContext = context;
        this.mList = list;
        this.mChildList = childList;
        this.mID = product_id;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mRowTitle, mRowOption;
        RecyclerView mRowOptionOld;

        public ViewHolder(View itemView) {
            super(itemView);
            mRowTitle = itemView.findViewById(R.id.product_detail_color_title);
            mRowOption = itemView.findViewById(R.id.product_detail_spinner_listing_text);
            mRowOptionOld = itemView.findViewById(R.id.product_detail_spinner_listing);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_detail_option_row_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final ProductRowAdapter productRowAdapter = new ProductRowAdapter(mContext, mChildList.get(position),
                mList.get(position).getProduct_option_id(), mID);

        holder.mRowOptionOld.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.HORIZONTAL, false));
        holder.mRowOptionOld.setAdapter(productRowAdapter);

        holder.mRowTitle.setText(mList.get(position).getProduct_option_name());

        holder.mRowOption.setText(mContext.getResources().getString(R.string.please_select));

        final ViewHolder viewHolder = holder;

        holder.mRowOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View mSpinnerView = LayoutInflater.from(mContext).inflate(R.layout.product_option_inner_spinner_row, null);

                ProductOptionLister productOptionLister = new ProductOptionLister(activity, android.R.layout.simple_spinner_item, mChildList.get(position));
                ListView spinner = mSpinnerView.findViewById(R.id.product_detail_option_spinner);
                spinner.setAdapter(productOptionLister);
                final PopupWindow popupWindow = new PopupWindow(
                        mSpinnerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                popupWindow.setFocusable(true);

                popupWindow.setBackgroundDrawable((new ColorDrawable(
                        android.graphics.Color.TRANSPARENT)));

                popupWindow.showAtLocation(viewHolder.mRowOption, Gravity.CENTER, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int positionSpinner, long id) {

                        if (!mChildList.get(position).get(positionSpinner).getProduct_option_value_id().equals("-1")) {
                            DataStorage.mStoreSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_OPTION_ID + mList.get(position).getProduct_option_id(),
                                    mList.get(position).getProduct_option_id());
                            DataStorage.mStoreSharedPreferenceString(mContext, JSON_Names.KEY_SECOND_ROW_PRODUCT_OPTION_VALUE_ID + mList.get(position).getProduct_option_id(),
                                    mChildList.get(position).get(positionSpinner).getProduct_option_value_id());
                            viewHolder.mRowOption.setText(mChildList.get(position).get(positionSpinner).getProduct_name());
                            popupWindow.dismiss();

                        }
                    }
                });

                mSpinnerView.findViewById(R.id.product_detail_option_list_holder).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
