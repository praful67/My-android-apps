package com.praful.praful.uber.Model;

public class Trackingmessage {
    String bookingid, message;

    public Trackingmessage() {
    }

    public String getBookingid() {

        return bookingid;
    }

    public void setBookingid(String bookingid) {
        this.bookingid = bookingid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Trackingmessage(String bookingid, String message) {

        this.bookingid = bookingid;
        this.message = message;
    }
}
