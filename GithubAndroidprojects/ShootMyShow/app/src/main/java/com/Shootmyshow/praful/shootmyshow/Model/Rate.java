package com.Shootmyshow.praful.shootmyshow.Model;

public class Rate {
    private String rates;

    public Rate() {
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Rate(String rates, String comments) {
        this.rates = rates;
        this.comments = comments;
    }

    private String comments;


}
