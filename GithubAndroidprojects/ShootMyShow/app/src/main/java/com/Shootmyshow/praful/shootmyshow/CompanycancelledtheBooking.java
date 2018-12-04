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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanycancelledtheBooking extends AppCompatActivity {

    TextView TimeCB, DateCB, AddressCB, messageCB, eventtype;
    String Id;
    Bookings cancelledBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_companycancelledthe_booking);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        TimeCB = (TextView) findViewById(R.id.timeevent);
        DateCB = (TextView) findViewById(R.id.dateevent);
        AddressCB = (TextView) findViewById(R.id.addressevent);
        messageCB = (TextView) findViewById(R.id.message);
        eventtype = (TextView) findViewById(R.id.eventtype);
        Button button = (Button) findViewById(R.id.home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent() != null) {

           /* TimeCB.setText(getIntent().getStringExtra("TimeCB"));
            DateCB.setText(getIntent().getStringExtra("DateCB"));
            AddressCB.setText(getIntent().getStringExtra("AddressCB"));
            eventtype.setText(getIntent().getStringExtra("EventType"));
        */
            Id = getIntent().getStringExtra("Id");
            messageCB.setText(getIntent().getStringExtra("messageCB"));

        }


        FirebaseDatabase.getInstance().getReference().child("cancelledcustomerBookings").child(pref.getString("Id", ""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1.getKey().equals(Id)) {
                                cancelledBooking = dataSnapshot1.getValue(Bookings.class);
                                eventtype.setText(cancelledBooking.getEventType());
                                AddressCB.setText(cancelledBooking.getAddress());
                                DateCB.setText(cancelledBooking.getDate());
                                TimeCB.setText(cancelledBooking.getTime());
                                String companyname = cancelledBooking.getCompanyName();
                                messageCB.setText("We are very sorry to tell you that Studio "+companyname+" has cancelled your booking unfortunately . Thanks for your Booking.");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        final SharedPreferences.Editor editor = pref.edit();


        final TextView bookingdate = (TextView) findViewById(R.id.bookingdate);
        final TextView bookingtime = (TextView) findViewById(R.id.bookingtime);


        if (Id != null) {
            FirebaseDatabase.getInstance().getReference("BookingsDateandTime")
                    .child(Id).child("Date")
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
                    .child(Id).child("Time")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            bookingtime.setText(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        FirebaseDatabase.getInstance().getReference().child("cancelBooking")
                .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                {
                    finish();
                    Toast.makeText(CompanycancelledtheBooking.this, "You have responded", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                FirebaseDatabase.getInstance().getReference().child("cancelBooking")
                        .child(pref.getString("Id", "")).removeValue();
            }
        });

    }
}
