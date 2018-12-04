package com.dev.praful.admintracker;

public class Timeoff {
    String date;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timeoff(String date, String name, String dateofsubmit, String message, String id) {

        this.date = date;
        this.name = name;
        this.dateofsubmit = dateofsubmit;
        this.message = message;
        this.id = id;
    }

    String dateofsubmit;
    String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public Timeoff() {
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateofsubmit() {
        return dateofsubmit;
    }

    public void setDateofsubmit(String dateofsubmit) {
        this.dateofsubmit = dateofsubmit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
