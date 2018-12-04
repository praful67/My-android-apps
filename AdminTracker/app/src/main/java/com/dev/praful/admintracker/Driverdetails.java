package com.dev.praful.admintracker;

public class Driverdetails {
    String username;
    String password;
    String phone;
    String drivinglicense;
    String company;

    public Driverdetails(String username, String password, String phone, String drivinglicense, String company, String address, String addresslat, String addresslng, String id) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.drivinglicense = drivinglicense;
        this.company = company;
        this.address = address;
        this.addresslat = addresslat;
        this.addresslng = addresslng;
        this.id = id;
    }

    String address;

    public String getAddresslat() {
        return addresslat;
    }

    public void setAddresslat(String addresslat) {
        this.addresslat = addresslat;
    }

    public String getAddresslng() {
        return addresslng;
    }

    public void setAddresslng(String addresslng) {
        this.addresslng = addresslng;
    }

    String addresslat;
    String addresslng;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDrivinglicense() {
        return drivinglicense;
    }

    public void setDrivinglicense(String drivinglicense) {
        this.drivinglicense = drivinglicense;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Driverdetails() {

    }



    String id;


}
