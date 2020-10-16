package com.example.wowtime;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.appcompat.app.AppCompatDelegate;

public class MainApplication extends Application {
    public static final String PACKAGE = "com.example.wowtime";
    public static final String VERSION = "Version " + BuildConfig.VERSION_NAME + '(' + BuildConfig.VERSION_CODE + ')';

}
