package com.praful.praful.uber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.praful.praful.uber.Common.Common;
import com.praful.praful.uber.Model.Bookings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResponseYES extends AppCompatActivity {

    Button done;

    TextView id, key;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String BookingIdC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_yes);
        done = (Button) findViewById(R.id.Done11);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent() != null) {
            BookingIdC = getIntent().getStringExtra("BookingIdC");

        }
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        final String userid = pref.getString("Id" , "");
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
       /* FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Bookings").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(BookingIdC)) {
                        Bookings bookings = snapshot.getValue(Bookings.class);
                        databaseReference.child("CompletedBookings").child(userid).child(BookingIdC).setValue(bookings);
                        databaseReference.child("Bookings").child(userid).child(BookingIdC).removeValue();

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


    }
}
