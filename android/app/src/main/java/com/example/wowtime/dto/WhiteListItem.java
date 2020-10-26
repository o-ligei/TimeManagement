package com.example.wowtime.dto;

import android.graphics.drawable.Drawable;

public class WhiteListItem {
    private Drawable image;
    private String appName;

    public WhiteListItem(Drawable image, String appName) {
        this.image = image;
        this.appName = appName;
    }
    public WhiteListItem() {

    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
