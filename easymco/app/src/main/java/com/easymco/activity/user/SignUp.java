package com.easymco.activity.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.NoInternetConnection;
import com.easymco.adapter.SpinnerAdapter;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.interfaces.API_Result;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.Response;
import com.easymco.models.SpinnerDataSet;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity implements API_Result {
    public static Integer iStateId = -1, iCountryId = -1;
    public String mEmailValidCheck;
    public ArrayList<SpinnerDataSet> countryArrayList = new ArrayList<>();
    TextView go_login_page;
    Button submit;
    Toolbar toolbar;
    EditText get_first_name, get_last_name, get_email, get_telephone, get_fax;
    EditText get_company, get_address1, get_address2, get_city, get_postcode;
    EditText get_pwd, get_confirm_pwd;
    RadioGroup get_radio_btn_grp_status;
    RadioButton get_radio_btn_yes, get_radio_btn_no;
    CheckBox get_checkbox_status;
    String radio_status;
    int one = 1, zero = 0;
    StringBuffer temp_checkbox_result, temp_checkbox_string;
    ProgressBar progressBar;
    TextView error_f_name, error_l_name, error_email1, error_email2, error_telephone;
    TextView error_address1, error_city, error_postcode, error_country, error_region_state;
    TextView error_pwd, error_confirm_pwd1, error_confirm_pwd2;
    TextView error_privacy_policy;
    Spinner getSpinner_country, getSpinner_state;
    API_Result api_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sign_up_activity);

        api_result = this;

        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
           progressBar.setVisibility(View.VISIBLE);
            String url[] = {URL_Class.mURL + URL_Class.mURL_Country};
            new API_Get().get_method(url, api_result, "", JSON_Names.KEY_GET_TYPE, true, getBaseContext(), "SignUpCountry");
        } else {
            no_connection_transfer();
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        get_first_name = (EditText) findViewById(R.id.et_subtitle_1_first_name);
        get_last_name = (EditText) findViewById(R.id.et_subtitle_1_last_name);
        get_email = (EditText) findViewById(R.id.et_subtitle_1_email);
        get_telephone = (EditText) findViewById(R.id.et_subtitle_1_telephone);
        get_fax = (EditText) findViewById(R.id.et_subtitle_1_fax);

        get_company = (EditText) findViewById(R.id.et_subtitle_2_company);
        get_address1 = (EditText) findViewById(R.id.et_subtitle_2_address1);
        get_address2 = (EditText) findViewById(R.id.et_subtitle_2_address2);
        get_city = (EditText) findViewById(R.id.et_subtitle_2_city);
        get_postcode = (EditText) findViewById(R.id.et_subtitle_2_postcode);

        get_pwd = (EditText) findViewById(R.id.et_subtitle_3_password);
        get_confirm_pwd = (EditText) findViewById(R.id.et_subtitle_3_confirm_password);

        get_radio_btn_grp_status = (RadioGroup) findViewById(R.id.sign_up_radio_group);
        get_radio_btn_yes = (RadioButton) findViewById(R.id.radio_btn_yes);
        get_radio_btn_no = (RadioButton) findViewById(R.id.radio_btn_no);

        temp_checkbox_string = new StringBuffer();
        temp_checkbox_string.append(false);

        get_checkbox_status = (CheckBox) findViewById(R.id.c_box_tick);

        error_f_name = (TextView) findViewById(R.id.tv_error_s1_first_name);
        error_l_name = (TextView) findViewById(R.id.tv_error_s1_last_name);
        error_email1 = (TextView) findViewById(R.id.tv_error_s1_email1);
        error_email2 = (TextView) findViewById(R.id.tv_error_s1_email2);
        error_telephone = (TextView) findViewById(R.id.tv_error_s1_telephone);
        error_address1 = (TextView) findViewById(R.id.tv_error_s2_address1);
        error_city = (TextView) findViewById(R.id.tv_error_s2_city);
        error_postcode = (TextView) findViewById(R.id.tv_error_s2_postcode);
        error_country = (TextView) findViewById(R.id.tv_error_s2_country);
        error_region_state = (TextView) findViewById(R.id.tv_error_s2_region_state);
        error_pwd = (TextView) findViewById(R.id.tv_error_s3_pwd);
        error_confirm_pwd1 = (TextView) findViewById(R.id.tv_error_s3_confirm_pwd1);
        error_confirm_pwd2 = (TextView) findViewById(R.id.tv_error_s3_confirm_pwd2);
        error_privacy_policy = (TextView) findViewById(R.id.tv_error_s4_privacy);

        error_f_name.setVisibility(View.GONE);
        error_l_name.setVisibility(View.GONE);
        error_email1.setVisibility(View.GONE);
        error_email2.setVisibility(View.GONE);
        error_telephone.setVisibility(View.GONE);
        error_address1.setVisibility(View.GONE);
        error_city.setVisibility(View.GONE);
        error_postcode.setVisibility(View.GONE);
        error_country.setVisibility(View.GONE);
        error_region_state.setVisibility(View.GONE);
        error_pwd.setVisibility(View.GONE);
        error_confirm_pwd1.setVisibility(View.GONE);
        error_confirm_pwd2.setVisibility(View.GONE);
        error_privacy_policy.setVisibility(View.GONE);

        get_first_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        get_last_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        get_address1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        get_city.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        submit = (Button) findViewById(R.id.btn_sign_up_page_submit);
        go_login_page = (TextView) findViewById(R.id.tv_return_to_login_pg);
        go_login_page.setPaintFlags(go_login_page.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        go_login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer();
            }
        });

        // add click listener to Button "POST"
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (get_radio_btn_yes.isChecked()) {
                    radio_status = Integer.toString(one);
                } else {
                    radio_status = Integer.toString(zero);
                }
                temp_checkbox_result = new StringBuffer();
                temp_checkbox_result.append(get_checkbox_status.isChecked());

                if (!get_email.getText().toString().equals("")) {

                    switch (Boolean.toString(Methods.isEmailValidator(get_email.getText().toString()))) {
                        case "true": {
                            mEmailValidCheck = "MATCH";
                            break;
                        }
                        case "false": {
                            mEmailValidCheck = "NOT_MATCH";
                            break;
                        }
                    }
                }

                if (!get_first_name.getText().toString().equals("")
                        && (get_first_name.getText().toString().length() >= 1)
                        && (get_first_name.getText().toString().length() <= 32)

                        && !get_last_name.getText().toString().equals("")
                        && (get_last_name.getText().toString().length() >= 1)
                        && (get_last_name.getText().toString().length() <= 32)

                        && !get_email.getText().toString().equals("")
                        && mEmailValidCheck.equals("MATCH")

                        && !get_telephone.getText().toString().equals("")
                        && (get_telephone.getText().toString().length() >= 3)
                        && (get_telephone.getText().toString().length() <= 32)

                        && !get_address1.getText().toString().equals("")
                        && (get_address1.getText().toString().length() >= 3)
                        && (get_address1.getText().toString().length() <= 128)

                        && !get_city.getText().toString().equals("")
                        && (get_city.getText().toString().length() >= 2)
                        && (get_city.getText().toString().length() <= 128)

                        && !get_postcode.getText().toString().equals("")
                        && (get_postcode.getText().toString().length() >= 2)
                        && (get_postcode.getText().toString().length() <= 10)

                        && !get_pwd.getText().toString().equals("")
                        && !get_confirm_pwd.getText().toString().equals("")
                        && get_pwd.getText().toString().equals(get_confirm_pwd.getText().toString())
                        && (get_pwd.getText().toString().length() >= 5) && (get_pwd.getText().toString().length() <= 20)
                        && (get_confirm_pwd.getText().toString().length() >= 5) && (get_confirm_pwd.getText().toString().length() <= 20)


                        && (!temp_checkbox_result.toString().equals(temp_checkbox_string.toString()))

                        && iCountryId != -1
                        && iStateId != -1
                        ) {
                    if (NetworkConnection.connectionChecking(getApplicationContext())) {
                        if (getPostDataString() != null) {
                            Methods.hideKeyboard(SignUp.this);
                            progressBar.setVisibility(View.VISIBLE);
                            String url[] = {URL_Class.mURL + URL_Class.mURL_Registration};
                            new API_Get().get_method(url, api_result, getPostDataString(), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "SignUpPost");
                        }
                    } else {
                        no_connection_transfer();
                    }

                } else {
                    Methods.toast(getResources().getString(R.string.fill_required_field));
                }
                if (get_first_name.getText().toString().equals("")) {
                    error_f_name.setVisibility(View.VISIBLE);
                } else {
                    error_f_name.setVisibility(View.GONE);
                }
                if ((get_first_name.getText().toString().length() >= 1) && (get_first_name.getText().toString().length() <= 32)) {
                    error_f_name.setVisibility(View.GONE);
                } else {
                    error_f_name.setVisibility(View.VISIBLE);
                }
                if (get_last_name.getText().toString().equals("")) {
                    error_l_name.setVisibility(View.VISIBLE);
                } else {
                    error_l_name.setVisibility(View.GONE);
                }
                if ((get_last_name.getText().toString().length() >= 1) && (get_last_name.getText().toString().length() <= 32)) {
                    error_l_name.setVisibility(View.GONE);
                } else {
                    error_l_name.setVisibility(View.VISIBLE);
                }
                if (get_email.getText().toString().equals("")) {
                    error_email1.setVisibility(View.VISIBLE);
                } else {
                    error_email1.setVisibility(View.GONE);
                }
                if (!get_email.getText().toString().equals("") && mEmailValidCheck.equals("NOT_MATCH")) {
                    error_email2.setVisibility(View.VISIBLE);
                } else {
                    error_email2.setVisibility(View.GONE);
                }
                if (get_telephone.getText().toString().equals("")) {
                    error_telephone.setVisibility(View.VISIBLE);
                } else {
                    error_telephone.setVisibility(View.GONE);
                }
                if ((get_telephone.getText().toString().length() >= 3) && (get_telephone.getText().toString().length() <= 32)) {
                    error_telephone.setVisibility(View.GONE);
                } else {
                    error_telephone.setVisibility(View.VISIBLE);
                }
                if (get_address1.getText().toString().equals("")) {
                    error_address1.setVisibility(View.VISIBLE);
                } else {
                    error_address1.setVisibility(View.GONE);
                }
                if ((get_address1.getText().toString().length() >= 3) && (get_address1.getText().toString().length() <= 128)) {
                    error_address1.setVisibility(View.GONE);
                } else {
                    error_address1.setVisibility(View.VISIBLE);
                }
                if (get_city.getText().toString().equals("")) {
                    error_city.setVisibility(View.VISIBLE);
                } else {
                    error_city.setVisibility(View.GONE);
                }
                if ((get_city.getText().toString().length() >= 2) && (get_city.getText().toString().length() <= 128)) {
                    error_city.setVisibility(View.GONE);
                } else {
                    error_city.setVisibility(View.VISIBLE);
                }
                if (get_postcode.getText().toString().equals("")) {
                    error_postcode.setVisibility(View.VISIBLE);
                } else {
                    error_postcode.setVisibility(View.GONE);
                }
                if ((get_postcode.getText().toString().length() >= 2) && (get_postcode.getText().toString().length() <= 10)) {
                    error_postcode.setVisibility(View.GONE);
                } else {
                    error_postcode.setVisibility(View.VISIBLE);
                }
                if (get_pwd.getText().toString().equals("")) {
                    error_pwd.setVisibility(View.VISIBLE);
                } else {
                    error_pwd.setVisibility(View.GONE);
                }
                if ((get_pwd.getText().toString().length() >= 5) && (get_pwd.getText().toString().length() <= 20)) {
                    error_pwd.setVisibility(View.GONE);
                } else {
                    error_pwd.setVisibility(View.VISIBLE);
                }
                if (get_confirm_pwd.getText().toString().equals("")) {
                    error_confirm_pwd1.setVisibility(View.VISIBLE);
                } else {
                    error_confirm_pwd1.setVisibility(View.GONE);
                }
                if ((get_confirm_pwd.getText().toString().length() >= 5) && (get_confirm_pwd.getText().toString().length() <= 20)) {
                    error_confirm_pwd1.setVisibility(View.GONE);
                } else {
                    error_confirm_pwd1.setVisibility(View.VISIBLE);
                }


                if (!get_pwd.getText().toString().equals(get_confirm_pwd.getText().toString()) && !get_confirm_pwd.getText().toString().equals("") && !get_pwd.getText().toString().equals("")) {
                    error_confirm_pwd2.setVisibility(View.VISIBLE);
                } else {
                    error_confirm_pwd2.setVisibility(View.GONE);
                }
                if (!get_pwd.getText().toString().equals(get_confirm_pwd.getText().toString()) &&
                        (get_pwd.getText().toString().length() >= 5) && (get_pwd.getText().toString().length() <= 20) &&
                        (get_confirm_pwd.getText().toString().length() >= 5) && (get_confirm_pwd.getText().toString().length() <= 20)) {
                    error_confirm_pwd2.setVisibility(View.VISIBLE);
                } else {
                    error_confirm_pwd2.setVisibility(View.GONE);
                }


                if (temp_checkbox_result.toString().equals(temp_checkbox_string.toString())) {
                    error_privacy_policy.setVisibility(View.VISIBLE);
                } else {
                    error_privacy_policy.setVisibility(View.GONE);
                }

                if (iCountryId == -1) {
                    error_country.setVisibility(View.VISIBLE);
                } else {
                    error_country.setVisibility(View.GONE);
                }

                if (iStateId == -1) {
                    error_region_state.setVisibility(View.VISIBLE);
                } else {
                    error_region_state.setVisibility(View.GONE);
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
                    "ar".equals(AppLanguageSupport.getLanguage(SignUp.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Methods.hideKeyboard(SignUp.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void transfer() {
        /*if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN) != null)
            if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_SIGN_UP)) {
                DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
                onBackPressed();
                finish();
            } else {
                DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_FROM_SIGN_UP);
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }*/

        Methods.hideKeyboard(SignUp.this);

        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        finish();


    }

    public void no_connection_transfer() {
        Intent intent = new Intent(SignUp.this, NoInternetConnection.class);
        startActivity(intent);
        finish();
    }

    private String getPostDataString() {
        try {
            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("firstname", get_first_name.getText().toString());
            postDataParams.put("lastname", get_last_name.getText().toString());
            postDataParams.put("email", get_email.getText().toString());
            postDataParams.put("phone", get_telephone.getText().toString());
            postDataParams.put("fax", get_fax.getText().toString());
            postDataParams.put("company", get_company.getText().toString());
            postDataParams.put("address", get_address1.getText().toString());
            postDataParams.put("city", get_city.getText().toString());
            postDataParams.put("pincode", get_postcode.getText().toString());
            postDataParams.put("country", String.valueOf(iCountryId));
            postDataParams.put("state", String.valueOf(iStateId));
            postDataParams.put("password", get_confirm_pwd.getText().toString());
            postDataParams.put("newsletter", radio_status);

            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : postDataParams.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append(URL_Class.mAnd_Symbol);

                result.append(URLEncoder.encode(entry.getKey(), URL_Class.mConvertType));
                result.append(URL_Class.mEqual_Symbol);
                result.append(URLEncoder.encode(entry.getValue(), URL_Class.mConvertType));
            }
            return result.toString();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void result(String[] data, String source) {
        progressBar.setVisibility(View.GONE);
        if (source.equals("SignUpCountry")) {
            if (data != null) {
                countrySetting(data[0]);
            }
        } else {
            if (data != null) {
                Response response = GetJSONData.getResponse(data[0]);
                if (response != null) {
                    if (response.getmStatus() == 200) {
                        //Methods.toast(getResources().getString(R.string.registration_successful_sign_up));

                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setMessage(R.string.registration_successful_sign_up).setCancelable(false);
                        builder.setPositiveButton(R.string._continue, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               /* if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN) != null) {
                                    if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_SIGN_UP)) {
                                        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
                                        onBackPressed();
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SignUp.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Intent intent = new Intent(SignUp.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }*/
                                Intent intent = new Intent(SignUp.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Methods.toast(response.getmMessage());
                    }
                }
            } else {
                Methods.toast(getResources().getString(R.string.reg_error));
            }
        }

    }

    private void countrySetting(String s) {

        countryArrayList = GetJSONData.getCountryList(s,SignUp.this);

        getSpinner_country = (Spinner) findViewById(R.id.country_spinner);
        getSpinner_state = (Spinner) findViewById(R.id.region_or_state_spinner);

        if (countryArrayList != null) {

            SpinnerAdapter adapter_Country = new SpinnerAdapter(SignUp.this, android.R.layout.simple_spinner_item, countryArrayList);
            adapter_Country.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            getSpinner_country.setAdapter(adapter_Country);


            getSpinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                             public void onItemSelected(AdapterView<?> parent, View view, final int positionParent, long id) {

                                                                 if (countryArrayList.get(positionParent).get_id() != -1) {
                                                                     iCountryId = countryArrayList.get(positionParent).get_id();

                                                                     iStateId = -1 ; // reset state list;

                                                                     if (countryArrayList.get(positionParent).getStateList() != null) {

                                                                         if (countryArrayList.get(positionParent).getStateList().size() > 0) {
                                                                             final ArrayList<SpinnerDataSet> stateSpinnerList = Methods.getSpinnerDataSet(countryArrayList.get(positionParent).getStateList());
                                                                             SpinnerAdapter adapter_State = new SpinnerAdapter(SignUp.this, android.R.layout.simple_spinner_item,
                                                                                     stateSpinnerList);
                                                                             adapter_State.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                             getSpinner_state.getBaseline();
                                                                             getSpinner_state.setAdapter(adapter_State);

                                                                             getSpinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                                                 public void onItemSelected(AdapterView<?> parent, View view, int positionChild, long id) {
                                                                                     if (stateSpinnerList.get(positionChild).get_id() != -1) {
                                                                                         iStateId = stateSpinnerList.get(positionChild).get_id();
                                                                                     }
                                                                                 }

                                                                                 @Override
                                                                                 public void onNothingSelected(AdapterView<?> arg0) {
                                                                                 }

                                                                             });
                                                                         } else {
                                                                             List<String> noStates = new ArrayList<>();
                                                                             noStates.add(getResources().getString(R.string.none));

                                                                             ArrayAdapter<String> adapter_StateN = new ArrayAdapter<>(SignUp.this, R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                                                             getSpinner_state.setAdapter(adapter_StateN);
                                                                             iStateId = 0;
                                                                         }
                                                                     } else {
                                                                         List<String> noStates = new ArrayList<>();
                                                                         noStates.add(getResources().getString(R.string.none));

                                                                         ArrayAdapter<String> adapter_StateN = new ArrayAdapter<>(SignUp.this, R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                                                         getSpinner_state.setAdapter(adapter_StateN);
                                                                         iStateId = 0;
                                                                     }
                                                                 } else {

                                                                     iCountryId = -1 ; // reset country list;
                                                                     iStateId = -1 ; // reset state list;

                                                                     List<String> noStates = new ArrayList<>();
                                                                     noStates.add(getResources().getString(R.string.please_select_country_first));

                                                                     ArrayAdapter<String> adapter_StateN = new ArrayAdapter<>(SignUp.this, R.layout.spinner_item_nodata, R.id.spinner_state_no_data, noStates);
                                                                     getSpinner_state.setAdapter(adapter_StateN);
                                                                 }
                                                             }

                                                             @Override
                                                             public void onNothingSelected(AdapterView<?> arg0) {
                                                             }

                                                         }

            );
        }

    }
}