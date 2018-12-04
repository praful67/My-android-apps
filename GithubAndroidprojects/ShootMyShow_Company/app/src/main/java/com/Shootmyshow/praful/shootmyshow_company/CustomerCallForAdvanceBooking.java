package com.Shootmyshow.praful.shootmyshow_company;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow_company.Model.Bookings;
import com.Shootmyshow.praful.shootmyshow_company.Model.CustomerBookings;
import com.Shootmyshow.praful.shootmyshow_company.Model.DataMessage;
import com.Shootmyshow.praful.shootmyshow_company.Model.FCMResponse;
import com.Shootmyshow.praful.shootmyshow_company.Model.Presentbooking;
import com.Shootmyshow.praful.shootmyshow_company.Model.Token;
import com.Shootmyshow.praful.shootmyshow_company.Model.User;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IFCMSerives;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IGoogleAPI;
import com.Shootmyshow.praful.shootmyshow_company.Remote.RetrofitClient;
import com.Shootmyshow.praful.shootmyshow_company.Remote.RetrofitClientGson;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCallForAdvanceBooking extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Presentbooking presentbooking;
    TextView textTime, textAddress, textDate, txtrequestfrom, txtcustomerName, txtcustomerPhone, txtTime, txtDistance;
    MediaPlayer mediaPlayer;
    IGoogleAPI services;
    Geocoder geocoder1;
    List<android.location.Address> addresses1;

    Button btnCancel, btnAccept;
    String customerId;
    IFCMSerives ifcmSerives;
    String companyname = "", CompanyPhone = "", companyrates = "", userid;

    String lat, lat1, lng1, lng, Address, Time, Date, customerName, customerPhone, addressofcustomer, EventType, CustomerId;
    public static final String baseURL = "https://maps.googleapis.com";

    public static IGoogleAPI getGoogleAPI() {
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

    public static final String fcmURL = "https://fcm.googleapis.com";

    public static IFCMSerives getFCMService() {
        return RetrofitClientGson.getClient(fcmURL).create(IFCMSerives.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call_for_advance_booking);
        services = getGoogleAPI();
        ifcmSerives = getFCMService();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final TextView eventtype = (TextView) findViewById(R.id.eventtype);
        textAddress = (TextView) findViewById(R.id.Address);
        textTime = (TextView) findViewById(R.id.Time);
        textDate = (TextView) findViewById(R.id.Date);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDistance = (TextView) findViewById(R.id.txtDistance);

        txtcustomerName = (TextView) findViewById(R.id.customerName);
        txtcustomerPhone = (TextView) findViewById(R.id.customerPhone);
        txtrequestfrom = (TextView) findViewById(R.id.Addressofcustomer);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);


        if (getIntent() != null) {

            if (getIntent().getStringExtra("N") != null && (getIntent().getStringExtra("N").equals("N"))) {
                FirebaseDatabase.getInstance().getReference().child("PresentBookings")
                        .child(pref.getString("Id", ""))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                    presentbooking = dataSnapshot1.getValue(Presentbooking.class);

                                if (presentbooking != null) {

                                    lat = presentbooking.getLat();
                                    lat1 = presentbooking.getLat1();
                                    Time = presentbooking.getTime();
                                    Date = presentbooking.getDate();
                                    Address = presentbooking.getAddress();
                                    EventType = presentbooking.getEventType();
                                    CustomerId = presentbooking.getCustomerId();
                                    customerId = presentbooking.getCustomertoken();
                                    customerName = presentbooking.getCustomerName();
                                    customerPhone = presentbooking.getCustomerPhone();
                                    lng = presentbooking.getLng();
                                    lng1 = presentbooking.getLng1();
                                   // getDirection(lat1, lng1);
                                    details(lat , lng , lat1,lng1);

                                    eventtype.setText(EventType);
                                    textAddress.setText(Address);
                                    textDate.setText(Date);
                                    textTime.setText(Time);
                                    txtcustomerName.setText(customerName);
                                    txtcustomerPhone.setText(customerPhone);
                                } else {
                                    finish();
                                    Toast.makeText(CustomerCallForAdvanceBooking.this, "You have responded to this already", Toast.LENGTH_LONG).show();
                                }

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            } else {
                lat1 = getIntent().getStringExtra("lat1");
                lat = getIntent().getStringExtra("lat");
                lng1 = getIntent().getStringExtra("lng1");
                lng = getIntent().getStringExtra("lng");
                Time = getIntent().getStringExtra("Time");
                customerName = getIntent().getStringExtra("customerName");
                Date = getIntent().getStringExtra("Date");
                customerPhone = getIntent().getStringExtra("customerPhone");
                Address = getIntent().getStringExtra("Address");
                EventType = getIntent().getStringExtra("EventType");
                customerId = getIntent().getStringExtra("customer");
                CustomerId = getIntent().getStringExtra("customerId");
                getDirection(lat1, lng1);

                eventtype.setText(EventType);
                textAddress.setText(Address);
                textDate.setText(Date);
                textTime.setText(Time);
                txtcustomerName.setText(customerName);
                txtcustomerPhone.setText(customerPhone);

            }
        }

        btnAccept = (Button) findViewById(R.id.btnAccepAdvanceBooking);
        btnCancel = (Button) findViewById(R.id.btnDeclineAdvanceBooking);

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
                if (!TextUtils.isEmpty(customerId))
                    acceptBooking(customerId);

            }
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();


        userid = pref.getString("Id", "");

        FirebaseDatabase.getInstance().getReference("CompaniesInfo")
                .child(userid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        companyname = user.getName();
                        companyrates = user.getRates();
                        CompanyPhone = user.getPhone();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void details(String lat, String lng, String lat1, String lng1) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        geocoder1 = new Geocoder(CustomerCallForAdvanceBooking.this, Locale.getDefault());

        try {
            addresses1 = geocoder1.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);


            if (addresses1 != null && addresses1.size() > 0) {
                txtrequestfrom.setText(addresses1.get(0).getAddressLine(0));

            } else {
               txtrequestfrom.setText("Customer's address is not accessible");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Location locationA=new Location("A");
        locationA.setLatitude(Double.parseDouble(lat1));
        locationA.setLongitude(Double.parseDouble(lng1));

        Location locationB = new Location("B");
        locationB.setLatitude(Double.parseDouble(pref.getString("latitude" ,"")));
        locationB.setLongitude(Double.parseDouble(pref.getString("longitude" , "")));


        txtDistance.setText(String.format("%.2f",locationA.distanceTo(locationB)/1000) + " km");
        float time = ((locationA.distanceTo(locationB)/1000 )/40)*60;
        txtTime.setText(String.format("%.2f" , time) + " min");


    }

    private void acceptBooking(final String customerId) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.show();

        Token token = new Token(customerId);
        // Notification notification = new Notification("Cancel" , "Company has cancelled your Request");
        // Sender sender = new Sender(token.getToken() , notification);
        final Bookings bookings = new Bookings(UUID.randomUUID().toString(), CustomerId, customerName,
                customerPhone, addressofcustomer,
                Address, Date, Time, lat1, lng1, EventType);

        Map<String, String> content = new HashMap<>();
        content.put("title", "Accept");
        content.put("Date", Date);
        content.put("Time", Time);
        content.put("Address", Address);
        content.put("CompanyName", companyname);
        //  content.put("CompanyName", Common.currentUser.getName());
        // content.put("CompanyId", Common.userid);
        content.put("CompanyId", userid);
        //   content.put("CompanyPhone", Common.currentUser.getPhone());
        content.put("CompanyPhone", CompanyPhone);
        content.put("EventType", EventType);
        content.put("CompanyRates", companyrates);
        //   content.put("CompanyRates", Common.currentUser.getRates());
        content.put("message", "Company has accepted your Request");
        content.put("Bookingid", bookings.getId());
        DataMessage dataMessage = new DataMessage(token.getToken(), content);
        ifcmSerives.sendMessage(dataMessage)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1) {

                            CustomerBookings customerbookings = new CustomerBookings(bookings.getId(), userid, companyname, CompanyPhone, Address, Date, Time, EventType);
                            databaseReference.child("coustomerBookings").child(CustomerId).child(bookings.getId()).setValue(customerbookings);

                            dialog.dismiss();
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Responses")
                                    .child(CustomerId)
                                    .child("companyResponse").removeValue();

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Responses")
                                    .child(CustomerId)
                                    .child("companyResponse")
                                    .setValue("Congrats , the studio " + companyname + " has ACCEPTED your Booking . Please check your Bookings. ");

                            FirebaseDatabase.getInstance().getReference().child("PresentBookings")
                                    .child(pref.getString("Id", "")).removeValue();
                            Intent intent = new Intent(CustomerCallForAdvanceBooking.this, Acceptreqwindow.class);
                            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(CustomerCallForAdvanceBooking.this, "Accepted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });

        databaseReference.child("Bookings").child(userid).child(bookings.getId()).setValue(bookings);
        // databaseReference.child("Bookings").child(bookings.getId()).setValue(bookings);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        String monthh = null;

        switch (month) {
            case 1:
                monthh = "January";
                break;
            case 2:
                monthh = "February";
                break;
            case 3:
                monthh = "March";
                break;
            case 4:
                monthh = "April";
                break;
            case 5:
                monthh = "May";
                break;
            case 6:
                monthh = "June";
                break;
            case 7:
                monthh = "July";
                break;
            case 8:
                monthh = "August";
                break;
            case 9:
                monthh = "September";
                break;
            case 10:
                monthh = "October";
                break;
            case 11:
                monthh = "November";
                break;
            case 12:
                monthh = "December";

                break;


        }
        String date = monthh + " " + day_of_month + " , " + year;
        Calendar mcurrentTime = Calendar.getInstance();

        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        String AM_PM;
        if (hour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }
        if (hour > 12)
            hour = hour - 12;


        String time = hour + " : " + minute + " " + AM_PM;


        Map<String, Object> map = new HashMap<>();
        map.put("Date", date);
        map.put("Time", time);


        databaseReference.child("BookingsDateandTime").child(bookings.getId()).setValue(map);


    }

    private void cancelBooking(final String customerId) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.show();

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        Token token = new Token(customerId);
        // Notification notification = new Notification("Cancel" , "Company has cancelled your Request");
        // Sender sender = new Sender(token.getToken() , notification);

        Map<String, String> content = new HashMap<>();
        content.put("title", "Cancel");
        content.put("message", "Company has cancelled your Request");

        DataMessage dataMessage = new DataMessage(token.getToken(), content);
        ifcmSerives.sendMessage(dataMessage)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Responses")
                                    .child(CustomerId)
                                    .child("companyResponse").removeValue();

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Responses")
                                    .child(CustomerId)
                                    .child("companyResponse")
                                    .setValue("Sorry , the studio " + companyname + " has DECLINED your Booking . Please try again later. ");
                            FirebaseDatabase.getInstance().getReference().child("PresentBookings")
                                    .child(pref.getString("Id", "")).removeValue();
                            dialog.dismiss();
                            Intent intent = new Intent(CustomerCallForAdvanceBooking.this, CancelreqWindow.class);
                            startActivity(intent);
                            Toast.makeText(CustomerCallForAdvanceBooking.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });
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
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.start();

    }


    private void getDirection(String lat, String lng) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        String latitude = pref.getString("latitude", "");
        String longitude = pref.getString("longitude", "");
        String requestApi = null;
        try {

          /* requestApi = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" + Common.Lastlocation.getLatitude() + "," + Common.Lastlocation.getLongitude() + "&" +
                    "destination=" + lat + "," + lng + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api); */
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" + latitude + "," + longitude + "&" +
                    "destination=" + lat + "," + lng + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);

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
                        txtDistance.setText(distance.getString("text"));


                        JSONObject time = legsobject.getJSONObject("duration");
                        txtTime.setText(time.getString("text"));


                        addressofcustomer = legsobject.getString("end_address");
                        txtrequestfrom.setText(addressofcustomer);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(CustomerCallForAdvanceBooking.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
