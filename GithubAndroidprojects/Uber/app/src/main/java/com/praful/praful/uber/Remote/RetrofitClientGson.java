package com.praful.praful.uber.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientGson {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseURL)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }
}
