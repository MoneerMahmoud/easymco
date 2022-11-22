package com.easymco.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.models.ProductReviewListDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Review_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private ArrayList<ProductReviewListDataSet> mReviewList;
    private int DATA = 1;

    public Review_Adapter(Context context, ArrayList<ProductReviewListDataSet> value) {
        this.mContext = context;
        this.mReviewList = value;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == DATA) {
            view = LayoutInflater.from(mContext).inflate(R.layout.product_detail_review_row, parent, false);
            holder = new Review_ViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.no_data, parent, false);
            holder = new ViewHolderEmpty_View(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == DATA) {
            if (mReviewList != null) {
                Review_ViewHolder review_viewHolder = (Review_ViewHolder) holder;
                review_viewHolder.mReviewer_Name.setVisibility(View.VISIBLE);
                review_viewHolder.mReview.setVisibility(View.VISIBLE);
                review_viewHolder.mReview_Rating.setVisibility(View.VISIBLE);
                review_viewHolder.mReviewer_Name.setText(mReviewList.get(position).getProduct_author());
                String txtReview = "" + Html.fromHtml("<html><body>" + "<p align=\"justify\">" + mReviewList.get(position).getProduct_text() + "</p> " + "</body></html>");
                review_viewHolder.mReview.setText(txtReview.trim());
                review_viewHolder.mReview_Rating.setRating(Float.valueOf(mReviewList.get(position).getProduct_rating()));
                if (mReviewList.get(position).getProduct_date() != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = format.parse(mReviewList.get(position).getProduct_date());
                        SimpleDateFormat dfYear = new SimpleDateFormat("dd-MMM-yyyy");
                        String year = dfYear.format(date);
                        review_viewHolder.mReviewDate.setVisibility(View.VISIBLE);
                        review_viewHolder.mReviewDate.setText(year);
                    } catch (Exception e) {
                        review_viewHolder.mReviewDate.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            ViewHolderEmpty_View empty_view = (ViewHolderEmpty_View) holder;
            empty_view.empty_view.setText(R.string.there_is_no_review_for_this_product);
        }

    }

    @Override
    public int getItemCount() {
        if (mReviewList != null) {
            if (mReviewList.size() == 0) {
                return 1;
            } else {
                return mReviewList.size();
            }
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mReviewList == null || mReviewList.size() == 0) {
            return 0;
        } else {
            return DATA;
        }
    }

    private class Review_ViewHolder extends RecyclerView.ViewHolder {
        TextView mReviewer_Name, mReview, mReviewDate;
        RatingBar mReview_Rating;

        Review_ViewHolder(View view) {
            super(view);
            mReviewer_Name = view.findViewById(R.id.product_detail_and_description_review_name);
            mReview = view.findViewById(R.id.product_detail_and_description_review_text);
            mReview_Rating = view.findViewById(R.id.product_detail_and_description_review_rating);
            mReviewDate = view.findViewById(R.id.product_detail_and_description_review_date);
        }
    }

    private class ViewHolderEmpty_View extends RecyclerView.ViewHolder {
        TextView empty_view;

        ViewHolderEmpty_View(View view) {
            super(view);
            empty_view = view.findViewById(R.id.empty_view);
        }
    }
}
