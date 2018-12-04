package com.dev.praful.trackyourdriver;

public class Consent {

    String  id;
    String consent;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Consent(String id, String consent, String date, String time) {

        this.id = id;
        this.consent = consent;
        this.date = date;
        this.time = time;
    }

    String time;

    public Consent() {
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }


}
