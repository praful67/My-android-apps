package com.example.praful.fcm;

import com.example.praful.fcm.Remote.APIService;
import com.example.praful.fcm.Remote.RetrofitClient;

import retrofit2.Retrofit;

public class Common {

    public static String currentToken = "";
    private static String baseURL ="https://fcm.googleapis.com/";

    public static APIService getFCMClient(){
        return RetrofitClient.getClient(baseURL).create(APIService.class);
    }
}
