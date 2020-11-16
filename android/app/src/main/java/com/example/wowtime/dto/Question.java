package com.example.wowtime.dto;

import java.util.ArrayList;

public class Question {

    private String question = "";
    private ArrayList<OptionGameItem> options = new ArrayList<>();
    private String answer = "";

    public Question() {}
    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {return question;}
    public void setQuestion(String question) {this.question = question;}
    public ArrayList<OptionGameItem> getOptions() {return options;}
    public void setOptions(ArrayList<OptionGameItem> options) {this.options = options;}
    public String getAnswer() {return answer;}
    public void setAnswer(String answer) {this.answer = answer;}

    public void addOption(String opt, String statement) {options.add(new OptionGameItem(opt, statement));}
    public Boolean verifyCorrect(String clientAnswer) {return clientAnswer.equals(answer);}
}
