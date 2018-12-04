package com.dev.praful.admintracker;

public class Rosteritem {
    String rank , employeename,pickuptime;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getPickuptime() {
        return pickuptime;
    }

    public void setPickuptime(String pickuptime) {
        this.pickuptime = pickuptime;
    }

    public Rosteritem() {

    }

    public Rosteritem(String rank, String employeename, String pickuptime) {

        this.rank = rank;
        this.employeename = employeename;
        this.pickuptime = pickuptime;
    }
}
