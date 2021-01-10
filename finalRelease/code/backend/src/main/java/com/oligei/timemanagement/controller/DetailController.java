package com.oligei.timemanagement.controller;

import com.oligei.timemanagement.TimemanagementApplication;
import com.oligei.timemanagement.entity.Detail;
import com.oligei.timemanagement.service.DetailService;
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
@RequestMapping("/Detail")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DetailController {

    @Autowired
    private DetailService detailService;

    @RequestMapping("/SaveDetail")
    public Msg<Boolean> saveDetail(@RequestParam(name = "userid") Integer userId,
            @RequestParam(name = "event") String event,
            @RequestParam(name = "timestamp") String timestamp) {
        return detailService.saveDetail(userId, event, timestamp);
    }

    @RequestMapping("/AddScore")
    public Msg<Boolean> addScore(@RequestParam(name = "userid") Integer userId,
            @RequestParam(name = "earn") Integer earn) {
        return detailService.addScore(userId, earn);
    }

    //timestamp = "whole" if searching for all the details
    @RequestMapping("/GetDetail")
    public Msg<List<Detail>> getDetail(@RequestParam(name = "userid") Integer userId,
            @RequestParam(name = "timestamp") String timestamp) {
        return detailService.getDetail(userId, timestamp);
    }
}
