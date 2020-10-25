package com.example.wowtime.dto;

public class PomodoroListItem {
    private String name;
    private int totalGap;
    private int workGap;
    private int restGap;
    private int mode;

    public PomodoroListItem(){}

    public PomodoroListItem(String name,int totalGap,int workGap,int restGap,int mode){
        this.name = name;
        this.totalGap=totalGap;
        this.workGap=workGap;
        this.restGap=restGap;
        this.mode=mode;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getTotalGap() {
        return totalGap;
    }
    public void setTotalGap(int totalGap) {
        this.totalGap = totalGap;
    }

    public int getWorkGap() {
        return workGap;
    }
    public void setWorkGap(int workGap) {
        this.workGap = workGap;
    }

    public int getRestGap() {
        return restGap;
    }
    public void setRestGap(int restGap) {
        this.restGap = restGap;
    }
}
