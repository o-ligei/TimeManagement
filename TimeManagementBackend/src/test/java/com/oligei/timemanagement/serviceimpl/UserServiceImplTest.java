package com.oligei.timemanagement.serviceimpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.oligei.timemanagement.dao.UserDao;
import com.oligei.timemanagement.entity.User;
import com.oligei.timemanagement.entity.UserMongoDB;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.factories.UserFactory;
import com.oligei.timemanagement.factories.UserMongoDBFactory;
import com.oligei.timemanagement.factories.UserNeo4jFactory;
import com.oligei.timemanagement.service.UserService;
import com.oligei.timemanagement.utils.AliSmsUtil;
import com.oligei.timemanagement.utils.EmailUtil;
import com.oligei.timemanagement.utils.msgutils.MsgConstant;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private AliSmsUtil aliSmsUtil;

    @MockBean
    private EmailUtil emailUtil;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void loginWithPassword() {
        String phone1 = "13000000000", phone2 = "13000000001";
        String password1 = "password", password2 = "wrong";
        User user = UserFactory.buildUserByPhone(phone1);
        user.setPassword(encoder.encode(password1));
        when(userDao.getUserByPhone(phone1)).thenReturn(user);
        when(userDao.getUserByPhone(phone2)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> userService.loginWithPassword(null, null));
        assertEquals(MsgConstant.PHONE_NOT_FOUND, userService.loginWithPassword(phone2, password1).getStatus());
        assertEquals(MsgConstant.SUCCESS, userService.loginWithPassword(phone1, password1).getStatus());
        assertEquals(MsgConstant.WRONG_PASSWORD, userService.loginWithPassword(phone1, password2).getStatus());
    }

    @Test
    void loginWithCaptcha() {
        String phone1 = "13000000000", phone2 = "13000000001";
        String captcha1 = "000000", captcha2 = "111111";
        User user = UserFactory.buildUserByPhone(phone1);
        when(userDao.getUserByPhone(phone1)).thenReturn(user);
        when(userDao.getUserByPhone(phone2)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> userService.loginWithCaptcha(null, null));
        assertEquals(MsgConstant.PHONE_NOT_FOUND, userService.loginWithCaptcha(phone2, captcha1).getStatus());
        assertEquals(MsgConstant.SUCCESS, userService.loginWithCaptcha(phone1, captcha1).getStatus());
        assertNotEquals(MsgConstant.SUCCESS, userService.loginWithCaptcha(phone1, captcha2).getStatus());
    }

    @Test
    void sendCaptchaToPhone() {
        try {
            String phone = "13000000000";
            doNothing().when(aliSmsUtil).sendALiSms(any(), any());
            assertThrows(NullPointerException.class, () -> userService.sendCaptchaToPhone(null));
            assertEquals(MsgConstant.SUCCESS, userService.sendCaptchaToPhone(phone).getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void sendCaptchaToEmail() {
        String phone1 = "13300000000", phone2 = "13300000001";
        String email = "linda@sjtu.edu.cn";
        User user = UserFactory.buildUserByPhone(phone1);
        when(userDao.getUserByPhone(phone1)).thenReturn(user);
        when(userDao.getUserByPhone(phone2)).thenReturn(null);
        doNothing().when(emailUtil).sendEmail(any(), any(), any());
        assertThrows(NullPointerException.class, () -> userService.sendCaptchaToEmail(null, null));
        assertEquals(MsgConstant.PHONE_NOT_FOUND, userService.sendCaptchaToEmail(phone2, email).getStatus());
        assertEquals(MsgConstant.SUCCESS, userService.sendCaptchaToEmail(phone1, email).getStatus());
    }

    @Test
    void activateEmail() {
        String phone1 = "13300000000", phone2 = "13300000001", phone3 = "13300000002";
        String captcha1 = "000000", captcha2 = "111111";
        String email = "linda@sjtu.edu.cn";
        User user1 = UserFactory.buildUserByPhone(phone1), user2 = UserFactory.buildUserByPhone(phone2);
        user1.setEmail(email);
        when(userDao.getUserByPhone(phone1)).thenReturn(user1);
        when(userDao.getUserByPhone(phone2)).thenReturn(user2);
        when(userDao.getUserByPhone(phone3)).thenReturn(null);
        when(userDao.save(any(), any())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> userService.activateEmail(null, null, null));
        assertEquals(MsgConstant.PHONE_NOT_FOUND, userService.activateEmail(phone3, email, captcha1).getStatus());
        assertNotEquals(MsgConstant.SUCCESS, userService.activateEmail(phone2, email, captcha2).getStatus());
        assertEquals(MsgConstant.EMAIL_EXISTED, userService.activateEmail(phone1, email, captcha1).getStatus());
        assertEquals(MsgConstant.SUCCESS, userService.activateEmail(phone2, email, captcha1).getStatus());
    }

    @Test
    void register() {
        String phone1 = "13000000000", phone2 = "13000000001";
        String captcha1 = "000000", captcha2 = "111111";
        String username = "test", password = "password";
        User user = UserFactory.buildUserByPhone(phone1);
        when(userDao.getUserByPhone(phone1)).thenReturn(user);
        when(userDao.getUserByPhone(phone2)).thenReturn(null);
        when(userDao.save(any(), any())).thenReturn(user);
        assertThrows(NullPointerException.class, () -> userService.register(null, null, null, null));
        assertEquals(MsgConstant.PHONE_FOUND, userService.register(phone1, username, password, captcha1).getStatus());
        assertNotEquals(MsgConstant.SUCCESS, userService.register(phone2, username, password, captcha2).getStatus());
        assertEquals(MsgConstant.SUCCESS, userService.register(phone2, username, password, captcha1).getStatus());
    }

    @Test
    void getPersonalProfile() {
        Integer userId = 1;
        User user = UserFactory.buildUserByPhone("13300000000");
        user.setUserId(userId);
        UserNeo4j userNeo4j = UserNeo4jFactory.buildUserNeo4jByUserId(userId.toString());
        when(userDao.getUserByUserId(userId)).thenReturn(user);
        when(userDao.getUserNeo4jByUserId(userId)).thenReturn(userNeo4j);
        assertThrows(NullPointerException.class, () -> userService.getPersonalProfile(null));
        assertEquals(MsgConstant.SUCCESS, userService.getPersonalProfile(userId).getStatus());
    }

    @Test
    void getPersonalCredit() {
        Integer userId = 1;
        UserMongoDB userMongoDB = UserMongoDBFactory.buildUserMongoDBByUserId(userId);
        when(userDao.getUserMongoDBByUserId(userId)).thenReturn(userMongoDB);
        assertThrows(NullPointerException.class, () -> userService.getPersonalCredit(null));
        assertEquals(MsgConstant.SUCCESS, userService.getPersonalCredit(userId).getStatus());
    }

    @Test
    void resetPassword() {
        String phone1 = "13300000000", phone2 = "13300000001";
        String password = "password";
        User user = UserFactory.buildUserByPhone(phone1);
        when(userDao.getUserByPhone(phone1)).thenReturn(user);
        when(userDao.getUserByPhone(phone2)).thenReturn(null);
        when(userDao.save(any(), any())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> userService.resetPassword(null, null));
        assertEquals(MsgConstant.PHONE_NOT_FOUND, userService.resetPassword(phone2, password).getStatus());
        assertEquals(MsgConstant.SUCCESS, userService.resetPassword(phone1, password).getStatus());
    }

    @Test
    void verifyCaptcha() {
        String phone = "13300000000";
        String captcha1 = "000000", captcha2 = "111111";
        assertThrows(NullPointerException.class, () -> userService.verifyCaptcha(null, null));
        assertNotEquals(MsgConstant.SUCCESS, userService.verifyCaptcha(phone, captcha2).getStatus());
        assertEquals(MsgConstant.SUCCESS, userService.verifyCaptcha(phone, captcha1).getStatus());
    }
}
