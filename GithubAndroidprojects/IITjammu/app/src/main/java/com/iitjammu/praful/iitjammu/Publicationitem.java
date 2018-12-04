package com.iitjammu.praful.iitjammu;

public class Publicationitem {
    String imageurl;

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

    public Publicationitem() {

    }

    public Publicationitem(String imageurl, String description) {

        this.imageurl = imageurl;
        this.description = description;
    }

    String description;

}
