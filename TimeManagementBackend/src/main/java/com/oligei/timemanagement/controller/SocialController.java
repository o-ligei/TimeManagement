package com.oligei.timemanagement.controller;

import com.oligei.timemanagement.TimemanagementApplication;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.service.SocialService;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Social")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SocialController {

    @Autowired
    private SocialService socialService;

    private static final Logger logger = LoggerFactory.getLogger(TimemanagementApplication.class);

    @RequestMapping("/GetProfile")
    public Msg<List<Profile>> getProfile(@RequestParam(name = "username") String username) {
        try {
            return socialService.getProfile(username);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }

    @RequestMapping("/GetFriendsList")
    public Msg<List<Profile>> getFriendsList(@RequestParam(name = "userid") Integer userId) {
        try {
            return socialService.getFriendsList(userId);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }
}
