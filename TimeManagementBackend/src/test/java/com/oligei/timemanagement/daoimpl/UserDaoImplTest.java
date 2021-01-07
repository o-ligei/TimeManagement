package com.oligei.timemanagement.daoimpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.entity.User;
import com.oligei.timemanagement.entity.UserMongoDB;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.factories.UserFactory;
import com.oligei.timemanagement.factories.UserMongoDBFactory;
import com.oligei.timemanagement.factories.UserNeo4jFactory;
import com.oligei.timemanagement.repository.UserMongoDBRepository;
import com.oligei.timemanagement.repository.UserNeo4jRepository;
import com.oligei.timemanagement.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMongoDBRepository userMongoDBRepository;

    @MockBean
    private UserNeo4jRepository userNeo4jRepository;

    private static String phone = "13300000000";
    private static User user;

    private static Integer userId = 1;
    private static UserMongoDB userMongoDB;
    private static UserNeo4j userNeo4j;

    private static String username = "ziliuziliu";
    private static UserNeo4j userNeo4j2;

    @BeforeAll
    static public void buildInstance() {
        user = UserFactory.buildUserByPhone(phone);
        user.setUserId(userId);
        userMongoDB = UserMongoDBFactory.buildUserMongoDBByUserId(userId);
        userNeo4j = UserNeo4jFactory.buildUserNeo4jByUserId(userId.toString());
        userNeo4j2 = UserNeo4jFactory.buildUserNeo4jByUsername(username);
    }

    @Test
    void getUserByPhone() {
        when(userRepository.getUserByPhone(phone)).thenReturn(user);
        assertThrows(NullPointerException.class, () -> userDao.getUserByPhone(null));
        assertEquals(phone, userDao.getUserByPhone(phone).getPhone());
    }

    @Test
    void save1() {
        when(userRepository.save(user)).thenReturn(user);
        when(userMongoDBRepository.save(any())).thenReturn(null);
        when(userNeo4jRepository.save(any())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> userDao.save(null ,null));
        assertNotNull(userDao.save(user, true));
        assertNotNull(userDao.save(user, false));
    }

    @Test
    void save2() {
        when(userMongoDBRepository.save(userMongoDB)).thenReturn(userMongoDB);
        assertThrows(NullPointerException.class, () -> userDao.save((UserMongoDB) null));
        assertNotNull(userDao.save(userMongoDB));
    }

    @Test
    void save3() {
        when(userNeo4jRepository.save(userNeo4j)).thenReturn(userNeo4j);
        assertThrows(NullPointerException.class, () -> userDao.save((UserNeo4j) null));
        assertNotNull(userDao.save(userNeo4j));
    }

    @Test
    void getUserMongoDBByUserId() {
        when(userMongoDBRepository.getUserMongoDBByUserId(userId)).thenReturn(userMongoDB);
        assertThrows(NullPointerException.class, () -> userDao.getUserMongoDBByUserId(null));
        assertEquals(userId, userDao.getUserMongoDBByUserId(userId).getUserId());
    }

    @Test
    void getUserByUserId() {
        when(userRepository.getUserByUserId(userId)).thenReturn(user);
        assertThrows(NullPointerException.class, () -> userDao.getUserByUserId(null));
        assertNotNull(userDao.getUserByUserId(userId));
    }

    @Test
    void getUserNeo4jByUserId() {
        when(userNeo4jRepository.getUserNeo4jByUserId(userId.toString())).thenReturn(userNeo4j);
        assertThrows(NullPointerException.class, () -> userDao.getUserNeo4jByUserId(null));
        assertEquals(userId, Integer.valueOf(userDao.getUserNeo4jByUserId(userId).getUserId()));
    }

    @Test
    void getUserNeo4jsByUsername() {
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        userNeo4js.add(userNeo4j2);
        when(userNeo4jRepository.searchByUsername(username)).thenReturn(userNeo4js);
        assertThrows(NullPointerException.class, () -> userDao.getUserNeo4jsByUsername(null));
        assertEquals(1, userDao.getUserNeo4jsByUsername(username).size());
    }

    @Test
    void getFriendsList() {
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        userNeo4js.add(userNeo4j);
        userNeo4js.add(userNeo4j2);
        when(userNeo4jRepository.getFriendsList(userId.toString())).thenReturn(userNeo4js);
        assertThrows(NullPointerException.class, () -> userDao.getFriendsList(null));
        assertEquals(2, userDao.getFriendsList(userId).size());
    }

    @Test
    void getAllUsers() {
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        userNeo4js.add(userNeo4j);
        userNeo4js.add(userNeo4j2);
        when(userNeo4jRepository.getAllUsers()).thenReturn(userNeo4js);
        assertEquals(2, userDao.getAllUsers().size());
    }
}
