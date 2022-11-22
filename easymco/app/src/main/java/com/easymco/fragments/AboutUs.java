package com.easymco.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.easymco.R;
import com.easymco.constant_class.URL_Class;
import com.easymco.custom.AppLanguageSupport;

public class AboutUs extends DialogFragment implements View.OnClickListener {

    View viewAboutUs;
    WebView mAboutUsHolder;
    ImageButton mCloseButton;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewAboutUs = inflater.inflate(R.layout.about_us, container, false);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        action();
        return viewAboutUs;
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


    private void action() {
        mCloseButton = viewAboutUs.findViewById(R.id.about_us_closer_btn);
        mAboutUsHolder = viewAboutUs.findViewById(R.id.about_us_web_link_holder);
        mCloseButton.setOnClickListener(this);
        progressBar = viewAboutUs.findViewById(R.id.progressBar_about_us_dialog);

        mAboutUsHolder.setWebViewClient(new AboutUsWebClient());
        mAboutUsHolder.loadUrl(URL_Class.mAbout_us);
    }

    public class AboutUsWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null)
            return;

        int width = getResources().getDimensionPixelSize(R.dimen._300sdp);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        if (getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.about_us_closer_btn:
                dismiss();
                break;
        }

    }
}
