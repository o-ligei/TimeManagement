package com.example.wowtime.ui.games;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.wowtime.R;
import com.example.wowtime.util.SensorManagerHelper;

public class ShakingGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shaking_game_activity);
        SensorManagerHelper sensorHelper = new SensorManagerHelper(this);
        sensorHelper.setOnShakeListener(() -> Toast.makeText(ShakingGameActivity.this, "你在摇哦", Toast.LENGTH_SHORT).show());
    }
}