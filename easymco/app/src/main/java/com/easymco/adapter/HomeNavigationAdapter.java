package com.easymco.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easymco.R;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.models.Navigation_DataSet;

import java.util.ArrayList;

public class HomeNavigationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private String mCategoryListHolder;
    private Activity activity;

    public HomeNavigationAdapter(Context mContext, String categoryList, Activity activity) {
        this.mContext = mContext;
        this.mCategoryListHolder = categoryList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View expandableView = layoutInflater.inflate(R.layout.home_navigation_expandable_view_content, parent, false);
        viewHolder = new ExpandableViewHolder(expandableView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {

            if (GetJSONData.getNavigationData(mCategoryListHolder,mContext) != null) {
                ExpandableViewHolder expandableViewHolder = (ExpandableViewHolder) holder;
                ArrayList<Navigation_DataSet> mNavigation_dataSets = GetJSONData.getNavigationData(mCategoryListHolder,mContext);

                expandableViewHolder.newExpandableView.setLayoutManager(new LinearLayoutManager(mContext));
                expandableViewHolder.newExpandableView.setAdapter(new CustomExpandableViewHandler(mContext, mNavigation_dataSets));
            }

        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    private class ExpandableViewHolder extends RecyclerView.ViewHolder {
        RecyclerView newExpandableView;

        ExpandableViewHolder(View itemView) {
            super(itemView);
            newExpandableView = itemView.findViewById(R.id.custom_expandable_view);
        }
    }

}
