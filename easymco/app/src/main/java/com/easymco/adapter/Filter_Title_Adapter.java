package com.easymco.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.interfaces.FilterTitleSelection;
import com.easymco.models.FilterDataSet;

import java.util.ArrayList;

public class Filter_Title_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<FilterDataSet> mTitleList = new ArrayList<>();
    private int last_position = -1;
    private FilterTitleSelection filterTitleSelection;

    public Filter_Title_Adapter(Context context, ArrayList<FilterDataSet> mReturnValue, FilterTitleSelection filterTitleSelection) {
        this.mContext = context;
        this.mTitleList = mReturnValue;
        this.filterTitleSelection = filterTitleSelection;
    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        List_ViewHolder list_viewHolder = (List_ViewHolder) holder;
        list_viewHolder.title.setText(mTitleList.get(position).getmName().toUpperCase());

        if (mTitleList.get(position).isSelect()) {
            list_viewHolder.title.setBackgroundResource(R.color.colorPrimary);
            list_viewHolder.title.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            list_viewHolder.title.setBackgroundResource(R.color.grey_200);
            list_viewHolder.title.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup root, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.category_filter_title, root, false);
        viewHolder = new List_ViewHolder(view);
        return viewHolder;
    }

    private class List_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        List_ViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.filter_title);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.filter_title:
                    if (last_position == -1) {
                        mTitleList.get(0).setSelect(false);
                        mTitleList.get(getAdapterPosition()).setSelect(true);
                        last_position = getAdapterPosition();
                        filterTitleSelection.title_selection(mTitleList.get(getAdapterPosition()).getmName());
                        notifyDataSetChanged();
                    } else {
                        mTitleList.get(last_position).setSelect(false);
                        mTitleList.get(getAdapterPosition()).setSelect(true);
                        last_position = getAdapterPosition();
                        filterTitleSelection.title_selection(mTitleList.get(getAdapterPosition()).getmName());
                        notifyDataSetChanged();
                    }
                    break;
            }
        }
    }


}