package com.example.wowtime.ui.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.example.wowtime.R;

import java.io.IOException;

public class AlarmPlay extends AppCompatActivity {
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_play);
        Intent intent=getIntent();
        String ring=intent.getStringExtra("ring");
        int resId=getResources().getIdentifier("radar.mp3","raw",getPackageName());
//        mp=MediaPlayer.create(this,R.raw.radar);
        mp=MediaPlayer.create(this,resId);
        mp.start();
    }
}