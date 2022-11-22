package com.easymco.mechanism;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.easymco.Application_Context;
import com.easymco.R;
import com.easymco.constant_class.JSON_Names;
import com.easymco.constant_class.URL_Class;
import com.easymco.db_handler.DataBaseLanguageDetails;
import com.easymco.fragments.AboutUs;
import com.easymco.fragments.ContactUs;
import com.easymco.fragments.LanguageList;
import com.easymco.fragments.cart.CouponCode;
import com.easymco.fragments.cart.GiftVoucher;
import com.easymco.fragments.cart.RewardPoints;
import com.easymco.models.CategoryDataSet;
import com.easymco.models.SpinnerCountryList;
import com.easymco.models.SpinnerDataSet;
import com.easymco.shared_preferenc_estring.DataStorage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Methods {

    public static void toast(String message) {
        Toast.makeText(Application_Context.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static String[] getArrayList(ArrayList<CategoryDataSet> list) {
        if (list != null) {
            if (list.size() != 0) {
                String array[] = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = list.get(i).getName();
                }
                return array;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String convertInputStreamToString(BufferedReader bufferedReader) {
        try {
            String line;
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static void glide_image_loader(String url, final ImageView imageView) {
        Glide.with(Application_Context.getAppContext()).load(url).apply(getOption("Default")).into(imageView);
    }

    public static void glide_image_loader_fixed_size(String url, final ImageView imageView) {
        Glide.with(Application_Context.getAppContext()).load(url).apply(getOption("fixed")).into(imageView);
    }

    public static void glide_image_loader_banner(String url, final ImageView imageView) {
        Glide.with(Application_Context.getAppContext()).load(url).apply(getOption("Default")).into(imageView);
    }

    private static RequestOptions getOption(String which) {

        RequestOptions options;
        switch (which) {
            case "fixed":
                options = new RequestOptions()
                        .error(R.drawable.ic_app_error_icon)
                        .override(300, 300)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE);
                break;
            case "Category":
                options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE);
                break;
            default:
                options = new RequestOptions()
                        .error(R.drawable.ic_app_error_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE);
                break;
        }
        return options;

    }

    public static boolean isEmailValidator(String inputEmail) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(URL_Class.mMail_Pattern);
        matcher = pattern.matcher(inputEmail);
        return matcher.matches();
    }



    public static String current_language_id() {
        if(DataBaseLanguageDetails.getInstance(Application_Context.getAppContext()).check_language_selected()){
            String languageId = DataBaseLanguageDetails.getInstance(Application_Context.getAppContext()).get_language_id();
            if(languageId.equals("1")){
                return "1";
            }else {
                return "2";
            }
        }else {
            if (!Locale.getDefault().getLanguage().equals("ar")) {
                return "1";
            } else {
                return "2";
            }
        }
    }

    public static String current_language() {
        if(DataBaseLanguageDetails.getInstance(Application_Context.getAppContext()).check_language_selected()){
            String languageId = DataBaseLanguageDetails.getInstance(Application_Context.getAppContext()).get_language_id();
            if(languageId.equals("1")){
                return URL_Class.mLanguage_ENGLISH;
            }else {
                return URL_Class.mLanguage_ARABIC;
            }
        }else {
            if (!Locale.getDefault().getLanguage().equals("ar")) {
                return URL_Class.mLanguage_ENGLISH;
            } else {
                return URL_Class.mLanguage_ARABIC;
            }
        }
    }

    public static void change_language(String current_language, Context context) {
        Locale language;
        language = new Locale(current_language);
        Locale.setDefault(language);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = language;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        DataStorage.mStoreSharedPreferenceString(context.getApplicationContext(), JSON_Names.KEY_CURRENT_LANGUAGE, current_language);
    }

    public static void country_language(FragmentManager fragmentManager, String from, String data, String product_id) {
        LanguageList languageList = new LanguageList();
        Bundle bData = new Bundle();
        if (product_id.equals("0")) {
            bData.putString(JSON_Names.KEY_FROM, from);
            bData.putString(JSON_Names.KEY_DATA, data);
        } else {
            bData.putString(JSON_Names.KEY_FROM, from);
            bData.putString(JSON_Names.KEY_DATA, data);
            bData.putString(JSON_Names.KEY_PRODUCT_STRING, product_id);
        }
        languageList.setArguments(bData);
        languageList.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        languageList.show(fragmentManager, "Preferences");
    }

    public static void AboutUs(FragmentManager fragmentManager) {
        AboutUs navigationDrawerAboutUs = new AboutUs();
        navigationDrawerAboutUs.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        navigationDrawerAboutUs.setCancelable(false);
        navigationDrawerAboutUs.show(fragmentManager, "AboutUs");
    }

    public static void ContactUs(FragmentManager fragmentManager) {
        ContactUs navigationDrawerContactUs = new ContactUs();
        navigationDrawerContactUs.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        navigationDrawerContactUs.setCancelable(false);
        navigationDrawerContactUs.show(fragmentManager, "ContactUs");
    }

    public static ArrayList<SpinnerDataSet> getSpinnerDataSet(ArrayList<SpinnerCountryList> stateList) {

        if (stateList != null) {
            if (stateList.size() > 0) {
                ArrayList<SpinnerDataSet> mActualSpinnerList = new ArrayList<>();
                SpinnerDataSet spinnerDataSet;
                for (int i = 0; i < stateList.size(); i++) {
                    spinnerDataSet = new SpinnerDataSet();
                    spinnerDataSet.set_name(stateList.get(i).get_name());
                    spinnerDataSet.set_id(stateList.get(i).get_id());
                    mActualSpinnerList.add(spinnerDataSet);
                }
                return mActualSpinnerList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void dialog(final Activity mContext, String type, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        switch (type) {
            case "coupon":
                alertDialogBuilder
                        .setTitle(mContext.getString(R.string.coupon_code_error_title))
                        .setItems(new String[]{mContext.getString(R.string.coupon_code_error_1), mContext.getString(R.string.coupon_code_error_2)
                                , mContext.getString(R.string.coupon_code_error_3), mContext.getString(R.string.coupon_code_error_4)},
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                CouponCode(mContext);
                            }
                        });
                break;
            case "dismiss":
                alertDialogBuilder
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(mContext.getString(R.string._continue), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                mContext.onBackPressed();
                                mContext.finish();
                            }
                        });
                break;
            default:
                alertDialogBuilder
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                break;
        }
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void CouponCode(Activity activity) {
        CouponCode couponCode = new CouponCode();
        couponCode.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        couponCode.setCancelable(false);
        couponCode.show(activity.getFragmentManager(), "CouponCode");
    }

    public static void GiftVoucher(Activity activity) {
        GiftVoucher giftVoucher = new GiftVoucher();
        giftVoucher.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        giftVoucher.setCancelable(false);
        giftVoucher.show(activity.getFragmentManager(), "GiftVoucher");
    }

    public static void RewardPoints(Activity activity, int max_point, int customer_point) {
        RewardPoints rewardPoints = RewardPoints.getInstance(max_point, customer_point);
        rewardPoints.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        rewardPoints.setCancelable(false);
        rewardPoints.show(activity.getFragmentManager(), "RewardPoints");
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
