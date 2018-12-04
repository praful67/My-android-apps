package com.dev.praful.trackyourdriver;

public class Orderedemployeedetails {
    String name;
    String distance;
    String id;

    public Orderedemployeedetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Orderedemployeedetails(String name, String distance, String id) {

        this.name = name;
        this.distance = distance;
        this.id = id;
    }
}
