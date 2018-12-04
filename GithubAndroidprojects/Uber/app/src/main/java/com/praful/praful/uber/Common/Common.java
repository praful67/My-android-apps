package com.praful.praful.uber.Common;

import android.location.Location;

import com.praful.praful.uber.Model.FCMResponse;
import com.praful.praful.uber.Model.User;
import com.praful.praful.uber.Remote.FCMClient;
import com.praful.praful.uber.Remote.IFCMSerives;
import com.praful.praful.uber.Remote.IGoogleAPI;
import com.praful.praful.uber.Remote.RetrofitClient;
import com.praful.praful.uber.Remote.RetrofitClientGson;

import retrofit2.Retrofit;

public class Common {


    public static final String companies_table = "Companies";
    public static final String companiesInfo_table = "CompaniesInfo";
    public static final String coustomers_table = "Coustomers";
    public static final String pickupRequests_table = "PickupRequest";
    public static final String token_table = "Tokens";
    public static final int PICK_IMAGE_REQ = 9999;
    public static final String user_field = "usr";
    public static final String pwd_field = "pwd";

    public static String userid;

    public static User currentUser;
    public static Location Lastlocation = null;
    public static final String baseURL = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleAPI() {
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

    public static final String fcmURL = "https://fcm.googleapis.com";

    public static IFCMSerives getFCMService() {
        return RetrofitClientGson.getClient(fcmURL).create(IFCMSerives.class);
    }
}
