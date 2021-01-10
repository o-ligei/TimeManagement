package com.oligei.timemanagement.utils;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

    public static Timestamp strToTimestamp(String timestamp) {
        return Timestamp.valueOf(timestamp);
    }
}
