package com.oligei.timemanagement.repository;

import com.oligei.timemanagement.entity.UserNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface UserNeo4jRepository extends Neo4jRepository<UserNeo4j, Long> {
    UserNeo4j getUserNeo4jByUserId(String userId);

    @Query("MATCH (a:tm_users)\n" +
            "WHERE a.username contains username\n" +
            "RETURN a")
    List<UserNeo4j> searchByUsername(String username);

    @Query("MATCH (a:tm_users)-[:FOLLOW]->(b:tm_users)\n" +
            "WHERE a.userid=$userId\n" +
            "RETURN b")
    List<UserNeo4j> getFriendsList(String userId);

    @Query("MATCH (a:tm_users)-[:ASK]->(b:tm_users)\n" +
            "WHERE b.userid = $userId\n" +
            "RETURN a")
    List<UserNeo4j> getFriendRequest(String userId);
}
