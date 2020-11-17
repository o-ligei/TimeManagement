package com.example.wowtime.ui.alarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import com.example.wowtime.R;
import com.example.wowtime.dto.AlarmListItem;
import com.example.wowtime.util.Weekday;
import java.util.ArrayList;
import java.util.List;


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
        /*initialize*/
        tag = this.getString(R.string.alarm_list_title);
        game = this.getString(R.string.clock_setting_game_default);
        ring = "雷达";
        frequency = new ArrayList<>();
        frequency.add(0, true);
        for (int i = 1; i < 8; i++) {
            frequency.add(i, false);
        }
        Hour = 0;
        Minute = 0;
        sleepFlag = false;
        sleepHour = 0;
        sleepMinute = 0;

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
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

        TimePicker timePicker = findViewById(R.id.alarmtimePicker);
        timePicker.setIs24HourView(true);
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
        saveClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mySharedPreferences = getSharedPreferences("alarmList",
                                                                             Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                String shared = mySharedPreferences.getString("list", "");
//                System.out.println("alarmList:"+shared);
                List<AlarmListItem> alarmList = new ArrayList<>();
                AlarmListItem alarm = new AlarmListItem(tag, frequency, game, ring, Hour, Minute);
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
                finish();
            }
        });

        Button assistSetting = findViewById(R.id.SleepAssistButton);
        assistSetting.setOnClickListener(v -> startActivity(
                new Intent(ClockSettingActivity.this, SleepAssistSetting.class)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestart() {
        super.onRestart();
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
            repeatBtn.setText("无重复");
        } else {
            Weekday weekday = new Weekday();
            StringBuilder out = new StringBuilder("星期");
            for (int i = 1; i <= 7; i++) {
                if (frequency.get(i)) {
                    out.append(weekday.getDay(i));
                    repeatBtn.setText(out);
                }
            }
        }

        Button assistSetting = findViewById(R.id.SleepAssistButton);
        sleepFlag = mySharedPreferences.getBoolean("sleepFlag", false);
        if (sleepFlag) {
            assistSetting.setText("开启");
        } else {
            assistSetting.setText("关闭");
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