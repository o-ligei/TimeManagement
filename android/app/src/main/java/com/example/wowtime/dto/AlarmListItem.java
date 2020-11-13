package com.example.wowtime.dto;


import java.util.ArrayList;
import java.util.List;

public class AlarmListItem {
    private String tag;
    private List<Boolean> frequency;
    private String game;
    private String ring;
    private int hour;
    private int minute;

    public AlarmListItem(){
    }

    public AlarmListItem(String tag, List<Boolean> frequency, String game, String ring, int hour, int minute){
        this.tag=tag;
        this.frequency=frequency;
        this.game=game;
        this.ring=ring;
        this.hour=hour;
        this.minute=minute;
    }

    public String getTag(){
        return this.tag;
    }

    public List<Boolean> getFrequency(){
        return this.frequency;
    }

    public String getGame(){
        return this.game;
    }

    public String getRing(){
        return this.ring;
    }

    public int getHour(){
        return this.hour;
    }

    public int getMinute(){
        return this.minute;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFrequency(ArrayList<Boolean> frequency) {
        this.frequency = frequency;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

}
