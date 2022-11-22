package com.easymco.fragments.product;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.constant_class.JSON_Names;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.models.ProductDataSet;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.util.HashMap;

public class Product_Description_Fragment extends Fragment {
    TextView product_description;
    String mProduct_String;
    ProductDataSet mProductDataSet = new ProductDataSet();
    HashMap<String, Object> mDataSet = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail_description_fragment, container, false);
        product_description = view.findViewById(R.id.product_description);
        mProduct_String = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING);
        mDataSet = GetJSONData.getSeparateProductDetail(mProduct_String);
        if (mDataSet != null) {
            mProductDataSet = (ProductDataSet) mDataSet.get(JSON_Names.KEY_PD_PRODUCT);
            if (mProductDataSet.getDescription() != null) {
                String txtDescription = "     " + Html.fromHtml("<html><body>" + "<p align=\"justify\">" + mProductDataSet.getDescription() + "</p> " + "</body></html>");
                product_description.setText(txtDescription);
            }
        }
        return view;
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

}
