package com.example.praful.fcm.Remote;

import com.example.praful.fcm.Model.Response;
import com.example.praful.fcm.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAxsIW2i8:APA91bF-v8Im6InPTJufU3BtMDKNOEq0K8SOntkRFLSPaOJEQkuk5UHCrgEGNR2Rt_Y9f-yVJRrXsLo_8l7iUlbO3O4yNoStsKCbqtG8bQFDT8mGc3VQRlnFnpM4ZPqPhUazEnQ9oeED"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
