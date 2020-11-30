package com.oligei.timemanagement.repository;

import com.oligei.timemanagement.entity.SetNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface SetNeo4jRepository extends Neo4jRepository<SetNeo4j, Long> {

    @Query("MATCH (a:tm_users),(b:tm_users)\n" +
            "WHERE a.userid = $from AND b.userid = $to\n" +
            "CREATE (a)-[r:SET{username:$username,clocksetting:$clockSetting}]->(b)\n" +
            "RETURN r")
    SetNeo4j addSetRelation(String from, String to, String username, String clockSetting);

    @Query("MATCH p = (:tm_users)-[:SET]->(b:tm_users)\n" +
            "WHERE b.userid = $userId\n" +
            "RETURN p")
    List<SetNeo4j> getAlarmRequest(String userId);
}
