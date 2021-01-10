package com.example.wowtime.dto;

public class FriendsListItem {

    private Integer userId;
    private String userIcon;
    private String username;
    private String phone;
    private String email;

    public FriendsListItem() {
    }

    public FriendsListItem(Integer userId, String userIcon, String username) {
        this.userId = userId;
        this.userIcon = userIcon;
        this.username = username;
        this.phone = null;
        this.email = null;
    }

    public FriendsListItem(Integer userId, String userIcon, String username, String phone,
            String email) {
        this.userId = userId;
        this.userIcon = userIcon;
        this.username = username;
        this.phone = phone;
        this.email = email;
    }

    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {this.userId = userId;}

    public String getUserIcon() {return userIcon;}

    public void setUserIcon(String userIcon) {this.userIcon = userIcon;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}