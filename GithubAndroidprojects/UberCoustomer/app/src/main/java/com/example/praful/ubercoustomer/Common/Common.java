package com.example.praful.ubercoustomer.Common;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.praful.ubercoustomer.AcceptedWindow;
import com.example.praful.ubercoustomer.Home;
import com.example.praful.ubercoustomer.Model.Coustomers;
import com.example.praful.ubercoustomer.Model.DataMessage;
import com.example.praful.ubercoustomer.Model.FCMResponse;
import com.example.praful.ubercoustomer.Model.Token;
import com.example.praful.ubercoustomer.R;
import com.example.praful.ubercoustomer.Remote.FCMClient;
import com.example.praful.ubercoustomer.Remote.GoogleMapAPI;
import com.example.praful.ubercoustomer.Remote.IFCMSerives;
import com.example.praful.ubercoustomer.Remote.IGoogleAPI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Common {

    public static Coustomers currentUser = new Coustomers();
    public static final String companies_table = "Companies";
    public static final String coustomers_table = "Coustomers";
    public static final String pickupRequests_table = "PickupRequest";
    public static final String AdvanceBooking_table = "Advnace_Bookings";
    public static final String companiesInfo_table = "CompaniesInfo";
    public static final String token_table = "Tokens";
    public static String check = "";
    public static final String rate_company_tbl = "RateDetails";
    public static boolean isCompanyFound = false;
    public static String companyId = "";
    public static final int PICK_IMAGE_REQ = 9999;
    public static final String user_field = "usr";
    public static final String pwd_field = "pwd";

    public static Location Lastlocation = null;

    public static String userid;
    public static final String fcmURL = "https://fcm.googleapis.com";
    public static final String GoogleAPIUrl = "https://maps.googleapis.com";

    public static IFCMSerives getFCMService() {
        return FCMClient.getClient(fcmURL).create(IFCMSerives.class);
    }

    public static IGoogleAPI getGoogleService() {
        return GoogleMapAPI.getClient(GoogleAPIUrl).create(IGoogleAPI.class);
    }

    public static void sendrequesttocompany(String companyId, final IFCMSerives ifcmSerives, final Context context, final Location currentlocation) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Common.token_table);

        tokens.orderByKey().equalTo(companyId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot post : dataSnapshot.getChildren()) {
                            Token token = post.getValue(Token.class);

                            // String json_lat_lng = new Gson().toJson(new LatLng(location.getLatitude(), location.getLongitude()));

                            String customertoken = FirebaseInstanceId.getInstance().getToken();
                            // Notification data = new Notification( FirebaseInstanceId.getInstance().getToken(), json_lat_lng);
                            // Sender content = new Sender(token.getToken() , data);

                            Map<String, String> content = new HashMap<>();
                            content.put("Type", "InstantBooking");
                            content.put("customer", customertoken);
                            content.put("lat", String.valueOf(currentlocation.getLatitude()));
                            content.put("lng", String.valueOf(currentlocation.getLongitude()));
                            DataMessage dataMessage = new DataMessage(token.getToken(), content);

                            ifcmSerives.sendMessage(dataMessage)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body().success == 1)
                                                Toast.makeText(context, "Request sent!", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(context, "Failed to send the Request!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {

                                            Log.e("ERROR", t.getMessage());

                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static void sendrequesttocompanyForAdvanceBooking(String companyId,
                                                             final IFCMSerives ifcmSerives, final Context context, final Location currentlocation,
                                                             final String Name, final String Phone,
                                                             final String Date, final String Time,
                                                             final String Address) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Common.token_table);

        tokens.orderByKey().equalTo(companyId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot post : dataSnapshot.getChildren()) {
                            Token token = post.getValue(Token.class);

                            // String json_lat_lng = new Gson().toJson(new LatLng(location.getLatitude(), location.getLongitude()));

                            String customertoken = FirebaseInstanceId.getInstance().getToken();
                            // Notification data = new Notification( FirebaseInstanceId.getInstance().getToken(), json_lat_lng);
                            // Sender content = new Sender(token.getToken() , data);

                            Map<String, String> content = new HashMap<>();
                            content.put("Type", "AdvanceBooking");
                            content.put("Date", Date);
                            content.put("Time", Time);
                            content.put("Address", Address);
                            content.put("CustomerName", Name);
                            content.put("CustomerPhone", Phone);
                            content.put("customer", customertoken);
                            content.put("lat", String.valueOf(currentlocation.getLatitude()));
                            content.put("lng", String.valueOf(currentlocation.getLongitude()));
                            DataMessage dataMessage = new DataMessage(token.getToken(), content);

                            ifcmSerives.sendMessage(dataMessage)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body().success == 1) {
                                                Toast.makeText(context, "Request sent!", Toast.LENGTH_SHORT).show();

                                            } else
                                                Toast.makeText(context, "Failed to send the Request!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {

                                            Log.e("ERROR", t.getMessage());

                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


}
