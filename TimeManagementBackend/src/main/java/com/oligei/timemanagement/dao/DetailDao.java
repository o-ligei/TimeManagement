package com.oligei.timemanagement.dao;

import com.oligei.timemanagement.entity.Detail;

import java.sql.Timestamp;
import java.util.List;

public interface DetailDao {
    Detail save(Detail detail);
    List<Detail> getDetailsByUserId(Integer userId);
    List<Detail> getDetailsByUserIdAndTimestampGreaterThanEqual(Integer userId, Timestamp timestamp);
}
