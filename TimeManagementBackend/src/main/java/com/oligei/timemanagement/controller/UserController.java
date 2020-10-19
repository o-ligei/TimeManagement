package com.oligei.timemanagement.controller;

import com.oligei.timemanagement.TimemanagementApplication;
import com.oligei.timemanagement.dto.CreditInfo;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.entity.User;
import com.oligei.timemanagement.service.UserService;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/User")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TimemanagementApplication.class);

    @RequestMapping("/LoginWithPassword")
    public Msg<Map<String, Object>> loginWithPassword(@RequestParam(name = "phone") String phone,
                                          @RequestParam(name = "password") String password) {
        try {
            return userService.loginWithPassword(phone, password);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }

    @RequestMapping("/LoginWithCaptcha")
    public Msg<Map<String, Object>> loginWithCaptcha(@RequestParam(name = "phone") String phone,
                                                     @RequestParam(name = "captcha") String captcha) {
        try {
            return userService.loginWithCaptcha(phone, captcha);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }

    @RequestMapping("/SendCaptchaToPhone")
    public Msg<Boolean> sendCaptchaToPhone(@RequestParam(name = "phone") String phone) {
        try {
            return userService.sendCaptchaToPhone(phone);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }

    @RequestMapping("/SendCaptchaToEmail")
    public Msg<Boolean> sendCaptchaToEmail(@RequestParam(name = "phone") String phone,
                                           @RequestParam(name = "email") String email) {
        try {
            return userService.sendCaptchaToEmail(phone, email);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }

    @RequestMapping("/ActivateEmail")
    public Msg<Boolean> activateEmail(@RequestParam(name = "phone") String phone,
                                   @RequestParam(name = "email") String email,
                                   @RequestParam(name = "captcha") String captcha) {
        try {
            return userService.activateEmail(phone, email, captcha);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }

    @RequestMapping("/Register")
    public Msg<Map<String, Object>> register(@RequestParam(name = "phone") String phone,
                                 @RequestParam(name = "username") String username,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "captcha") String captcha) {
        try {
            return userService.register(phone, username, password, captcha);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }

    @RequestMapping("/GetPersonalProfile")
    public Msg<Profile> getPersonalProfile(@RequestParam(name = "userid") Integer userId) {
        try {
            return userService.getPersonalProfile(userId);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }

    @RequestMapping("/GetPersonalCredit")
    public Msg<CreditInfo> getPersonalCredit(@RequestParam(name = "userid") Integer userId) {
        try {
            return userService.getPersonalCredit(userId);
        } catch (NullPointerException e) {
            logger.error("NullPointerException", e);
            return new Msg<>(MsgCode.NULL_ARGUMENT);
        }
    }
}
