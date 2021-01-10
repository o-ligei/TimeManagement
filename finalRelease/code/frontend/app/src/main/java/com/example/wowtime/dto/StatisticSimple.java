package com.example.wowtime.dto;

import java.util.Calendar;

public class StatisticSimple {

    private float hour;
    Calendar day;

    public StatisticSimple() {

    }

    public StatisticSimple(float h, Calendar d) {
        hour = h;
        day = d;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public void setDay(Calendar day) {
        this.day = day;
    }

    public Calendar getDay() {
        return day;
    }

    public float getHour() {
        return hour;
    }
}
