package com.easymco.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.interfaces.FilterTitleSelection;
import com.easymco.models.FilterDataSet;

import java.util.ArrayList;

public class Filter_Row_Adapter extends RecyclerView.Adapter<Filter_Row_Adapter.List_ViewHolder> {
    Context mContext;
    private ArrayList<FilterDataSet> mChildList;
    private FilterTitleSelection filterTitleSelection;

    public Filter_Row_Adapter(Context context, ArrayList<FilterDataSet> mChildList, FilterTitleSelection filterTitleSelection) {
        this.mContext = context;
        this.mChildList = mChildList;
        this.filterTitleSelection = filterTitleSelection;
    }


    class List_ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView title;
        CheckBox checkBox;

        List_ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.filter_check_name);
            checkBox = itemView.findViewById(R.id.filter_check_item);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mChildList.get(getAdapterPosition()).setChecked(true);
                filterTitleSelection.selected_list(Integer.valueOf(mChildList.get(getAdapterPosition()).getmFilterId()));
            } else {
                filterTitleSelection.remove_from_list(Integer.valueOf(mChildList.get(getAdapterPosition()).getmFilterId()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChildList.size();
    }

    @Override
    public void onBindViewHolder(List_ViewHolder holder, int position) {

        String titleValue = mChildList.get(position).getmName().substring(0, 1).toUpperCase() + mChildList.get(position).getmName().substring(1);
        holder.title.setText(titleValue);

        if (mChildList.get(position).isChecked()) {
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public List_ViewHolder onCreateViewHolder(ViewGroup root, int viewType) {
        View list = LayoutInflater.from(root.getContext()).inflate(R.layout.category_filter_inner_row, root, false);
        return new List_ViewHolder(list);
    }

}
