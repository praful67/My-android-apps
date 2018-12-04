package com.example.praful.presencesystem;

public class Tracking {
    String lat , lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Tracking() {
    }

    public String getLng() {

        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Tracking(String lat, String lng) {

        this.lat = lat;
        this.lng = lng;
    }
}
