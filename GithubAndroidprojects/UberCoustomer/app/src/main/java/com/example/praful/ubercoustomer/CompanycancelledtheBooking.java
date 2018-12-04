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

import com.example.praful.ubercoustomer.Model.Bookings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanycancelledtheBooking extends AppCompatActivity {

    TextView TimeCB ,DateCB ,AddressCB, messageCB , eventtype;
    String Id;
    Bookings cancelledBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_companycancelledthe_booking);
        TimeCB = (TextView)findViewById(R.id.timeevent);
        DateCB = (TextView)findViewById(R.id.dateevent);
        AddressCB = (TextView)findViewById(R.id.addressevent);
        messageCB = (TextView)findViewById(R.id.message);
        eventtype = (TextView)findViewById(R.id.eventtype);
        Button button = (Button)findViewById(R.id.home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent() !=null){

           /* TimeCB.setText(getIntent().getStringExtra("TimeCB"));
            DateCB.setText(getIntent().getStringExtra("DateCB"));
            AddressCB.setText(getIntent().getStringExtra("AddressCB"));
            eventtype.setText(getIntent().getStringExtra("EventType"));
        */    Id =  getIntent().getStringExtra("Id");
            messageCB.setText(getIntent().getStringExtra("messageCB"));

        }

        FirebaseDatabase.getInstance().getReference().child("cancelledcustomerBookings").child(pref.getString("Id" ,""))
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
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })

        ;


        final TextView bookingdate = (TextView)findViewById(R.id.bookingdate);
        final TextView bookingtime = (TextView)findViewById(R.id.bookingtime);


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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();
            }
        });

    }
}
