package com.example.wowtime.ui.games;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.util.SensorManagerHelper;

public class ShakingGameActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressValue;

    private Long t1 = System.currentTimeMillis(), t2;
    private int shakeCount = 0;

    private static final int SHAKE_INTERVAL = 250;
    private static final int TOTAL_TIME = 10000;
    private static final int NEED_COUNT = TOTAL_TIME/SHAKE_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shaking_game_activity);
        progressBar = findViewById(R.id.progress1);
        progressValue = findViewById(R.id.progress_value1);
        SensorManagerHelper sensorHelper = new SensorManagerHelper(this);
        sensorHelper.setOnShakeListener(() -> {
            if (shakeCount == NEED_COUNT) return;
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
}