package com.dev.praful.admintracker;

public class Rostersetlistitem {
    String name, id ,dates , dateofchange;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rostersetlistitem() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getDateofchange() {
        return dateofchange;
    }

    public void setDateofchange(String dateofchange) {
        this.dateofchange = dateofchange;
    }

    public Rostersetlistitem(String name, String id, String dates, String dateofchange) {

        this.name = name;
        this.id = id;
        this.dates = dates;
        this.dateofchange = dateofchange;
    }
}
