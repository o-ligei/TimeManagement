package com.oligei.timemanagement.serviceimpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.oligei.timemanagement.dao.FriendDao;
import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.dto.FriendAlarmMsg;
import com.oligei.timemanagement.entity.AskNeo4j;
import com.oligei.timemanagement.entity.FollowNeo4j;
import com.oligei.timemanagement.entity.SetNeo4j;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.factories.AskNeo4jFactory;
import com.oligei.timemanagement.factories.FollowNeo4jFactory;
import com.oligei.timemanagement.factories.SetNeo4jFactory;
import com.oligei.timemanagement.factories.UserNeo4jFactory;
import com.oligei.timemanagement.service.SocialService;
import com.oligei.timemanagement.utils.msgutils.MsgConstant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SocialServiceImplTest {

    @Autowired
    private SocialService socialService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private FriendDao friendDao;

    @Test
    void getProfile() {
        String username = "username";
        Integer userId1 = 5, userId2 = 10;
        UserNeo4j userNeo4j = UserNeo4jFactory.buildUserNeo4jByUsername(username);
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        userNeo4js.add(userNeo4j);
        when(userDao.getUserNeo4jsByUsername(username)).thenReturn(userNeo4js);
        assertThrows(NullPointerException.class, () -> socialService.getProfile(null, null));
        assertEquals(1, socialService.getProfile(userId1, username).getData().size());
        assertEquals(0, socialService.getProfile(userId2, username).getData().size());
    }

    @Test
    void getFriendsList() {
        Integer userId = 1;
        UserNeo4j userNeo4j = UserNeo4jFactory.buildUserNeo4jByUserId("5");
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        userNeo4js.add(userNeo4j);
        when(friendDao.getFriendsList(userId)).thenReturn(userNeo4js);
        assertThrows(NullPointerException.class, () -> socialService.getFriendsList(null));
        assertEquals(MsgConstant.SUCCESS, socialService.getFriendsList(userId).getStatus());
    }

    @Test
    void addFriend() {
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        for (int i=0;i<=5;i++)
            userNeo4js.add(UserNeo4jFactory.buildUserNeo4jByUserId(String.valueOf(i)));
        FollowNeo4j followNeo4j = FollowNeo4jFactory.buildFollowNeo4jByNode(userNeo4js.get(1), userNeo4js.get(2));
        AskNeo4j askNeo4j1 = AskNeo4jFactory.buildAskNeo4jByNode(userNeo4js.get(1), userNeo4js.get(3)),
                askNeo4j2 = AskNeo4jFactory.buildAskNeo4jByNode(userNeo4js.get(4), userNeo4js.get(1));
        when(friendDao.getFollowRelation(1,2)).thenReturn(followNeo4j);
        when(friendDao.getAskRelation(1,3)).thenReturn(askNeo4j1);
        when(friendDao.getAskRelation(4,1)).thenReturn(askNeo4j2);
        when(friendDao.addAskRelation(1,5)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> socialService.addFriend(null, null));
        assertEquals(MsgConstant.FOUND_YOURSELF, socialService.addFriend(1, 1).getStatus());
        assertEquals(MsgConstant.ALREADY_FRIEND, socialService.addFriend(1, 2).getStatus());
        assertEquals(MsgConstant.ALREADY_SEND_FRIEND_REQUEST, socialService.addFriend(1,3).getStatus());
        assertEquals(MsgConstant.ALREADY_SEND_FRIEND_REQUEST, socialService.addFriend(1,4).getStatus());
        assertEquals(MsgConstant.SUCCESS, socialService.addFriend(1,5).getStatus());
    }

    @Test
    void getFriendRequest() {
        Integer userId = 1;
        UserNeo4j userNeo4j = UserNeo4jFactory.buildUserNeo4jByUserId("10");
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        userNeo4js.add(userNeo4j);
        when(friendDao.getFriendRequest(userId)).thenReturn(userNeo4js);
        assertThrows(NullPointerException.class, () -> socialService.getFriendRequest(null));
        assertEquals(1, socialService.getFriendRequest(userId).getData().size());
    }

    @Test
    void acceptFriend() {
        Integer from = 1, to = 2;
        doNothing().when(friendDao).deleteAskRelation(to, from);
        when(friendDao.addFollowRelation(from, to)).thenReturn(null);
        when(friendDao.addFollowRelation(to, from)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> socialService.acceptFriend(null, null));
        assertEquals(MsgConstant.FOUND_YOURSELF, socialService.acceptFriend(from, from).getStatus());
        assertEquals(MsgConstant.SUCCESS, socialService.acceptFriend(from, to).getStatus());
        verify(friendDao).deleteAskRelation(to, from);
        verify(friendDao).addFollowRelation(from, to);
        verify(friendDao).addFollowRelation(to, from);
    }

    @Test
    void saveAlarmForFriend() {
        Integer from = 1, to = 2;
        FriendAlarmMsg friendAlarmMsg = new FriendAlarmMsg();
        when(friendDao.saveAlarmForFriend(from, to, friendAlarmMsg)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> socialService.saveAlarmForFriend(null, null, null));
        assertEquals(MsgConstant.SUCCESS, socialService.saveAlarmForFriend(from, to, friendAlarmMsg).getStatus());
        verify(friendDao).saveAlarmForFriend(from, to, friendAlarmMsg);
    }

    @Test
    void getAlarmRequest() {
        Integer userId = 1;
        UserNeo4j userNeo4j1 = UserNeo4jFactory.buildUserNeo4jByUserId("1"),
                userNeo4j2 = UserNeo4jFactory.buildUserNeo4jByUserId("2");
        SetNeo4j setNeo4j = SetNeo4jFactory.buildSetNeo4jByNode(userNeo4j1, userNeo4j2);
        List<SetNeo4j> setNeo4js = new ArrayList<>();
        setNeo4js.add(setNeo4j);
        when(friendDao.getAlarmRequest(userId)).thenReturn(setNeo4js);
        assertThrows(NullPointerException.class, () -> socialService.getAlarmRequest(null));
        assertEquals(1, socialService.getAlarmRequest(userId).getData().size());
    }

    @Test
    void testKMeans() {
        Integer userId = 10;
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        for (int i=0;i<=16;i++)
            userNeo4js.add(UserNeo4jFactory.buildUserNeo4jByUserId(String.valueOf(i)));
        when(userDao.getAllUsers()).thenReturn(userNeo4js);
        when(userDao.getUserNeo4jByUserId(any())).thenReturn(userNeo4js.get(userId));
        assertEquals(MsgConstant.SUCCESS, socialService.kMeans().getStatus());
        assertThrows(NullPointerException.class, () -> socialService.recommendFriend(null));
        assertEquals(MsgConstant.SUCCESS, socialService.recommendFriend(userId).getStatus());
    }
}
