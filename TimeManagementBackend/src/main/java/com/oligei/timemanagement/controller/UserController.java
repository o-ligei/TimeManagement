package com.oligei.timemanagement.controller;

import com.oligei.timemanagement.TimemanagementApplication;
import com.oligei.timemanagement.dto.CreditInfo;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.entity.User;
import com.oligei.timemanagement.service.UserService;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/User")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/LoginWithPassword")
    public Msg<Map<String, Object>> loginWithPassword(
            @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "205")
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "password") String password) {
        return userService.loginWithPassword(phone, password);
    }

    @RequestMapping("/LoginWithCaptcha")
    public Msg<Map<String, Object>> loginWithCaptcha(
            @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "205")
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "captcha") String captcha) {
        return userService.loginWithCaptcha(phone, captcha);
    }

    @RequestMapping("/SendCaptchaToPhone")
    public Msg<Boolean> sendCaptchaToPhone(
            @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "205")
            @RequestParam(name = "phone") String phone) {
        return userService.sendCaptchaToPhone(phone);
    }

    @RequestMapping("/SendCaptchaToEmail")
    public Msg<Boolean> sendCaptchaToEmail(
            @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "205")
            @RequestParam(name = "phone") String phone,
            @Email(message = "204")
            @RequestParam(name = "email") String email) {
        return userService.sendCaptchaToEmail(phone, email);
    }

    @RequestMapping("/ActivateEmail")
    public Msg<Boolean> activateEmail(
            @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "205")
            @RequestParam(name = "phone") String phone,
            @Email(message = "204")
            @RequestParam(name = "email") String email,
            @RequestParam(name = "captcha") String captcha) {
        return userService.activateEmail(phone, email, captcha);
    }

    @RequestMapping("/Register")
    public Msg<Map<String, Object>> register(
            @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "205")
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "username") String username,
            @Length(min = 6, max = 12, message = "221")
            @Pattern(regexp = "^(?!\\d+$)(?![a-zA-Z]+$)[a-zA-Z\\d]+$", message = "222")
            @RequestParam(name = "password") String password,
            @RequestParam(name = "captcha") String captcha) {
        return userService.register(phone, username, password, captcha);
    }

    @RequestMapping("/GetPersonalProfile")
    public Msg<Profile> getPersonalProfile(@RequestParam(name = "userid") Integer userId) {
        return userService.getPersonalProfile(userId);
    }

    @RequestMapping("/GetPersonalCredit")
    public Msg<CreditInfo> getPersonalCredit(@RequestParam(name = "userid") Integer userId) {
        return userService.getPersonalCredit(userId);
    }

    @RequestMapping("/ResetPassword")
    public Msg<Boolean> resetPassword(
            @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "205")
            @RequestParam(name = "phone") String phone,
            @Length(min = 6, max = 12, message = "221")
            @Pattern(regexp = "^(?!\\d+$)(?![a-zA-Z]+$)[a-zA-Z\\d]+$", message = "222")
            @RequestParam(name = "password") String password) {
        return userService.resetPassword(phone, password);
    }

    @RequestMapping("/VerifyCaptcha")
    public Msg<Boolean> verifyCaptcha(
            @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", message = "205")
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "captcha") String captcha) {
        return userService.verifyCaptcha(phone, captcha);
    }
}
