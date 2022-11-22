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

import com.easymco.R;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerConfirmOrder;
import com.easymco.interfaces.CheckOutAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.ShippingAndPayment_DataSet;

import java.util.ArrayList;

public class CheckOut_Payment_Type extends Fragment {
    View view;
    RadioGroup mPaymentGroup;
    Button mPaymentContinue;
    ArrayList<ShippingAndPayment_DataSet> PaymentList = new ArrayList<>();
    ShippingAndPayment_DataSet dataSet = new ShippingAndPayment_DataSet();
    RadioGroup.LayoutParams layoutParams;
    ImageButton mCart, mDeliveryDetail, mDeliveryType;
    CheckOutAPIRequest checkOutAPIRequest;
    String result;

    public CheckOut_Payment_Type() {
    }

    public static CheckOut_Payment_Type getInstance(String data) {
        CheckOut_Payment_Type checkOut_payment_type = new CheckOut_Payment_Type();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        checkOut_payment_type.setArguments(bundle);
        return checkOut_payment_type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkOutAPIRequest = (CheckOutAPIRequest) getActivity();
        if (getArguments() != null) {
            result = getArguments().getString("Data");
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.checkout_payment_type, container, false);
        action(result);
        return view;
    }

    public void action(String data) {
        final int check_list[];
        PaymentList = GetJSONData.getPaymentMethod(data);
        mPaymentGroup = view.findViewById(R.id.payment_type_chooser);
        mPaymentContinue = view.findViewById(R.id.payment_type_continue);
        mCart = view.findViewById(R.id.check_out_payment_type_cart);
        mDeliveryDetail = view.findViewById(R.id.check_out_payment_type_delivery_detail);
        mDeliveryType = view.findViewById(R.id.check_out_payment_type_delivery_type);

        mDeliveryDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack("Second", 1);
            }
        });

        mDeliveryType.setOnClickListener(new View.OnClickListener() {
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

        if (PaymentList != null) {
            check_list = new int[PaymentList.size()];
            for (int i = 0; i < PaymentList.size(); i++) {
                if (PaymentList.get(i).getmCode().equals("cod") ) {
                    RadioButton radioButton = new RadioButton(getActivity());
                    int id = i + 10;
                    check_list[i] = id;
                    radioButton.setId(id);

                    if (PaymentList.get(i).getmCode().equals("cod")) {
                        radioButton.setText(getActivity().getResources().getString(R.string.cash_on_delivery));
                    } else {
                        String cap = PaymentList.get(i).getmTitle().substring(0, 1).toUpperCase() + PaymentList.get(i).getmTitle().substring(1);
                        radioButton.setText(cap);
                    }

                    radioButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color));
                    layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mPaymentGroup.addView(radioButton, layoutParams);
                }
            }
            mPaymentContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = mPaymentGroup.getCheckedRadioButtonId();
                    int count = 0;
                    for (int t = 0; t < check_list.length; t++) {
                        if (id == check_list[t]) {
                            if (DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).get_Size_payment() == 0) {
                                dataSet.setmTitle(PaymentList.get(t).getmTitle());
                                dataSet.setmCode(PaymentList.get(t).getmCode());
                                dataSet.setmTerms(PaymentList.get(t).getmTerms());
                                dataSet.setmSortOrder(PaymentList.get(t).getmSortOrder());
                                DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).insert_payment_type(dataSet);
                            } else {
                                dataSet.setmTitle(PaymentList.get(t).getmTitle());
                                dataSet.setmCode(PaymentList.get(t).getmCode());
                                dataSet.setmTerms(PaymentList.get(t).getmTerms());
                                dataSet.setmSortOrder(PaymentList.get(t).getmSortOrder());
                                DataBaseHandlerConfirmOrder.getInstance(getActivity().getApplicationContext()).update_payment_type(dataSet);
                            }
                            count++;
                        }
                    }
                    if (count == 0) {
                        Methods.toast(getActivity().getResources().getString(R.string.check_out_select_any_one));
                    } else {
                        checkOutAPIRequest.checkout_confirmation();
                    }

                }
            });
        }
    }
}