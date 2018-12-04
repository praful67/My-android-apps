package com.Shootmyshow.praful.shootmyshow;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.Shootmyshow.praful.shootmyshow.Model.Bookings;
import com.Shootmyshow.praful.shootmyshow.Remote.IFCMSerives;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


       /* if (getIntent() != null) {
             Time = getIntent().getStringExtra("Time");
            companyName = getIntent().getStringExtra("companyName");
            Date = getIntent().getStringExtra("Date");
            companyPhone = getIntent().getStringExtra("companyPhone");
            Address = getIntent().getStringExtra("Address");
            companyRates = getIntent().getStringExtra("CompanyRates");
            companyId = getIntent().getStringExtra("companyId");
            Bookingid = getIntent().getStringExtra("Bookingid");
            EventType = getIntent().getStringExtra("EventType");

        }*/

        /*acceptBooking();*/

    }

    /*private void acceptBooking() {

        Bookings bookings = new Bookings(Bookingid, companyId ,companyName, companyPhone, Address, Date, Time , EventType);
        databaseReference.child("coustomerBookings").child(userid).child(bookings.getId()).setValue(bookings);


    }*/

}
