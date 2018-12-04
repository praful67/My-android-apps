package com.dev.praful.admintracker;

public class Comment {
    String name , comment ,dateofsubmit,id , stars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateofsubmit() {
        return dateofsubmit;
    }

    public void setDateofsubmit(String dateofsubmit) {
        this.dateofsubmit = dateofsubmit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment() {

    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public Comment(String name, String comment, String dateofsubmit, String id, String stars) {

        this.name = name;
        this.comment = comment;
        this.dateofsubmit = dateofsubmit;
        this.id = id;
        this.stars = stars;
    }
}
