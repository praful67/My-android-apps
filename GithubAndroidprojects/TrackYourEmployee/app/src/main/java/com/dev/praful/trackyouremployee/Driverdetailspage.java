package com.dev.praful.trackyouremployee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Driverdetailspage extends AppCompatActivity {

    String driverid;
    ImageView drivinglicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverdetailspage);
        if (getIntent().getStringExtra("driverid") != null) {
            driverid = getIntent().getStringExtra("driverid");
        }
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        final TextView phone = (TextView) findViewById(R.id.phone);
        final TextView company = (TextView) findViewById(R.id.company);
        final TextView address = (TextView) findViewById(R.id.address);
        drivinglicense = (ImageView) findViewById(R.id.drivinglicense);

        if (driverid != null)
            {
            FirebaseDatabase.getInstance().getReference()
                    .child("SignedDrivers")
                    .child(driverid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                if (Driverdetails != null) {
                                    username.setText(Driverdetails.getUsername());
                                    password.setText(Driverdetails.getPassword());
                                    phone.setText(Driverdetails.getPhone());
                                    company.setText(Driverdetails.getCompany());
                                    if (TextUtils.isEmpty(Driverdetails.getAddress()))
                                        address.setText("No address");
                                    else
                                        address.setText(Driverdetails.getAddress());
                                    String DL = Driverdetails.getDrivinglicense();

                                    if (!TextUtils.isEmpty(DL)) {
                                        Picasso.with(Driverdetailspage.this).load(DL)
                                                .into(drivinglicense);
                                    } else
                                        drivinglicense.setImageResource(R.drawable.add);


                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}
