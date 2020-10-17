package com.oligei.timemanagement.repository;

import com.oligei.timemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByPhone(String phone);
    User getUserByUserId(Integer userId);
}
