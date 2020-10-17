package com.oligei.timemanagement.entity;

public class Credit {

    private Integer score = 0;

    public Credit() {}
    public Credit(Integer score) {this.score = score;}

    public Integer getScore() {return score;}
    public void setScore(Integer score) {this.score = score;}

    public void addScore(Integer earn) {this.score += earn;}
}
