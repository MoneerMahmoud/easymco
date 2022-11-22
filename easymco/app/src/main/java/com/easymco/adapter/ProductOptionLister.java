package com.easymco.adapter;


import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.models.ProductOptionDataSet;

import java.util.ArrayList;

class ProductOptionLister extends ArrayAdapter<ProductOptionDataSet> {

    private ArrayList<ProductOptionDataSet> data = null;
    private Activity context;

    ProductOptionLister(Activity context, int resource, ArrayList<ProductOptionDataSet> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.product_detail_option_row_text, parent, false);
        }
        if (data.get(position) != null) {
            final TextView CountryName = row.findViewById(R.id.product_detail_option_title);
            if (CountryName != null) {
                if (data.get(position).getProduct_name().equals("SELECT")) {
                    CountryName.setBackgroundColor(context.getResources().getColor(R.color.grey_600));
                    CountryName.setTextColor(context.getResources().getColor(R.color.white));
                }

                CountryName.setText(data.get(position).getProduct_name());
            }
        }
        return row;
    }

}
