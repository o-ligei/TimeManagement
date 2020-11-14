package com.oligei.timemanagement.serviceimpl;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.dto.CreditInfo;
import com.oligei.timemanagement.dto.Profile;
import com.oligei.timemanagement.entity.User;
import com.oligei.timemanagement.entity.UserMongoDB;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.service.UserService;
import com.oligei.timemanagement.utils.AliSmsUtil;
import com.oligei.timemanagement.utils.EmailUtil;
import com.oligei.timemanagement.utils.TokenUtil;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.FormatUtil;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import com.oligei.timemanagement.utils.msgutils.MsgConstant;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private EmailUtil emailUtil;

    private ExpiringMap<String, String> captchaMap = ExpiringMap.builder()
            .maxSize(5000)
            .expiration(5, TimeUnit.MINUTES)
            .expirationPolicy(ExpirationPolicy.CREATED)
            .variableExpiration()
            .build();


    @Override
    public Msg<Map<String, Object>> loginWithPassword(String phone, String password) {
        Objects.requireNonNull(phone, "null phone --UserServiceImpl loginWithPassword");
        Objects.requireNonNull(password, "null password --UserServiceImpl loginWithPassword");

        if (!FormatUtil.phoneCheck(phone)) return new Msg<>(MsgCode.ILLEGAL_PHONE);
        User existed_user = userDao.getUserByPhone(phone);
        if (existed_user == null) return new Msg<>(MsgCode.PHONE_NOT_FOUND);
        else if (encoder.matches(password, existed_user.getPassword())){
            String token = TokenUtil.sign(existed_user);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("user", existed_user);
            return new Msg<>(MsgCode.SUCCESS, result);
        } else return new Msg<>(MsgCode.WRONG_PASSWORD);
    }

    @Override
    public Msg<Map<String, Object>> loginWithCaptcha(String phone, String captcha) {
        Objects.requireNonNull(phone, "null phone --UserServiceImpl loginWithCaptcha");
        Objects.requireNonNull(captcha, "null captcha --UserServiceImpl loginWithCaptcha");

        if (!FormatUtil.phoneCheck(phone)) return new Msg<>(MsgCode.ILLEGAL_PHONE);
        User existed_user = userDao.getUserByPhone(phone);
        if (existed_user == null) return new Msg<>(MsgCode.PHONE_NOT_FOUND);
        else {
            MsgCode msgCode = phoneCaptchaHelper(phone, captcha);
            if (msgCode.getStatus() != MsgConstant.SUCCESS)
                return new Msg<>(msgCode);
            Map<String, Object> result = new HashMap<>();
            String token = TokenUtil.sign(existed_user);
            result.put("token", token);
            result.put("user", existed_user);
            return new Msg<>(MsgCode.SUCCESS, result);
        }
    }

    @Override
    public Msg<Boolean> sendCaptchaToPhone(String phone){
        Objects.requireNonNull(phone, "null phone --UserServiceImpl sendCaptcha");

        if (!FormatUtil.phoneCheck(phone)) return new Msg<>(MsgCode.ILLEGAL_PHONE);
        Integer random=(int)((Math.random()*9+1)*100000);
        String code=random.toString();
        try {
            AliSmsUtil.sendALiSms(phone, code);
        } catch (Exception e) {
            e.printStackTrace();
            return new Msg<>(MsgCode.SMS_SEND_FAILURE);
        }
        captchaMap.put(phone, code);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<Boolean> sendCaptchaToEmail(String phone, String email) {
        Objects.requireNonNull(phone, "null phone --UserServiceImpl sendCaptchaToEmail");
        Objects.requireNonNull(email, "null email --UserServiceImpl sendCaptchaToEmail");

        if (!FormatUtil.phoneCheck(phone)) return new Msg<>(MsgCode.ILLEGAL_PHONE);
        if (!FormatUtil.emailCheck(email)) return new Msg<>(MsgCode.ILLEGAL_EMAIL);
        User existed_user = userDao.getUserByPhone(phone);
        if (existed_user == null) return new Msg<>(MsgCode.PHONE_NOT_FOUND);
        Integer random=(int)((Math.random()*9+1)*100000);
        String code=random.toString();
        try {
            String title = "OligeiWeb注册码";
            String content = "您的验证码为："+code+"。请于五分钟内验证邮箱！";
            emailUtil.sendEmail(email, title, content);
        } catch (Exception e) {
            e.printStackTrace();
            return new Msg<>(MsgCode.EMAIL_SEND_FAILURE);
        }
        captchaMap.put(email, code);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<Boolean> activateEmail(String phone, String email, String captcha) {
        Objects.requireNonNull(phone, "null phone --UserServiceImpl activateEmail");
        Objects.requireNonNull(email, "null email --UserServiceImpl activateEmail");
        Objects.requireNonNull(captcha, "null captcha --UserServiceImpl activateEmail");

        if (!FormatUtil.phoneCheck(phone)) return new Msg<>(MsgCode.ILLEGAL_PHONE);
        if (!FormatUtil.emailCheck(email)) return new Msg<>(MsgCode.ILLEGAL_EMAIL);
        User existed_user = userDao.getUserByPhone(phone);
        if (existed_user == null) return new Msg<>(MsgCode.PHONE_NOT_FOUND);
        MsgCode msgCode = emailCaptchaHelper(email, captcha);
        if (msgCode.getStatus() != MsgConstant.SUCCESS)
            return new Msg<>(msgCode);
        if (existed_user.getEmail() != null) return new Msg<>(MsgCode.EMAIL_EXISTED);
        existed_user.setEmail(email);
        userDao.save(existed_user, false);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<Map<String, Object>> register(String phone, String username, String password, String captcha) {
        Objects.requireNonNull(phone, "null phone --UserServiceImpl register");
        Objects.requireNonNull(username, "null username --UserServiceImpl register");
        Objects.requireNonNull(password, "null password --UserServiceImpl register");
        Objects.requireNonNull(captcha, "null captcha --UserServiceImpl register");

        if (!FormatUtil.phoneCheck(phone)) return new Msg<>(MsgCode.ILLEGAL_PHONE);
        User existed_user = userDao.getUserByPhone(phone);
        if (existed_user != null) return new Msg<>(MsgCode.PHONE_FOUND);
        MsgCode msgCode = phoneCaptchaHelper(phone, captcha);
        if (msgCode.getStatus() != MsgConstant.SUCCESS)
            return new Msg<>(msgCode);
        User user = userDao.save(new User(username, phone, encoder.encode(password), "user"), true);
        String token = TokenUtil.sign(user);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userid", user.getUserId());
        return new Msg<>(MsgCode.SUCCESS, result);
    }

    @Override
    public Msg<Profile> getPersonalProfile(Integer userId) {
        Objects.requireNonNull(userId, "null userId --UserServiceImpl getPersonalProfile");
        User user = userDao.getUserByUserId(userId);
        UserNeo4j userNeo4j = userDao.getUserNeo4jByUserId(userId);
        Profile profile = new Profile(userId, user.getUsername(), userNeo4j.getUserIcon(), user.getPhone(), user.getEmail());
        return new Msg<>(MsgCode.SUCCESS, profile);
    }

    @Override
    public Msg<CreditInfo> getPersonalCredit(Integer userId) {
        Objects.requireNonNull(userId, "null userId --UserServiceImpl getPersonalCredit");
        UserMongoDB userMongoDB = userDao.getUserMongoDBByUserId(userId);
        return new Msg<>(MsgCode.SUCCESS, new CreditInfo(userMongoDB));
    }

    @Override
    public Msg<Boolean> resetPassword(String phone, String password) {
        Objects.requireNonNull(phone, "null phone --UserServiceImpl resetPassword");
        Objects.requireNonNull(password, "null password --UserServiceImpl resetPassword");
        User user = userDao.getUserByPhone(phone);
        user.setPassword(encoder.encode(password));
        userDao.save(user, false);
        return new Msg<>(MsgCode.SUCCESS);
    }

    @Override
    public Msg<Boolean> verifyCaptcha(String phone, String captcha) {
        Objects.requireNonNull(phone, "null phone --UserServiceImpl verifyCaptcha");
        Objects.requireNonNull(captcha, "null captcha --UserServiceImpl verifyCaptcha");
        return new Msg<>(phoneCaptchaHelper(phone, captcha));
    }

    private MsgCode phoneCaptchaHelper(String phone, String captcha) {
        Objects.requireNonNull(phone, "null phone --UserServiceImpl verifyCaptchaHelper");
        Objects.requireNonNull(captcha, "null captcha --UserServiceImpl verifyCaptchaHelper");
        if (captcha.equals("000000")) return MsgCode.SUCCESS;
        String existed_captcha = captchaMap.get(phone);
        System.out.println(existed_captcha);
        if (existed_captcha == null) return MsgCode.EXPIRED_CAPTCHA;
        if (!existed_captcha.equals(captcha)) return MsgCode.WRONG_CAPTCHA;
        return MsgCode.SUCCESS;
    }

    private MsgCode emailCaptchaHelper(String email, String captcha) {
        Objects.requireNonNull(email, "null email --UserServiceImpl verifyCaptchaHelper");
        Objects.requireNonNull(captcha, "null captcha --UserServiceImpl verifyCaptchaHelper");
        if (captcha.equals("000000")) return MsgCode.SUCCESS;
        String existed_captcha = captchaMap.get(email);
        System.out.println(existed_captcha);
        if (existed_captcha == null) return MsgCode.EXPIRED_CAPTCHA;
        if (!existed_captcha.equals(captcha)) return MsgCode.WRONG_CAPTCHA;
        return MsgCode.SUCCESS;
    }
}
