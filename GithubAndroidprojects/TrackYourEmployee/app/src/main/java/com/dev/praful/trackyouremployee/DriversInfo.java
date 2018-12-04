package com.dev.praful.trackyouremployee;

public class DriversInfo {
    String name, lat, lng , id;

    public DriversInfo() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DriversInfo(String name, String lat, String lng, String id) {

        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
    }
}
