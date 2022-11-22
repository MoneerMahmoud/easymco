package com.easymco.fragments.check_out;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.NoInternetConnection;
import com.easymco.adapter.SpinnerAdapter;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.interfaces.CheckOutAPIRequest;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.AccountDataSet;
import com.easymco.models.SpinnerCountryList;
import com.easymco.models.SpinnerDataSet;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOut_Address_Edit extends Fragment {
    EditText mChangeAddressFirstName, mChangeAddressLastName, mChangeAddressCompany,
            mChangeAddressAddress, mChangeAddressCity, mChangeAddressPostCode;
    Spinner mChangeAddressCountry, mChangeAddressState;
    Button mChangeAddressBack, mChangeAddressContinue;
    View view;
    HashMap<String, String> edit_detail = new HashMap<>();
    int mChangeAddress_Country_ID = -1, mChangeAddress_State_ID = -1;
    AccountDataSet accountDataSet;
    TextView error_edit_pro_first_name, error_edit_pro_last_name,
            error_edit_pro_adds, error_edit_pro_city, error_edit_pro_postcode,
            error_edit_pro_country, error_edit_pro_state;
    String result, address_list = null;
    CheckOutAPIRequest checkOutAPIRequest;
    AccountDataSet accountDataSetAddress;

    public CheckOut_Address_Edit() {
    }

    public static CheckOut_Address_Edit getInstance(String data, String list) {
        CheckOut_Address_Edit checkOut_address_edit = new CheckOut_Address_Edit();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        bundle.putString("AddressList", list);
        checkOut_address_edit.setArguments(bundle);
        return checkOut_address_edit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            result = getArguments().getString("Data");
            address_list = getArguments().getString("AddressList");
        }
        checkOutAPIRequest = (CheckOutAPIRequest) getActivity();
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
        view = LayoutInflater.from(getActivity()).inflate(R.layout.checkout_address_edit, container, false);
        action();
        setting(result);
        return view;
    }

    public AccountDataSet getDefaultAddress(ArrayList<AccountDataSet> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getmAddress_Id().equals(list.get(i).getmDefaultAddressId())) {
                    DataStorage.mStoreSharedPreferenceString(getActivity().getApplicationContext(),
                            JSON_Names.KEY_DEFAULT_ADDRESS_ID, list.get(i).getmDefaultAddressId());
                    return list.get(i);
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public void action() {
        accountDataSet = new AccountDataSet();
        mChangeAddressFirstName = view.findViewById(R.id.et_edit_pro_first_name);
        mChangeAddressLastName = view.findViewById(R.id.et_edit_pro_last_name);
        mChangeAddressCompany = view.findViewById(R.id.et_edit_pro_company);
        mChangeAddressAddress = view.findViewById(R.id.et_edit_pro_address);
        mChangeAddressCity = view.findViewById(R.id.et_edit_pro_city);
        mChangeAddressPostCode = view.findViewById(R.id.et_edit_pro_postcode);
        mChangeAddressCountry = view.findViewById(R.id.spinner_edit_pro_country);
        mChangeAddressState = view.findViewById(R.id.spinner_edit_pro_region_or_state);
        mChangeAddressBack = view.findViewById(R.id.btn_edit_pro_back);
        mChangeAddressContinue = view.findViewById(R.id.btn_edit_pro_continue);

        error_edit_pro_first_name = view.findViewById(R.id.tv_error_edit_pro_first_name);
        error_edit_pro_last_name = view.findViewById(R.id.tv_error_edit_pro_last_name);
        error_edit_pro_adds = view.findViewById(R.id.tv_error_edit_pro_address);
        error_edit_pro_city = view.findViewById(R.id.tv_error_edit_pro_city);
        error_edit_pro_postcode = view.findViewById(R.id.tv_error_edit_pro_postcode);
        error_edit_pro_country = view.findViewById(R.id.tv_error_edit_pro_country);
        error_edit_pro_state = view.findViewById(R.id.tv_error_edit_pro_region_state);

        mChangeAddressFirstName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mChangeAddressLastName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mChangeAddressAddress.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mChangeAddressCity.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        if (address_list != null) {
            ArrayList<AccountDataSet> accountDataSetAddressList;

            accountDataSetAddressList = GetJSONData.getCustomerAddress(address_list);
            accountDataSetAddress = getDefaultAddress(accountDataSetAddressList);

            if (accountDataSetAddress != null) {
                if (accountDataSetAddress.getmFirstName() != null)
                    mChangeAddressFirstName.setText(accountDataSetAddress.getmFirstName());
                if (accountDataSetAddress.getmLastName() != null)
                    mChangeAddressLastName.setText(accountDataSetAddress.getmLastName());
                if (accountDataSetAddress.getmCompany() != null)
                    mChangeAddressCompany.setText(accountDataSetAddress.getmCompany());
                if (accountDataSetAddress.getmAddress_1() != null)
                    mChangeAddressAddress.setText(accountDataSetAddress.getmAddress_1());
                if (accountDataSetAddress.getmCity() != null)
                    mChangeAddressCity.setText(accountDataSetAddress.getmCity());
                if (accountDataSetAddress.getmPostcode() != null)
                    mChangeAddressPostCode.setText(accountDataSetAddress.getmPostcode());
            }
        }
    }

    public void setting(String country_list) {

        final ArrayList<SpinnerDataSet> country_data_set = GetJSONData.getCountryList(country_list,getActivity());
        if (country_data_set != null) {

            SpinnerAdapter adapter_country = new SpinnerAdapter(getActivity(),
                    android.R.layout.simple_spinner_item, country_data_set);
            adapter_country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mChangeAddressCountry.setAdapter(adapter_country);

            if (accountDataSetAddress != null) {
                if (accountDataSetAddress.getmCountry_id() != null) {
                    mChangeAddressCountry.setSelection(getCountryIndex(country_data_set, Integer.valueOf(accountDataSetAddress.getmCountry_id())));
                }
            }

            mChangeAddressCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    mChangeAddress_Country_ID = country_data_set.get(position).get_id();

                    mChangeAddress_State_ID = -1; // reset state list;

                    if (mChangeAddress_Country_ID != -1) {

                        if (country_data_set.get(position).getStateList() != null) {



                            ArrayList<SpinnerDataSet> mStateList = Methods.getSpinnerDataSet(country_data_set.get(position).getStateList());

                            if(mStateList != null && mStateList.size() > 0){

                                SpinnerAdapter adapter_state = new SpinnerAdapter(getActivity(),
                                        android.R.layout.simple_spinner_item, Methods.getSpinnerDataSet(country_data_set.get(position).getStateList()));

                                adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mChangeAddressState.setAdapter(adapter_state);

                                if (accountDataSetAddress != null) {
                                    if (accountDataSetAddress.getmZone_id() != null) {
                                        mChangeAddressState.setSelection(getStateIndex(country_data_set.get(position).getStateList(),
                                                Integer.valueOf(accountDataSetAddress.getmZone_id())));
                                    }
                                }

                                mChangeAddressState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int positionChild, long id) {
                                        mChangeAddress_State_ID = country_data_set.get(position).getStateList().get(positionChild).get_id();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });

                            }else {
                                List<String> noStates = new ArrayList<>();
                                noStates.add(getResources().getString(R.string.none));
                                ArrayAdapter<String> adapter_State = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                mChangeAddressState.setAdapter(adapter_State);
                                mChangeAddressState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        mChangeAddress_State_ID = 0;
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            }





                        } else {
                            List<String> noStates = new ArrayList<>();
                            noStates.add(getResources().getString(R.string.none));
                            ArrayAdapter<String> adapter_State = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                            mChangeAddressState.setAdapter(adapter_State);
                            mChangeAddressState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    mChangeAddress_State_ID = 0;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }else{

                        mChangeAddress_Country_ID = -1; // reset country list;
                        mChangeAddress_State_ID = -1; // reset state list;

                        List<String> noStates = new ArrayList<>();
                        noStates.add(getResources().getString(R.string.please_select_country_first));
                        ArrayAdapter<String> adapter_State = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                        mChangeAddressState.setAdapter(adapter_State);


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mChangeAddressBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Methods.hideKeyboard(getActivity());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            mChangeAddressContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Methods.hideKeyboard(getActivity());
                    if (mChangeAddressFirstName.getText().toString().length() != 0) {
                        if (mChangeAddressFirstName.getText().toString().length() > 0 && mChangeAddressFirstName.getText().toString().length() <= 32) {
                            if (mChangeAddressLastName.getText().toString().length() != 0) {
                                if (mChangeAddressLastName.getText().toString().length() > 0 && mChangeAddressLastName.getText().toString().length() <= 32) {
                                    if (mChangeAddressAddress.getText().toString().length() != 0) {
                                        if (mChangeAddressAddress.getText().toString().length() >= 3 && mChangeAddressAddress.getText().toString().length() <= 128) {
                                            if (mChangeAddressCity.getText().toString().length() != 0) {
                                                if (mChangeAddress_Country_ID != -1) {
                                                    if (mChangeAddress_State_ID != -1) {
                                                        String c_id = String.valueOf(DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).get_customer_id());
                                                        String a_id = DataStorage.mRetrieveSharedPreferenceString(getActivity().getApplicationContext(), JSON_Names.KEY_DEFAULT_ADDRESS_ID);
                                                        if (a_id != null) {
                                                            edit_detail.put(JSON_Names.KEY_ADDRESS_ID, a_id);
                                                        } else {
                                                            edit_detail.put(JSON_Names.KEY_ADDRESS_ID, "0");
                                                        }
                                                        edit_detail.put(JSON_Names.KEY_CUSTOMER_ID, c_id);
                                                        edit_detail.put(JSON_Names.KEY_FIRST_NAME, mChangeAddressFirstName.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_LAST_NAME, mChangeAddressLastName.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_COMPANY, mChangeAddressCompany.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_ADDRESS_1, mChangeAddressAddress.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_CITY, mChangeAddressCity.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_POSTCODE, mChangeAddressPostCode.getText().toString());
                                                        edit_detail.put(JSON_Names.KEY_COUNTRY, String.valueOf(mChangeAddress_Country_ID));
                                                        edit_detail.put(JSON_Names.KEY_STATE, String.valueOf(mChangeAddress_State_ID));
                                                        if (NetworkConnection.connectionChecking(getActivity().getApplicationContext())) {
                                                            accountDataSet.setmFirstName(mChangeAddressFirstName.getText().toString());
                                                            accountDataSet.setmLastName(mChangeAddressLastName.getText().toString());

                                                            if (writeOut() != null) {
                                                                checkOutAPIRequest.checkout_edit_address_post(writeOut());
                                                            }

                                                        } else {
                                                            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                                                            startActivity(intent);
                                                            getActivity().finish();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (mChangeAddressFirstName.getText().toString().equals("")) {
                        error_edit_pro_first_name.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_first_name.setVisibility(View.GONE);
                    }
                    if ((mChangeAddressFirstName.getText().toString().length() >= 1) &&
                            (mChangeAddressFirstName.getText().toString().length() <= 32)) {
                        error_edit_pro_first_name.setVisibility(View.GONE);
                    } else {
                        error_edit_pro_first_name.setVisibility(View.VISIBLE);
                    }
                    if (mChangeAddressLastName.getText().toString().equals("")) {
                        error_edit_pro_last_name.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_last_name.setVisibility(View.GONE);
                    }
                    if ((mChangeAddressLastName.getText().toString().length() >= 1) &&
                            (mChangeAddressLastName.getText().toString().length() <= 32)) {
                        error_edit_pro_last_name.setVisibility(View.GONE);
                    } else {
                        error_edit_pro_last_name.setVisibility(View.VISIBLE);
                    }
                    if (mChangeAddressAddress.getText().toString().equals("")) {
                        error_edit_pro_adds.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_adds.setVisibility(View.GONE);
                    }
                    if ((mChangeAddressAddress.getText().toString().length() >= 3) &&
                            (mChangeAddressAddress.getText().toString().length() <= 128)) {
                        error_edit_pro_adds.setVisibility(View.GONE);
                    } else {
                        error_edit_pro_adds.setVisibility(View.VISIBLE);
                    }
                    if (mChangeAddressCity.getText().toString().equals("")) {
                        error_edit_pro_city.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_city.setVisibility(View.GONE);
                    }
                    if ((mChangeAddressCity.getText().toString().length() >= 2) &&
                            (mChangeAddressCity.getText().toString().length() <= 128)) {
                        error_edit_pro_city.setVisibility(View.GONE);
                    } else {
                        error_edit_pro_city.setVisibility(View.VISIBLE);
                    }
                    if (mChangeAddress_Country_ID == -1) {
                        error_edit_pro_country.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_country.setVisibility(View.GONE);
                    }

                    if (mChangeAddress_State_ID == -1) {
                        error_edit_pro_state.setVisibility(View.VISIBLE);
                    } else {
                        error_edit_pro_state.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public String writeOut() {
        try {
            StringBuilder s = new StringBuilder();
            Boolean first = true;
            for (Map.Entry<String, String> entry : edit_detail.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    s.append(URL_Class.mAnd_Symbol);
                }
                s.append(URLEncoder.encode(entry.getKey(), URL_Class.mConvertType));
                s.append(URL_Class.mEqual_Symbol);
                s.append(URLEncoder.encode(entry.getValue(), URL_Class.mConvertType));
            }

            return s.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private int getCountryIndex(ArrayList<SpinnerDataSet> countryAL, Integer countryid) {
        Integer index = 0;
        for (int i = 1; i < countryAL.size(); i++) {
            SpinnerDataSet Ccc = countryAL.get(i);
            if (Ccc.get_id().equals(countryid)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int getStateIndex(ArrayList<SpinnerCountryList> stateAL, Integer stateid) {
        Integer index = 0;
        ArrayList<SpinnerCountryList> ssState = new ArrayList<>();

        for (int j = 1; j < stateAL.size(); j++) {
            SpinnerCountryList Sss = stateAL.get(j);
            if (Sss.get_id().equals(stateid)) {
                index = j;
                break;
            }
        }
        return index;
    }
}
