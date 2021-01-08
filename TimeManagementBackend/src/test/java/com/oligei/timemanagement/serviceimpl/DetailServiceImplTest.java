package com.oligei.timemanagement.serviceimpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.oligei.timemanagement.dao.DetailDao;
import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.entity.UserMongoDB;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.factories.UserMongoDBFactory;
import com.oligei.timemanagement.factories.UserNeo4jFactory;
import com.oligei.timemanagement.service.DetailService;
import com.oligei.timemanagement.utils.msgutils.MsgConstant;
import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DetailServiceImplTest {

    @Autowired
    private DetailService detailService;

    @MockBean
    private DetailDao detailDao;

    @MockBean
    private UserDao userDao;

    @Test
    void saveDetail() {
        Integer userId = 1;
        String event = "event";
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        UserNeo4j userNeo4j = UserNeo4jFactory.buildUserNeo4jByUserId(userId.toString());
        when(detailDao.save(any())).thenReturn(null);
        when(userDao.getUserNeo4jByUserId(userId)).thenReturn(userNeo4j);
        when(userDao.save((UserNeo4j)any())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> detailService.saveDetail(null, null, null));
        assertEquals(MsgConstant.SUCCESS, detailService.saveDetail(userId, event, currentTime.toString()).getStatus());
        verify(detailDao).save(argThat(detail -> detail.getUserId().equals(userId)));
        verify(userDao).getUserNeo4jByUserId(userId);
        verify(userDao).save((UserNeo4j)any());
    }

    @Test
    void addScore() {
        Integer userId = 1;
        Integer earn = 100;
        UserMongoDB userMongoDB = UserMongoDBFactory.buildUserMongoDBByUserId(userId);
        when(userDao.getUserMongoDBByUserId(userId)).thenReturn(userMongoDB);
        when(userDao.save((UserMongoDB)any())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> detailService.addScore(null, null));
        assertEquals(MsgConstant.SUCCESS, detailService.addScore(userId, earn).getStatus());
        verify(userDao).getUserMongoDBByUserId(userId);
        verify(userDao).save((UserMongoDB)any());
    }

    @Test
    void getDetail() {
        Integer userId = 1;
        String timestamp1 = "whole";
        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        when(detailDao.getDetailsByUserId(userId)).thenReturn(null);
        when(detailDao.getDetailsByUserIdAndTimestampGreaterThanEqual(userId, timestamp2)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> detailService.getDetail(null, null));
        assertEquals(MsgConstant.SUCCESS, detailService.getDetail(userId, timestamp1).getStatus());
        verify(detailDao).getDetailsByUserId(userId);
        assertEquals(MsgConstant.SUCCESS, detailService.getDetail(userId, timestamp2.toString()).getStatus());
        verify(detailDao).getDetailsByUserIdAndTimestampGreaterThanEqual(userId, timestamp2);
    }
}
