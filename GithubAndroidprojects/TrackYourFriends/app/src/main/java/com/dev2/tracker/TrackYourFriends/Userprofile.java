package com.dev2.tracker.TrackYourFriends;

public class Userprofile {
    String username , face,id,about;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Userprofile() {

    }

    public Userprofile(String username, String face, String id, String about) {

        this.username = username;
        this.face = face;
        this.id = id;
        this.about = about;
    }
}
