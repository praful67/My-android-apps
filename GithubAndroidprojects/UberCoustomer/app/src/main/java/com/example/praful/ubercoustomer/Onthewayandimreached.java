package com.example.praful.ubercoustomer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Model.Bookings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class Onthewayandimreached extends AppCompatActivity {

    TextView message , eventtype ,address  , date ,time;
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

         eventtype = (TextView)findViewById(R.id.eventtype);
         address = (TextView)findViewById(R.id.addressevent);
         date = (TextView)findViewById(R.id.dateevent);
         time = (TextView)findViewById(R.id.timeevent);
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
    }

    private void load() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
       String userid = pref.getString("Id", "");

        databaseReference.child("coustomerBookings").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals(BookingIdT)){
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
