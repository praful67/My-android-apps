package com.praful.praful.uber.Model;

import java.util.Map;

public class DataMessage {

    public String to;

    public DataMessage() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public DataMessage(String to, Map<String, String> data) {

        this.to = to;
        this.data = data;
    }

    public Map<String , String> data;


}
