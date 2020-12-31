package com.oligei.timemanagement.factories;

import com.oligei.timemanagement.entity.Detail;
import java.sql.Timestamp;

public class DetailFactory {

    public static Detail buildDetailByUserId(Integer userId) {
        return new Detail(userId, "UnitTest", new Timestamp(System.currentTimeMillis()));
    }
}
