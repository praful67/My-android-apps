package com.example.praful.ubercoustomer.Remote;

import com.example.praful.ubercoustomer.Model.DataMessage;
import com.example.praful.ubercoustomer.Model.FCMResponse;
import com.example.praful.ubercoustomer.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMSerives {
 @Headers({

        "Content-Type:application/json",
        "Authorization:key=AAAARjafq38:APA91bF7vG_UXnev9a1RSraTfZZMiQm5fCBNA9wirlaUePBPEEmgrBp16REqEbReQ8_m9XUQUWGerNyEEQWR-2YVt1ZAII8adzXrddsaGk0fAfjxT_NN2v8OaMjelfDybl_rIC3Q7trK"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body DataMessage body);

}
