package com.oligei.timemanagement.dao;

import com.oligei.timemanagement.dto.FriendAlarmMsg;
import com.oligei.timemanagement.entity.AskNeo4j;
import com.oligei.timemanagement.entity.FollowNeo4j;
import com.oligei.timemanagement.entity.SetNeo4j;
import com.oligei.timemanagement.entity.UserNeo4j;

import java.util.List;

public interface FriendDao {

    List<UserNeo4j> getFriendsList(Integer userId);

    FollowNeo4j getFollowRelation(Integer from, Integer to);

    AskNeo4j getAskRelation(Integer from, Integer to);

    AskNeo4j addAskRelation(Integer from, Integer to);

    List<UserNeo4j> getFriendRequest(Integer userId);

    void deleteAskRelation(Integer from, Integer to);

    FollowNeo4j addFollowRelation(Integer from, Integer to);

    SetNeo4j saveAlarmForFriend(Integer friend, Integer to, FriendAlarmMsg friendAlarmMsg);

    List<SetNeo4j> getAlarmRequest(Integer userId);
}
