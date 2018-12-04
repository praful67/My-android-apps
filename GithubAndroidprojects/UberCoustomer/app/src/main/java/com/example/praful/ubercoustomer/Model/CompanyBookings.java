package com.example.praful.ubercoustomer.Model;

public class CompanyBookings {


    String id;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    String eventType;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    String customerId;
    String customerName;
    String customerPhone;
    String customerAddress;
    String address;
    String date;
    String lat1, lng1;

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

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CompanyBookings(String id, String customerId, String customerName, String customerPhone, String customerAddress,
                           String address, String date,
                           String time, String lat1, String lng1, String eventType) {

        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.address = address;
        this.date = date;
        this.time = time;
        this.lat1 = lat1;
        this.lng1 = lng1;
        this.eventType = eventType;
    }

    public CompanyBookings() {

    }

    String time;

}
