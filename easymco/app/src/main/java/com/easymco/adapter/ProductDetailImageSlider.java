package com.easymco.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.easymco.R;
import com.easymco.interfaces.EnquiryPost;
import com.easymco.mechanism.Methods;

public class ProductDetailImageSlider extends PagerAdapter {

    Context mContext;
    private String value[];
    private EnquiryPost enquiryPost;

    public ProductDetailImageSlider(Context context, String sources[], EnquiryPost enquiryPost) {
        this.mContext = context;
        this.value = sources;
        this.enquiryPost=enquiryPost;
    }

    @Override
    public int getCount() {
        return value.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.product_detail_image_holder, container, false);
        ImageView image =  view.findViewById(R.id.home_image);

        Methods.glide_image_loader_banner(value[position],image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enquiryPost.enquiryPost("Clicked");
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
