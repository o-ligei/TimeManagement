package com.oligei.timemanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oligei.timemanagement.service.UserService;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import com.oligei.timemanagement.utils.msgutils.MsgConstant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private JSONObject mockResponseBuilder(String url, List<String> argsList, List<String> valsList) {
        try {
            StringBuilder urlBuilder = new StringBuilder(url);
            int size = argsList.size();
            if (size > 0) urlBuilder.append("?");
            for (int i = 0; i < size; i++) {
                if (i > 0)
                    urlBuilder.append("&");
                urlBuilder.append(argsList.get(i));
                urlBuilder.append("=");
                urlBuilder.append(valsList.get(i));
            }
            MvcResult result = mockMvc.perform(get(urlBuilder.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
                                      .andExpect(status().isOk()).andReturn();
            String resultContent = result.getResponse().getContentAsString();
            return om.readValue(resultContent, new TypeReference<JSONObject>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    @Test
    void loginWithPassword() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        String phone1 = "13300000000", phone2 = "phone";
        String password = "password";
        when(userService.loginWithPassword(phone1, password)).thenReturn(new Msg<>(MsgCode.SUCCESS));

        argsList.add("phone"); argsList.add("password");
        valsList.add(phone1); valsList.add(password);
        JSONObject msg1 = mockResponseBuilder("/User/LoginWithPassword", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg1.get("status"));

        valsList.set(0, phone2);
        JSONObject msg2 = mockResponseBuilder("/User/LoginWithPassword", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_PHONE, msg2.get("status"));
    }

    @Test
    void loginWithCaptcha() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        String phone1 = "13300000000", phone2 = "phone";
        String captcha = "captcha";
        when(userService.loginWithCaptcha(phone1, captcha)).thenReturn(new Msg<>(MsgCode.SUCCESS));

        argsList.add("phone"); argsList.add("captcha");
        valsList.add(phone1); valsList.add(captcha);
        JSONObject msg1 = mockResponseBuilder("/User/LoginWithCaptcha", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg1.get("status"));

        valsList.set(0, phone2);
        JSONObject msg2 = mockResponseBuilder("/User/LoginWithCaptcha", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_PHONE, msg2.get("status"));
    }

    @Test
    void sendCaptchaToPhone() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        String phone1 = "13300000000", phone2 = "phone";
        when(userService.sendCaptchaToPhone(phone1)).thenReturn(new Msg<>(MsgCode.SUCCESS));

        argsList.add("phone"); valsList.add(phone1);
        JSONObject msg1 = mockResponseBuilder("/User/SendCaptchaToPhone", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg1.get("status"));

        valsList.set(0, phone2);
        JSONObject msg2 = mockResponseBuilder("/User/SendCaptchaToPhone", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_PHONE, msg2.get("status"));
    }

    @Test
    void sendCaptchaToEmail() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        String phone1 = "13300000000", phone2 = "phone";
        String email1 = "123456789@qq.com", email2 = "email";
        when(userService.sendCaptchaToEmail(phone1, email1)).thenReturn(new Msg<>(MsgCode.SUCCESS));

        argsList.add("phone"); argsList.add("email");
        valsList.add(phone1); valsList.add(email1);
        JSONObject msg1 = mockResponseBuilder("/User/SendCaptchaToEmail", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg1.get("status"));

        valsList.set(0, phone2);
        JSONObject msg2 = mockResponseBuilder("/User/SendCaptchaToEmail", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_PHONE, msg2.get("status"));

        valsList.set(0, phone1); valsList.set(1, email2);
        JSONObject msg3 = mockResponseBuilder("/User/SendCaptchaToEmail", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_EMAIL, msg3.get("status"));
    }

    @Test
    void activateEmail() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        String phone1 = "13300000000", phone2 = "phone";
        String email1 = "123456789@qq.com", email2 = "email";
        String captcha = "captcha";
        when(userService.activateEmail(phone1, email1, captcha)).thenReturn(new Msg<>(MsgCode.SUCCESS));

        argsList.add("phone"); argsList.add("email"); argsList.add("captcha");
        valsList.add(phone1); valsList.add(email1); valsList.add(captcha);
        JSONObject msg1 = mockResponseBuilder("/User/ActivateEmail", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg1.get("status"));

        valsList.set(0, phone2);
        JSONObject msg2 = mockResponseBuilder("/User/ActivateEmail", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_PHONE, msg2.get("status"));

        valsList.set(0, phone1); valsList.set(1, email2);
        JSONObject msg3 = mockResponseBuilder("/User/ActivateEmail", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_EMAIL, msg3.get("status"));
    }

    @Test
    void register() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        String phone1 = "13300000000", phone2 = "phone";
        String password1 = "abcde12345", password2 = "weakweakweak";
        String username = "username", captcha = "captcha";
        when(userService.register(phone1, username, password1, captcha)).thenReturn(new Msg<>(MsgCode.SUCCESS));

        argsList.add("phone"); argsList.add("username"); argsList.add("password"); argsList.add("captcha");
        valsList.add(phone1); valsList.add(username); valsList.add(password1); valsList.add(captcha);
        JSONObject msg1 = mockResponseBuilder("/User/Register", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg1.get("status"));

        valsList.set(0, phone2);
        JSONObject msg2 = mockResponseBuilder("/User/Register", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_PHONE, msg2.get("status"));

        valsList.set(0, phone1); valsList.set(2, password2);
        JSONObject msg3 = mockResponseBuilder("/User/Register", argsList, valsList);
        assertEquals(MsgConstant.WEAK_PASSWORD, msg3.get("status"));
    }

    @Test
    void getPersonalProfile() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid"); valsList.add("1");
        when(userService.getPersonalProfile(1)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/User/GetPersonalProfile", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void getPersonalCredit() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid"); valsList.add("1");
        when(userService.getPersonalCredit(1)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/User/GetPersonalCredit", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void resetPassword() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        String phone1 = "13300000000", phone2 = "phone";
        String password1 = "abcde12345", password2 = "weakweakweak";
        when(userService.resetPassword(phone1, password1)).thenReturn(new Msg<>(MsgCode.SUCCESS));

        argsList.add("phone"); argsList.add("password");
        valsList.add(phone1); valsList.add(password1);
        JSONObject msg1 = mockResponseBuilder("/User/ResetPassword", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg1.get("status"));

        valsList.set(0, phone2);
        JSONObject msg2 = mockResponseBuilder("/User/ResetPassword", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_PHONE, msg2.get("status"));

        valsList.set(0, phone1); valsList.set(1, password2);
        JSONObject msg3 = mockResponseBuilder("/User/ResetPassword", argsList, valsList);
        assertEquals(MsgConstant.WEAK_PASSWORD, msg3.get("status"));
    }

    @Test
    void verifyCaptcha() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        String phone1 = "13300000000", phone2 = "phone";
        String captcha = "captcha";
        when(userService.verifyCaptcha(phone1, captcha)).thenReturn(new Msg<>(MsgCode.SUCCESS));

        argsList.add("phone"); argsList.add("captcha");
        valsList.add(phone1); valsList.add(captcha);
        JSONObject msg1 = mockResponseBuilder("/User/VerifyCaptcha", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg1.get("status"));

        valsList.set(0, phone2);
        JSONObject msg2 = mockResponseBuilder("/User/VerifyCaptcha", argsList, valsList);
        assertEquals(MsgConstant.ILLEGAL_PHONE, msg2.get("status"));
    }
}
