package com.example.wowtime.dto;

import android.graphics.drawable.Drawable;

public class WhiteListItem {

    private Drawable image;
    private String appName;
    private String packageName;
    private Boolean selected;

    public WhiteListItem(Drawable image, String appName, String packageName) {
        this.image = image;
        this.appName = appName;
        this.packageName = packageName;
        this.selected = false;
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

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public Boolean getSelected() {
        if (this.selected == null) {
            System.out.println("whitelistitem:null select when" + appName);
            return false;
        }
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
