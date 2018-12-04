package com.example.praful.fcm.Model;

public class Notification {
    public String body;

    public Notification(String body, String title) {

        this.body = body;
        this.title = title;
    }

    public String title;
}
