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
}
