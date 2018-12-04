package com.dev2.tracker.TrackYourFriends;

import android.widget.Switch;

public class Common {

    public static final String GoogleAPIUrl = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleService() {
        return GoogleMapAPI.getClient(GoogleAPIUrl).create(IGoogleAPI.class);
    }

}
