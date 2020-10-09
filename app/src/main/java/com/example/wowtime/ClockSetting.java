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
        Button button=findViewById(R.id.button8);
        button.setOnClickListener(v->startActivity(new Intent(ClockSetting.this,TaskListActivity.class)));
    }
}