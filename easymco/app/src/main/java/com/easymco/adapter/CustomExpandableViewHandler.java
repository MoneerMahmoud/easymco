package com.easymco.adapter;


import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.db_handler.DataBaseLanguageDetails;
import com.easymco.mechanism.Methods;
import com.easymco.models.Navigation_DataSet;

import java.util.ArrayList;

class CustomExpandableViewHandler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Navigation_DataSet> mParentList = new ArrayList<>();

    CustomExpandableViewHandler(Context context, ArrayList<Navigation_DataSet> list) {
        this.mContext = context;
        this.mParentList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomExpandableRowHolder(LayoutInflater.from(mContext).inflate(R.layout.home_custom_expandable_view_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomExpandableRowHolder customExpandableRowHolder = (CustomExpandableRowHolder) holder;

        if (mParentList.get(position).getmParentList().getOpened()) {
            if(DataBaseLanguageDetails.getInstance(mContext).check_language_selected()) {
                String languageId = DataBaseLanguageDetails.getInstance(mContext).get_language_id();
                if (languageId.equals("1")) {
                    customExpandableRowHolder.title.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, ContextCompat.getDrawable(mContext, R.drawable.ic_remove_black_18dp), null);

                } else {
                    customExpandableRowHolder.title.setCompoundDrawablesWithIntrinsicBounds(
                            ContextCompat.getDrawable(mContext, R.drawable.ic_remove_black_18dp),
                            null, null, null);
                }
            }
            customExpandableRowHolder.subListHolder.setVisibility(View.VISIBLE);
        } else {
            if(DataBaseLanguageDetails.getInstance(mContext).check_language_selected()) {
                String languageId = DataBaseLanguageDetails.getInstance(mContext).get_language_id();
                if (languageId.equals("1")) {
                    customExpandableRowHolder.title.setCompoundDrawablesWithIntrinsicBounds(null,
                            null, ContextCompat.getDrawable(mContext, R.drawable.ic_add_black_18dp), null);
                } else {
                    customExpandableRowHolder.title.setCompoundDrawablesWithIntrinsicBounds(
                            ContextCompat.getDrawable(mContext, R.drawable.ic_add_black_18dp),
                            null, null, null);
                }
            }
            customExpandableRowHolder.subListHolder.setVisibility(View.GONE);
        }
        customExpandableRowHolder.title.setText(mParentList.get(position).getmParentList().getName());
        customExpandableRowHolder.subListHolder.setLayoutManager(new LinearLayoutManager(mContext));
        customExpandableRowHolder.subListHolder.setAdapter(new SubCategoryAdapter(mContext, mParentList.get(position).getmChildAdder()));
        Methods.glide_image_loader(mParentList.get(position).getmParentList().getMenu_icon(), customExpandableRowHolder.mCategoryIcon);
    }

    @Override
    public int getItemCount() {
        return mParentList.size();
    }

    private class CustomExpandableRowHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView subListHolder;
        ImageView mCategoryIcon;

        CustomExpandableRowHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.custom_expandable_row_title);
            subListHolder = itemView.findViewById(R.id.custom_expandable_row_child_list);
            mCategoryIcon = itemView.findViewById(R.id.category_menu_title_icon);

            mCategoryIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() != -1) {
                        if (!mParentList.get(getAdapterPosition()).getmParentList().getOpened()) {
                            mParentList.get(getAdapterPosition()).getmParentList().setOpened(true);
                        } else {
                            mParentList.get(getAdapterPosition()).getmParentList().setOpened(false);
                        }
                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != -1) {
                        if (!mParentList.get(getAdapterPosition()).getmParentList().getOpened()) {
                            mParentList.get(getAdapterPosition()).getmParentList().setOpened(true);
                        } else {
                            mParentList.get(getAdapterPosition()).getmParentList().setOpened(false);
                        }
                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });
        }
    }

}
