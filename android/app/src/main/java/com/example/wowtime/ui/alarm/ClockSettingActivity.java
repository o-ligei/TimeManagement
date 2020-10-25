package com.example.wowtime.ui.alarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.wowtime.R;
import com.example.wowtime.dto.AlarmListItem;

import com.alibaba.fastjson.*;

import java.util.ArrayList;
import java.util.List;


public class ClockSettingActivity extends AppCompatActivity {
    private int position;
    private String tag;
    private String game;
    private String ring;
    private String frequency;
    private int Hour;
    private int Minute;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag=this.getString(R.string.alarm_list_title);
        game=this.getString(R.string.clock_setting_game_default);
        ring="雷达";
        frequency="无重复";
        Hour=0;
        Minute=0;
        Intent intent=getIntent();
        position=intent.getIntExtra("position",-1);
        //navigate from existed alarm
        if(position!=-1){
            SharedPreferences mySharedPreferences= getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mySharedPreferences.edit();
            String shared=mySharedPreferences.getString("list","");
            List<AlarmListItem> alarmList=new ArrayList<>();
            if(!shared.equals("") &&shared!=null){
                alarmList=JSONObject.parseArray(shared,AlarmListItem.class);
            }
            AlarmListItem alarm=alarmList.get(position);
            tag=alarm.getTag();
            game=alarm.getGame();
            ring=alarm.getRing();
            frequency=alarm.getFrequency();
            Hour=alarm.getHour();
            Minute=alarm.getMinute();
        }
        setContentView(R.layout.clock_setting_activity);
        Button gameSetting=findViewById(R.id.GameSetting);
        gameSetting.setText(game);
        gameSetting.setOnClickListener(v->startActivity(new Intent(ClockSettingActivity.this, TaskListActivity.class)));
        Button ringSetting=findViewById(R.id.ClockRingSetting);
        ringSetting.setText(ring);
        ringSetting.setOnClickListener(v->startActivity(new Intent(ClockSettingActivity.this, RingSettingActivity.class)));
        Button cancelSetting=findViewById(R.id.ClockSettingCancel);
        cancelSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockSettingActivity.this.finish();
            }
        });
        Button tagSetting=findViewById(R.id.ClockTagSetting);
        tagSetting.setText(tag);
        Intent i = new Intent(ClockSettingActivity.this, TagSettingActivity.class);
        i.putExtra("tag",tag);
        tagSetting.setOnClickListener(v->startActivity(i));
        Button repeatBtn=findViewById(R.id.ClockRepeatButton);
        repeatBtn.setOnClickListener(v->startActivity(new Intent(ClockSettingActivity.this, ClockFrequencyActivity.class)));

        TimePicker timePicker=findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Hour=hourOfDay;
                Minute=minute;
                System.out.println(Hour+":"+Minute);
            }
        });

        //save
        Button saveClock=findViewById(R.id.ClockSettingConfirm);
        saveClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mySharedPreferences= getSharedPreferences("alarmList", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                String shared=mySharedPreferences.getString("list","");
//                System.out.println("alarmList:"+shared);
                List<AlarmListItem> alarmList=new ArrayList<>();
                AlarmListItem alarm=new AlarmListItem(tag,frequency,game,ring,Hour,Minute);
                System.out.println("add alarm:"+alarm.getTag());
                if(!shared.equals("") &&shared!=null){
                    alarmList=JSONObject.parseArray(shared,AlarmListItem.class);
                }
                if(position!=-1){
                    alarmList.remove(position);
                }
                alarmList.add(alarm);
                shared=JSONObject.toJSONString(alarmList);
                editor.putString("list",shared);
                editor.apply();
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences mySharedPreferences = getSharedPreferences("clock",
                Activity.MODE_PRIVATE);
        tag = mySharedPreferences.getString("tag", tag);
        Button tagSetting = findViewById(R.id.ClockTagSetting);
//        if(tag.equals("")){
//            tag=this.getString(R.string.alarm_list_title);
//        }
//        System.out.println("tag:"+tag);
        tagSetting.setText(tag);

        game = mySharedPreferences.getString("game",game);
//        if(game==null||game.equals("")){
//            game=this.getString(R.string.clock_setting_game_default);
//        }
//        System.out.println("game:"+game);
        Button gameSetting=findViewById(R.id.GameSetting);
        gameSetting.setText(game);

        ring = mySharedPreferences.getString("ring",ring);
//        if(ring==null||ring.equals("")){
//            ring=getString((R.string.clock_setting_ring_default));
//        }
//        System.out.println("game:"+game);
        Button ringSetting=findViewById(R.id.ClockRingSetting);
        ringSetting.setText(ring);

        frequency = mySharedPreferences.getString("frequency",frequency);
//        if(frequency==null||frequency.equals("")){
//            frequency=getString(R.string.clock_setting_repeat_select);
//        }
//        System.out.println("game:"+game);
        Button repeatBtn=findViewById(R.id.ClockRepeatButton);
        repeatBtn.setText(frequency);


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