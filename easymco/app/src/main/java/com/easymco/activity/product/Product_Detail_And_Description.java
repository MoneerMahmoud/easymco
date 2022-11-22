package com.easymco.activity.product;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.easymco.R;
import com.easymco.adapter.Fragment_Page_Adapter;
import com.easymco.constant_class.JSON_Names;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.models.ProductDataSet;

import java.util.HashMap;

public class Product_Detail_And_Description extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    Fragment_Page_Adapter fragment_page_adapter;

    String mProduct_String;
    ProductDataSet mProductDataSet = new ProductDataSet();
    HashMap<String, Object> mDataSet = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_and_description_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = (TabLayout) findViewById(R.id.product_detail_and_description_tab);
        viewPager = (ViewPager) findViewById(R.id.product_detail_and_description_viewpager);

        if (getIntent().getExtras().getString(JSON_Names.KEY_PRODUCT_STRING) != null) {
            mProduct_String = getIntent().getExtras().getString(JSON_Names.KEY_PRODUCT_STRING);
            //mProduct_String = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
            mDataSet = GetJSONData.getSeparateProductDetail(mProduct_String);
            if (mDataSet != null) {
                mProductDataSet = (ProductDataSet) mDataSet.get(JSON_Names.KEY_PD_PRODUCT);
            }

            fragment_page_adapter = new Fragment_Page_Adapter(Product_Detail_And_Description.this.getSupportFragmentManager(), mProduct_String/*,
             mProductDataSet.getDescription()*/,Product_Detail_And_Description.this);
            viewPager.setAdapter(fragment_page_adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base));
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(
                    "ar".equals(AppLanguageSupport.getLanguage(Product_Detail_And_Description.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
