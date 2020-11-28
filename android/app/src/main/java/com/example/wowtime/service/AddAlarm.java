package com.example.wowtime.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.AlarmListItem;
import java.util.ArrayList;
import java.util.List;

public class AddAlarm {

    AlarmListItem newAlarm;
    private Context mContext;

//    public AddAlarm(Context mContext){
//        this.mContext = mContext;
//        ArrayList<Boolean> options=new ArrayList<>();
//        for (int i=0;i<8;i++){
//            options.add(false);
//        }
//        newAlarm=new AlarmListItem("made by voice",options,"使劲摇摇摇","radar",0,0);
//    }

    public AddAlarm(boolean flag, int weekday, int hour, Context mContext) {
        this.mContext = mContext;
        ArrayList<Boolean> options = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            options.add(false);
        }
        if (flag) {
            options.set(0, true);
        } else {
            options.set(weekday, true);
        }
        String game=mContext.getResources().getString(R.string.blowing_game_setting_header);
        newAlarm = new AlarmListItem("made by voice", options, game, "radar", hour, 0);
    }

    public void setTime(int hour, int minute) {
        newAlarm.setHour(hour);
        newAlarm.setMinute(minute);
    }

    public void setFrequency(ArrayList<Boolean> options) {
        newAlarm.setFrequency(options);
    }

    public void storeAlarm() {
        SharedPreferences mySharedPreferences = mContext
                .getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        String shared = mySharedPreferences.getString("list", "");
        List<AlarmListItem> alarmList = new ArrayList<>();
        if (!shared.equals("") && shared != null) {
            alarmList = JSONObject.parseArray(shared, AlarmListItem.class);
        }
        alarmList.add(newAlarm);
        shared = JSONObject.toJSONString(alarmList);
        editor.putString("list", shared);
        editor.apply();
    }
}
