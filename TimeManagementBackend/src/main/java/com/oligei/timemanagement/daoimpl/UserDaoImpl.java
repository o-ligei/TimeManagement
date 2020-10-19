package com.oligei.timemanagement.daoimpl;

import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.entity.Credit;
import com.oligei.timemanagement.entity.User;
import com.oligei.timemanagement.entity.UserMongoDB;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.repository.UserMongoDBRepository;
import com.oligei.timemanagement.repository.UserNeo4jRepository;
import com.oligei.timemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMongoDBRepository userMongoDBRepository;

    @Autowired
    private UserNeo4jRepository userNeo4jRepository;

    @Override
    public User getUserByPhone(String phone) {
        Objects.requireNonNull(phone, "null phone --UserDaoImpl getUserByPhone");
        return userRepository.getUserByPhone(phone);
    }

    @Override
    public User save(User user, Boolean new_user) {
        Objects.requireNonNull(user, "null user --UserDaoImpl save");
        Objects.requireNonNull(new_user, "null new_user --UserDaoImpl save");
        if (new_user) {
            User saved_user = userRepository.save(user);
            UserMongoDB userMongoDB = new UserMongoDB(saved_user.getUserId(), saved_user.getUsername(), new Credit());
            UserNeo4j userNeo4j = new UserNeo4j(saved_user.getUserId().toString(), saved_user.getUsername(), null);
            userMongoDBRepository.save(userMongoDB);
            userNeo4jRepository.save(userNeo4j);
            return saved_user;
        }
        else return userRepository.save(user);
    }

    @Override
    public UserMongoDB save(UserMongoDB userMongoDB) {
        Objects.requireNonNull(userMongoDB, "null userMongoDB --UserDaoImpl save");
        return userMongoDBRepository.save(userMongoDB);
    }

    @Override
    public UserMongoDB getUserMongoDBByUserId(Integer userId) {
        Objects.requireNonNull(userId, "null userId --UserDaoImpl userId");
        return userMongoDBRepository.getUserMongoDBByUserId(userId);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        Objects.requireNonNull(userId, "null userId --UserDaoImpl getUserByUserId");
        return userRepository.getUserByUserId(userId);
    }

    @Override
    public UserNeo4j getUserNeo4jByUserId(Integer userId) {
        Objects.requireNonNull(userId, "null userId --UserDaoImpl getUserNeo4jByUserId");
        return userNeo4jRepository.getUserNeo4jByUserId(userId.toString());
    }

    @Override
    public List<UserNeo4j> getUserNeo4jsByUsername(String username) {
        Objects.requireNonNull(username, "null username --UserDaoImpl getUserNeo4jsByUsername");
        return userNeo4jRepository.getUserNeo4jsByUsername(username);
    }

    @Override
    public List<UserNeo4j> getFriendsList(Integer userId) {
        Objects.requireNonNull(userId, "null userId --UserDaoImpl getFriendsList");
        return userNeo4jRepository.getFriendsList(userId.toString());
    }
}
