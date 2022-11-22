package com.easymco.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.db_handler.DataBaseLanguageDetails;

public class Category_Sub_List_Adapter extends RecyclerView.Adapter<Category_Sub_List_Adapter.View_List> {

    private String[] list;
    Context mContext;

    public Category_Sub_List_Adapter(String[] list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public View_List onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_sub_category_list_row, parent, false);
        return new View_List(view);
    }

    @Override
    public void onBindViewHolder(View_List holder, int position) {
        holder.title.setText(list[position]);

        if(DataBaseLanguageDetails.getInstance(mContext).check_language_selected()) {
            String languageId = DataBaseLanguageDetails.getInstance(mContext).get_language_id();
            if (languageId.equals("1")) {

                holder.title.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, ContextCompat.getDrawable(mContext,
                                R.drawable.ic_keyboard_arrow_right_grey_500_24dp), null);
            }else {

                holder.title.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_keyboard_arrow_left_grey_500_24dp),
                        null, null, null);

            }
        }
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    class View_List extends RecyclerView.ViewHolder {
        TextView title;

        View_List(View view) {
            super(view);
            title = view.findViewById(R.id.sub_category_recycler_view_row_text);
        }
    }
}