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
import android.widget.TextView;

import com.easymco.R;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerDiscount;
import com.easymco.interfaces.CartHandler;
import com.easymco.mechanism.Methods;


public class RewardPoints extends DialogFragment {

    private View mRewardPointsHandler;
    private EditText mRewardPointsData;
    private String mRewardPoints;
    private CartHandler cartHandler;
    private int mMaxRewardPoints = 0, mCustomerRewardPoints = 0;
    private TextInputLayout mRewardPointHolder;

    public RewardPoints() {
    }

    public static RewardPoints getInstance(int max_reward_point, int customer_point) {
        RewardPoints rewardPoints = new RewardPoints();
        Bundle bundle = new Bundle();
        bundle.putInt("max_point", max_reward_point);
        bundle.putInt("customer_point", customer_point);
        rewardPoints.setArguments(bundle);
        return rewardPoints;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartHandler = (CartHandler) getActivity();
        if (getArguments() != null) {
            mMaxRewardPoints = getArguments().getInt("max_point");
            mCustomerRewardPoints = getArguments().getInt("customer_point");
        }
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
        mRewardPointsHandler = inflater.inflate(R.layout.cart_reward_points, container, false);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setting();
        return mRewardPointsHandler;
    }

    private void setting() {
        Button mRewardPointBtn = mRewardPointsHandler.findViewById(R.id.reward_points_btn);
        TextView mRewardPointTitle = mRewardPointsHandler.findViewById(R.id.reward_points_title);
        mRewardPointsData = mRewardPointsHandler.findViewById(R.id.reward_points_data);
        ImageButton mRewordPointDismiss = mRewardPointsHandler.findViewById(R.id.reward_points_cancel);
        mRewardPointHolder = mRewardPointsHandler.findViewById(R.id.reward_points_holder);

        if (DataBaseHandlerDiscount.getInstance().get_reward_points() != null) {
            mRewardPointsData.setText(DataBaseHandlerDiscount.getInstance().get_reward_points());
        }

        String title = getString(R.string.reward_points_title_1) + " " + mCustomerRewardPoints + " " + getString(R.string.reward_points_title_2);
        mRewardPointTitle.setText(title);

        String hint = getString(R.string.reward_points_content_1) + " " + mMaxRewardPoints + " " + getString(R.string.reward_points_content_2);
        mRewardPointHolder.setHint(hint);

        mRewordPointDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Methods.hideKeyboard(getActivity());
                dismiss();
            }
        });

        mRewardPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Methods.hideKeyboard(getActivity());
                if (!mRewardPointsData.getText().toString().isEmpty()) {
                    mRewardPoints = mRewardPointsData.getText().toString();
                    if (Integer.valueOf(mRewardPoints) > 0) {
                        if (Integer.valueOf(mRewardPoints) <= mCustomerRewardPoints) {
                            if (Integer.valueOf(mRewardPoints) <= mMaxRewardPoints) {
                                mRewardPointHolder.setErrorEnabled(false);
                                cartHandler.CartTransferHandler(mRewardPoints, "RewardPoints");
                                dismiss();
                                Methods.hideKeyboard(getActivity());
                            } else {
                                String error = getString(R.string.reward_points_max_error) + " " + mMaxRewardPoints + ".";
                                enable_error(error);
                            }
                        } else {
                            String error = getString(R.string.reward_points_max_error_2) + " " + mCustomerRewardPoints + ".";
                            enable_error(error);
                        }
                    } else {
                        String error = getString(R.string.reward_points_error);
                        enable_error(error);
                    }
                } else {
                    String error = getString(R.string.reward_points_error);
                    enable_error(error);
                }
            }
        });
    }

    private void enable_error(String message) {
        mRewardPointHolder.setErrorEnabled(true);
        mRewardPointHolder.setError(message);
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
