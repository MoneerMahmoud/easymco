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
import com.easymco.adapter.Product_Specification_Adapter;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.json_mechanism.GetJSONData;

import java.util.ArrayList;

public class Product_Details_Description_fragment extends Fragment {

    String product_string;
    RecyclerView mSpecification_holder;
    View view;
    ArrayList<Object> mResultDataSet = new ArrayList<>();

    public Product_Details_Description_fragment() {

    }

    public static Product_Details_Description_fragment getInstance(String data) {
        Product_Details_Description_fragment checkOut_delivery_type = new Product_Details_Description_fragment();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        checkOut_delivery_type.setArguments(bundle);
        return checkOut_delivery_type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product_string = getArguments().getString("Data");
        }
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

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_details_specification_fragment, container, false);
        mSpecification_holder = view.findViewById(R.id.product_specification_recycler_view);
        mResultDataSet = GetJSONData.getProductSpecificationForSpecification(product_string);
        if (mResultDataSet != null) {
            mSpecification_holder.setLayoutManager(new LinearLayoutManager(getActivity()));
            mSpecification_holder.setAdapter(new Product_Specification_Adapter(getActivity(), mResultDataSet));
        }
        return view;
    }
}
