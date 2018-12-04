package com.praful.praful.uber.Model;

public class Presentbooking {

    String eventType;
    String time;
    String customerId;
    String customerName;
    String customerPhone;
    String address;

    public Presentbooking() {
    }

    public String getEventType() {

        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLat1() {
        return lat1;
    }

    public void setLat1(String lat1) {
        this.lat1 = lat1;
    }

    public String getLng1() {
        return lng1;
    }

    public void setLng1(String lng1) {
        this.lng1 = lng1;
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

    public String getCustomertoken() {
        return customertoken;
    }

    public void setCustomertoken(String customertoken) {
        this.customertoken = customertoken;
    }

    public Presentbooking(String eventType, String time, String customerId, String customerName, String customerPhone, String address, String date, String lat1, String lng1, String lat, String lng, String customertoken) {

        this.eventType = eventType;
        this.time = time;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.address = address;
        this.date = date;
        this.lat1 = lat1;
        this.lng1 = lng1;
        this.lat = lat;
        this.lng = lng;
        this.customertoken = customertoken;
    }

    String date;
    String lat1, lng1;
    String lat, lng, customertoken;

}
