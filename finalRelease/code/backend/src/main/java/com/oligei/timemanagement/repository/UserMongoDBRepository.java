package com.oligei.timemanagement.repository;

import com.oligei.timemanagement.entity.UserMongoDB;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMongoDBRepository extends MongoRepository<UserMongoDB, Integer> {

    UserMongoDB getUserMongoDBByUserId(Integer userId);
}
