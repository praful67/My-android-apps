package com.dev.praful.trackyourdriver;

public class CarsInfo {
    String name, id, model, carnumber, listid;
    ;

    public CarsInfo() {
    }

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
        this.listid = listid;
    }

    public CarsInfo(String name, String id, String model, String carnumber, String listid) {
        this.name = name;
        this.id = id;
        this.model = model;
        this.carnumber = carnumber;
        this.listid = listid;
    }
}
