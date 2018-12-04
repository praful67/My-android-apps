package com.Shootmyshow.praful.shootmyshow.Model;

public class User {

    private String phone;
    private String name;
    private String rates;
    private String avatarUrl;
    private String numberofsuccessfulbookings;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNumberofsuccessfulbookings() {
        return numberofsuccessfulbookings;
    }

    public void setNumberofsuccessfulbookings(String numberofsuccessfulbookings) {
        this.numberofsuccessfulbookings = numberofsuccessfulbookings;
    }

    public User() {

    }

    public User(String phone, String name, String rates, String avatarUrl, String numberofsuccessfulbookings) {

        this.phone = phone;
        this.name = name;
        this.rates = rates;
        this.avatarUrl = avatarUrl;
        this.numberofsuccessfulbookings = numberofsuccessfulbookings;
    }
}
