package com.oligei.timemanagement.service;

import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.utils.msgutils.Msg;

import java.util.List;

public interface SocialService {
    Msg<List<Profile>> getProfile(String username);
    Msg<List<Profile>> getFriendsList(Integer userId);
    Msg<Boolean> addFriend(Integer from, Integer to);
    Msg<List<Profile>> getFriendRequest(Integer userId);
    Msg<Boolean> acceptFriend(Integer from, Integer to);
}
