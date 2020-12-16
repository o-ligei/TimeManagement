package com.oligei.timemanagement.serviceimpl;

import com.oligei.timemanagement.dao.FriendDao;
import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.dto.FriendAlarmMsg;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.entity.AskNeo4j;
import com.oligei.timemanagement.entity.SetNeo4j;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.service.SocialService;
import com.oligei.timemanagement.utils.clusterutils.Cluster;
import com.oligei.timemanagement.utils.clusterutils.KMeans;
import com.oligei.timemanagement.utils.clusterutils.Point;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SocialServiceImpl implements SocialService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private FriendDao friendDao;

    private Set<Cluster> clusterSet = new HashSet<>();

    @Override
    public Msg<List<Profile>> getProfile(Integer myId, String username) {
        Objects.requireNonNull(myId, "null myId --SocialServiceImpl getProfile");
        Objects.requireNonNull(username, "null username --SocialServiceImpl getProfile");
        List<UserNeo4j> userNeo4js = userDao.getUserNeo4jsByUsername(username);
        List<Profile> profiles = new ArrayList<>();
        for (UserNeo4j userNeo4j : userNeo4js) {
            if (!userNeo4j.getUserId().equals(String.valueOf(myId))) { profiles.add(new Profile(userNeo4j)); }
        }
        return new Msg<>(MsgCode.SUCCESS, profiles);
    }

    @Override
    public Msg<List<Profile>> getFriendsList(Integer userId) {
        Objects.requireNonNull(userId, "null userId --SocialServiceImpl getFriendsList");
        List<UserNeo4j> userNeo4js = friendDao.getFriendsList(userId);
        List<Profile> profiles = new ArrayList<>();
        for (UserNeo4j userNeo4j : userNeo4js) { profiles.add(new Profile(userNeo4j)); }
        return new Msg<>(MsgCode.SUCCESS, profiles);
    }

    @Override
    public Msg<Boolean> addFriend(Integer from, Integer to) {
        Objects.requireNonNull(from, "null from --SocialServiceImpl addFriend");
        Objects.requireNonNull(to, "null to --SocialServiceImpl addFriend");
        if (from.equals(to)) { return new Msg<>(MsgCode.FOUND_YOURSELF); }
        if (friendDao.getFollowRelation(from, to) != null) { return new Msg<>(MsgCode.ALREADY_FRIEND); }
        if (friendDao.getAskRelation(from, to) != null) { return new Msg<>(MsgCode.ALREADY_SEND_FRIEND_REQUEST); }
        if (friendDao.getAskRelation(to, from) != null) { return new Msg<>(MsgCode.ALREADY_SEND_FRIEND_REQUEST); }
        friendDao.addAskRelation(from, to);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<List<Profile>> getFriendRequest(Integer userId) {
        Objects.requireNonNull(userId, "null userId --SocialServiceImpl getFriendRequest");
        List<UserNeo4j> userNeo4js = friendDao.getFriendRequest(userId);
        List<Profile> profiles = new ArrayList<>();
        for (UserNeo4j userNeo4j : userNeo4js) { profiles.add(new Profile(userNeo4j)); }
        return new Msg<>(MsgCode.SUCCESS, profiles);
    }

    @Override
    public Msg<Boolean> acceptFriend(Integer from, Integer to) {
        Objects.requireNonNull(from, "null from --SocialServiceImpl acceptFriend");
        Objects.requireNonNull(to, "null to --SocialServiceImpl acceptFriend");
        if (from.equals(to)) { return new Msg<>(MsgCode.FOUND_YOURSELF); }
        friendDao.deleteAskRelation(to, from);
        friendDao.addFollowRelation(from, to);
        friendDao.addFollowRelation(to, from);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<Boolean> saveAlarmForFriend(Integer from, Integer to, FriendAlarmMsg friendAlarmMsg) {
        Objects.requireNonNull(from, "null from --SocialServiceImpl saveAlarmForFriend");
        Objects.requireNonNull(to, "null to --SocialServiceImpl saveAlarmForFriend");
        Objects.requireNonNull(friendAlarmMsg, "null friendAlarmMsg --SocialServiceImpl saveAlarmForFriend");
        friendDao.saveAlarmForFriend(from, to, friendAlarmMsg);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<List<FriendAlarmMsg>> getAlarmRequest(Integer userId) {
        Objects.requireNonNull(userId, "null userId --SocialServiceImpl getAlarmRequest");
        List<SetNeo4j> setNeo4js = friendDao.getAlarmRequest(userId);
        List<FriendAlarmMsg> friendAlarmMsgs = new ArrayList<>();
        for (SetNeo4j setNeo4j : setNeo4js) {
            friendAlarmMsgs.add(new FriendAlarmMsg(setNeo4j.getUsername(), setNeo4j.getClockSetting()));
        }
        return new Msg<>(MsgCode.SUCCESS, friendAlarmMsgs);
    }

    @Override
    public Msg<Boolean> kMeans() {
        List<UserNeo4j> users = userDao.getAllUsers();
        KMeans kMeans = new KMeans(8, users);
        clusterSet = kMeans.run();
        for (Cluster cluster : clusterSet) {
            System.out.println(cluster);
        }
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<List<Profile>> recommendFriend(Integer userId) {
        Objects.requireNonNull(userId, "null userId --SocialServiceImpl recommendFriend");
        Cluster clusterForUser = new Cluster();
        for (Cluster cluster : clusterSet) {
            if (cluster.getMembers().containsKey(userId)) {
                clusterForUser = cluster;
                break;
            }
        }
        List<Profile> profiles = new ArrayList<>();
        int cnt = 0;
        for (Point point : clusterForUser.getMembers().values()) {
            if (point.getId() == userId) { continue; }
            profiles.add(new Profile(userDao.getUserNeo4jByUserId(point.getId())));
            if (cnt >= 10) { break; }
            cnt++;
        }
        return new Msg<>(MsgCode.SUCCESS, profiles);
    }
}
