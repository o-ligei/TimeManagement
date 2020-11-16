package com.oligei.timemanagement.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserIconDefault {

    public List<String> defaultUserIcon = new ArrayList<>();

    public String convertFileToBase64(String imgPath) {
        byte[] data = null;
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    public UserIconDefault() {
        for (int i=1;i<=5;i++)
            defaultUserIcon.add(convertFileToBase64("static/icon"+i+".jpg"));
    }
}
