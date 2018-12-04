package com.Shootmyshow.praful.shootmyshow.Common;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow.Model.DataMessage;
import com.Shootmyshow.praful.shootmyshow.Model.FCMResponse;
import com.Shootmyshow.praful.shootmyshow.Model.Token;
import com.Shootmyshow.praful.shootmyshow.Model.Coustomers;
import com.Shootmyshow.praful.shootmyshow.Remote.FCMClient;
import com.Shootmyshow.praful.shootmyshow.Remote.GoogleMapAPI;
import com.Shootmyshow.praful.shootmyshow.Remote.IFCMSerives;
import com.Shootmyshow.praful.shootmyshow.Remote.IGoogleAPI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
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

    public static Switch Switch;

    public static Location Lastlocation = null;

    public static String userid;
    public static final String fcmURL = "https://fcm.googleapis.com";

    public static IFCMSerives getFCMService() {
        return FCMClient.getClient(fcmURL).create(IFCMSerives.class);
    }
    public static final String GoogleAPIUrl = "https://maps.googleapis.com";

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
