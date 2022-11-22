package com.easymco.fragments.account;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.easymco.R;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.interfaces.AccountListener;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class AccountChangePassword extends DialogFragment implements View.OnClickListener {

    View mAccountChangePassword;
    Button mAccountChangePasswordSubmit;
    TextInputLayout mAccountChangePasswordPassword, mAccountChangePasswordConfirmPassword;
    EditText mAccountChangePasswordPasswordEditText, mAccountChangePasswordConfirmPasswordEditText;
    AccountListener accountHandler;
    ImageButton close;

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null)
            return;

        int width = getResources().getDimensionPixelSize(R.dimen._300sdp);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, height);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountHandler = (AccountListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAccountChangePassword = inflater.inflate(R.layout.account_change_password, container, false);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        action();
        return mAccountChangePassword;
    }

    private void action() {
        /*Button*/
        close = mAccountChangePassword.findViewById(R.id.change_pwd_closer_btn);
        close.setOnClickListener(this);
        mAccountChangePasswordSubmit = mAccountChangePassword.findViewById(R.id.account_change_password_submit_btn);
        /*TextInputLayout*/
        mAccountChangePasswordPassword = mAccountChangePassword.findViewById(R.id.account_password_text_input_layout);
        mAccountChangePasswordConfirmPassword = mAccountChangePassword.findViewById(R.id.account_confirm_password_text_input_layout);
        /*EditText*/
        mAccountChangePasswordPasswordEditText = mAccountChangePassword.findViewById(R.id.account_password_edit_text);
        mAccountChangePasswordConfirmPasswordEditText = mAccountChangePassword.findViewById(R.id.account_confirm_password_edit_text);

        mAccountChangePasswordSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.account_change_password_submit_btn:
                check();
                break;
            case R.id.change_pwd_closer_btn:
                dismiss();
                break;
        }

    }

    private void check() {

        if (!mAccountChangePasswordPasswordEditText.getText().toString().isEmpty()
                && !mAccountChangePasswordConfirmPasswordEditText.getText().toString().isEmpty()
                && mAccountChangePasswordConfirmPasswordEditText.getText().toString().equals(mAccountChangePasswordPasswordEditText.getText().toString())
                && mAccountChangePasswordPasswordEditText.getText().toString().length() >= 6
                && mAccountChangePasswordPasswordEditText.getText().toString().length() <= 20) {
            /*Change password success*/
            if (getChangePasswordString() != null) {
                accountHandler.postChangePassword(getChangePasswordString());
            }
            dismiss();
        }
        if (mAccountChangePasswordPasswordEditText.getText().toString().isEmpty()) {
            mAccountChangePasswordPassword.setErrorEnabled(true);
            mAccountChangePasswordPassword.setError(getActivity().getResources().getString(R.string.reg_error_pwd));
        } else {
            if (mAccountChangePasswordPasswordEditText.getText().toString().length() >= 6
                    && mAccountChangePasswordPasswordEditText.getText().toString().length() <= 20) {
                if (mAccountChangePasswordConfirmPasswordEditText.getText().toString().isEmpty()) {
                    mAccountChangePasswordConfirmPassword.setErrorEnabled(true);
                    mAccountChangePasswordConfirmPassword.setError(getActivity().getResources().getString(R.string.reg_error_confirm_pwd));
                } else {
                    if (!mAccountChangePasswordConfirmPasswordEditText.getText().toString().equals(mAccountChangePasswordPasswordEditText.getText().toString())) {
                        mAccountChangePasswordConfirmPassword.setErrorEnabled(true);
                        mAccountChangePasswordConfirmPassword.setError(getActivity().getResources().getString(R.string.reg_error_confirm_pwd));
                    } else {
                        mAccountChangePasswordConfirmPassword.setErrorEnabled(false);
                    }
                }
                mAccountChangePasswordPassword.setErrorEnabled(false);
            } else {
                mAccountChangePasswordPassword.setErrorEnabled(true);
                mAccountChangePasswordPassword.setError(getActivity().getResources().getString(R.string.reg_error_pwd));
            }
        }


    }

    public String getChangePasswordString() {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put(JSON_Names.KEY_PASSWORD, mAccountChangePasswordPasswordEditText.getText().toString());
            params.put(JSON_Names.KEY_CONFIRM, mAccountChangePasswordConfirmPasswordEditText.getText().toString());
            params.put(JSON_Names.KEY_CUSTOMER_ID, String.valueOf(DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).get_customer_id()));

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
}
