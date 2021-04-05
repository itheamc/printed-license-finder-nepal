package com.itheamc.licensefinder.models;

public class Query {
    private String name;
    private String DlNo;

    // Constructor
    public Query() {
    }

    public Query(String name, String dlNo) {
        this.name = name;
        DlNo = dlNo;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDlNo() {
        return DlNo;
    }

    public void setDlNo(String dlNo) {
        DlNo = dlNo;
    }
}
