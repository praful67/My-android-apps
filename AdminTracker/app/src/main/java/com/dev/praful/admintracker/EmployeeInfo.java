package com.dev.praful.admintracker;

public class EmployeeInfo {

    String name , lat ,lng , id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EmployeeInfo(String name, String lat, String lng, String id) {

        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
    }

    public EmployeeInfo() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

}
