package com.example.praful.ubercoustomer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Model.Bookings;
import com.example.praful.ubercoustomer.Model.DataMessage;
import com.example.praful.ubercoustomer.Model.FCMResponse;
import com.example.praful.ubercoustomer.Model.Token;
import com.example.praful.ubercoustomer.Remote.IFCMSerives;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedWindow extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Button btnDone;
    String customerId;
    IFCMSerives ifcmSerives;

    String lat , userid, lng, EventType,Address, Time, Date, companyPhone, companyName,companyRates,companyId , Bookingid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_window);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        userid = pref.getString("Id", "");

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        if (getIntent() != null) {
             Time = getIntent().getStringExtra("Time");
            companyName = getIntent().getStringExtra("companyName");
            Date = getIntent().getStringExtra("Date");
            companyPhone = getIntent().getStringExtra("companyPhone");
            Address = getIntent().getStringExtra("Address");
            companyRates = getIntent().getStringExtra("CompanyRates");
            companyId = getIntent().getStringExtra("companyId");
            Bookingid = getIntent().getStringExtra("Bookingid");
            EventType = getIntent().getStringExtra("EventType");

        }

        acceptBooking();

    }

    private void acceptBooking() {

        Bookings bookings = new Bookings(Bookingid, companyId ,companyName, companyPhone, Address, Date, Time , EventType);
        databaseReference.child("coustomerBookings").child(userid).child(bookings.getId()).setValue(bookings);


    }

}
