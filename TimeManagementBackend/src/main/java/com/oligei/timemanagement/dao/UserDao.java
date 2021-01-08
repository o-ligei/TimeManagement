package com.oligei.timemanagement.dao;

import com.oligei.timemanagement.entity.User;
import com.oligei.timemanagement.entity.UserMongoDB;
import com.oligei.timemanagement.entity.UserNeo4j;

import java.util.List;

public interface UserDao {

    User getUserByPhone(String phone);

    User getUserByUserId(Integer userId);

    User save(User user, Boolean new_user);

    UserMongoDB getUserMongoDBByUserId(Integer userId);

    UserMongoDB save(UserMongoDB userMongoDB);

    UserNeo4j save(UserNeo4j userNeo4j);

    UserNeo4j getUserNeo4jByUserId(Integer userId);

    List<UserNeo4j> getUserNeo4jsByUsername(String username);

    List<UserNeo4j> getFriendsList(Integer userId);

    List<UserNeo4j> getAllUsers();
}
