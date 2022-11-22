package com.easymco.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.category.Category_Details;
import com.easymco.constant_class.JSON_Names;
import com.easymco.mechanism.Methods;
import com.easymco.models.CategoryDataSet;

import java.util.ArrayList;

class HomeCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    private ArrayList<CategoryDataSet> mHomeCategoryList = new ArrayList<>();

    HomeCategoryAdapter(Context context, ArrayList<CategoryDataSet> list) {
        this.mContext = context;
        this.mHomeCategoryList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.home_category_row, parent, false);
        viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;


        if (position == 0 || position == 3) {
            categoryViewHolder.mCategoryTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            categoryViewHolder.mCategoryTitle.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_transparent_60));
        } else {
            categoryViewHolder.mCategoryTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            categoryViewHolder.mCategoryTitle.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_transparent_60));
        }
        categoryViewHolder.mCategoryTitle.setText(mHomeCategoryList.get(position).getName());

        if (mHomeCategoryList.get(position).getIcon_image() != null) {
            String url = mHomeCategoryList.get(position).getIcon_image();
            Methods.glide_image_loader_banner(url, categoryViewHolder.mCategoryImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mHomeCategoryList != null) {
            if (mHomeCategoryList.size() > 7) {
                return 6;
            } else {
                if (mHomeCategoryList.size() % 2 == 0) {
                    return mHomeCategoryList.size();
                } else {
                    return mHomeCategoryList.size() - 1;
                }
            }
        } else {
            return 0;
        }
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mCategoryImageView;
        TextView mCategoryTitle;

        CategoryViewHolder(View view) {
            super(view);
            mCategoryImageView = view.findViewById(R.id.home_category_image);
            mCategoryTitle = view.findViewById(R.id.home_category_title);

            mCategoryImageView.setOnClickListener(this);
            mCategoryTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.home_category_image:
                    intent_transfer(getAdapterPosition());
                    break;
                case R.id.home_category_title:
                    intent_transfer(getAdapterPosition());
                    break;
            }
        }
    }

    private void intent_transfer(int position) {
        Intent intent = new Intent(mContext, Category_Details.class);
        intent.putExtra(JSON_Names.KEY_TITLE_SHARED_PREFERENCE, mHomeCategoryList.get(position).getName());
        intent.putExtra(JSON_Names.KEY_IMAGE_URL_SHARED_PREFERENCE, mHomeCategoryList.get(position).getImage());
        intent.putExtra(JSON_Names.KEY_CATEGORY_ID_SHARED_PREFERENCE, mHomeCategoryList.get(position).getCategory_id());
        intent.putExtra(JSON_Names.KEY_TYPE_SHARED_PREFERENCE, R.string.sort_category);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
