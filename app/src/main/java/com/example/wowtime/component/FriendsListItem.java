package com.example.wowtime.component;

public class FriendsListItem {

    private Integer userId;
    private String userIcon;
    private String username;

    public FriendsListItem() {
    }

    public FriendsListItem (Integer userId, String userIcon, String username){
        this.userId = userId;
        this.userIcon = userIcon;
        this.username = username;
    }

    public Integer getUserId() {return userId;}
    public void setUserId(Integer userId) {this.userId = userId;}
    public String getUserIcon() {return userIcon;}
    public void setUserIcon(String userIcon) {this.userIcon = userIcon;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
}