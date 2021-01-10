package com.oligei.timemanagement.repository;

import com.oligei.timemanagement.entity.AskNeo4j;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AskNeo4jRepository extends Neo4jRepository<AskNeo4j, Long> {

    @Query("MATCH p=(a:tm_users{userid: $from})-[:ASK]->(b:tm_users{userid: $to})\n" +
            "RETURN p")
    AskNeo4j getAskRelation(String from, String to);

    @Query("MATCH (a:tm_users),(b:tm_users)\n" +
            "WHERE a.userid = $from AND b.userid = $to\n" +
            "CREATE (a)-[r:ASK]->(b)\n" +
            "RETURN r")
    AskNeo4j addAskRelation(String from, String to);

    @Query("MATCH (a:tm_users)-[r:ASK]->(b:tm_users)\n" +
            "WHERE a.userid = $from AND b.userid = $to\n" +
            "DELETE r")
    void deleteAskRelation(String from, String to);
}

