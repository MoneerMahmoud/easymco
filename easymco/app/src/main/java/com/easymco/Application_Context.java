package com.easymco;

import android.app.Application;
import android.content.Context;

import com.easymco.custom.AppLanguageSupport;

public class Application_Context extends Application {
    private static Application_Context mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(AppLanguageSupport.onAttach(base, "en"));
    }
}
