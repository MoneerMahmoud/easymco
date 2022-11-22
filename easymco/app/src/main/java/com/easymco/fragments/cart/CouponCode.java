package com.easymco.fragments.cart;

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
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.interfaces.CartHandler;
import com.easymco.mechanism.Methods;


public class CouponCode extends DialogFragment {

    private View mCouponCodeHandler;
    private EditText mCouponData;
    private String mCoupon;
    private CartHandler cartHandler;
    private TextInputLayout mCartCouponCodeHolder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartHandler = (CartHandler) getActivity();
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
        mCouponCodeHandler = inflater.inflate(R.layout.cart_coupon_code, container, false);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setting();
        return mCouponCodeHandler;
    }

    private void setting() {
        Button mCouponBtn = mCouponCodeHandler.findViewById(R.id.coupon_btn);
        mCouponData = mCouponCodeHandler.findViewById(R.id.coupon_data);
        ImageButton mCouponDismiss = mCouponCodeHandler.findViewById(R.id.coupon_code_cancel);
        mCartCouponCodeHolder=mCouponCodeHandler.findViewById(R.id.coupon_code_holder);

        if (DataBaseHandlerDiscount.getInstance().get_coupon_code() != null) {
            mCouponData.setText(DataBaseHandlerDiscount.getInstance().get_coupon_code());
        }

        mCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                if (!mCouponData.getText().toString().isEmpty()) {
                    mCartCouponCodeHolder.setErrorEnabled(false);
                    mCoupon = mCouponData.getText().toString();
                    cartHandler.CartTransferHandler(mCoupon, "Coupon");
                    dismiss();
                } else {
                    mCartCouponCodeHolder.setErrorEnabled(true);
                    mCartCouponCodeHolder.setError(getString(R.string.check_out_enter_coupon_code));
                }
            }
        });

        mCouponDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Methods.hideKeyboard(getActivity());
                dismiss();
            }
        });
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
}
