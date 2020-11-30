package com.oligei.timemanagement.repository;

import com.oligei.timemanagement.entity.FollowNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FollowNeo4jRepository extends Neo4jRepository<FollowNeo4j, Long> {

    @Query("MATCH p=(a:tm_users{userid: $from})-[r:FOLLOW]->(b:tm_users{userid: $to})\n" +
            "RETURN p")
    FollowNeo4j getFollowRelation(String from, String to);

    @Query("MATCH (a:tm_users),(b:tm_users)\n" +
            "WHERE a.userid = $from AND b.userid = $to\n" +
            "CREATE (a)-[r:FOLLOW]->(b)\n" +
            "RETURN r")
    FollowNeo4j addFollowRelation(String from, String to);
}
