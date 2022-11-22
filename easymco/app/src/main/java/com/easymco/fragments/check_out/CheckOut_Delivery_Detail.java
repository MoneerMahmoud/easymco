package com.easymco.fragments.check_out;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.user.Login;
import com.easymco.constant_class.JSON_Names;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerAccountAddress;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.CheckOutAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.models.AccountDataSet;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.util.ArrayList;

public class CheckOut_Delivery_Detail extends Fragment {
    View view;
    Button mDeliveryDetailContinue;
    TextView mShippingFirstName, mShippingLastName, mShippingPhoneNumber, mShippingCompany,
            mShippingStreetAddress, mShippingCity, mShippingZipCode, mShippingCountry, mShippingState;
    ImageButton mChangeAddress;
    ArrayList<AccountDataSet> accountDataSetAddressList = new ArrayList<>();
    AccountDataSet accountDataSetAddress = new AccountDataSet();
    AccountDataSet mShippingDataSet = new AccountDataSet();
    AccountDataSet mPaymentDataSet = new AccountDataSet();
    ImageButton mCart;
    API_Result api_result;
    CheckOutAPIRequest checkOutAPIRequest;
    String result;

    public CheckOut_Delivery_Detail() {
    }

    public static CheckOut_Delivery_Detail getInstance(String data) {
        CheckOut_Delivery_Detail checkOut_delivery_detail = new CheckOut_Delivery_Detail();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        checkOut_delivery_detail.setArguments(bundle);
        return checkOut_delivery_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api_result = (API_Result) getActivity();
        checkOutAPIRequest = (CheckOutAPIRequest) getActivity();
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.getString("Data") != null) {
                result = bundle.getString("Data");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.checkout_delivery_detail, container, false);
        action();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void action() {
        if (DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).check_login()) {
            mShippingFirstName = view.findViewById(R.id.shipment_first_name);
            mShippingLastName = view.findViewById(R.id.shipment_last_name);
            mShippingPhoneNumber = view.findViewById(R.id.shipment_phone_number);
            mShippingCompany = view.findViewById(R.id.shipment_company);
            mShippingStreetAddress = view.findViewById(R.id.shipment_address);
            mShippingCity = view.findViewById(R.id.shipment_city);
            mShippingZipCode = view.findViewById(R.id.shipment_zip_code);
            mShippingState = view.findViewById(R.id.shipment_state);
            mShippingCountry = view.findViewById(R.id.shipment_country);
            mChangeAddress = view.findViewById(R.id.confirmation_shipping_address_change);
            mCart = view.findViewById(R.id.check_out_delivery_detail_cart);

            mDeliveryDetailContinue = view.findViewById(R.id.delivery_continue_btn);

            data_setting(result);

        } else {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

    }

    public void continue_to_delivery_method() {
        checkOutAPIRequest.checkout_delivery_detail_request();
    }

    public AccountDataSet getDefaultAddress(ArrayList<AccountDataSet> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getmAddress_Id().equals(list.get(i).getmDefaultAddressId())) {
                    DataStorage.mStoreSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_DEFAULT_ADDRESS_ID, list.get(i).getmDefaultAddressId());
                    return list.get(i);
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public void data_setting(String profile_details) {
        accountDataSetAddressList = GetJSONData.getCustomerAddress(profile_details);
        accountDataSetAddress = getDefaultAddress(accountDataSetAddressList);
        if (accountDataSetAddress != null) {
            mShippingFirstName.setText(accountDataSetAddress.getmFirstName());
            mShippingLastName.setText(accountDataSetAddress.getmLastName());
            mShippingPhoneNumber.setText(accountDataSetAddress.getmTelePhone());
            mShippingCompany.setText(accountDataSetAddress.getmCompany());
            if (accountDataSetAddress.getmAddress_2() != null) {
                String address = accountDataSetAddress.getmAddress_1() + accountDataSetAddress.getmAddress_2();
                mShippingStreetAddress.setText(address);
            } else {
                mShippingStreetAddress.setText(accountDataSetAddress.getmAddress_1());
            }
            mShippingCity.setText(accountDataSetAddress.getmCity());
            mShippingZipCode.setText(accountDataSetAddress.getmPostcode());
            mShippingCountry.setText(accountDataSetAddress.getmCountry());
            if (accountDataSetAddress.getmZone_id() != null) {
                mShippingState.setText(accountDataSetAddress.getmState());
            } else {
                String dummy_state = "None";
                mShippingState.setText(dummy_state);
            }
        }

        mChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutAPIRequest.checkout_edit_address();
            }
        });

        mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mDeliveryDetailContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mShippingDataSet.setmFirstName(mShippingFirstName.getText().toString());
                mShippingDataSet.setmLastName(mShippingLastName.getText().toString());
                mShippingDataSet.setmTelePhone(mShippingPhoneNumber.getText().toString());
                mShippingDataSet.setmCompany(mShippingCompany.getText().toString());
                mShippingDataSet.setmAddress_1(mShippingStreetAddress.getText().toString());
                mShippingDataSet.setmCity(mShippingCity.getText().toString());
                mShippingDataSet.setmPostcode(mShippingZipCode.getText().toString());
                if (accountDataSetAddress.getmState() != null) {
                    mShippingDataSet.setmState(accountDataSetAddress.getmState());
                    mShippingDataSet.setmState_Id(accountDataSetAddress.getmZone_id());
                } else {
                    mShippingDataSet.setmState("0");
                    mShippingDataSet.setmState_Id("0");
                }
                mShippingDataSet.setmCountry(accountDataSetAddress.getmCountry());
                mShippingDataSet.setmEmailId(accountDataSetAddress.getmEmailId());
                mShippingDataSet.setmCountry_id(accountDataSetAddress.getmCountry_id());
                mShippingDataSet.setmAddress_2("0");
                mPaymentDataSet.setmFirstName(mShippingFirstName.getText().toString());
                mPaymentDataSet.setmLastName(mShippingLastName.getText().toString());
                mPaymentDataSet.setmTelePhone(mShippingPhoneNumber.getText().toString());
                mPaymentDataSet.setmCompany(mShippingCompany.getText().toString());
                mPaymentDataSet.setmAddress_1(mShippingStreetAddress.getText().toString());
                mPaymentDataSet.setmCity(mShippingCity.getText().toString());
                mPaymentDataSet.setmPostcode(mShippingZipCode.getText().toString());
                if (accountDataSetAddress.getmState() != null) {
                    mPaymentDataSet.setmState(accountDataSetAddress.getmState());
                    mPaymentDataSet.setmState_Id(accountDataSetAddress.getmZone_id());
                } else {
                    mPaymentDataSet.setmState("0");
                    mPaymentDataSet.setmState_Id("0");
                }
                mPaymentDataSet.setmCountry(accountDataSetAddress.getmCountry());
                mPaymentDataSet.setmEmailId(accountDataSetAddress.getmEmailId());
                mPaymentDataSet.setmCountry_id(accountDataSetAddress.getmCountry_id());
                mPaymentDataSet.setmAddress_2("0");

                if (DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).get_Size_Address() == 0) {
                    DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).insert_account_shipping_address(mShippingDataSet);
                    DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).insert_account_payment_address(mPaymentDataSet);
                } else {
                    DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).update_payment_address(mPaymentDataSet);
                    DataBaseHandlerAccountAddress.getInstance(getActivity().getApplicationContext()).update_shipping_address(mShippingDataSet);
                }
                continue_to_delivery_method();

            }
        });

    }
}