package com.example.wowtime.dto;


public class AlarmListItem {
    private String tag;
    private String frequency;
    private String game;
    private String ring;
    private int hour;
    private int minute;

    public AlarmListItem(){
    }

    public AlarmListItem(String tag,String frequency,String game,String ring,int hour,int minute){
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

    public String getFrequency(){
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


}
