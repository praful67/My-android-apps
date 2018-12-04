package com.praful.praful.uber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.praful.praful.uber.Model.Bookings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CustomercancelledtheBooking extends AppCompatActivity {

    TextView TimeCB ,DateCB ,AddressCB, messageCB , eventtype;
    String Id;
    Bookings cancelledBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customercancelledthe_booking);
        TimeCB = (TextView)findViewById(R.id.timeCB);
        DateCB = (TextView)findViewById(R.id.dateCB);
        AddressCB = (TextView)findViewById(R.id.AddressCB);
        messageCB = (TextView)findViewById(R.id.messageCB);
        eventtype = (TextView)findViewById(R.id.eventtype);
        Button button = (Button)findViewById(R.id.btnDone1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent() !=null){

          /*  TimeCB.setText(getIntent().getStringExtra("TimeCB"));
            DateCB.setText(getIntent().getStringExtra("DateCB"));
            AddressCB.setText(getIntent().getStringExtra("AddressCB"));
          messageCB.setText(getIntent().getStringExtra("messageCB")); */
            Id = getIntent().getStringExtra("Id");

        }

        final TextView bookingdate = (TextView)findViewById(R.id.bookingdate);
        final TextView bookingtime = (TextView)findViewById(R.id.bookingtime);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);


        FirebaseDatabase.getInstance().getReference().child("cancelledBookings").child(pref.getString("Id" ,""))
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
                                String MCB =  cancelledBooking.getCustomerName() + " has cancelled your Advance Booking unfortunately";
                                messageCB.setText(MCB);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


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
