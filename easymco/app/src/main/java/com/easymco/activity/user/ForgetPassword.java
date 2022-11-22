package com.easymco.activity.user;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.NoInternetConnection;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.interfaces.EnquiryPost;
import com.easymco.mechanism.Methods;
import com.easymco.network_checker.NetworkConnection;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends DialogFragment {
    TextView btn_send_email;
    EditText get_email;
    EnquiryPost enquiryPost;
    View mForgetPasswordView;
    ImageButton close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enquiryPost = (EnquiryPost) getActivity();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mForgetPasswordView = inflater.inflate(R.layout.user_forget_password_activity, container, false);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setting();
        return mForgetPasswordView;
    }

    private void setting() {
        close= mForgetPasswordView.findViewById(R.id.forget_pwd_closer_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        get_email = mForgetPasswordView.findViewById(R.id.et_enter_email_id_for_link);
        btn_send_email = mForgetPasswordView.findViewById(R.id.btn_send_email);
        btn_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                if (!get_email.getText().toString().equals("")) {

                    switch (Boolean.toString(Methods.isEmailValidator(get_email.getText().toString()))) {

                        case "true": {
                            if (NetworkConnection.connectionChecking(getActivity().getApplicationContext())) {
                                HashMap<String, String> postDataParams = new HashMap<>();
                                postDataParams.put(JSON_Names.KEY_MAIL_ID, get_email.getText().toString());

                                if (getPostDataString(postDataParams) != null) {
                                    enquiryPost.enquiryPost(getPostDataString(postDataParams));
                                    dismiss();
                                } else {
                                    Methods.toast(getActivity().getResources().getString(R.string.error));
                                }
                            } else {
                                Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                                startActivity(intent);
                                dismiss();
                                getActivity().finish();
                            }
                            break;

                        }
                        case "false": {
                            Methods.toast(getResources().getString(R.string.please_enter_valid_email));

                        }

                    }
                } else {
                    Methods.toast(getResources().getString(R.string.please_enter_your_email_id));
                }
            }

        });
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
}
