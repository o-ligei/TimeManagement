package com.oligei.timemanagement.serviceimpl;

import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.service.SocialService;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SocialServiceImpl implements SocialService {

    @Autowired
    private UserDao userDao;

    @Override
    public Msg<List<Profile>> getProfile(String username) {
        Objects.requireNonNull(username, "null username --SocialServiceImpl getProfile");
        List<UserNeo4j> userNeo4js = userDao.getUserNeo4jsByUsername(username);
        List<Profile> profiles = new ArrayList<>();
        for (UserNeo4j userNeo4j:userNeo4js) profiles.add(new Profile(userNeo4j));
        return new Msg<>(MsgCode.SUCCESS, profiles);
    }

    @Override
    public Msg<List<Profile>> getFriendsList(Integer userId) {
        Objects.requireNonNull(userId, "null userId --SocialServiceImpl getFriendsList");
        List<UserNeo4j> userNeo4js = userDao.getFriendsList(userId);
        List<Profile> profiles = new ArrayList<>();
        for (UserNeo4j userNeo4j:userNeo4js) profiles.add(new Profile(userNeo4j));
        return new Msg<>(MsgCode.SUCCESS, profiles);
    }
}
