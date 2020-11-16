package com.oligei.timemanagement.serviceimpl;

import com.oligei.timemanagement.dao.DetailDao;
import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.entity.Detail;
import com.oligei.timemanagement.entity.UserMongoDB;
import com.oligei.timemanagement.service.DetailService;
import com.oligei.timemanagement.utils.FormatUtil;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
public class DetailServiceImpl implements DetailService {

    @Autowired
    private DetailDao detailDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Msg<Boolean> saveDetail(Integer userId, String event, String timestamp) {
        Objects.requireNonNull(userId, "null userId --DetailServiceImpl saveDetail");
        Objects.requireNonNull(event, "null event --DetailServiceImpl saveDetail");
        Objects.requireNonNull(timestamp, "null timestamp --DetailServiceImpl saveDetail");
        detailDao.save(new Detail(userId, event, FormatUtil.strToTimestamp(timestamp)));
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<Boolean> addScore(Integer userId, Integer earn) {
        Objects.requireNonNull(userId, "null userId --DetailServiceImpl addScore");
        Objects.requireNonNull(earn, "null earn --DetailServiceImpl addScore");
        UserMongoDB userMongoDB = userDao.getUserMongoDBByUserId(userId);
        userMongoDB.getCredit().addScore(earn);
        userDao.save(userMongoDB);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<List<Detail>> getDetail(Integer userId, String timestamp) {
        Objects.requireNonNull(userId, "null userId --DetailServiceImpl getDetail");
        Objects.requireNonNull(timestamp, "null timestamp --DetailServiceImpl getDetail");
        if (timestamp.equals("whole")) return new Msg<>(MsgCode.SUCCESS, detailDao.getDetailsByUserId(userId));
        return new Msg<>(MsgCode.SUCCESS, detailDao.getDetailsByUserIdAndTimestampGreaterThanEqual(userId, FormatUtil.strToTimestamp(timestamp)));
    }
}
