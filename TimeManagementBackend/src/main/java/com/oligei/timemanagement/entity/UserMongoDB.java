package com.oligei.timemanagement.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document(collection = "tm_users")
public class UserMongoDB {

    @Id
    @Field("userid")
    private Integer userId;

    @Field("username")
    private String username;

    @Field("credit")
    private Credit credit;

    public UserMongoDB() {}
    public UserMongoDB(Integer userId, String username, Credit credit) {
        this.userId = userId;
        this.username = username;
        this.credit = credit;
    }

    public Integer getUserId() {return userId;}
    public void setUserId(Integer userId) {this.userId = userId;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public Credit getCredit() {return credit;}
    public void setCredit(Credit credit) {this.credit = credit;}
}
