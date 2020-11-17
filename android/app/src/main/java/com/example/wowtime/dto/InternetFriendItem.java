package com.example.wowtime.dto;

public class InternetFriendItem {

    private Integer userId;
    private String userIcon;
    private String username;

    public InternetFriendItem(Integer userId, String userIcon, String username) {
        this.userIcon = userIcon;
        this.userId = userId;
        this.username = username;
    }

    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {this.userId = userId;}

    public String getUserIcon() {return userIcon;}

    public void setUserIcon(String userIcon) {this.userIcon = userIcon;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}
}
