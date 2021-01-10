package com.oligei.timemanagement.entity;

import java.sql.Timestamp;
import java.util.Random;
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

    @Property(name = "record")
    private float[] record = new float[24];

    public UserNeo4j() {}

    public UserNeo4j(String userId, String username, String userIcon) {
        this.userId = userId;
        this.username = username;
        this.userIcon = userIcon;
        Random random = new Random();
        for (int i = 0; i < 24; i++) { record[i] = random.nextInt(3); }
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUserId() {return userId;}

    public void setUserId(String userId) {this.userId = userId;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getUserIcon() {return userIcon;}

    public void setUserIcon(String userIcon) {this.userIcon = userIcon;}

    public float[] getRecord() {return record;}

    public void setRecord(float[] record) {this.record = record;}

    public void saveRecord(Timestamp timestamp) {
        int hour = timestamp.getHours();
        record[hour] = record[hour] + 1;
    }
}
