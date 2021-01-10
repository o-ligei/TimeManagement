package com.example.wowtime.ui.games;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.example.wowtime.R;
import com.example.wowtime.ui.alarm.TaskSuccessActivity;
import java.util.Random;

public class CalculateGameActivity extends AppCompatActivity {

    int magnitude = 3;
    int cnt = 4;
    char[] op = {'+', '*'};
    int answer;

    private void paramSetting() {
        SharedPreferences calculateSettingPreference =
                PreferenceManager.getDefaultSharedPreferences(this);
        magnitude = Integer
                .parseInt(calculateSettingPreference.getString("calculate_magnitude", "3"));
        cnt = Integer.parseInt(calculateSettingPreference.getString("calculate_cnt", "4"));
        System.out.println("magnitude: " + magnitude);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_game_activity);
        Random random = new Random();
        paramSetting();
        int min = (int) Math.pow(10.0, magnitude - 1), max = (int) Math.pow(10.0, magnitude);
        int left = random.nextInt(max - min) + min;
        int right = random.nextInt(9) + 1;
        int NumberOp = random.nextInt(2);
        switch (NumberOp) {
            case 0:
                answer = left + right;
                break;
            case 1:
                answer = left * right;
                break;
        }
        @SuppressLint("DefaultLocale") String message = String
                .format("%d %c %d", left, op[NumberOp], right);
        TextView title = findViewById(R.id.calculate_title);
        title.setText(message);
    }

    public void judgeAnswer(View view) {
        EditText editText = findViewById(R.id.calculate_answer);
        String message = editText.getText().toString();
        int user_answer = Integer.parseInt(message);
        if (answer == user_answer) {
            Intent intent = new Intent(CalculateGameActivity.this, TaskSuccessActivity.class);
            startActivity(intent);

//            AlertDialog successAlertDialog = new AlertDialog.Builder(this)
//                .setTitle("成功了")
//                .setMessage("成功了")
//                .setIcon(R.mipmap.ic_launcher)
//                .create();
//            successAlertDialog.show();
        }
//        else {
//            AlertDialog failureAlertDialog = new AlertDialog.Builder(this)
//                .setTitle("就这？")
//                .setMessage("就这？")
//                .setIcon(R.mipmap.ic_launcher)
//                .create();
//            failureAlertDialog.show();
//        }
    }
}