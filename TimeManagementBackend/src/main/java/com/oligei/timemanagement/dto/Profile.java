package com.oligei.timemanagement.dto;

import com.oligei.timemanagement.entity.UserNeo4j;

public class Profile {

    private Integer userId;
    private String username;
    private String userIcon;
    private String phone;
    private String email;

    public Profile() {}
    public Profile(UserNeo4j userNeo4j) {
        this.userId = Integer.valueOf(userNeo4j.getUserId());
        this.username = userNeo4j.getUsername();
        this.userIcon = userNeo4j.getUserIcon();
    }
    public Profile(Integer userId, String username, String userIcon, String phone, String email) {
        this.userId = userId;
        this.username = username;
        this.userIcon = userIcon;
        this.phone = phone;
        this.email = email;
    }

    public Integer getUserId() {return userId;}
    public String getUsername() {return username;}
    public String getUserIcon() {return userIcon;}
    public String getPhone() {return phone;}
    public String getEmail() {return email;}
}
