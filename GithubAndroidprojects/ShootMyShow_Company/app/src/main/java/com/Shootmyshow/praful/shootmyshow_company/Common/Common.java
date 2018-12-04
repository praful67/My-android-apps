package com.Shootmyshow.praful.shootmyshow_company.Common;

import android.location.Location;

import com.Shootmyshow.praful.shootmyshow_company.Model.User;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IFCMSerives;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IGoogleAPI;
import com.Shootmyshow.praful.shootmyshow_company.Remote.RetrofitClient;
import com.Shootmyshow.praful.shootmyshow_company.Remote.RetrofitClientGson;

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
