package com.example.wowtime.ui.games;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.ui.alarm.TaskSuccessActivity;

public class RandomNumberGameActivity extends AppCompatActivity {
    int digit = 3;
    StringBuilder targetNumber = new StringBuilder();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_number_game_activity);
        TextView input = findViewById(R.id.input_number);
        input.setText("");
        for(int i = digit; i > 0 ; i--){
            targetNumber.append((int) (Math.random() * 10));
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
        Button[] btn = { btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9};
        int []number = new int [10];
        int start = (int)(Math.random() * 10);
        for(int i = 0 ; i < 9 ; i++){
            if(i + start <= 9)
                number[i] = i + start;
            else
                number[i] = i + start - 9;
        }
        int j;
        for(j = 0; j < 9; j++){
            btn[j].setText(String.valueOf(number[j]));
            int finalJ = j;
            btn[j].setOnClickListener(v->input.append(btn[finalJ].getText()));
        }
        Button handson_btn = findViewById(R.id.number_handson);
        handson_btn.setOnClickListener(v -> {
            int target = Integer.parseInt(String.valueOf(targetNumber));
            int answer = Integer.parseInt(input.getText().toString());
            if(answer == target) {
                Intent intent = new Intent(RandomNumberGameActivity.this, TaskSuccessActivity.class);
                startActivity(intent);
            }
            else
                input.setText("");
        });

    }
}