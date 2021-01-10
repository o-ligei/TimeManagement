package com.example.wowtime;

import android.app.Application;


public class MainApplication extends Application {

    public static final String PACKAGE = "com.example.wowtime";
    public static final String VERSION =
            "Version " + BuildConfig.VERSION_NAME + '(' + BuildConfig.VERSION_CODE + ')';

    private static MainApplication mApp;

    private static Integer themeNumber = 0;

    private static Integer userId=-1;

    public static MainApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static void setThemeNumber(Integer themeNumber) {
        MainApplication.themeNumber = themeNumber;
    }

    public static Integer getThemeNumber() {
        return themeNumber;
    }

    public static Integer getUserId() {
        return userId;
    }

    public static void setUserId(Integer userId) {
        MainApplication.userId = userId;
    }

}
