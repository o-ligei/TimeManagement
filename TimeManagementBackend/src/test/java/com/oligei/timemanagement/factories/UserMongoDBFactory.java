package com.oligei.timemanagement.factories;

import com.oligei.timemanagement.entity.Credit;
import com.oligei.timemanagement.entity.UserMongoDB;

public class UserMongoDBFactory {

    public static UserMongoDB buildUserMongoDBByUserId(Integer userId) {
        return new UserMongoDB(userId, "username", new Credit());
    }
}
