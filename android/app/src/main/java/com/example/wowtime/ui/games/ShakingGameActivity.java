package com.example.wowtime.ui.games;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.service.Credit;
import com.example.wowtime.util.InternetConstant;
import com.example.wowtime.util.SensorManagerHelper;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ShakingGameActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private ProgressBar progressBar;
    private TextView progressValue;

    Timer timer;
    TimerTask timerTask;

    boolean ring;

    private Long t1 = System.currentTimeMillis(), t2;
    private int shakeCount = 0;

    private static final int SHAKE_INTERVAL = 250;
    private static final int TOTAL_TIME = 10000;
    private static final int NEED_COUNT = TOTAL_TIME/SHAKE_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shaking_game_activity);

        mp = MediaPlayer.create(this, R.raw.radar);
        mp.setLooping(true);
        mp.start();

        progressBar = findViewById(R.id.progress1);
        progressValue = findViewById(R.id.progress_value1);
        SensorManagerHelper sensorHelper = new SensorManagerHelper(this);

        ring=true;
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                ring=!ring;
                if(ring){
                    System.out.println("ring on");
                    mp.start();
                }
                else{
                    System.out.println("ring stop");
                    mp.pause();
                }
            }
        };
        timer.schedule(timerTask,60*1000,60*1000);

        sensorHelper.setOnShakeListener(() -> {
            if (shakeCount == NEED_COUNT)
            {
                mp.stop();
                Credit credit=new Credit();
                credit.modifyCredit(InternetConstant.alarm_credit);
                ShakingGameActivity.this.finish();
            }
            t2 = System.currentTimeMillis();
            if (t2-t1>=SHAKE_INTERVAL) {
                t1 = t2;
                shakeCount++;
                progressBar.setProgress(100*shakeCount/NEED_COUNT);
                progressValue.setText(new StringBuffer().append(progressBar.getProgress()).append("%"));
                System.out.println(shakeCount);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timerTask.cancel();
    }
}