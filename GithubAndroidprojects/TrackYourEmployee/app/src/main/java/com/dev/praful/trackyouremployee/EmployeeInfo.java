package com.dev.praful.trackyouremployee;

public class EmployeeInfo {

    String name , lat ,lng;

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

    public EmployeeInfo(String name, String lat, String lng) {

        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
}
