package com.easymco.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.Cart;
import com.easymco.activity.NoInternetConnection;
import com.easymco.activity.SplashScreen;
import com.easymco.activity.Wish_List;
import com.easymco.activity.user.Login;
import com.easymco.activity.user.SignUp;
import com.easymco.adapter.SpinnerAdapter;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerCart;
import com.easymco.db_handler.DataBaseHandlerCartOptions;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.interfaces.API_Result;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.AccountDataSet;
import com.easymco.models.Response;
import com.easymco.models.SpinnerCountryList;
import com.easymco.models.SpinnerDataSet;
import com.easymco.network_checker.NetworkConnection;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddsBook_EditAdds extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, API_Result {

    public Integer iStateId = -1, iCountryId = -1, CountryId_cc, ZoneId_ss;
    public ArrayList<SpinnerDataSet> countryArrayList = new ArrayList<>();
    public String mAddress_id, mCustomer_id, CountryName_cc, ZoneNameSS;
    public Bundle extras;
    Toolbar toolbar;
    ProgressBar progressBar;
    Button btn_edit_pro_back, btn_edit_pro_continue;
    EditText edt_first_name, edt_last_name, edt_company, edt_adds, edt_city, edt_postcode;
    TextView error_edit_pro_first_name, error_edit_pro_last_name, error_edit_pro_adds, error_edit_pro_city, error_edit_pro_postcode,
            error_edit_pro_country, error_edit_pro_state;
    Spinner spinner_edit_pro_country, Spinner_edit_pro_state;
    Boolean default_id = false;
    AccountDataSet accountDataSet;
    API_Result api_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_adds_book_edit_adds);

        api_result = this;

        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);
            String url[] = {URL_Class.mURL + URL_Class.mURL_Country +"&"+Methods.current_language()};
            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "AddressBookCountry");
        } else {
            no_connection_transfer();
        }

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountDataSet = new AccountDataSet();

        edt_first_name = (EditText) findViewById(R.id.et_edit_pro_first_name);
        edt_last_name = (EditText) findViewById(R.id.et_edit_pro_last_name);
        edt_company = (EditText) findViewById(R.id.et_edit_pro_company);
        edt_adds = (EditText) findViewById(R.id.et_edit_pro_address);
        edt_city = (EditText) findViewById(R.id.et_edit_pro_city);
        edt_postcode = (EditText) findViewById(R.id.et_edit_pro_postcode);

        error_edit_pro_first_name = (TextView) findViewById(R.id.tv_error_edit_pro_first_name);
        error_edit_pro_last_name = (TextView) findViewById(R.id.tv_error_edit_pro_last_name);
        error_edit_pro_adds = (TextView) findViewById(R.id.tv_error_edit_pro_address);
        error_edit_pro_city = (TextView) findViewById(R.id.tv_error_edit_pro_city);
        error_edit_pro_postcode = (TextView) findViewById(R.id.tv_error_edit_pro_postcode);
        error_edit_pro_country = (TextView) findViewById(R.id.tv_error_edit_pro_country);
        error_edit_pro_state = (TextView) findViewById(R.id.tv_error_edit_pro_region_state);

        btn_edit_pro_back = (Button) findViewById(R.id.btn_edit_pro_back);
        btn_edit_pro_continue = (Button) findViewById(R.id.btn_edit_pro_continue);

        edt_first_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        edt_last_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        edt_adds.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        edt_city.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        spinner_edit_pro_country = (Spinner) findViewById(R.id.spinner_edit_pro_country);
        Spinner_edit_pro_state = (Spinner) findViewById(R.id.spinner_edit_pro_region_or_state);

        extras = getIntent().getExtras();
        if (extras != null) {
            mAddress_id = extras.getString(JSON_Names.KEY_ADDRESS_ID);
            mCustomer_id = extras.getString(JSON_Names.KEY_CUSTOMER_ID);
            edt_first_name.setText(extras.getString(JSON_Names.KEY_FIRST_NAME));
            edt_last_name.setText(extras.getString(JSON_Names.KEY_LAST_NAME));
            edt_company.setText(extras.getString(JSON_Names.KEY_COMPANY));
            edt_adds.setText(extras.getString(JSON_Names.KEY_ADDRESS_1));
            edt_city.setText(extras.getString(JSON_Names.KEY_CITY));
            edt_postcode.setText(extras.getString(JSON_Names.KEY_POSTCODE));
            CountryName_cc = extras.getString(JSON_Names.KEY_COUNTRY);
            ZoneNameSS = extras.getString(JSON_Names.KEY_STATE);
            CountryId_cc = extras.getInt(JSON_Names.KEY_COUNTRY_ID);
            ZoneId_ss = extras.getInt(JSON_Names.KEY_ZONE_ID);
            default_id = extras.getBoolean(JSON_Names.KEY_DEFAULT_ADDRESS_ID);
        }
        btn_edit_pro_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddressList();
            }
        });

        btn_edit_pro_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(AddsBook_EditAdds.this);

                if (!edt_first_name.getText().toString().equals("")
                        && (edt_first_name.getText().toString().length() >= 1)
                        && (edt_first_name.getText().toString().length() <= 32)

                        && !edt_last_name.getText().toString().equals("")
                        && (edt_last_name.getText().toString().length() >= 1)
                        && (edt_last_name.getText().toString().length() <= 32)

                        && !edt_adds.getText().toString().equals("")
                        && (edt_adds.getText().toString().length() >= 3)
                        && (edt_adds.getText().toString().length() <= 128)

                        && !edt_city.getText().toString().equals("")
                        && (edt_city.getText().toString().length() >= 2)
                        && (edt_city.getText().toString().length() <= 128)

                        && iCountryId != -1
                        && iStateId != -1) {

                    if (NetworkConnection.connectionChecking(getApplicationContext())) {
                        if (extras != null) {
                            if (writeOut() != null) {
                                progressBar.setVisibility(View.VISIBLE);
                                String url[] = {URL_Class.mURL + URL_Class.mURL_Edit_Address};
                                new API_Get().get_method(url, api_result, writeOut(),
                                        JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "AddressBookPostEdit");
                            }
                            accountDataSet.setmFirstName(edt_first_name.getText().toString());
                            accountDataSet.setmLastName(edt_last_name.getText().toString());
                        } else {
                            if (writeOut() != null) {
                                progressBar.setVisibility(View.VISIBLE);
                                String url[] = {URL_Class.mURL + URL_Class.mURL_New_Address};
                                new API_Get().get_method(url, api_result, writeOut(), JSON_Names.KEY_POST_TYPE,
                                        true, getBaseContext(), "AddressBookPostNew");
                            }
                        }
                    } else {
                        no_connection_transfer();
                    }


                }
                if (edt_first_name.getText().toString().equals("")) {
                    error_edit_pro_first_name.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_first_name.setVisibility(View.GONE);
                }
                if ((edt_first_name.getText().toString().length() >= 1) && (edt_first_name.getText().toString().length() <= 32)) {
                    error_edit_pro_first_name.setVisibility(View.GONE);
                } else {
                    error_edit_pro_first_name.setVisibility(View.VISIBLE);
                }
                if (edt_last_name.getText().toString().equals("")) {
                    error_edit_pro_last_name.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_last_name.setVisibility(View.GONE);
                }
                if ((edt_last_name.getText().toString().length() >= 1) && (edt_last_name.getText().toString().length() <= 32)) {
                    error_edit_pro_last_name.setVisibility(View.GONE);
                } else {
                    error_edit_pro_last_name.setVisibility(View.VISIBLE);
                }


                if (edt_adds.getText().toString().equals("")) {
                    error_edit_pro_adds.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_adds.setVisibility(View.GONE);
                }
                if ((edt_adds.getText().toString().length() >= 3) && (edt_adds.getText().toString().length() <= 128)) {
                    error_edit_pro_adds.setVisibility(View.GONE);
                } else {
                    error_edit_pro_adds.setVisibility(View.VISIBLE);
                }
                if (edt_city.getText().toString().equals("")) {
                    error_edit_pro_city.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_city.setVisibility(View.GONE);
                }
                if ((edt_city.getText().toString().length() >= 2) && (edt_city.getText().toString().length() <= 128)) {
                    error_edit_pro_city.setVisibility(View.GONE);
                } else {
                    error_edit_pro_city.setVisibility(View.VISIBLE);
                }
                if (iCountryId == -1) {
                    error_edit_pro_country.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_country.setVisibility(View.GONE);
                }

                if (iStateId == -1) {
                    error_edit_pro_state.setVisibility(View.VISIBLE);
                } else {
                    error_edit_pro_state.setVisibility(View.GONE);
                }
            }
        });
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
                    "ar".equals(AppLanguageSupport.getLanguage(AddsBook_EditAdds.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    private void toAddressList() {
        Methods.hideKeyboard(AddsBook_EditAdds.this);
        startActivity(new Intent(AddsBook_EditAdds.this, AddressBook.class));
        finish();
    }

    public void no_connection_transfer() {
        Intent intent = new Intent(AddsBook_EditAdds.this, NoInternetConnection.class);
        startActivity(intent);
        finish();
    }

    public void success() {
        toAddressList();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cart_count_value) {
            Intent intent = new Intent(AddsBook_EditAdds.this, Cart.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.cart_image_view) {
            Intent intent = new Intent(AddsBook_EditAdds.this, Cart.class);
            startActivity(intent);
        }
        return false;
    }

    public String writeOut() {
        try {
            HashMap<String, String> params = new HashMap<>();

            if (extras != null) {
                params.put(JSON_Names.KEY_ADDRESS_ID, mAddress_id);
                params.put(JSON_Names.KEY_CUSTOMER_ID, mCustomer_id);
            } else {
                params.put(JSON_Names.KEY_CUSTOMER_ID,
                        String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id()));
            }
            params.put(JSON_Names.KEY_FIRST_NAME, edt_first_name.getText().toString());
            params.put(JSON_Names.KEY_LAST_NAME, edt_last_name.getText().toString());
            params.put(JSON_Names.KEY_COMPANY, edt_company.getText().toString());
            params.put(JSON_Names.KEY_ADDRESS_1, edt_adds.getText().toString());
            params.put(JSON_Names.KEY_CITY, edt_city.getText().toString());
            params.put(JSON_Names.KEY_POSTCODE, edt_postcode.getText().toString());
            params.put(JSON_Names.KEY_COUNTRY, String.valueOf(iCountryId));
            params.put(JSON_Names.KEY_STATE, String.valueOf(iStateId));

            StringBuilder s = new StringBuilder();
            Boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
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

    private int getStateIndex(ArrayList<SpinnerCountryList> stateList, Integer stateId) {
        Integer index = 0;
        for (int j = 1; j < stateList.size(); j++) {
            if (stateList.get(j).get_id().equals(stateId)) {
                index = j;
                break;
            }
        }
        return index;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        update(menu);

        if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
            menu.findItem(R.id.user_name).setVisible(false);
            menu.findItem(R.id.my_order).setVisible(false);
            menu.findItem(R.id.logout).setVisible(false);
        } else {
            String title = DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_name();
            menu.findItem(R.id.user_name).setTitle(title);
            menu.findItem(R.id.register).setVisible(false);
            menu.findItem(R.id.login).setVisible(false);
        }
        return true;
    }

    public void update(Menu menu) {
        View view = menu.findItem(R.id.cart_count).getActionView();
        ImageView t1 = view.findViewById(R.id.cart_image_view);
        TextView t2 = view.findViewById(R.id.cart_count_value);
        String tempData = DataBaseHandlerCart.getInstance(getApplicationContext()).get_whole_list_count();
        if (!tempData.equals("0")) {
            t2.setText(tempData);
            t2.setOnClickListener(this);
            t1.setOnTouchListener(this);
        } else {
            t2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Methods.hideKeyboard(AddsBook_EditAdds.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        } else if (id == R.id.user_name) {
            Intent intent = new Intent(AddsBook_EditAdds.this, MyAccountMainMenu.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            Intent intent = new Intent(AddsBook_EditAdds.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.register) {
            Intent intent = new Intent(AddsBook_EditAdds.this, SignUp.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            DataBaseHandlerAccount.getInstance(getApplicationContext()).delete_account_detail();
            DataBaseHandlerWishList.getInstance(getApplicationContext()).delete_wish_list();
            DataBaseHandlerCart.getInstance(getApplicationContext()).delete_cart();
            DataBaseHandlerCartOptions.getInstance(getApplicationContext()).delete_cart_option();
            DataBaseHandlerDiscount.getInstance().delete_coupon_code();
            DataBaseHandlerDiscount.getInstance().delete_gift_voucher();
            DataBaseHandlerDiscount.getInstance().delete_reward_points();
            Intent intent = new Intent(AddsBook_EditAdds.this, SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_wish_list) {
            if (!DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                Methods.toast(getResources().getString(R.string.must_login));
                Intent intent = new Intent(AddsBook_EditAdds.this, Login.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AddsBook_EditAdds.this, Wish_List.class);
                startActivity(intent);
            }
        } else if (id == R.id.my_order) {
            Intent intent = new Intent(AddsBook_EditAdds.this, OrderHistory.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.language) {
            Methods.country_language(getFragmentManager(), "AddsBookEditAdds", "Language", "0");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (source.equals("AddressBookCountry")) {
            if (data != null) {

                countryArrayList = GetJSONData.getCountryList(data[0],AddsBook_EditAdds.this);

                if (countryArrayList != null) {
                    spinner_edit_pro_country = (Spinner) findViewById(R.id.spinner_edit_pro_country);
                    Spinner_edit_pro_state = (Spinner) findViewById(R.id.spinner_edit_pro_region_or_state);

                    SpinnerAdapter adapter_Country = new SpinnerAdapter(AddsBook_EditAdds.this, android.R.layout.simple_spinner_item, countryArrayList);
                    adapter_Country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_edit_pro_country.setAdapter(adapter_Country);

                    if (CountryId_cc != null) {
                        spinner_edit_pro_country.setSelection(getCountryIndex(countryArrayList, CountryId_cc));
                    }

                    spinner_edit_pro_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                           public void onItemSelected(AdapterView<?> parent, View view, final int positionParent, long id) {

                                                                               if (countryArrayList.get(positionParent).get_id() != -1) {

                                                                                   iCountryId = countryArrayList.get(positionParent).get_id();

                                                                                   iStateId = -1; // reset state list;

                                                                                   if (countryArrayList.get(positionParent).getStateList() != null) {
                                                                                       if (countryArrayList.get(positionParent).getStateList().size() > 0) {
                                                                                           SpinnerAdapter adapter_State = new SpinnerAdapter(AddsBook_EditAdds.this, android.R.layout.simple_spinner_item,
                                                                                                   Methods.getSpinnerDataSet(countryArrayList.get(positionParent).getStateList()));
                                                                                           adapter_State.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                           Spinner_edit_pro_state.getBaseline();
                                                                                           Spinner_edit_pro_state.setAdapter(adapter_State);

                                                                                           if (ZoneId_ss != null) {
                                                                                               Spinner_edit_pro_state.setSelection(getStateIndex(countryArrayList.get(positionParent).getStateList(), ZoneId_ss));
                                                                                           }
                                                                                           Spinner_edit_pro_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                                                               public void onItemSelected(AdapterView<?> parent, View view, int positionChild, long id) {
                                                                                                   if (countryArrayList.get(positionParent).getStateList().get(positionChild).get_id() != -1) {
                                                                                                       iStateId = countryArrayList.get(positionParent).getStateList().get(positionChild).get_id();
                                                                                                   }
                                                                                               }

                                                                                               @Override
                                                                                               public void onNothingSelected(AdapterView<?> arg0) {
                                                                                               }

                                                                                           });
                                                                                       } else {
                                                                                           List<String> noStates = new ArrayList<>();
                                                                                           noStates.add(getResources().getString(R.string.none));

                                                                                           ArrayAdapter<String> adapter_StateN = new ArrayAdapter<>(AddsBook_EditAdds.this, R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                                                                           Spinner_edit_pro_state.setAdapter(adapter_StateN);
                                                                                           iStateId = 0;
                                                                                       }
                                                                                   } else {
                                                                                       List<String> noStates = new ArrayList<>();
                                                                                       noStates.add(getResources().getString(R.string.none));

                                                                                       ArrayAdapter<String> adapter_StateN = new ArrayAdapter<>(AddsBook_EditAdds.this, R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                                                                       Spinner_edit_pro_state.setAdapter(adapter_StateN);
                                                                                       iStateId = 0;
                                                                                   }



                                                                               } else {

                                                                                   iCountryId = -1; // reset state list;
                                                                                   iStateId = -1; // reset state list;

                                                                                   List<String> noStates = new ArrayList<>();
                                                                                   noStates.add(getResources().getString(R.string.please_select_country_first));

                                                                                   ArrayAdapter<String> adapter_StateN = new ArrayAdapter<>(AddsBook_EditAdds.this, R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                                                                   Spinner_edit_pro_state.setAdapter(adapter_StateN);
                                                                               }
                                                                           }

                                                                           @Override
                                                                           public void onNothingSelected(AdapterView<?> arg0) {
                                                                           }
                                                                       }
                    );
                }
            }
        } else if (source.equals("AddressBookPostNew") || source.equals("AddressBookPostEdit")) {
            if (data != null) {
                if (data[0] != null) {
                    Response response = GetJSONData.getResponse(data[0]);
                    if (response != null) {
                        if (response.getmStatus() == 200) {
                            Methods.toast(getResources().getString(R.string.address_update));
                            if (default_id) {
                                DataBaseHandlerAccount.getInstance(getApplicationContext()).update_account_detail_name(accountDataSet);
                            }
                            success();
                        } else {
                            Methods.toast(response.getmMessage());
                        }
                    }
                } else {
                    Methods.toast(getResources().getString(R.string.error));
                }
            }
        }
    }

}