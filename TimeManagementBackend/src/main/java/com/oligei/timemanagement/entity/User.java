package com.oligei.timemanagement.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "tm_users")
@Validated
public class User {

    private Integer userId;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String type;
    private String userIcon;
    private Credit credit;

    public User() {}

    public User(String username, String phone, String password, String type) {
        this.username = username;
        this.email = null;
        this.phone = phone;
        this.password = password;
        this.type = type;
    }

    @Id
    @Column(name = "userid")
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {this.userId = userId;}

    @Column(name = "username")
    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    @Column(name = "email")
    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    @Column(name = "phone")
    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    @Column(name = "password")
    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    @Column(name = "type")
    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    @Transient
    public String getUserIcon() {return userIcon;}

    public void setUserIcon(String userIcon) {this.userIcon = userIcon;}

    @Transient
    public Credit getCredit() {return credit;}

    public void setCredit(Credit credit) {this.credit = credit;}
}
