package com.easymco.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.models.ProductDataSet;
import com.easymco.models.ProductSpecificationDataSet;

import java.util.ArrayList;

public class Product_Specification_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<Object> mResult_DataSet;
    private final int TYPE_DEFAULT = 0, TYPE_SPECIFICATION_TITLE = 1, TYPE_SPECIFICATION_CONTENT = 2;

    public Product_Specification_Adapter(Context context, ArrayList<Object> list) {
        this.mContext = context;
        this.mResult_DataSet = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        if (viewType == TYPE_DEFAULT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.product_detail_specification_default, parent, false);
            holder = new ViewHolder_Default(view);
        } else if (viewType == TYPE_SPECIFICATION_TITLE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.product_detail_specification_title, parent, false);
            holder = new ViewHolder_Title(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.product_detail_specification_values, parent, false);
            holder = new ViewHolder_Content(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_DEFAULT:
                ViewHolder_Default viewHolder_default = (ViewHolder_Default) holder;
                ProductDataSet productDataSet = (ProductDataSet) mResult_DataSet.get(position);
                viewHolder_default.txt_view_product_title.setText(productDataSet.getTitle());
                String txtManufacturer = productDataSet.getManufacturer();
                if ((txtManufacturer != null) && !(txtManufacturer.equals(""))) {
                    viewHolder_default.txt_view_product_manufacture.setText(txtManufacturer);
                } else {
                    viewHolder_default.txt_view_product_manufacture_title.setVisibility(View.GONE);
                    viewHolder_default.txt_view_product_manufacture.setVisibility(View.GONE);
                }
                String txtStockStatus = productDataSet.getQuantity();
                if ((txtStockStatus != null) && !(txtStockStatus.equals(""))) {
                    viewHolder_default.txt_view_product_status.setText(txtStockStatus);
                } else {
                    viewHolder_default.txt_view_product_status_title.setVisibility(View.GONE);
                    viewHolder_default.txt_view_product_status.setVisibility(View.GONE);
                }

                break;
            case TYPE_SPECIFICATION_TITLE:
                ViewHolder_Title viewHolder_title = (ViewHolder_Title) holder;
                String title = (String) mResult_DataSet.get(position);
                viewHolder_title.txt_view_product_specification_title.setText(title);
                break;
            case TYPE_SPECIFICATION_CONTENT:
                ViewHolder_Content viewHolder_content = (ViewHolder_Content) holder;
                ProductSpecificationDataSet productSpecificationDataSet = (ProductSpecificationDataSet) mResult_DataSet.get(position);
                viewHolder_content.txt_view_value_name.setText(productSpecificationDataSet.getProduct_name());
                viewHolder_content.txt_view_value_name_value.setText(productSpecificationDataSet.getProduct_text());
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (mResult_DataSet != null) {
            return mResult_DataSet.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mResult_DataSet.get(position) instanceof ProductDataSet) {
            return TYPE_DEFAULT;
        } else if (mResult_DataSet.get(position) instanceof String) {
            return TYPE_SPECIFICATION_TITLE;
        } else {
            return TYPE_SPECIFICATION_CONTENT;
        }
    }

    private class ViewHolder_Title extends RecyclerView.ViewHolder {
        TextView txt_view_product_specification_title;

        ViewHolder_Title(View view) {
            super(view);
            txt_view_product_specification_title = view.findViewById(R.id.product_detail_value_title);
        }
    }

    private class ViewHolder_Content extends RecyclerView.ViewHolder {
        TextView txt_view_value_name, txt_view_value_name_value;

        ViewHolder_Content(View view) {
            super(view);
            txt_view_value_name = view.findViewById(R.id.product_detail_value_name);
            txt_view_value_name_value = view.findViewById(R.id.product_detail_value_name_value);
        }
    }

    private class ViewHolder_Default extends RecyclerView.ViewHolder {
        TextView txt_view_product_title, txt_view_product_manufacture, txt_view_product_status,
                txt_view_product_manufacture_title, txt_view_product_status_title;

        ViewHolder_Default(View view) {
            super(view);
            txt_view_product_title = view.findViewById(R.id.product_detail_and_description_title);
            txt_view_product_status = view.findViewById(R.id.product_detail_and_description_stock_detail);
            txt_view_product_manufacture = view.findViewById(R.id.product_detail_and_description_manufacture);
            txt_view_product_manufacture_title = view.findViewById(R.id.product_detail_and_description_manufacture_title);
            txt_view_product_status_title = view.findViewById(R.id.product_detail_and_description_stock_detail_title);
        }
    }

}
