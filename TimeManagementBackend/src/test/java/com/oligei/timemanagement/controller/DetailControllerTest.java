package com.oligei.timemanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oligei.timemanagement.service.DetailService;
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
class DetailControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DetailService detailService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private JSONObject mockResponseBuilder(String url, List<String> argsList, List<String> valsList) {
        try {
            StringBuilder urlBuilder = new StringBuilder(url);
            urlBuilder.append("?");
            int size = argsList.size();
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
    void saveDetail() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid"); argsList.add("event"); argsList.add("timestamp");
        valsList.add("1"); valsList.add("event"); valsList.add("timestamp");
        when(detailService.saveDetail(1, "event", "timestamp")).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Detail/SaveDetail", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void addScore() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid"); argsList.add("earn");
        valsList.add("1"); valsList.add("100");
        when(detailService.addScore(1, 100)).thenReturn(new Msg<>(MsgCode.SUCCESS));
        JSONObject msg = mockResponseBuilder("/Detail/AddScore", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }

    @Test
    void getDetail() {
        List<String> argsList = new ArrayList<>(), valsList = new ArrayList<>();
        argsList.add("userid"); argsList.add("timestamp");
        valsList.add("1"); valsList.add("whole");
        when(detailService.getDetail(1, "whole")).thenReturn(new Msg<>(MsgCode.SUCCESS, new ArrayList<>()));
        JSONObject msg = mockResponseBuilder("/Detail/GetDetail", argsList, valsList);
        assertEquals(MsgConstant.SUCCESS, msg.get("status"));
    }
}
