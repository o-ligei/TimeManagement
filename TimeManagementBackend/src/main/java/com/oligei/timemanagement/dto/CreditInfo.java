package com.oligei.timemanagement.dto;

import com.oligei.timemanagement.entity.Credit;
import com.oligei.timemanagement.entity.UserMongoDB;

public class CreditInfo {

    private Integer userId;
    private String username;
    private Credit credit;

    public CreditInfo() {}

    public CreditInfo(UserMongoDB userMongoDB) {
        this.userId = userMongoDB.getUserId();
        this.username = userMongoDB.getUsername();
        this.credit = userMongoDB.getCredit();
    }

    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {this.userId = userId;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public Credit getCredit() {return credit;}

    public void setCredit(Credit credit) {this.credit = credit;}
}
