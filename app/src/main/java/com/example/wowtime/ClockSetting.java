package com.example.wowtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ClockSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_setting);
        Button gameSetting=findViewById(R.id.button8);
        gameSetting.setOnClickListener(v->startActivity(new Intent(ClockSetting.this,TaskListActivity.class)));
        Button ringSetting=findViewById(R.id.ClockRingSetting);
        ringSetting.setOnClickListener(v->startActivity(new Intent(ClockSetting.this,RingSetting.class)));
    }

}