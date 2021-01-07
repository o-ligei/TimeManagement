package com.oligei.timemanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oligei.timemanagement.service.SocialService;
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
class SocialControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private SocialService socialService;

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
    void getProfile() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid"); argsList.add("username");
        valsList.add("1"); valsList.add("username");
        when(socialService.getProfile(1, "username")).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/GetProfile", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void getFriendsList() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid");
        valsList.add("1");
        when(socialService.getFriendsList(1)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/GetFriendsList", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void addFriend() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("from"); argsList.add("to");
        valsList.add("1"); valsList.add("2");
        when(socialService.addFriend(1, 2)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/AddFriend", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void getFriendRequest() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid");
        valsList.add("1");
        when(socialService.getFriendRequest(1)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/GetFriendRequest", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void acceptFriend() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("from"); argsList.add("to");
        valsList.add("1"); valsList.add("2");
        when(socialService.acceptFriend(1, 2)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/AcceptFriend", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void setAlarmForFriend() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("from"); argsList.add("to"); argsList.add("username"); argsList.add("clocksetting");
        valsList.add("1"); valsList.add("2"); valsList.add("username"); valsList.add("clocksetting");
        when(socialService.saveAlarmForFriend(any(), any(), any())).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/SetAlarmForFriend", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void getAlarmRequest() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid");
        valsList.add("1");
        when(socialService.getAlarmRequest(1)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/GetAlarmRequest", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void kMeans() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        when(socialService.kMeans()).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/KMeans", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void recommendFriend() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid");
        valsList.add("1");
        when(socialService.recommendFriend(1)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Social/RecommendFriend", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }
}
