package com.example.wowtime.ui.games;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.wowtime.R;
import com.example.wowtime.adapter.OptionGameAdapter;
import com.example.wowtime.dto.EnglishWord;
import com.example.wowtime.dto.OptionGameItem;
import com.example.wowtime.dto.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OptionGameActivity extends AppCompatActivity {

    private Integer QUESTION_NUM = 5;
    private Integer INTERVAL = 20000;
    private Integer PROGRESSBAR_ACCURACY = 80;
    private Integer correct = 0;
    private volatile Integer phase = 0;

    private ArrayList<EnglishWord> englishWords = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();

    private void loadDictionary() {
        try {
            InputStream is = getResources().getAssets().open("dict.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                englishWords.add(JSON.parseObject(line, EnglishWord.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(englishWords.size());
    }

    private void loadQuestions() {
        Random random = new Random();
        for (int i = 0; i < QUESTION_NUM; i++) {
            int questionType = random.nextInt(2);
            int chosenWord = random.nextInt(15000) + 20;
            int correct = random.nextInt(4);
            if (questionType == 0) {
                Question question = new Question(englishWords.get(chosenWord).getWord(),
                                                 String.valueOf((char) ('A' + correct)));
                Set<Integer> chosenBias = new HashSet<>();
                chosenBias.add(0);
                for (int j = 0; j < 4; j++) {
                    String opt = String.valueOf((char) ('A' + j));
                    if (j == correct) {
                        question.addOption(opt, englishWords.get(chosenWord).getType());
                    } else {
                        Integer bias = random.nextInt(41) - 20;
                        while (chosenBias.contains(bias)) { bias = random.nextInt(41) - 20; }
                        chosenBias.add(bias);
                        question.addOption(opt, englishWords.get(chosenWord + bias).getType());
                    }
                }
                questions.add(question);
            } else {
                Question question = new Question(englishWords.get(chosenWord).getType(),
                                                 String.valueOf((char) ('A' + correct)));
                Set<Integer> chosenBias = new HashSet<>();
                chosenBias.add(0);
                for (int j = 0; j < 4; j++) {
                    String opt = String.valueOf((char) ('A' + j));
                    if (j == correct) {
                        question.addOption(opt, englishWords.get(chosenWord).getWord());
                    } else {
                        Integer bias = random.nextInt(41) - 20;
                        while (chosenBias.contains(bias)) { bias = random.nextInt(41) - 20; }
                        chosenBias.add(bias);
                        question.addOption(opt, englishWords.get(chosenWord + bias).getWord());
                    }
                }
                questions.add(question);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_game_activity);

        TextView questionView = findViewById(R.id.option_game_question);
        ListView options = findViewById(R.id.option_game);
        Button next = findViewById(R.id.option_game_next);
        Button nextQuestion = findViewById(R.id.option_game_next_question);
        ProgressBar progressBar = findViewById(R.id.progress1);
        TextView progressValue = findViewById(R.id.progress_value1);
        progressValue.setVisibility(View.GONE);

        loadDictionary();
        loadQuestions();

        new Thread(() -> {
            LOOP:
            for (int i = 0; i < QUESTION_NUM; i++) {
                try {
                    phase = 0;
                    Question question = questions.get(i);
                    ArrayList<OptionGameItem> optionGameItems = question.getOptions();
                    OptionGameAdapter optionGameAdapter = new OptionGameAdapter(optionGameItems,
                                                                                getApplicationContext());

                    runOnUiThread(() -> {
                        next.setVisibility(View.VISIBLE);
                        nextQuestion.setVisibility(View.GONE);
                        questionView.setText(question.getQuestion());
                        options.setAdapter(optionGameAdapter);
                        options.setOnItemClickListener((parent, view, position, id) -> {
                            optionGameAdapter.setChoose(position);
                            optionGameAdapter.setGray(position);
                            optionGameAdapter.notifyDataSetChanged();
                        });
                        next.setOnClickListener(v -> phase = 1);
                    });
                    Thread.sleep(INTERVAL / PROGRESSBAR_ACCURACY * 2);
                    for (int j = 1; j <= PROGRESSBAR_ACCURACY; j++) {
                        progressBar.setProgress(100 * j / PROGRESSBAR_ACCURACY);
                        if (phase == 1) { break; }
                        Thread.sleep(INTERVAL / PROGRESSBAR_ACCURACY);
                    }

                    phase = 2;
                    if (question.verifyCorrect(optionGameAdapter.getChoose())) { correct++; }

                    runOnUiThread(() -> {
                        while (phase != 2) { ; }
                        next.setVisibility(View.GONE);
                        nextQuestion.setVisibility(View.VISIBLE);
                        String chosenStr = optionGameAdapter.getChoose();
                        int chosenOpt = chosenStr.equals("") ? -1 : chosenStr.charAt(0) - 'A';
                        int correctOpt = question.getAnswer().charAt(0) - 'A';
                        optionGameAdapter.setRed(chosenOpt);
                        optionGameAdapter.setGreen(correctOpt);
                        optionGameAdapter.notifyDataSetChanged();
                        nextQuestion.setOnClickListener(v -> phase = 3);
                    });
                    Thread.sleep(INTERVAL / PROGRESSBAR_ACCURACY * 2);
                    for (int j = 1; j <= PROGRESSBAR_ACCURACY * 100; j++) {
                        if (phase == 3) { continue LOOP; }
                        Thread.sleep(INTERVAL / PROGRESSBAR_ACCURACY);
                    }
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