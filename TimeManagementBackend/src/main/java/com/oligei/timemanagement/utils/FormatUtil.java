package com.oligei.timemanagement.utils;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

    private static boolean phoneParse(String pat, String phone) {
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean phoneCheck(String phone){
        if (phone.length() != 11) return false;
        else{
            //移动号段正则表达式
            String pat1 = "^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}|(1705)\\d{7}$";
            //联通号段正则表达式
            String pat2  = "^((13[0-2])|(145)|(15[5-6])|(176)|(18[5,6]))\\d{8}|(1709)\\d{7}$";
            //电信号段正则表达式
            String pat3  = "^((133)|(153)|(177)|(18[0,1,9])|(149))\\d{8}$";
            //虚拟运营商正则表达式
            String pat4 = "^((170))\\d{8}|(1718)|(1719)\\d{7}$";

            return phoneParse(pat1, phone) || phoneParse(pat2, phone) || phoneParse(pat3, phone) || phoneParse(pat4, phone);
        }
    }

    public static boolean emailCheck(String email) {
        return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
    }

    public static Timestamp strToTimestamp(String timestamp) {
        return Timestamp.valueOf(timestamp);
    }
}
