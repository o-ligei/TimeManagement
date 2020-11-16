package com.example.wowtime.ui.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.wowtime.R;

public class SleepAssistSetting extends AppCompatActivity {
    private int Hour;
    private int Minute;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_assist_setting);

        flag=false;
        Minute=0;
        Hour=0;

        TimePicker timePicker=findViewById(R.id.alarmtimePicker);
        timePicker.setVisibility(View.INVISIBLE);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Hour=hourOfDay;
                Minute=minute;
                System.out.println(Hour+":"+Minute);
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch trigger = findViewById(R.id.SleepAssistSwitch);
        trigger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    timePicker.setVisibility(View.VISIBLE);
                    flag=true;
                }
                else{
                    timePicker.setVisibility(View.INVISIBLE);
                    flag=false;
                }
            }
        });
    }

    @Override
    @SuppressLint("CommitPrefEdits")
    protected void onPause() {
        super.onPause();
        SharedPreferences mySharedPreferences= getSharedPreferences("clock", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("sleepFlag", flag);
        editor.putInt("sleepHour",Hour);
        editor.putInt("sleepMinute",Minute);
        editor.apply();
    }
}