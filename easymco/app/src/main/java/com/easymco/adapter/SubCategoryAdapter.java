package com.easymco.adapter;


import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.category.Category_Details;
import com.easymco.constant_class.JSON_Names;
import com.easymco.models.CategoryDataSet;

import java.util.ArrayList;

class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CategoryDataSet> mSubFullList = new ArrayList<>();

    SubCategoryAdapter(Context context, ArrayList<CategoryDataSet> subList) {
        this.mContext = context;
        this.mSubFullList = subList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubCategoryHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_category, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SubCategoryHolder subCategoryHolder = (SubCategoryHolder) holder;
        subCategoryHolder.value.setText(mSubFullList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mSubFullList.size();
    }

    private class SubCategoryHolder extends RecyclerView.ViewHolder {
        TextView value;

        SubCategoryHolder(View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.lblListItem);

            value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != -1) {
                        Intent i = new Intent(mContext, Category_Details.class);
                        i.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, mSubFullList.get(getAdapterPosition()).getName());
                        i.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, mSubFullList.get(getAdapterPosition()).getImage());
                        i.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mSubFullList.get(getAdapterPosition()).getCategory_id());
                        i.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, R.string.sort_category);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);
                    }
                }
            });
        }
    }
}
