package com.example.wowtime.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.Calendar;

public class Accumulation {

    private SharedPreferences mySharedPreferences;
    private static final long checkTime = 1000 * 60 * 60 * 24 * 3;

    public Accumulation(Context mContext) {
        mySharedPreferences = mContext.getSharedPreferences("accumulation", Activity.MODE_PRIVATE);
    }

    public int getAccumulation() {
        return mySharedPreferences.getInt("accumulation", 0);
    }

    public void setAccumulation(int num) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
//        int accumulation=mySharedPreferences.getInt("accumulation",0);
        editor.putInt("accumulation", num);
        editor.apply();
    }

    public void addAccumulation(int addNum) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        int accumulation = mySharedPreferences.getInt("accumulation", 0);
        accumulation += addNum;
        editor.putInt("accumulation", accumulation);
        editor.apply();
    }

    public void initStartTime() {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        Calendar calendar = Calendar.getInstance();
        editor.putLong("startTime", calendar.getTimeInMillis());
        editor.apply();
    }

    public void checkStartTime() {
        Calendar calendar = Calendar.getInstance();
        long startTime = mySharedPreferences.getLong("startTime", 0);
        if (calendar.getTimeInMillis() - startTime > checkTime) {
            initStartTime();
            setAccumulation(0);
        }
    }
}
