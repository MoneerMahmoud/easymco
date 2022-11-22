package com.easymco.fragments.check_out;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.NoInternetConnection;
import com.easymco.adapter.CheckOut_Adapter;
import com.easymco.constant_class.JSON_Names;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccountAddress;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.CheckOutAPIRequest;
import com.easymco.interfaces.CheckOutTransaction;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.AccountDataSet;
import com.easymco.models.CartDataSet;
import com.easymco.models.ConfirmResponseDataSet;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.util.ArrayList;

public class CheckOut_Confirmation extends Fragment {
    View view;
    Button mChangeOrder, mPlaceOrder;
    String order_id = "0";
    RecyclerView mPurchasedList/*, mPurchasedShippingList*/;
    LinearLayout mPurchasedShippingList;
    TextView mShippingAddress;
    ArrayList<ConfirmResponseDataSet> totals_list = new ArrayList<>();
    ConfirmResponseDataSet confirmResponseDataSet;
    ImageButton mCart, mDeliveryDetail, mDeliveryType, mPaymentType;
    API_Result api_result;
    CheckOutTransaction checkOutTransaction;
    CheckOutAPIRequest checkOutAPIRequest;
    String result;

    public CheckOut_Confirmation() {
    }

    public static CheckOut_Confirmation getInstance(String data) {
        CheckOut_Confirmation checkOutConfirmation = new CheckOut_Confirmation();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        checkOutConfirmation.setArguments(bundle);
        return checkOutConfirmation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api_result = (API_Result) getActivity();
        checkOutTransaction = (CheckOutTransaction) getActivity();
        checkOutAPIRequest = (CheckOutAPIRequest) getActivity();
        if (getArguments() != null) {
            result = getArguments().getString("Data");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.checkout_confirmation_place_order, container, false);
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

    @SuppressWarnings("unchecked")
    public void action(String data) {

        mShippingAddress = view.findViewById(R.id.confirmation_shipping_address);
        mPurchasedList = view.findViewById(R.id.confirmation_shipping_product_list);
        mChangeOrder = view.findViewById(R.id.confirmation_shipping_product_modification);
        mPlaceOrder = view.findViewById(R.id.confirmation_shipping_place_order);
        mCart = view.findViewById(R.id.check_out_confirm_order_cart);
        mDeliveryDetail = view.findViewById(R.id.check_out_confirm_order_delivery_detail);
        mDeliveryType = view.findViewById(R.id.check_out_confirm_order_delivery_type);
        mPaymentType = view.findViewById(R.id.check_out_confirm_order_payment_type);
      //  mPurchasedShippingList = view.findViewById(R.id.confirmation_shipping_purchase_list);
        mPurchasedShippingList =  view.findViewById(R.id.confirmation_shipping_purchase_list);

        confirmResponseDataSet = GetJSONData.get_confirmation_detail(data);
        if (confirmResponseDataSet != null) {
            if (confirmResponseDataSet.getmTotalList() != null)
                totals_list = confirmResponseDataSet.getmTotalList();
            order_id = confirmResponseDataSet.getmOrderId();
        }

        if (totals_list != null) {
          /*  mPurchasedShippingList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mPurchasedShippingList.setAdapter(new CheckOut_Confirmation_Shipping_List(getActivity().getApplicationContext(), totals_list));
       */


            LayoutInflater calInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for(int t = 0;t < totals_list.size() ; t++){
                View shipView = calInflater.inflate(R.layout.checkout_totals_row, null);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                TextView shipTitle, shipValue;
                shipTitle =  shipView.findViewById(R.id.tv_title);
                shipValue =  shipView.findViewById(R.id.tv_value);
                shipTitle.setText(totals_list.get(t).getmTotalsTitle());
                shipValue.setText(totals_list.get(t).getmTotalsValue());

                LinearLayout.LayoutParams shipParams = new LinearLayout.LayoutParams(width, height);
                shipView.setLayoutParams(shipParams);
                mPurchasedShippingList.addView(shipView);
            }



        }

        mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                getActivity().finish();
            }
        });

        mDeliveryDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                getActivity().getSupportFragmentManager().popBackStack("Second", 1);
            }
        });

        mDeliveryType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                getActivity().getSupportFragmentManager().popBackStack("Third", 3);
            }
        });

        mPaymentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        AccountDataSet dataSet = DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).get_account_shipping_address();
        String address = dataSet.getmFirstName() + dataSet.getmLastName() + " " + dataSet.getmAddress_1() + " " + dataSet.getmCity() + " " + dataSet.getmState() + " " + dataSet.getmCountry();
        mShippingAddress.setText(address);

        String result = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_CART_DATA);
        ArrayList<CartDataSet> mDataSet = GetJSONData.get_cart_detail(result);
        if (mDataSet != null) {
            mPurchasedList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mPurchasedList.setAdapter(new CheckOut_Adapter(getActivity().getApplicationContext(), mDataSet));
        }

        mChangeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                checkOutTransaction.checkout_cart_changer();
            }
        });

        mPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                if (NetworkConnection.connectionChecking(getActivity().getApplicationContext())) {
                    checkOutAPIRequest.checkout_place_order(order_id);
                } else {
                    Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }
}
