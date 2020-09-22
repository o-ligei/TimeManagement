package com.example.wowtime.component;


public class AlarmListItem {
    private String tag;
    private String time;

    public AlarmListItem(){

    }
    public AlarmListItem(String tag,String time){
        this.tag=tag;
        this.time=time;
    }

    public String getTag(){
        return this.tag;
    }

    public String getTime(){
        return this.time;
    }
}
