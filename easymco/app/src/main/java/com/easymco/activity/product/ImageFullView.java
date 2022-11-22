package com.easymco.activity.product;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.easymco.R;
import com.easymco.constant_class.JSON_Names;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.fragments.product.Image_Full_View;
import com.easymco.fragments.product.Product_Review;

public class ImageFullView extends AppCompatActivity {
    Toolbar toolbar;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_image_full_view_activity);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(JSON_Names.KEY_TRUE_BOOLEAN);

        String source = getIntent().getExtras().getString(JSON_Names.KEY_FROM);
        String product_string = getIntent().getExtras().getString(JSON_Names.KEY_PRODUCT_STRING);
        if (source != null) {
            if (source.equals("image")) {
                Image_Full_View image_full_view = Image_Full_View.getInstance(product_string);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.view_holder_image_or_review, image_full_view, "Image");
                fragmentTransaction.commit();
            } else if (source.equals("Review")) {
                Product_Review product_review = Product_Review.getInstance(product_string);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.view_holder_image_or_review, product_review, "Review");
                fragmentTransaction.commit();
            }
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
                    "ar".equals(AppLanguageSupport.getLanguage(ImageFullView.this)) ?
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
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
