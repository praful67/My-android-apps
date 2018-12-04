package com.example.praful.blog;

public class ImageUpload {

    public String name;
    public String url;

    public ImageUpload(String name, String tip, String des , String url) {
        this.name = name;
        this.url = url;
        this.tip = tip;
        this.des = des;
    }

    public String tip;public String des;

    public String getTip() {
        return tip;
    }

    public String getDes() {
        return des;
    }



    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ImageUpload(){}
}
