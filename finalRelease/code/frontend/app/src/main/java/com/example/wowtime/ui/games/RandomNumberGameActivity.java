package com.example.wowtime.ui.games;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.example.wowtime.R;
import com.example.wowtime.ui.alarm.TaskSuccessActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomNumberGameActivity extends AppCompatActivity {

    int digit = 15;
    StringBuilder targetNumber = new StringBuilder();

    private void paramSetting() {
        SharedPreferences randomSettingPreference =
                PreferenceManager.getDefaultSharedPreferences(this);
        digit = Integer.parseInt(randomSettingPreference.getString("random_digit", "15"));
        System.out.println("digit: " + digit);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_number_game_activity);
        paramSetting();
        TextView input = findViewById(R.id.input_number);
        input.setText("");
        for (int i = digit; i > 0; i--) {
            Random random = new Random();
            targetNumber.append(random.nextInt(9) + 1);
        }
        TextView targetText = findViewById(R.id.target_number);
        targetText.setText(targetNumber.toString());
        Button btn_1 = findViewById(R.id.random_button_2);
        Button btn_2 = findViewById(R.id.random_button_7);
        Button btn_3 = findViewById(R.id.random_button_4);
        Button btn_4 = findViewById(R.id.random_button_5);
        Button btn_5 = findViewById(R.id.random_button_3);
        Button btn_6 = findViewById(R.id.random_button_1);
        Button btn_7 = findViewById(R.id.random_button_8);
        Button btn_8 = findViewById(R.id.random_button_6);
        Button btn_9 = findViewById(R.id.random_button_9);
        Button[] btn = {btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9};
        ArrayList<Integer> number = new ArrayList<>(9);
        for (int i = 1; i <= 9; i++) { number.add(i); }
        Collections.shuffle(number);
        int j;
        for (j = 0; j < 9; j++) {
            btn[j].setText(String.valueOf(number.get(j)));
            int finalJ = j;
            btn[j].setOnClickListener(v -> input.append(btn[finalJ].getText()));
        }
        Button handson_btn = findViewById(R.id.number_handson);
        handson_btn.setOnClickListener(v -> {
            String target = String.valueOf(targetNumber);
            String answer = input.getText().toString();
            if (answer.equals(target)) {
                Intent intent = new Intent(RandomNumberGameActivity.this,
                                           TaskSuccessActivity.class);
                startActivity(intent);
            } else { input.setText(""); }
        });

    }
}