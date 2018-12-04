package com.Shootmyshow.praful.shootmyshow.Model;

public class AboutMe {

    public AboutMe() {
    }

    public String getAboutcompany() {

        return aboutcompany;
    }

    public void setAboutcompany(String aboutcompany) {
        this.aboutcompany = aboutcompany;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AboutMe(String aboutcompany, String link) {

        this.aboutcompany = aboutcompany;
        this.link = link;
    }

    String aboutcompany, link;
}
