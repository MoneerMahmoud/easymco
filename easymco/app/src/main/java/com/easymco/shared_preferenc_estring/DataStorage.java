package com.easymco.shared_preferenc_estring;

import android.content.Context;
import android.content.SharedPreferences;

import com.easymco.constant_class.JSON_Names;

public class DataStorage {

    public static void mStoreSharedPreferenceString(Context context, String name, String value) {
        SharedPreferences pref = context.getSharedPreferences(JSON_Names.KEY_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (pref.contains(name)) {
            DataStorage.mRemoveSharedPreferenceString(context, name);
            editor.putString(name, value);
            editor.apply();
        } else {
            editor.putString(name, value);
            editor.apply();
        }
    }

    public static String mRetrieveSharedPreferenceString(Context context, String name) {
        SharedPreferences pref = context.getSharedPreferences(JSON_Names.KEY_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getString(name, null);
    }

    public static void mRemoveSharedPreferenceString(Context context, String name) {
        SharedPreferences pref = context.getSharedPreferences(JSON_Names.KEY_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(name);
        editor.apply();
    }
}
