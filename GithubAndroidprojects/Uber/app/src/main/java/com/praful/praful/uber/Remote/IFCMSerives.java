package com.praful.praful.uber.Remote;

import com.praful.praful.uber.Model.DataMessage;
import com.praful.praful.uber.Model.FCMResponse;
import com.praful.praful.uber.Model.Sender;
import com.google.firebase.database.DatabaseReference;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;

public interface IFCMSerives {
 @Headers({

        "Content-Type:application/json",
        "Authorization:key=AAAARjafq38:APA91bF7vG_UXnev9a1RSraTfZZMiQm5fCBNA9wirlaUePBPEEmgrBp16REqEbReQ8_m9XUQUWGerNyEEQWR-2YVt1ZAII8adzXrddsaGk0fAfjxT_NN2v8OaMjelfDybl_rIC3Q7trK"
 })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body DataMessage body);

}
