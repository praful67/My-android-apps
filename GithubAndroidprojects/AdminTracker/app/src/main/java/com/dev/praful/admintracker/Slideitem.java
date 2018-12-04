package com.dev.praful.admintracker;

public class Slideitem {
    Integer integer ;
    String text;

    public Slideitem() {
    }

    public Integer getInteger() {

        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Slideitem(Integer integer, String text) {

        this.integer = integer;
        this.text = text;
    }
}
