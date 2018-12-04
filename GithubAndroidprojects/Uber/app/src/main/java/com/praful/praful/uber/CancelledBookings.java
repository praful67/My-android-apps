package com.praful.praful.uber;

import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.praful.praful.uber.Common.Common;
import com.praful.praful.uber.Model.Bookings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CancelledBookings extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ListView listdata;
    List<Bookings> bookingsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_bookings);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listdata = (ListView)findViewById(R.id.listofcancelledbookings);

        addEventFirebaselistener();


    }
    private void addEventFirebaselistener() {

        databaseReference.child("cancelledBookings").child(Common.userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (bookingsList.size() > 0)
                    bookingsList.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Bookings users = dataSnapshot1.getValue(Bookings.class);
                    bookingsList.add(users);
                }
                CancelledBookingListAdapter adapter = new CancelledBookingListAdapter(CancelledBookings.this , bookingsList);

                TextView t = (TextView)findViewById(R.id.t);

                listdata.setEmptyView(t);

                listdata.setAdapter(adapter);

                // showt();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
