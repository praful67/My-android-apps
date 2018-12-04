package com.dev.praful.trackyouremployee;

public class CarsInfo {
    String name;
    String id, listid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CarsInfo() {

    }

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
        this.listid = listid;
    }

    public CarsInfo(String name, String id, String text, String listid) {

        this.name = name;
        this.id = id;
        this.listid = listid;
        this.text = text;
    }

    String text;


}
