package com.oligei.timemanagement.dto;

public class FriendAlarmMsg {

    private String username;
    private String clockSetting;

    public FriendAlarmMsg() {}

    public FriendAlarmMsg(String username, String clockSetting) {
        this.username = username;
        this.clockSetting = clockSetting;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getClockSetting() {return clockSetting;}

    public void setClockSetting(String clockSetting) {this.clockSetting = clockSetting;}
}
