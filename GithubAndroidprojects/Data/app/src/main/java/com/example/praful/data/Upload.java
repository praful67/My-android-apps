package com.example.praful.data;

public class Upload {
    private String tip;
    private String title;
    private String des;
    private String url;

    public Upload(){

    }

    public String getTip() {
        return tip;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public String getUrl() {
        return url;
    }

    public Upload(String tip, String title, String des, String url) {
        this.tip = tip;
        this.title = title;
        this.des = des;
        this.url = url;
    }

}
