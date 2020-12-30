package com.oligei.timemanagement.factories;

import com.oligei.timemanagement.entity.FollowNeo4j;
import com.oligei.timemanagement.entity.UserNeo4j;

public class FollowNeo4jFactory {

    public static FollowNeo4j buildFollowNeo4jByNode(UserNeo4j me, UserNeo4j friend) {
        return new FollowNeo4j(me, friend);
    }
}
