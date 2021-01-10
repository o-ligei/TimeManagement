package com.oligei.timemanagement.daoimpl;

import com.oligei.timemanagement.dao.DetailDao;
import com.oligei.timemanagement.entity.Detail;
import com.oligei.timemanagement.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public class DetailDaoImpl implements DetailDao {

    @Autowired
    private DetailRepository detailRepository;

    @Override
    public Detail save(Detail detail) {
        Objects.requireNonNull(detail, "null detail --DetailDaoImpl saveDetail");
        return detailRepository.save(detail);
    }

    @Override
    public List<Detail> getDetailsByUserId(Integer userId) {
        Objects.requireNonNull(userId, "null userId --DetailDaoImpl getDetailsByUserId");
        return detailRepository.getDetailsByUserId(userId);
    }

    @Override
    public List<Detail> getDetailsByUserIdAndTimestampGreaterThanEqual(Integer userId, Timestamp timestamp) {
        Objects.requireNonNull(userId, "null userId --DetailDaoImpl getDetailsByUserIdAndTimestampAfter");
        Objects.requireNonNull(timestamp, "null timestamp --DetailDaoImpl getDetailsByUserIdAndTimestampAfter");
        return detailRepository.getDetailsByUserIdAndTimestampGreaterThanEqual(userId, timestamp);
    }
}
