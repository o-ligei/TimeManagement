package com.example.wowtime.ui.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.wowtime.R;

public class ClockSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_setting_activity);
        Button gameSetting=findViewById(R.id.button8);
        gameSetting.setOnClickListener(v->startActivity(new Intent(ClockSettingActivity.this, TaskListActivity.class)));
        Button ringSetting=findViewById(R.id.ClockRingSetting);
        ringSetting.setOnClickListener(v->startActivity(new Intent(ClockSettingActivity.this, RingSettingActivity.class)));
    }

}