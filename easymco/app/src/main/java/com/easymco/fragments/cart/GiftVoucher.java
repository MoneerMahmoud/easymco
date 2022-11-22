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


public class GiftVoucher extends DialogFragment {

    private View mGiftVoucherHandler;
    private EditText mGiftVoucherData;
    private String mGiftVoucher;
    private CartHandler cartHandler;
    private TextInputLayout mGiftVoucherHolder;

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
        mGiftVoucherHandler = inflater.inflate(R.layout.cart_gift_voucher, container, false);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setting();
        return mGiftVoucherHandler;
    }

    private void setting() {
        Button mGiftVoucherBtn = mGiftVoucherHandler.findViewById(R.id.gift_voucher_btn);
        mGiftVoucherData = mGiftVoucherHandler.findViewById(R.id.gift_voucher_data);
        ImageButton mGiftVoucherDismiss = mGiftVoucherHandler.findViewById(R.id.gift_voucher_cancel);
        mGiftVoucherHolder = mGiftVoucherHandler.findViewById(R.id.gift_voucher_holder);

        if (DataBaseHandlerDiscount.getInstance().get_gift_voucher() != null) {
            mGiftVoucherData.setText(DataBaseHandlerDiscount.getInstance().get_gift_voucher());
        }

        mGiftVoucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGiftVoucherData.getText().toString().isEmpty()) {
                    mGiftVoucherHolder.setErrorEnabled(false);
                    mGiftVoucher = mGiftVoucherData.getText().toString();
                    cartHandler.CartTransferHandler(mGiftVoucher, "GiftVoucher");
                    dismiss();
                } else {
                    mGiftVoucherHolder.setErrorEnabled(true);
                    mGiftVoucherHolder.setError(getString(R.string.check_out_enter_gift_voucher_code));
                }
            }
        });

        mGiftVoucherDismiss.setOnClickListener(new View.OnClickListener() {
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
