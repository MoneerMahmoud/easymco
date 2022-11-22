package com.easymco.activity.user;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.NoInternetConnection;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.db_handler.DataBaseHandlerWishList;
import com.easymco.interfaces.API_Result;
import com.easymco.interfaces.EnquiryPost;
import com.easymco.json_mechanism.GetJSONData;
import com.easymco.mechanism.Methods;
import com.easymco.models.AccountDataSet;
import com.easymco.models.Response;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements API_Result, EnquiryPost {

    Toolbar toolbar;
    Button login;
    TextView forget_pwd, sign_up;
    EditText get_email_id, get_pwd;
    CheckBox chk_box_show_pwd;
    API_Result api_result;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_activity);

        api_result = this;
        progressBar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        progressBar.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(JSON_Names.KEY_TRUE_BOOLEAN);

        get_email_id = (EditText) findViewById(R.id.et_login_enter_email_id);
        get_pwd = (EditText) findViewById(R.id.et_login_enter_password);

        login = (Button) findViewById(R.id.btn_login_pg_login);
        forget_pwd = (TextView) findViewById(R.id.tv_login_pg_forget_pwd);
        sign_up = (TextView) findViewById(R.id.tv_login_pg_sign_up);
        chk_box_show_pwd = (CheckBox) findViewById(R.id.c_box_tick_login);

        chk_box_show_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    get_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    get_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        forget_pwd.setPaintFlags(forget_pwd.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sign_up.setPaintFlags(sign_up.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    if (!get_email_id.getText().toString().equals("") && !get_pwd.getText().toString().equals("")) {

                        switch (Boolean.toString(Methods.isEmailValidator(get_email_id.getText().toString()))) {
                            case "true": {
                                Methods.hideKeyboard(Login.this);
                                progressBar.setVisibility(View.VISIBLE);
                                HashMap<String, String> postDataParams = new HashMap<>();
                                postDataParams.put(URL_Class.mLogin_UserName, get_email_id.getText().toString());
                                postDataParams.put(URL_Class.mLogin_Password, get_pwd.getText().toString());
                                if (getPostDataString(postDataParams) != null) {
                                    String[] data = {URL_Class.mURL + URL_Class.mURL_Login};
                                    new API_Get().get_method(data, api_result, getPostDataString(postDataParams), JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "LoginPost");
                                }
                                break;
                            }
                            case "false": {
                                Methods.toast(getResources().getString(R.string.please_enter_valid_email));
                                break;
                            }
                        }

                    } else if (get_email_id.getText().toString().equals("") && get_pwd.getText().toString().equals("")) {

                        Methods.toast(getResources().getString(R.string.enter_email_id_and_pwd));

                    } else if (!get_email_id.getText().toString().equals("") && get_pwd.getText().toString().equals("")) {

                        Methods.toast(getResources().getString(R.string.please_enter_your_pwd));

                    } else if (get_email_id.getText().toString().equals("") && !get_pwd.getText().toString().equals("")) {

                        Methods.toast(getResources().getString(R.string.please_enter_your_email_id));
                    }
                } else {
                    NoConnectionIntentTransfer();
                }

            }
        });
        forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(Login.this);
                if (NetworkConnection.connectionChecking(getApplicationContext())) {
                    forgetPassword();
                } else {
                    NoConnectionIntentTransfer();
                }

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(Login.this);
                if (NetworkConnection.connectionChecking(getApplicationContext())) {



                    /*if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN) != null) {
                        if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN)
                                .equals(JSON_Names.KEY_CURRENT_LOGIN_FROM_SIGN_UP)) {
                            DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
                            onBackPressed();
                            finish();
                        } else {
                            Intent intent_sign_up = new Intent(Login.this, SignUp.class);
                            startActivity(intent_sign_up);
                            DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_SIGN_UP);
                        }
                    } else {
                        Intent intent_sign_up = new Intent(Login.this, SignUp.class);
                        startActivity(intent_sign_up);
                        DataStorage.mStoreSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN, JSON_Names.KEY_CURRENT_LOGIN_SIGN_UP);
                    }*/


                    Intent intent_sign_up = new Intent(Login.this, SignUp.class);
                    startActivity(intent_sign_up);
                    finish();







                } else {
                    NoConnectionIntentTransfer();
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
                    "ar".equals(AppLanguageSupport.getLanguage(Login.this)) ?
                            View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    private void forgetPassword() {
        ForgetPassword navigationDrawerAboutUs = new ForgetPassword();
        navigationDrawerAboutUs.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        navigationDrawerAboutUs.setCancelable(true);
        navigationDrawerAboutUs.show(getFragmentManager(), "ForgetPassword");
    }

    public void NoConnectionIntentTransfer() {
        Intent intent = new Intent(Login.this, NoInternetConnection.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Methods.hideKeyboard(Login.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private String getPostDataString(HashMap<String, String> params) {
        try {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
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
        if (data != null) {
            if (data[0] != null) {
                switch (source) {
                    case "LoginPost": {
                        Response response = GetJSONData.getResponse(data[0]);
                        if (response != null) {
                            if (response.getmStatus() == 200) {
                                Methods.toast(getResources().getString(R.string.login_success));
                                AccountDataSet accountDataSets = GetJSONData.getLoginData(data[0]);
                                if (accountDataSets != null) {
                                    DataBaseHandlerAccount.getInstance(getApplicationContext()).insert_account_detail_new(accountDataSets);
                                }
                                String url[] = {URL_Class.mURL + URL_Class.mURL_Add_To_WishList};
                                if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN) != null)
                                    if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_PRODUCT_DETAIL)) {
                                        if (wish_list_builder() != null) {
                                            new API_Get().get_method(url, api_result, wish_list_builder(),
                                                    JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "LoginWishListPostProductList");
                                        }
                                        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
                                    } else if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN).equals(JSON_Names.KEY_CURRENT_LOGIN_CATEGORY_LISTING)) {
                                        new API_Get().get_method(url, api_result, wish_list_builder(),
                                                JSON_Names.KEY_POST_TYPE, true, getBaseContext(), "LoginWishListPostCategoryList");
                                        DataStorage.mRemoveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_LOGIN);
                                    }
                                onBackPressed();
                                finish();
                            } else {
                              //  Methods.toast(response.getmMessage());
                                Methods.toast(getResources().getString(R.string.invalid_user_name_or_pwd));
                            }
                        }
                        break;
                    }
                    case "ForgetPwd":
                        Response responseForgetPassword = GetJSONData.getResponse(data[0]);
                        if (responseForgetPassword != null) {
                            if (responseForgetPassword.getmStatus() == 200) {
                                // Methods.toast(responseForgetPassword.getmMessage());
                                Methods.toast(getResources().getString(R.string.pwd_successfully_sent_at_your_email));
                            } else {
                                //  Methods.toast(responseForgetPassword.getmMessage());
                                Methods.toast(getResources().getString(R.string.e_mail_id_not_exist));
                                forgetPassword();
                            }
                        }
                        break;
                    default: {
                        Response response = GetJSONData.getResponse(data[0]);
                        if (response != null) {
                            if (response.getmStatus() == 200) {
                                Methods.toast(getResources().getString(R.string.wish_list_success));
                            } else {
                                Methods.toast(response.getmMessage());
                            }
                        } else {
                            Methods.toast(getResources().getString(R.string.error));
                        }
                        break;
                    }
                }
            } else {
                Methods.toast(getResources().getString(R.string.error));
            }
        } else {
            Methods.toast(getResources().getString(R.string.error));
        }
    }

    public String wish_list_builder() {
        String mProduct_id;
        String mCustomer_id;
        try {
            if (DataBaseHandlerAccount.getInstance(getApplicationContext()).check_login()) {
                mProduct_id = DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_CURRENT_PRODUCT_ID);
                mCustomer_id = String.valueOf(DataBaseHandlerAccount.getInstance(getApplicationContext()).get_customer_id());
                if (mProduct_id != null) {
                    if (DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING) != null) {
                        DataBaseHandlerWishList.getInstance(getApplicationContext()).add_to_wish_list(mProduct_id,
                                DataStorage.mRetrieveSharedPreferenceString(getApplicationContext(), JSON_Names.KEY_PRODUCT_STRING));

                        return URLEncoder.encode(JSON_Names.KEY_PRODUCT_ID, URL_Class.mConvertType)
                                + URL_Class.mEqual_Symbol
                                + URLEncoder.encode(mProduct_id, URL_Class.mConvertType)
                                + URL_Class.mAnd_Symbol
                                + URLEncoder.encode(JSON_Names.KEY_USER_ID, URL_Class.mConvertType)
                                + URL_Class.mEqual_Symbol
                                + URLEncoder.encode(mCustomer_id, URL_Class.mConvertType);
                    }
                }
            }
            return null;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void enquiryPost(String data) {
        progressBar.setVisibility(View.VISIBLE);
        String url[] = {URL_Class.mURL + URL_Class.mURL_ForgetPassword};
        new API_Get().get_method(url, api_result, data, JSON_Names.KEY_POST_TYPE, true,
                getBaseContext(), "ForgetPwd");
    }
}
