package com.oligei.timemanagement.service;

import com.oligei.timemanagement.entity.Detail;
import com.oligei.timemanagement.utils.msgutils.Msg;

import java.util.List;

public interface DetailService {
    Msg<Boolean> saveDetail(Integer userId, String event, String timestamp, Integer earn);
    Msg<List<Detail>> getDetail(Integer userId);
}
