package com.example.wowtime.ui.alarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;

public class TagSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_setting);
        Intent intent = getIntent();
        String tag = intent.getStringExtra("tag");
        TextView textView = findViewById(R.id.ClockTag);
        textView.setText(tag);
    }

    @Override
    @SuppressLint("CommitPrefEdits")
    protected void onPause() {
        super.onPause();
        SharedPreferences mySharedPreferences = getSharedPreferences("clock",
                                                                     Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        TextView text = findViewById(R.id.ClockTag);
//        System.out.println(text.getText().toString());
        editor.putString("tag", text.getText().toString());
        editor.apply();
    }
}