package com.example.wowtime.entity;

public class AppListItem {
    private String name;
    private int allowed;
    private int icon;

    public AppListItem(){
    }

    public AppListItem(String name,int allowed,int icon){
        this.name = name;
        this.allowed = allowed;
        this.icon = icon;
    }

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public int getAllowed(){return this.allowed;}
    public void setAllowed(int allowed){this.allowed = allowed;}

    public int getIcon(){return icon;}
    public void setIcon(int icon){this.icon = icon;}
}
