package com.example.wowtime.ui.games;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;

public class OptionGameResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_game_result_activity);
        String result = "";
        result += getIntent().getStringExtra("CORRECT");
        result += "/";
        result += getIntent().getStringExtra("QUESTION_NUM");
        TextView resultView = findViewById(R.id.option_game_result);
        resultView.setText(result);
        Button button=findViewById(R.id.option_game_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
