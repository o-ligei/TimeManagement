package com.oligei.timemanagement.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "tm_users")
public class UserNeo4j {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "userid")
    private String userId;

    @Property(name = "username")
    private String username;

    @Property(name = "usericon")
    private String userIcon;

    public UserNeo4j() {}
    public UserNeo4j(String userId, String username, String userIcon) {
        this.userId = userId;
        this.username = username;
        this.userIcon = userIcon;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getUserIcon() {return userIcon;}
    public void setUserIcon(String userIcon) {this.userIcon = userIcon;}
}
