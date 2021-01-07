package com.example.wowtime.dto;

public class EnglishWord {

    private String type;
    private String word;
    private String sent;

    public EnglishWord() {}

    public EnglishWord(String type, String word, String sent) {
        this.type = type;
        this.word = word;
        this.sent = sent;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getWord() {return word;}

    public void setWord(String word) {this.word = word;}

    public String getSent() {return sent;}

    public void setSent(String sent) {this.sent = sent;}
}
