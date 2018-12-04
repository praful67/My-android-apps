package com.dev.praful.trackyourdriver;

import android.widget.Switch;

public class CommonSwitch {
    public static Switch aSwitch , switch3;

    public static final String GoogleAPIUrl = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleService() {
        return GoogleMapAPI.getClient(GoogleAPIUrl).create(IGoogleAPI.class);
    }

}
