package com.itheamc.licensefinder.models;

public class User {
    private String key;
    private String value;

    // Constructor
    public User(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Getters and Setters

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //toString()

    @Override
    public String toString() {
        return "Datas{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
