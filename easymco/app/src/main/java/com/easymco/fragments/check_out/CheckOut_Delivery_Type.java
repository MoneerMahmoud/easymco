package com.easymco.fragments.check_out;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerConfirmOrder;
import com.easymco.interfaces.CheckOutAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.ShippingAndPayment_DataSet;

import java.util.ArrayList;

public class CheckOut_Delivery_Type extends Fragment {
    View view;
    RadioGroup mShippingGroup;
    RadioGroup.LayoutParams layoutParams;
    Button mShippingContinue;
    ArrayList<ShippingAndPayment_DataSet> ShippingList = new ArrayList<>();
    ShippingAndPayment_DataSet dataSet = new ShippingAndPayment_DataSet();
    ImageButton mCart, mDeliveryDetail;
    CheckOutAPIRequest checkOutAPIRequest;
    String result;
    TextView mChangeShippingAdds;

    public CheckOut_Delivery_Type() {

    }

    public static CheckOut_Delivery_Type getInstance(String data) {
        CheckOut_Delivery_Type checkOut_delivery_type = new CheckOut_Delivery_Type();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        checkOut_delivery_type.setArguments(bundle);
        return checkOut_delivery_type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkOutAPIRequest = (CheckOutAPIRequest) getActivity();
        if (getArguments() != null) {
            result = getArguments().getString("Data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.checkout_delivery_type, container, false);
        action(result);
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

    public void action(String data) {
        final int check_list[];
        mShippingGroup = view.findViewById(R.id.shipping_type_chooser);
        mShippingContinue = view.findViewById(R.id.shipping_type_continue);
        mCart = view.findViewById(R.id.check_out_delivery_type_cart);
        mDeliveryDetail = view.findViewById(R.id.check_out_delivery_type_delivery_detail);
        mChangeShippingAdds = view.findViewById(R.id.shipping_type_not_avail_message);
        mChangeShippingAdds.setVisibility(View.GONE);

        ShippingList = GetJSONData.getShippingMethod(data);
        if (ShippingList != null && ShippingList.size() > 0) {

            mChangeShippingAdds.setVisibility(View.GONE);

            check_list = new int[ShippingList.size()];
            for (int i = 0; i < ShippingList.size(); i++) {
                RadioButton radioButton = new RadioButton(getActivity());
                int id = i + 10;
                check_list[i] = id;
                radioButton.setId(id);
                if (Methods.current_language().equals("ar")) {
                    radioButton.setText(ShippingList.get(i).getmTitle());
                } else {
                    String cap = ShippingList.get(i).getmTitle().substring(0, 1).toUpperCase() + ShippingList.get(i).getmTitle().substring(1);
                    radioButton.setText(cap);
                }
                radioButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color));
                layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mShippingGroup.addView(radioButton, layoutParams);
            }
            mDeliveryDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            mCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            mShippingContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = mShippingGroup.getCheckedRadioButtonId();
                    int count = 0;
                    for (int t = 0; t < check_list.length; t++) {
                        if (id == check_list[t]) {
                            if (DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).get_Size_shipping() == 0) {
                                dataSet.setmTitle(ShippingList.get(t).getmTitle());
                                dataSet.setmCode(ShippingList.get(t).getmCode());
                                dataSet.setmCost(ShippingList.get(t).getmCost());
                                dataSet.setmTaxClassId(ShippingList.get(t).getmTaxClassId());
                                dataSet.setmSortOrder(ShippingList.get(t).getmSortOrder());
                                DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).insert_shipping_type(dataSet);
                            } else {
                                dataSet.setmTitle(ShippingList.get(t).getmTitle());
                                dataSet.setmCode(ShippingList.get(t).getmCode());
                                dataSet.setmCost(ShippingList.get(t).getmCost());
                                dataSet.setmTaxClassId(ShippingList.get(t).getmTaxClassId());
                                dataSet.setmSortOrder(ShippingList.get(t).getmSortOrder());
                                DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).update_shipping_type(dataSet);
                            }
                            count++;
                        }
                    }
                    if (count == 0) {
                        Methods.toast(getActivity().getResources().getString(R.string.check_out_select_any_one));
                    } else {
                        checkOutAPIRequest.checkout_payment_type_request();
                    }

                }
            });
        }else {
            mChangeShippingAdds.setVisibility(View.VISIBLE);
            mShippingContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }
    }
}
