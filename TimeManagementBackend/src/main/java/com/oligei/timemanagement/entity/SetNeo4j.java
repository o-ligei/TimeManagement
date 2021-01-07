package com.oligei.timemanagement.entity;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "SET")
public class SetNeo4j {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private UserNeo4j me;

    @EndNode
    private UserNeo4j friend;

    @Property(name = "username")
    private String username;

    @Property(name = "clocksetting")
    private String clockSetting;

    public SetNeo4j() {}

    public SetNeo4j(UserNeo4j me, UserNeo4j friend, String username, String clockSetting) {
        this.me = me;
        this.friend = friend;
        this.username = username;
        this.clockSetting = clockSetting;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public UserNeo4j getMe() {return me;}

    public void setMe(UserNeo4j me) {this.me = me;}

    public UserNeo4j getFriend() {return friend;}

    public void setFriend(UserNeo4j friend) {this.friend = friend;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getClockSetting() {return clockSetting;}

    public void setClockSetting(String clockSetting) {this.clockSetting = clockSetting;}
}
