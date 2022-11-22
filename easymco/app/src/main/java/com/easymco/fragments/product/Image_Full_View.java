package com.easymco.fragments.product;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easymco.R;
import com.easymco.adapter.ImageFullViewAdapter;
import com.easymco.constant_class.JSON_Names;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.custom.ZoomImageView;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.ProductImageDataSet;

import java.util.ArrayList;

public class Image_Full_View extends Fragment {
    ZoomImageView mParent;
    RecyclerView mImageRecyclerView;
    String mImageData;
    ArrayList<ProductImageDataSet> mImageList;

    public Image_Full_View() {
    }

    public static Image_Full_View getInstance(String data) {
        Image_Full_View image_full_view = new Image_Full_View();
        Bundle bundle = new Bundle();
        bundle.putString(JSON_Names.KEY_PRODUCT_ID, data);
        image_full_view.setArguments(bundle);
        return image_full_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageData = getArguments().getString(JSON_Names.KEY_PRODUCT_ID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(AppLanguageSupport.onAttach(context));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getActivity().getWindow().getDecorView().setLayoutDirection(
                    "ar".equals(AppLanguageSupport.getLanguage(getActivity())) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail_image_full_view, container, false);
        setting(view);
        return view;
    }

    @SuppressWarnings("unchecked")
    public void setting(View view) {

        mImageList = GetJSONData.getImageList(mImageData);

        mParent = (ZoomImageView) view.findViewById(R.id.image_full_view_parent_image);

        image_caller(mParent, mImageList.get(0).getChildImage());

        mImageRecyclerView = (RecyclerView) view.findViewById(R.id.image_full_view_recycler_view);

        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, JSON_Names.KEY_FALSE_BOOLEAN));
        mImageRecyclerView.setAdapter(new ImageFullViewAdapter(getActivity().getApplicationContext(), mImageList, mParent));
    }

    public void image_caller(ZoomImageView imageView, String url) {
        imageView.reset();
        imageView.setMaxZoom(3.0f);
        Methods.glide_image_loader_banner(url, imageView);
    }
}
