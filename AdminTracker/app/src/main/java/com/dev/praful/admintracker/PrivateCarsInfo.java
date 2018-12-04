package com.dev.praful.admintracker;

public class PrivateCarsInfo {
    String name;
    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public PrivateCarsInfo() {

    }

    public PrivateCarsInfo(String name, String id, String carnumber) {

        this.name = name;
        this.id = id;
        this.carnumber = carnumber;
    }

    String carnumber;

}
