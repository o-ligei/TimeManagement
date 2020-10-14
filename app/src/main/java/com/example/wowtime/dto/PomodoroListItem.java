package com.example.wowtime.dto;

public class PomodoroListItem {
    private String name;
    private String gap;

    public PomodoroListItem(){}

    public PomodoroListItem(String name,String gap){
        this.name = name;
        this.gap = gap;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getGap(){return gap;}
    public void setGap(String gap){this.gap = gap;}

}
