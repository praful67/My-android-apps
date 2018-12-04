package com.Shootmyshow.praful.shootmyshow_company.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleAPI {
        @GET
    Call<String> getPath(@Url String url);
}
