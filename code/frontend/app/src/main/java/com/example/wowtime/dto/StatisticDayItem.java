package com.example.wowtime.dto;

import java.util.Date;

public class StatisticDayItem {

    String name;
    int hour;
    int minute;
    Date begin;
    Date end;

    public StatisticDayItem() {

    }

    public StatisticDayItem(String n, int h, int m, Date b, Date e) {
        name = n;
        hour = h;
        minute = m;
        begin = b;
        end = e;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getName() {
        return name;
    }
}
