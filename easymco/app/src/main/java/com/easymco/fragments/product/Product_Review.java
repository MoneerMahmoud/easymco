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
import android.widget.Button;

import com.easymco.R;
import com.easymco.adapter.Review_Adapter;
import com.easymco.constant_class.JSON_Names;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.models.ProductReviewListDataSet;

import java.util.ArrayList;

public class Product_Review extends Fragment {
    View view;
    RecyclerView review_result;
    String product_string;
    ArrayList<ProductReviewListDataSet> mReviewList = new ArrayList<>();
    Button back_to_list;

    public Product_Review() {
    }

    public static Product_Review getInstance(String data) {
        Product_Review product_review = new Product_Review();
        Bundle bundle = new Bundle();
        bundle.putString(JSON_Names.KEY_PRODUCT_ID, data);
        product_review.setArguments(bundle);
        return product_review;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product_string = getArguments().getString(JSON_Names.KEY_PRODUCT_ID);
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
        view = inflater.inflate(R.layout.fragment_review, container, false);
        setup();
        return view;
    }

    public void setup() {
        review_result = view.findViewById(R.id.review_loader_recycler_view);
        back_to_list = view.findViewById(R.id.btn_back);
        review_result.setLayoutManager(new LinearLayoutManager(getActivity()));

        mReviewList = GetJSONData.getProductReviewList(product_string);
        review_result.setAdapter(new Review_Adapter(getActivity(), mReviewList));

        back_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                getActivity().finish();
            }
        });
    }

}
