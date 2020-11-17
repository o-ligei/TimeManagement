package com.example.wowtime.ui.games;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wowtime.R;
import com.example.wowtime.adapter.OptionGameAdapter;
import com.example.wowtime.dto.OptionGameItem;
import com.example.wowtime.dto.Question;
import java.util.ArrayList;
import java.util.Collections;

public class OptionGameActivity extends AppCompatActivity {

    private ArrayList<Question> addQuestions() {
        ArrayList<Question> questions = new ArrayList<>();

        Question question1 = new Question("resemblance", "A");
        question1.addOption("A", "相像");
        question1.addOption("B", "组建");
        question1.addOption("C", "聚合");
        question1.addOption("D", "装配");
        questions.add(question1);

        Question question2 = new Question("convict", "C");
        question2.addOption("A", "认错");
        question2.addOption("B", "承诺");
        question2.addOption("C", "定罪");
        question2.addOption("D", "咨询");
        questions.add(question2);

        Question question3 = new Question("chestnut", "D");
        question3.addOption("A", "杏子");
        question3.addOption("B", "核桃");
        question3.addOption("C", "胡桃");
        question3.addOption("D", "栗子");
        questions.add(question3);

        Question question4 = new Question("commemorate", "B");
        question4.addOption("A", "记得");
        question4.addOption("B", "纪念");
        question4.addOption("C", "怀念");
        question4.addOption("D", "记忆");
        questions.add(question4);

        Question question5 = new Question("friction", "C");
        question5.addOption("A", "碎片");
        question5.addOption("B", "小说");
        question5.addOption("C", "摩擦");
        question5.addOption("D", "吵架");
        questions.add(question5);

        return questions;
    }

    private Integer QUESTION_NUM = 5;
    private Integer INTERVAL = 10000;
    private Integer PROGRESSBAR_ACCURACY = 40;
    private Integer correct = 0;
    private Boolean continueFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_game_activity);
        TextView questionView = findViewById(R.id.option_game_question);
        ListView options = findViewById(R.id.option_game);
        Button next = findViewById(R.id.option_game_next);
        ProgressBar progressBar = findViewById(R.id.progress1);
        TextView progressValue = findViewById(R.id.progress_value1);
        progressValue.setVisibility(View.GONE);
        ArrayList<Question> questions = addQuestions();
        Collections.shuffle(questions);
        new Thread(() -> {
            LOOP:
            for (int i = 0; i < QUESTION_NUM; i++) {
                try {
                    continueFlag = false;
                    Question question = questions.get(i);
                    ArrayList<OptionGameItem> optionGameItems = question.getOptions();
                    OptionGameAdapter optionGameAdapter = new OptionGameAdapter(optionGameItems,
                                                                                getApplicationContext());
                    runOnUiThread(() -> {
                        questionView.setText(question.getQuestion());
                        options.setAdapter(optionGameAdapter);
                        options.setOnItemClickListener((parent, view, position, id) -> {
                            optionGameAdapter.setChoose(position);
                            optionGameAdapter.setGreen(position);
                            optionGameAdapter.notifyDataSetChanged();
                        });
                        next.setOnClickListener(v -> {
                            if (question.verifyCorrect(optionGameAdapter.getChoose())) {
                                correct++;
                            }
                            continueFlag = true;
                        });
                    });
                    Thread.sleep(INTERVAL / PROGRESSBAR_ACCURACY * 2);
                    for (int j = 1; j <= PROGRESSBAR_ACCURACY; j++) {
                        progressBar.setProgress(100 * j / PROGRESSBAR_ACCURACY);
                        if (continueFlag) { continue LOOP; }
                        Thread.sleep(INTERVAL / PROGRESSBAR_ACCURACY);
                    }
                    Thread.sleep(INTERVAL / PROGRESSBAR_ACCURACY * 2);
                    if (question.verifyCorrect(optionGameAdapter.getChoose())) { correct++; }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                Intent intent = new Intent(OptionGameActivity.this, OptionGameResultActivity.class);
                intent.putExtra("QUESTION_NUM", QUESTION_NUM.toString());
                intent.putExtra("CORRECT", correct.toString());
                startActivity(intent);
            });
        }).start();
    }
}