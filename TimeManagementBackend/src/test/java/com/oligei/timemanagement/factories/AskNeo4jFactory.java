package com.oligei.timemanagement.factories;

import com.oligei.timemanagement.entity.AskNeo4j;
import com.oligei.timemanagement.entity.UserNeo4j;

public class AskNeo4jFactory {

    public static AskNeo4j buildAskNeo4jByNode(UserNeo4j me, UserNeo4j friend) {
        return new AskNeo4j(me, friend);
    }
}
