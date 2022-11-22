package com.easymco.fragments;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.easymco.R;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.interfaces.EnquiryPost;
import com.easymco.mechanism.Methods;

import org.json.JSONObject;

public class ContactUs extends DialogFragment implements View.OnClickListener {

    View viewContactUs;
    ImageButton mCloseButton;
    TextInputLayout mFirstName, mEmailId, mTelephone,
            mEnquiry;
    EditText mFirstNameEditText, mEmailIdEditText, mTelephoneEditText,
            mEnquiryEditText;
    Button mSubmitBtn;
    EnquiryPost enquiryPostCaller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enquiryPostCaller = (EnquiryPost) getActivity();
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
        viewContactUs = inflater.inflate(R.layout.contact_us, container, false);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        action();
        return viewContactUs;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onViewCreated(view, savedInstanceState);
    }

    private void action() {
        /*TextInputLayout*/
        mFirstName = viewContactUs.findViewById(R.id.contact_form_first_name_text_input_layout);
        mEmailId = viewContactUs.findViewById(R.id.contact_form_text_input_layout);
        mTelephone = viewContactUs.findViewById(R.id.contact_form_telephone_text_input_layout);
        mEnquiry = viewContactUs.findViewById(R.id.contact_form_message_text_input_layout);
        /*EditText*/
        mFirstNameEditText = viewContactUs.findViewById(R.id.contact_form_first_name_edit_text);
        mEmailIdEditText = viewContactUs.findViewById(R.id.contact_form_email_edit_text);
        mTelephoneEditText = viewContactUs.findViewById(R.id.contact_form_telephone_edit_text);
        mEnquiryEditText = viewContactUs.findViewById(R.id.contact_form_message_edit_text);
        /*Button*/
        mCloseButton = viewContactUs.findViewById(R.id.navigation_drawer_about_us_closer_btn);
        mSubmitBtn = viewContactUs.findViewById(R.id.contact_form_submit_btn);

        mCloseButton.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);

    }

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
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.navigation_drawer_about_us_closer_btn:
                Methods.hideKeyboard(getActivity());
                dismiss();
                break;
            case R.id.contact_form_submit_btn:
                Methods.hideKeyboard(getActivity());
                check();
                break;
        }

    }

    private void check() {

        if (!mFirstNameEditText.getText().toString().isEmpty()
                && !mEmailIdEditText.getText().toString().isEmpty()
                && !mTelephoneEditText.getText().toString().isEmpty()
                && !mEnquiryEditText.getText().toString().isEmpty()
                && mFirstNameEditText.getText().toString().length() > 2
                && mFirstNameEditText.getText().toString().length() < 33
                && Methods.isEmailValidator(mEmailIdEditText.getText().toString())
                && mEnquiryEditText.getText().toString().length() > 9
                && mEnquiryEditText.getText().toString().length() < 3001
                && mTelephoneEditText.getText().toString().length() > 2
                && mTelephoneEditText.getText().toString().length() < 33) {

            /*Enquiry success*/
            if (getContactUsData() != null) {
                enquiryPostCaller.enquiryPost(getContactUsData());
                dismiss();
            } else {
                Methods.toast(getResources().getString(R.string.error));
                dismiss();
            }
        }

        if (mFirstNameEditText.getText().toString().isEmpty()
                ) {
            mFirstName.setErrorEnabled(true);
            mFirstName.setError(getActivity().getResources().getString(R.string.enter_first_name));
        } else {
            if (mFirstNameEditText.getText().toString().length() > 0
                    && mFirstNameEditText.getText().toString().length() < 33) {
                mFirstName.setErrorEnabled(false);
            } else {
                mFirstName.setErrorEnabled(true);
                mFirstName.setError(getActivity().getResources().getString(R.string.enter_first_name_length));
            }
        }
        if (mEmailIdEditText.getText().toString().isEmpty()) {
            mEmailId.setErrorEnabled(true);
            mEmailId.setError(getActivity().getResources().getString(R.string.reg_error_email2));
        } else {
            if (!Methods.isEmailValidator(mEmailIdEditText.getText().toString())) {
                mEmailId.setErrorEnabled(true);
                mEmailId.setError(getActivity().getResources().getString(R.string.reg_error_email2));
            } else {
                mEmailId.setErrorEnabled(false);
            }
        }
        if (mTelephoneEditText.getText().toString().isEmpty()) {
            mTelephone.setErrorEnabled(true);
            mTelephone.setError(getActivity().getResources().getString(R.string.reg_error_telephone));
        } else {
            if (mTelephoneEditText.getText().toString().length() > 2
                    && mTelephoneEditText.getText().toString().length() < 33) {
                mTelephone.setErrorEnabled(false);
            } else {
                mTelephone.setErrorEnabled(true);
                mTelephone.setError(getActivity().getResources().getString(R.string.reg_error_telephone));
            }
        }

        if (mEnquiryEditText.getText().toString().isEmpty()) {
            mEnquiry.setErrorEnabled(true);
            mEnquiry.setError(getActivity().getResources().getString(R.string.enquiry_error));
        } else {
            if (mEnquiryEditText.getText().toString().length() > 9
                    && mEnquiryEditText.getText().toString().length() < 3001) {
                mEnquiry.setErrorEnabled(false);
            } else {
                mEnquiry.setErrorEnabled(true);
                mEnquiry.setError(getActivity().getResources().getString(R.string.enquiry_error));
            }
        }

    }

    private String getContactUsData() {
        try {
            JSONObject result = new JSONObject();
            result.put("firstname", mFirstNameEditText.getText().toString());
            result.put("email", mEmailIdEditText.getText().toString());
            result.put("phone", mTelephoneEditText.getText().toString());
            result.put("message", mEnquiryEditText.getText().toString());
            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
