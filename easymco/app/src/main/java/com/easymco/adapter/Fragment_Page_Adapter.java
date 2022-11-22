package com.easymco.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.easymco.R;
import com.easymco.fragments.product.Product_Details_Description_fragment;

public class Fragment_Page_Adapter extends FragmentStatePagerAdapter {

   // private String title[] = {Application_Context.getAppContext().getResources().getString(R.string.detail_specification)};
    private String title[] = new String[1];
    private String mProductString;

    public Fragment_Page_Adapter(FragmentManager fm, String product_string/*,String description*/,
                                 Context context) {
        super(fm);
        this.mProductString = product_string;
        title[0] = context.getResources().getString(R.string.detail_specification);
    }

    @Override
    public Fragment getItem(int position) {
        return Product_Details_Description_fragment.getInstance(mProductString);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return title[position];
    }

    @Override
    public int getCount() {
        return title.length;
    }

}
