package com.example.wowtime.ui.pomodoro;

import android.os.Bundle;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wowtime.R;


public class PomodoroSettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro_setting_activity);

        TimePicker timePicker=findViewById(R.id.PomodorotimePicker);
        timePicker.setIs24HourView(true);
    }
}
