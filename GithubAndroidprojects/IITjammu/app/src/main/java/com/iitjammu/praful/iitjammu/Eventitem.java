package com.iitjammu.praful.iitjammu;

public class Eventitem {

    String imageurl, description;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Eventitem() {

    }

    public Eventitem(String imageurl, String description) {

        this.imageurl = imageurl;
        this.description = description;
    }
}
