package com.praful.praful.uber.Model;

public class CustomerBookings {


    String id;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    String eventType;
    String companyName;
    String companyPhone;
    String address;
    String date;
    String companyId;



    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public CustomerBookings(String id , String companyId, String companyName, String companyPhone,
                            String address, String date, String time , String eventType) {

        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyPhone = companyPhone;
        this.address = address;
        this.date = date;
        this.time = time;
        this.eventType = eventType;
    }

    public CustomerBookings() {

    }

    String time;

}
