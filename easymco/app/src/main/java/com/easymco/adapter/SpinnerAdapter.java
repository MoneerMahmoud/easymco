package com.easymco.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.models.SpinnerDataSet;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerDataSet> {
    private ArrayList<SpinnerDataSet> data = null;
    private Activity context;

    public SpinnerAdapter(Activity context, int resource, ArrayList<SpinnerDataSet> data) {
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
            row = inflater.inflate(R.layout.spinner_item_country_spinner, parent, false);
        }
        if (data.get(position) != null) {
            TextView CountryName =  row.findViewById(R.id.spinner_country_value_spinner);
            if (CountryName != null) {
                CountryName.setText(data.get(position).get_name());
                CountryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
            }
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.country_spinner, parent, false);
        }

        if (data.get(position) != null) {
            TextView CountryName =  row.findViewById(R.id.spinner_country_value);
            int color = ContextCompat.getColor(context, R.color.white);
            CountryName.setBackgroundColor(color);
            CountryName.setText(data.get(position).get_name());
        }
        return row;
    }
}

