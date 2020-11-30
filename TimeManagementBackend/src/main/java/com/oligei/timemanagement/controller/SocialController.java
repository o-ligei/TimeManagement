package com.oligei.timemanagement.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oligei.timemanagement.TimemanagementApplication;
import com.oligei.timemanagement.dto.FriendAlarmMsg;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.exceptions.NotOnlineException;
import com.oligei.timemanagement.service.SocialService;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import com.oligei.timemanagement.utils.msgutils.MsgConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/Social")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SocialController {

    @Autowired
    private SocialService socialService;

    private static final Logger logger = LoggerFactory.getLogger(TimemanagementApplication.class);

    @RequestMapping("/GetProfile")
    public Msg<List<Profile>> getProfile(@RequestParam(name = "userid") Integer myId,
            @RequestParam(name = "username") String username) {
        return socialService.getProfile(myId, username);
    }

    @RequestMapping("/GetFriendsList")
    public Msg<List<Profile>> getFriendsList(@RequestParam(name = "userid") Integer userId) {
        return socialService.getFriendsList(userId);
    }

    @RequestMapping("/AddFriend")
    public Msg<Boolean> addFriend(@RequestParam(name = "from") Integer from,
            @RequestParam(name = "to") Integer to) {
        try {
            Msg<Boolean> msg = socialService.addFriend(from, to);
            if (msg.getStatus() == MsgConstant.ALREADY_FRIEND
                    || msg.getStatus() == MsgConstant.ALREADY_SEND_FRIEND_REQUEST
                    || msg.getStatus() == MsgConstant.FOUND_YOURSELF) { return msg; }
            Msg<Boolean> request = new Msg<>(MsgCode.NEW_FRIEND_REQUEST);
            WebSocketController.sendMessage((JSONObject) JSONObject.toJSON(request), to.toString());
            return msg;
        } catch (IOException e) {
            logger.error("IOException", e);
            return new Msg<>(MsgCode.CONNECTION_FAILURE);
        } catch (NotOnlineException e) {
            logger.error("NotOnlineException", e);
            return new Msg<>(MsgCode.SUCCESS);
        }
    }

    @RequestMapping("/GetFriendRequest")
    public Msg<List<Profile>> getFriendRequest(@RequestParam(name = "userid") Integer userId) {
        return socialService.getFriendRequest(userId);
    }

    @RequestMapping("/AcceptFriend")
    public Msg<Boolean> acceptFriend(@RequestParam(name = "from") Integer from,
            @RequestParam(name = "to") Integer to) {
        try {
            Msg<Boolean> msg = socialService.acceptFriend(from, to);
            Msg<Boolean> newFriend = new Msg<>(MsgCode.NEW_FRIEND);
            WebSocketController.sendMessage((JSONObject) JSONObject.toJSON(newFriend), from.toString());
            WebSocketController.sendMessage((JSONObject) JSONObject.toJSON(newFriend), to.toString());
            return msg;
        } catch (IOException e) {
            logger.error("IOException", e);
            return new Msg<>(MsgCode.CONNECTION_FAILURE);
        } catch (NotOnlineException e) {
            logger.error("NotOnlineException", e);
            return new Msg<>(MsgCode.SUCCESS);
        }
    }

    @RequestMapping("/SetAlarmForFriend")
    public Msg<Boolean> setAlarmForFriend(@RequestParam(name = "from") Integer from,
            @RequestParam(name = "to") Integer to,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "clocksetting") String clockSetting) {
        FriendAlarmMsg friendAlarmMsg = new FriendAlarmMsg(username, clockSetting);
        Msg<FriendAlarmMsg> msg = new Msg<>(MsgCode.NEW_FRIEND_ALARM, friendAlarmMsg);
        try {
            WebSocketController.sendMessage((JSONObject) JSONObject.toJSON(msg), to.toString());
            return new Msg<>(MsgCode.SUCCESS);
        } catch (NotOnlineException e) {
            logger.error("NotOnlineException", e);
            socialService.saveAlarmForFriend(from, to, friendAlarmMsg);
            return new Msg<>(MsgCode.SUCCESS);
        } catch (IOException e) {
            logger.error("IOException", e);
            return new Msg<>(MsgCode.CONNECTION_FAILURE);
        }
    }

    @RequestMapping("/GetAlarmRequest")
    public Msg<List<FriendAlarmMsg>> getAlarmRequest(@RequestParam(name = "userid") Integer userId) {
        return socialService.getAlarmRequest(userId);
    }
}
