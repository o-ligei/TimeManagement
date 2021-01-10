package com.oligei.timemanagement.service;

import com.oligei.timemanagement.dto.CreditInfo;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.utils.msgutils.Msg;

import java.util.Map;

public interface UserService {

    Msg<Map<String, Object>> loginWithPassword(String phone, String password);

    Msg<Map<String, Object>> loginWithCaptcha(String phone, String captcha);

    Msg<Boolean> sendCaptchaToPhone(String phone);

    Msg<Boolean> sendCaptchaToEmail(String phone, String email);

    Msg<Boolean> activateEmail(String phone, String email, String captcha);

    Msg<Map<String, Object>> register(String phone, String username, String password, String captcha);

    Msg<Profile> getPersonalProfile(Integer userId);

    Msg<CreditInfo> getPersonalCredit(Integer userId);

    Msg<Boolean> resetPassword(String phone, String password);

    Msg<Boolean> verifyCaptcha(String phone, String captcha);
}
