package com.example.wowtime.dto;

public class TaskListItem {

    private String name;

    public TaskListItem() { }

    public TaskListItem(String name) {
        this.name = name;
    }

    public String getName() {return this.name;}

    public void setName(String name) {this.name = name;}
}
