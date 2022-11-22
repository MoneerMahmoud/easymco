package com.easymco.fragments.product;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.easymco.R;
import com.easymco.activity.NoInternetConnection;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseHandlerAccount;
import com.easymco.interfaces.ReviewPost;
import com.easymco.mechanism.Methods;
import com.easymco.network_checker.NetworkConnection;

import java.net.URLEncoder;

public class Product_Review_Post extends Fragment {
    View view;
    EditText mReviewContent;
    RatingBar mReviewRating;
    Button mReviewPost, mReviewCancel;
    String mRating_Value, mReview_Commend, mProductID;
    ReviewPost reviewPost;

    public static Product_Review_Post getInstant(String data) {
        Product_Review_Post product_review_post = new Product_Review_Post();
        Bundle bundle = new Bundle();
        bundle.putString("Data", data);
        product_review_post.setArguments(bundle);
        return product_review_post;
    }

    public Product_Review_Post() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewPost = (ReviewPost) getActivity();
        if (getArguments() != null) {
            mProductID = getArguments().getString("Data");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_detail_review_post, container, false);
        action();
        return view;
    }

    public void action() {
        mReviewContent = view.findViewById(R.id.review_name_comment_value);
        mReviewPost = view.findViewById(R.id.review_submit);
        mReviewRating = view.findViewById(R.id.product_detail_rating);
        mReviewCancel = view.findViewById(R.id.review_cancel);

        mReviewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                if (DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext()).check_login()) {
                    if (mReviewContent.getText().length() != 0) {
                        if (mReviewContent.getText().length() > 25 && mReviewContent.getText().length() <= 100) {
                            if (mReviewRating.getRating() != 0) {
                                Float value = mReviewRating.getRating();
                                if (value != 0 && value <= 5) {
                                    mRating_Value = String.valueOf(mReviewRating.getRating());
                                    mReview_Commend = mReviewContent.getText().toString();
                                    if (NetworkConnection.connectionChecking(getActivity().getApplicationContext())) {
                                        if (getReviewPostString() != null) {
                                            reviewPost.review_post(getReviewPostString());
                                        }
                                    } else {
                                        Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                } else {
                                    toast_call(getResources().getString(R.string.enter_rating_value));
                                }
                            } else {
                                toast_call(getResources().getString(R.string.enter_rating));
                            }
                        } else {
                            toast_call(getResources().getString(R.string.enter_command_length));
                        }
                    } else {
                        toast_call(getResources().getString(R.string.enter_command));
                    }
                }
            }
        });

        mReviewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.hideKeyboard(getActivity());
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    public void toast_call(String id) {
        Methods.toast(id);
    }

    public String getReviewPostString() {
        try {
            StringBuilder mBuilder = new StringBuilder();
            mBuilder.append(URLEncoder.encode(JSON_Names.KEY_REVIEW, URL_Class.mConvertType));
            mBuilder.append(URL_Class.mEqual_Symbol);
            mBuilder.append(URLEncoder.encode(mReview_Commend, URL_Class.mConvertType));
            mBuilder.append(URL_Class.mAnd_Symbol);
            mBuilder.append(URLEncoder.encode(JSON_Names.KEY_RATING, URL_Class.mConvertType));
            mBuilder.append(URL_Class.mEqual_Symbol);
            mBuilder.append(URLEncoder.encode(mRating_Value, URL_Class.mConvertType));
            mBuilder.append(URL_Class.mAnd_Symbol);
            mBuilder.append(URLEncoder.encode(JSON_Names.KEY_CUSTOMER_ID, URL_Class.mConvertType));
            mBuilder.append(URL_Class.mEqual_Symbol);
            String temp2 = String.valueOf(DataBaseHandlerAccount.getInstance(getActivity().getApplicationContext())
                    .get_customer_id());
            mBuilder.append(URLEncoder.encode(temp2, URL_Class.mConvertType));
            mBuilder.append(URL_Class.mAnd_Symbol);
            mBuilder.append(URLEncoder.encode(JSON_Names.KEY_PRODUCT_ID, URL_Class.mConvertType));
            mBuilder.append(URL_Class.mEqual_Symbol);
            mBuilder.append(URLEncoder.encode(mProductID, URL_Class.mConvertType));
            return mBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
