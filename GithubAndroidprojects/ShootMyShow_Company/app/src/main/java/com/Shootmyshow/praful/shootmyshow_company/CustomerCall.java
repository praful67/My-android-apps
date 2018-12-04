package com.Shootmyshow.praful.shootmyshow_company;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.Shootmyshow.praful.shootmyshow_company.Common.Common;
import com.Shootmyshow.praful.shootmyshow_company.Model.DataMessage;
import com.Shootmyshow.praful.shootmyshow_company.Model.FCMResponse;
import com.Shootmyshow.praful.shootmyshow_company.Model.Token;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IFCMSerives;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IGoogleAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCall extends AppCompatActivity {

    TextView textTime , textAddress , textDistance;
    MediaPlayer mediaPlayer;
    IGoogleAPI services;
    Button btnCancel , btnAccept;
    String customerId;
    IFCMSerives ifcmSerives;

    String lat ,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call);

        services = Common.getGoogleAPI();
        ifcmSerives = Common.getFCMService();

        textAddress = (TextView)findViewById(R.id.textAddress);
        textTime = (TextView)findViewById(R.id.textTime);
        textDistance = (TextView)findViewById(R.id.textDistance);

        btnAccept = (Button)findViewById(R.id.btnAccept);
        btnCancel = (Button)findViewById(R.id.btnDecline);

        if(getIntent() != null)
        {
            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");
            customerId = getIntent().getStringExtra("customer");

            getDirection(lat , lng);
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if (!TextUtils.isEmpty(customerId))
                    cancelBooking(customerId);
                //cancelBooking(Common.customer);

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // acceptBooking(customerId);
                Intent intent = new Intent(CustomerCall.this, Companytracking.class);
                intent.putExtra("lat" , lat);
                intent.putExtra("lng" , lng);
                intent.putExtra("customerId" , customerId);
                startActivity(intent);
                finish();
            }
        });


        mediaPlayer = MediaPlayer.create(this ,R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

      //getDirection(Common.lat , Common.lng);


    }

    private void acceptBooking(String customerId) {
        Token token = new Token(customerId);
        // Notification notification = new Notification("Cancel" , "Company has cancelled your Request");
        // Sender sender = new Sender(token.getToken() , notification);

        Map<String , String> content = new HashMap<>();
        content.put("title" , "Accept");
        content.put("message" , "Company has accepted your Request");

        DataMessage dataMessage = new DataMessage(token.getToken() , content);
        ifcmSerives.sendMessage(dataMessage)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1)
                        {
                            Toast.makeText(CustomerCall.this , "Accepted" , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });
    }

    private void cancelBooking(String customerId) {

        Token token = new Token(customerId);
      // Notification notification = new Notification("Cancel" , "Company has cancelled your Request");
      // Sender sender = new Sender(token.getToken() , notification);

        Map<String , String> content = new HashMap<>();
        content.put("title" , "Cancel");
        content.put("message" , "Company has cancelled your Request");

       DataMessage dataMessage = new DataMessage(token.getToken() , content);
        ifcmSerives.sendMessage(dataMessage)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1)
                        {
                            Toast.makeText(CustomerCall.this , "Cancelled" , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });
    }

    private void getDirection(String  lat , String lng) {


        String requestApi =null;
        try{

            requestApi = "https://maps.googleapis.com/maps/api/directions/json?"+"mode=driving&"+
                    "transit_routing_preference=less_driving&"+"origin="+ Common.Lastlocation.getLatitude()+","+Common.Lastlocation.getLongitude()+"&"+
                    "destination="+lat+","+lng+"&"+
                    "key="+getResources().getString(R.string.google_direction_api);
            Log.d("C A M E R A N", requestApi);
            services.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        JSONArray routes = jsonObject.getJSONArray("routes");

                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");

                        JSONObject legsobject = legs.getJSONObject(0);


                        JSONObject distance = legsobject.getJSONObject("distance");
                        textDistance.setText(distance.getString("text"));


                        JSONObject time = legsobject.getJSONObject("duration");
                        textTime.setText(time.getString("text"));


                        String address = legsobject.getString("end_address");
                        textAddress.setText(address);





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(CustomerCall.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        if (mediaPlayer.isPlaying())
        mediaPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer !=null && !mediaPlayer.isPlaying())
        mediaPlayer.start();

    }
}
