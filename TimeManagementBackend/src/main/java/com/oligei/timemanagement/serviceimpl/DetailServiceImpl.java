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
    public Msg<Boolean> saveDetail(Integer userId, String event, String timestamp, Integer earn) {
        Objects.requireNonNull(userId, "null userid --DetailServiceImpl saveDetail");
        Objects.requireNonNull(event, "null event --DetailServiceImpl saveDetail");
        Objects.requireNonNull(timestamp, "null timestamp --DetailServiceImpl saveDetail");
        Objects.requireNonNull(earn, "null earn --DetailServiceImpl saveDetail");

        detailDao.save(new Detail(userId, event, FormatUtil.strToTimestamp(timestamp)));
        UserMongoDB userMongoDB = userDao.getUserMongoDBByUserId(userId);
        userMongoDB.getCredit().addScore(earn);
        userDao.save(userMongoDB);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<List<Detail>> getDetail(Integer userId) {
        Objects.requireNonNull(userId, "null userId --DetailServiceImpl getDetail");
        return new Msg<>(MsgCode.SUCCESS, detailDao.getDetailsByUserId(userId));
    }
}
