package com.itheamc.licensefinder.models;

public class Notice {
    private String title;
    private String desc;

    // Constructor
    public Notice() {
    }

    public Notice(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
