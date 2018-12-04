package com.dev.praful.admintracker;

import android.widget.Switch;

public class CommonSwitch {
    public static final int PICK_IMAGE_REQ = 9999;
    public static Switch switch1;
    public static Switch switch2;
    public static final String GoogleAPIUrl = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleService() {
        return GoogleMapAPI.getClient(GoogleAPIUrl).create(IGoogleAPI.class);
    }

}
