package com.oligei.timemanagement.repository;

import com.oligei.timemanagement.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, Integer> {
    List<Detail> getDetailsByUserId(Integer userId);
}
