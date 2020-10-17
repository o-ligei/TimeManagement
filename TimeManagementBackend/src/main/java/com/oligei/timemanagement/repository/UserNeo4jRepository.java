package com.oligei.timemanagement.repository;

import com.oligei.timemanagement.entity.UserNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface UserNeo4jRepository extends Neo4jRepository<UserNeo4j, Long> {
    UserNeo4j getUserNeo4jByUserId(String userId);
    List<UserNeo4j> getUserNeo4jsByUsername(String username);

    @Query("MATCH (a:tm_users)-[:FOLLOW]->(b:tm_users)\n" +
            "WHERE a.userid=$userId\n" +
            "RETURN b")
    List<UserNeo4j> getFriendsList(String userId);
}
