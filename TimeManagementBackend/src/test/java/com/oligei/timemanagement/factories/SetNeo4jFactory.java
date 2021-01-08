package com.oligei.timemanagement.factories;

import com.oligei.timemanagement.entity.SetNeo4j;
import com.oligei.timemanagement.entity.UserNeo4j;

public class SetNeo4jFactory {

    public static SetNeo4j buildSetNeo4jByNode(UserNeo4j friend, UserNeo4j me) {
        return new SetNeo4j(friend, me, "user", "clock");
    }

}
