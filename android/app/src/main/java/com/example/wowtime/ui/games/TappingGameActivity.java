package com.example.wowtime.ui.games;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.example.wowtime.R;
import com.example.wowtime.adapter.TappingGameAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class TappingGameActivity extends AppCompatActivity {

    private Integer INTERVAL = 1500;
    private Integer TOTAL_ROUND = 11;
    private Integer NEED_GREEN = 20;

    private Integer getGreenCount = 0;
    private Boolean gameStarted = false;

    private Set<Integer> green = new HashSet<>();
    private Set<Integer> red = new HashSet<>();
    private Set<Integer> clicked = new HashSet<>();

    private void paramSetting() {
        SharedPreferences calculateSettingPreference =
                PreferenceManager.getDefaultSharedPreferences(this);
        TOTAL_ROUND = Integer.parseInt(calculateSettingPreference.getString("tap_round", "11"));
        System.out.println("round: " + TOTAL_ROUND);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tapping_game_activity);
        paramSetting();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 12; i++) { arrayList.add(Integer.toString(i)); }
        TappingGameAdapter adapter = new TappingGameAdapter(arrayList, getApplicationContext());
        GridView gridView = findViewById(R.id.TappingGameTable);
        TextView getGreen = findViewById(R.id.GetGreen);
        gridView.setAdapter(adapter);

        class GameThread extends Thread {

            @Override
            public void run() {
                try {
                    for (Integer i = 1; i <= TOTAL_ROUND; i++) {
                        Thread.sleep(INTERVAL);
                        runOnUiThread(() -> {
                            generateColorPosition();
                            clicked.clear();
                            adapter.setSelection(green, red);
                            adapter.notifyDataSetChanged();
                        });
                    }
                    Thread.sleep(INTERVAL);
                    runOnUiThread(() -> {
                        if (getGreenCount > NEED_GREEN) {
                            getGreen.setText(R.string.tapping_game_success);
                        } else { getGreen.setText(R.string.tapping_game_failure); }
                        gameStarted = false;
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> getGreen.setText(R.string.tapping_game_failure));
                    gameStarted = false;
                }
            }

            void generateColorPosition() {
                green.clear();
                red.clear();
                Random random = new Random();
                for (int i = 1; i <= 3; i++) {
                    while (true) {
                        int num = random.nextInt() % 12;
                        num = (num < 0) ? -num : num;
                        if (green.contains(num)) { continue; }
                        green.add(num);
                        break;
                    }
                }
                for (int i = 1; i <= 3; i++) {
                    while (true) {
                        int num = random.nextInt() % 12;
                        num = (num < 0) ? -num : num;
                        if (green.contains(num) || red.contains(num)) { continue; }
                        red.add(num);
                        break;
                    }
                }
            }
        }
        GameThread gameThread = new GameThread();

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (gameStarted) {
                if (clicked.contains(position)) { return; }
                clicked.add(position);
                if (red.contains(position)) { gameThread.interrupt(); }
                if (green.contains(position)) {
                    getGreenCount++;
                    runOnUiThread(() -> getGreen.setText(getGreenCount.toString()));
                    adapter.setWhite(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        Button startButton = findViewById(R.id.tapStartButton);
        startButton.setOnClickListener(v -> {
            runOnUiThread(() -> getGreen.setText(R.string.tapping_game_rule));
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> getGreen.setText(R.string.tapping_game_start));
                    getGreenCount = 0;
                    gameStarted = true;
                    gameThread.start();
                }
            };
            timer.schedule(timerTask, 5000);
            startButton.setEnabled(false);
        });

        Button refreshButton = findViewById(R.id.tapRefreshButton);
        refreshButton.setOnClickListener(v -> recreate());
    }
}
