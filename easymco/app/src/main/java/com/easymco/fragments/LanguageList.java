package com.easymco.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easymco.R;
import com.easymco.activity.Network_Error;
import com.easymco.activity.NoInternetConnection;
import com.easymco.activity.SplashScreen;
import com.easymco.api_call.API_Get;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;
import com.easymco.db_handler.DataBaseLanguageDetails;
import com.easymco.interfaces.API_Result;
import com.easymco.mechanism.Methods;
import com.easymco.network_checker.NetworkConnection;
import com.easymco.shared_preferenc_estring.DataStorage;

public class LanguageList extends DialogFragment implements View.OnClickListener, API_Result {

    String from, content, id;
    ProgressBar pro;
    TextView arabic, english;
    Button btn_cancel;
    API_Result api_result;
    TextView title;
    ImageButton close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api_result = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.language_list_activity, container, false);
       // getDialog().setTitle(R.string.please_select);

        if(getDialog().getWindow()!=null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        from = getArguments().getString(JSON_Names.KEY_FROM);
        content = getArguments().getString(JSON_Names.KEY_DATA);
        if (from.equals("Product")) {
            id = getArguments().getString(JSON_Names.KEY_PRODUCT_STRING);
        }

        title = rootView.findViewById(R.id.tv_select_language_title);
        title.setText(getResources().getString(R.string.please_select_language));
        close = rootView.findViewById(R.id.language_closer_btn);
        close.setOnClickListener(this);

        pro = rootView.findViewById(R.id.country_list_progress_bar);
        arabic = rootView.findViewById(R.id.language_arabic);
        english = rootView.findViewById(R.id.language_english);
        btn_cancel = rootView.findViewById(R.id.bt_preferences_cancel);

        english.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        arabic.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        if (NetworkConnection.connectionChecking(getActivity().getApplicationContext())) {
            if (NetworkConnection.connectionType(getActivity().getApplicationContext())) {
                pro.setVisibility(View.GONE);
                english.setOnClickListener(this);
                arabic.setOnClickListener(this);
            } else {
                two_g_transfer();
            }
        } else {
            Intent intent = new Intent(getActivity().getBaseContext(), NoInternetConnection.class);
            startActivity(intent);
            dismiss();
        }

        btn_cancel.setOnClickListener(this);
        return rootView;
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

    public void load_url() {
        pro.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.loading_please_wait));
        String value[] = {URL_Class.mURL + URL_Class.mURL_MainCategory + Methods.current_language()};
        new API_Get().get_method(value, api_result, "", JSON_Names.KEY_GET_TYPE, true, getActivity(), "LanguageCountry");
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.language_english:
                AppLanguageSupport.setLocale(getActivity().getBaseContext(),"en");
                DataStorage.mStoreSharedPreferenceString(getActivity(), JSON_Names.KEY_CURRENT_LANGUAGE, "en");

                if(DataBaseLanguageDetails.getInstance(getActivity()).check_language_selected()){
                    DataBaseLanguageDetails.getInstance(getActivity()).delete_language_detail();
                    DataBaseLanguageDetails.getInstance(getActivity()).insert_language_detail("1");
                }
                hide();
                load_url();
                break;
            case R.id.language_arabic:
                AppLanguageSupport.setLocale(getActivity().getBaseContext(),"ar");
                DataStorage.mStoreSharedPreferenceString(getActivity(), JSON_Names.KEY_CURRENT_LANGUAGE, "ar");


                if(DataBaseLanguageDetails.getInstance(getActivity()).check_language_selected()){
                    DataBaseLanguageDetails.getInstance(getActivity()).delete_language_detail();
                    DataBaseLanguageDetails.getInstance(getActivity()).insert_language_detail("2");
                }
                hide();
                load_url();
                break;
            case R.id.bt_preferences_cancel:
                dismiss();
                break;
            case R.id.language_closer_btn:
                dismiss();
                break;
        }
    }

    public void hide() {
        english.setVisibility(View.GONE);
        arabic.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
    }


    public void intent_transfer_home() {

        Intent intent;
        switch (from) {
            case "MyAccountMainMenu":
                //intent = new Intent(getBaseContext(), MyAccountMainMenu.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "MyAccountInfo":
                //intent = new Intent(getBaseContext(), MyAccountInfo.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "OrderHistory":
                //intent = new Intent(getBaseContext(), OrderHistory.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "OrderHistoryOrderInfo":
                //intent = new Intent(getBaseContext(), OrderHistoryOrderInfo.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "CardViewActivity":
                //intent = new Intent(getBaseContext(), CardViewActivity.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "AddsBookEditAdds":
                //intent = new Intent(getBaseContext(), AddsBook_EditAdds.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "RewardPoints":
                //intent = new Intent(getBaseContext(), RewardPoints.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "Transactions":
                //intent = new Intent(getBaseContext(), Transactions.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "Search":
                //intent = new Intent(getBaseContext(), Search.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            case "Product":
                //intent = new Intent(getBaseContext(), Product_Details.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                intent.putExtra(JSON_Names.KEY_PRODUCT_STRING, id);
                break;
            case "Category":
                //intent = new Intent(getBaseContext(), Category_Details.class);
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
            default:
                intent = new Intent(getActivity().getBaseContext(), SplashScreen.class);
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        dismiss();
    }

    private void two_g_transfer() {
        Intent intent = new Intent(getActivity().getBaseContext(), Network_Error.class);
        startActivity(intent);
        dismiss();
    }

    @Override
    public void result(String[] data, String source) {
        if (data != null) {
            if (data[0] != null) {
                if (source.equals("LanguageCountry")) {
                    DataStorage.mStoreSharedPreferenceString(getActivity().getApplicationContext(),
                            JSON_Names.KEY_NAVIGATION_DATA, data[0]);
                    intent_transfer_home();
                }
            }
        }
    }
}
