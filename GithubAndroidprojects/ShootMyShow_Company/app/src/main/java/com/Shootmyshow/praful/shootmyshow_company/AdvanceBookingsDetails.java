package com.Shootmyshow.praful.shootmyshow_company;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.Shootmyshow.praful.shootmyshow_company.Common.Common;
import com.Shootmyshow.praful.shootmyshow_company.Model.Coustomers;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdvanceBookingsDetails extends AppCompatActivity {


    TextView time, customername, date, customerphone, address, customerid , eventype;
    CircleImageView customerPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_bookings_details);
        time = (TextView) findViewById(R.id.timeevent);
        date = (TextView) findViewById(R.id.dateevent);
        address = (TextView) findViewById(R.id.addressevent);
        customerid = (TextView) findViewById(R.id.customerId);
        customerphone = (TextView) findViewById(R.id.customerphone);
        customername = (TextView) findViewById(R.id.customerName);
        eventype = (TextView)findViewById(R.id.eventtype);
        customerPhoto = (CircleImageView)findViewById(R.id.customerPhoto);
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        if (getIntent() != null) {
            time.setText(getIntent().getStringExtra("Time"));
            date.setText(getIntent().getStringExtra("Date"));
            address.setText(getIntent().getStringExtra("Address"));
            customername.setText(getIntent().getStringExtra("customername"));
            customerphone.setText(getIntent().getStringExtra("customerphone"));
            customerid.setText(getIntent().getStringExtra("customerId"));
            eventype.setText(getIntent().getStringExtra("Eventtype"));
        }

        FirebaseDatabase.getInstance().getReference(Common.coustomers_table)
                .child(customerid.getText().toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Coustomers user = dataSnapshot.getValue(Coustomers.class);

                        if (!user.getAvatarUrl().isEmpty()) {

                            Picasso.with(getBaseContext()).load(user.getAvatarUrl())
                                    .into(customerPhoto);
                        }

                        customername.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
