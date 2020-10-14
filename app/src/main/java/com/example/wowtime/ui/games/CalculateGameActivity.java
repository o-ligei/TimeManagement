package com.example.wowtime.ui.games;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wowtime.R;
import com.example.wowtime.ui.alarm.TaskSuccessActivity;

public class CalculateGameActivity extends AppCompatActivity {
    int magnitude = 2;
    int min = (int)Math.pow(10.0,magnitude - 1),max = (int)Math.pow(10.0,magnitude);
    char[] op = {'+','-','*'};
    int answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_game_activity);
        int left = (int)(1+Math.random()*(max - min + 1)),
                right = (int)(1+Math.random()*(max - min + 1)),
                NumberOp = (int)(1+Math.random()*(3));
        switch (NumberOp){
            case 0:answer=left+right;break;
            case 1:answer=left-right;break;
            case 2:answer=left*right;break;
        }
        @SuppressLint("DefaultLocale") String message = String.format("%d %c %d",left,op[NumberOp],right);
        TextView title = findViewById(R.id.calculate_title);
        title.setText(message);
    }

    public void judgeAnswer(View view){
        EditText editText = findViewById(R.id.calculate_answer);
        String message = editText.getText().toString();
        int user_answer = Integer.parseInt(message);
        if(answer == user_answer) {
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