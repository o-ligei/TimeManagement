package com.oligei.timemanagement.factories;

import com.oligei.timemanagement.entity.User;

public class UserFactory {

    public static User buildUserByPhone(String phone) {
        return new User("user", phone, "password", "type");
    }
}
