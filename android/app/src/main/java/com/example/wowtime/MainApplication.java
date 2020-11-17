package com.example.wowtime;

import android.app.Application;


public class MainApplication extends Application {

    public static final String PACKAGE = "com.example.wowtime";
    public static final String VERSION =
            "Version " + BuildConfig.VERSION_NAME + '(' + BuildConfig.VERSION_CODE + ')';

    private static MainApplication mApp;

    public static MainApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

}
