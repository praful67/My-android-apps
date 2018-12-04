package com.Shootmyshow.praful.shootmyshow_company.Remote;


import com.Shootmyshow.praful.shootmyshow_company.Model.DataMessage;
import com.Shootmyshow.praful.shootmyshow_company.Model.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMSerives {
 @Headers({

        "Content-Type:application/json",
        "Authorization:key=AAAAHmgDORw:APA91bG7-CIHX4Cg84MAGEL_XpZ_L_dC9a52r924GoMnJy9QXHL7K26XdiXV6aYSvWQ1eh4biDWaxky5vJQDWaAVRIxKdyCu1g2WguryscoU08MDJ9diAdV9KBfPE2Co-sjnbHaz9irT7X-BHHc7fQKQCCmOMBVOjQ"
 })

    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body DataMessage body);

}
