package com.example.praful.presencesystem;

public class User {

    public User() {
    }

   private String status , name;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name, String status) {

        this.status = status;
        this.name = name;
    }
}
