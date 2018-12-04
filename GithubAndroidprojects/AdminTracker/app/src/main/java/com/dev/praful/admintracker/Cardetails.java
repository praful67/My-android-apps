package com.dev.praful.admintracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cardetails extends AppCompatActivity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardetails);
        final TextView carname = (TextView) findViewById(R.id.Carname);
        final TextView carNumber = (TextView) findViewById(R.id.Carnumber);

        if (getIntent().getStringExtra("driverid") != null) {
            id = getIntent().getStringExtra("driverid");
            if (id != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("DriverprivateCars")
                        .child(id)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    PrivateCarsInfo PrivateCarsInfo = dataSnapshot.getValue(PrivateCarsInfo.class);
                                    if (PrivateCarsInfo != null) {
                                        carname.setText(PrivateCarsInfo.getName());
                                        carNumber.setText(PrivateCarsInfo.getCarnumber());
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }


        Button edit = (Button) findViewById(R.id.edit);
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cardetails.this, Editcar.class);
                intent.putExtra("driverid", id);
                startActivity(intent);
            }
        });


    }
}
