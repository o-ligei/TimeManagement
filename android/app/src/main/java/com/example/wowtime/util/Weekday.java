package com.example.wowtime.util;

import java.util.HashMap;
import java.util.Map;

public class Weekday {

    Map<Integer, String> days = new HashMap<Integer, String>();

    public Weekday(boolean flag) {
        if(flag) {
            days.put(0, "无重复");
            days.put(1, "一");
            days.put(2, "二");
            days.put(3, "三");
            days.put(4, "四");
            days.put(5, "五");
            days.put(6, "六");
            days.put(7, "日");
        }
        else{
            days.put(0,"one time");
            days.put(1,"Monday");
            days.put(2,"Tuesday");
            days.put(3,"Wednesday");
            days.put(4,"Thursday");
            days.put(5,"Friday");
            days.put(6,"Saturday");
            days.put(7,"Sunday");
        }
    }

    public String getDay(int dayNumber) {
        return days.get(dayNumber);
    }
}
