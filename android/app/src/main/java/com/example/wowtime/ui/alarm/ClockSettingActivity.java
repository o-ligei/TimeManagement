package com.example.wowtime.ui.alarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.example.wowtime.MainApplication;
import com.example.wowtime.R;
import com.example.wowtime.dto.AlarmListItem;
import com.example.wowtime.util.Ajax;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.UserInfoAfterLogin;
import com.example.wowtime.util.Weekday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import okhttp3.FormBody;


public class ClockSettingActivity extends AppCompatActivity {

    private int position;
    private String tag;
    private String game;
    private String ring;
    private List<Boolean> frequency;
    private int Hour;
    private int Minute;
    private int sleepHour;
    private int sleepMinute;
    private boolean sleepFlag;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainApplication.getThemeNumber() == 1) { setTheme(R.style.DarkTheme); }
        /*initialize*/
        tag = getResources().getString(R.string.clock_setting_tag_default);
        game = getResources().getString(R.string.shaking_game_setting_header);
        ring = getResources().getString(R.string.alarm_ring_radar);
        frequency = new ArrayList<>();
        frequency.add(0, true);
        for (int i = 1; i < 8; i++) {
            frequency.add(i, false);
        }
        Calendar calendar = Calendar.getInstance();
        Hour = calendar.get(Calendar.HOUR_OF_DAY);
        Minute = calendar.get(Calendar.MINUTE);
        sleepFlag = false;
        sleepHour = 0;
        sleepMinute = 0;

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        int userid = intent.getIntExtra("userid", 0);
        String action;
        action = intent.getStringExtra("action");

        //navigate from existed alarm
        if (position != -1) {
            SharedPreferences mySharedPreferences = getSharedPreferences("alarmList",
                                                                         Activity.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mySharedPreferences
                    .edit();
            String shared = mySharedPreferences.getString("list", "");
            List<AlarmListItem> alarmList = new ArrayList<>();
            if (!shared.equals("") && shared != null) {
                alarmList = JSONObject.parseArray(shared, AlarmListItem.class);
            }
            AlarmListItem alarm = alarmList.get(position);
            tag = alarm.getTag();
            game = alarm.getGame();
            ring = alarm.getRing();
            frequency = alarm.getFrequency();
            Hour = alarm.getHour();
            Minute = alarm.getMinute();
            sleepFlag = alarm.getSleepFlag();
            sleepHour = alarm.getSleepHour();
            sleepMinute = alarm.getSleepMinute();
        }

        setContentView(R.layout.clock_setting_activity);
        Button gameSetting = findViewById(R.id.GameSetting);
        gameSetting.setText(game);
        gameSetting.setOnClickListener(
                v -> startActivity(new Intent(ClockSettingActivity.this, TaskListActivity.class)));
        Button ringSetting = findViewById(R.id.ClockRingSetting);
        ringSetting.setText(ring);
        ringSetting.setOnClickListener(v -> startActivity(
                new Intent(ClockSettingActivity.this, RingSettingActivity.class)));
        Button cancelSetting = findViewById(R.id.ClockSettingCancel);
        cancelSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockSettingActivity.this.finish();
            }
        });
        Button tagSetting = findViewById(R.id.ClockTagSetting);
        tagSetting.setText(tag);
        Intent i = new Intent(ClockSettingActivity.this, TagSettingActivity.class);
        i.putExtra("tag", tag);
        tagSetting.setOnClickListener(v -> startActivity(i));
        Button repeatBtn = findViewById(R.id.ClockRepeatButton);
        repeatBtn.setOnClickListener(v -> startActivity(
                new Intent(ClockSettingActivity.this, ClockFrequencyActivity.class)));

        if (frequency.get(0)) {
            repeatBtn.setText(getResources().getString(R.string.alarm_frequency_no_repeat));
        } else {
            boolean flag = getResources()
                    .getString(R.string.alarm_frequency_no_repeat).equals("无重复");
            Weekday weekday = new Weekday(flag);
            StringBuilder out;
            if (flag) {
                out = new StringBuilder("星期");
            } else {
                out = new StringBuilder("");
            }
            for (int j = 1; j <= 7; j++) {
                if (frequency.get(j)) {
                    out.append(weekday.getDay(j));
                    out.append(" ");
                }
            }
            repeatBtn.setText(out);
        }
        TimePicker timePicker = findViewById(R.id.alarmtimePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(Hour);
        timePicker.setMinute(Minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Hour = hourOfDay;
                Minute = minute;
                System.out.println(Hour + ":" + Minute);
            }
        });

        //save
        Button saveClock = findViewById(R.id.ClockSettingConfirm);
        if (action != null && action.equals("set for friend")) {
            saveClock.setText(getResources().getString(R.string.send_alarm));
        }
        saveClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmListItem alarm = new AlarmListItem(tag, frequency, game, ring, Hour, Minute);
                if (action != null && action.equals("set for friend")) {
                    send_alarm(userid, alarm);
                }
                SharedPreferences mySharedPreferences = getSharedPreferences("alarmList",
                                                                             Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                String shared = mySharedPreferences.getString("list", "");
//                System.out.println("alarmList:"+shared);
                List<AlarmListItem> alarmList = new ArrayList<>();
                alarm.setSleepFlag(sleepFlag);
                alarm.setSleepHour(sleepHour);
                alarm.setSleepMinute(sleepMinute);
                System.out.println("add alarm:" + alarm.getTag());
                if (!shared.equals("") && shared != null) {
                    alarmList = JSONObject.parseArray(shared, AlarmListItem.class);
                }
                if (position != -1) {
                    alarmList.remove(position);
                }
                alarmList.add(alarm);
                shared = JSONObject.toJSONString(alarmList);
                editor.putString("list", shared);
                editor.apply();
                //achievement
                //count of alarm
                SharedPreferences achievement = ClockSettingActivity.super
                        .getSharedPreferences("achievement", MODE_PRIVATE);
                int count = Integer.parseInt(achievement.getString("alarm_count", "0"));
                count += 1;
                SharedPreferences.Editor editorAchievement = achievement.edit();
                editorAchievement.putString("alarm_count", Integer.toString(count));
                //try of sleep alarm
                if (sleepFlag) { editorAchievement.putString("alarm_sleep", "1"); }
                editorAchievement.apply();
                finish();
            }
        });

        Button assistSetting = findViewById(R.id.SleepAssistButton);
        if(getResources().getString(R.string.alarm_guide).equals("设置几点入睡")){
            NewbieGuide.with(ClockSettingActivity.this)
                       .setLabel("guide1")
//                       .alwaysShow(true)
                       .addGuidePage(GuidePage.newInstance()
                                              .addHighLight(assistSetting)
                                              .setLayoutRes(R.layout.alarm_guide))
                       .show();
        }else{
            NewbieGuide.with(ClockSettingActivity.this)
                       .setLabel("guide1")
//                       .alwaysShow(true)
                       .addGuidePage(GuidePage.newInstance()
                                              .addHighLight(assistSetting)
                                              .setLayoutRes(R.layout.alarm_guide_en))
                       .show();
        }

        if(sleepFlag){
            assistSetting.setText(getResources().getString(R.string.sleep_assist_open));
        } else {
            assistSetting.setText(getResources().getString(R.string.sleep_assist_default));
        }
        assistSetting.setOnClickListener(v -> startActivity(
                new Intent(ClockSettingActivity.this, SleepAssistSetting.class)));
    }

    private void send_alarm(int userid, AlarmListItem alarm) {
        String clockSetting = JSON.toJSONString(alarm);
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("from", String.valueOf(UserInfoAfterLogin.userid));
        formBody.add("to", String.valueOf(userid));
        formBody.add("username", UserInfoAfterLogin.username);
        formBody.add("clocksetting", clockSetting);
        /*handler*/
        android.os.Handler handler = new Handler(message -> {
            if (message.what == InternetConstant.FETCH) {
                String msg = message.getData().get("msg").toString();
                if (msg.equals("success")) {
                    System.out.println("send clock success");
                } else {
                    System.out.println("send clock failed");
                }
            }
            return false;
        });

        Ajax ajax = new Ajax("/Social/SetAlarmForFriend", formBody, handler,
                             InternetConstant.FETCH);
        ajax.fetch();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mySharedPreferences = getSharedPreferences("clock",
                                                                     Activity.MODE_PRIVATE);
        tag = mySharedPreferences.getString("tag", tag);
        Button tagSetting = findViewById(R.id.ClockTagSetting);
        tagSetting.setText(tag);

        game = mySharedPreferences.getString("game", game);
        Button gameSetting = findViewById(R.id.GameSetting);
        gameSetting.setText(game);

        ring = mySharedPreferences.getString("ring", ring);
        Button ringSetting = findViewById(R.id.ClockRingSetting);
        ringSetting.setText(ring);

        String tmp = mySharedPreferences
                .getString("frequency", "[true,false,false,false,false,false,false,false]");
        frequency = JSONObject.parseArray(tmp, boolean.class);
        Button repeatBtn = findViewById(R.id.ClockRepeatButton);
        if (frequency.get(0)) {
            repeatBtn.setText(getResources().getString(R.string.alarm_frequency_no_repeat));
        } else {
            boolean flag = getResources()
                    .getString(R.string.alarm_frequency_no_repeat).equals("无重复");
            Weekday weekday = new Weekday(flag);
            StringBuilder out;
            if (flag) {
                out = new StringBuilder("星期");
            } else {
                out = new StringBuilder("");
            }
            for (int j = 1; j <= 7; j++) {
                if (frequency.get(j)) {
                    out.append(weekday.getDay(j));
                    out.append(" ");
                }
            }
            repeatBtn.setText(out);
        }

        Button assistSetting = findViewById(R.id.SleepAssistButton);
        sleepFlag = mySharedPreferences.getBoolean("sleepFlag", false);
        if (sleepFlag) {
            assistSetting.setText(getResources().getString(R.string.sleep_assist_open));
        } else {
            assistSetting.setText(getResources().getString(R.string.sleep_assist_default));
        }
        sleepHour = mySharedPreferences.getInt("sleepHour", 0);
        sleepMinute = mySharedPreferences.getInt("sleepMinute", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences mySharedPreferences = getSharedPreferences("clock",
                                                                     Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}