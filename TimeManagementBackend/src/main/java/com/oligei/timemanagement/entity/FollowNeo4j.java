package com.oligei.timemanagement.entity;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "FOLLOW")
public class FollowNeo4j {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private UserNeo4j me;

    @EndNode
    private UserNeo4j friend;

    public FollowNeo4j() {}
    public FollowNeo4j(UserNeo4j me, UserNeo4j friend) {
        this.me = me;
        this.friend = friend;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public UserNeo4j getMe() {return me;}
    public void setMe(UserNeo4j me) {this.me = me;}

    public UserNeo4j getFriend() {return friend;}
    public void setFriend(UserNeo4j friend) {this.friend = friend;}
}
