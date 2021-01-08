package com.oligei.timemanagement.factories;

import com.oligei.timemanagement.entity.UserNeo4j;

public class UserNeo4jFactory {

    public static UserNeo4j buildUserNeo4jByUserId(String userId) {
        return new UserNeo4j(userId, "test"+userId, "icon");
    }

    public static UserNeo4j buildUserNeo4jByUsername(String username) {
        return new UserNeo4j("10", username, "icon");
    }
}
