package com.Shootmyshow.praful.shootmyshow;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow.Model.Bookings;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Onthewayandimreached extends AppCompatActivity {

    TextView message, eventtype, address, date, time;
    Button home;
    String BookingIdT;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ontheway);
        message = (TextView) findViewById(R.id.message);
        home = (Button) findViewById(R.id.home);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        eventtype = (TextView) findViewById(R.id.eventtype);
        address = (TextView) findViewById(R.id.addressevent);
        date = (TextView) findViewById(R.id.dateevent);
        time = (TextView) findViewById(R.id.timeevent);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        if (getIntent() != null) {
            BookingIdT = getIntent().getStringExtra("BookingIdT");
            message.setText(getIntent().getStringExtra("message"));
        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        load();
        final TextView bookingdate = (TextView) findViewById(R.id.bookingdate);
        final TextView bookingtime = (TextView) findViewById(R.id.bookingtime);


        FirebaseDatabase.getInstance().getReference("BookingsDateandTime")
                .child(BookingIdT).child("Date")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        bookingdate.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference("BookingsDateandTime")
                .child(BookingIdT).child("Time")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        bookingtime.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        final InterstitialAd interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial));
        interstitialAd.loadAd(adRequest);
        interstitialAd.show();

        interstitialAd.setAdListener(new AdListener()

        {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                interstitialAd.show();
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

    }

    private void load() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String userid = pref.getString("Id", "");

        databaseReference.child("coustomerBookings").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals(BookingIdT)) {
                        Bookings bookings = dataSnapshot1.getValue(Bookings.class);
                        date.setText(bookings.getDate());
                        time.setText(bookings.getTime());
                        address.setText(bookings.getAddress());
                        eventtype.setText(bookings.getEventType());

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
