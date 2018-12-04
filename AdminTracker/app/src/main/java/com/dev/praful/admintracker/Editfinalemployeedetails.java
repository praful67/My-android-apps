package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Editfinalemployeedetails extends AppCompatActivity {
    String carId;
    List<Finalcaremployees> finalcaremployees = new ArrayList<>();
    ListView listView;
    ArrayList<String> ranks = new ArrayList<String>();
    ArrayList<String> ids = new ArrayList<String>();
    int X = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfinalemployeedetails);
        listView = (ListView) findViewById(R.id.employeeslist);
        getWindow().getAttributes().windowAnimations = R.style.Style;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Current Ride");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
/*
        Button done = (Button) findViewById(R.id.done);
*/
        carId = getIntent().getStringExtra("carid");

        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Rank")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                            ids.add(String.valueOf(map.get("id")));
                            ranks.add(String.valueOf(map.get("rank")));
                            X++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();

                FirebaseDatabase.getInstance().getReference()
                        .child("Employee's Car")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                    if (map != null) {
                                        if (carId.equals(String.valueOf(map.get("car id")))) {
                                            Finalcaremployees finalcaremployees1 = new Finalcaremployees("", dataSnapshot1.getKey());
                                            finalcaremployees.add(finalcaremployees1);


                                        }
                                    }

                                }
                                Editfinalcaremployeesadapter Editfinalcaremployeesadapter = new Editfinalcaremployeesadapter(finalcaremployees, Editfinalemployeedetails.this);
                                TextView t = (TextView) findViewById(R.id.t);

                                listView.setEmptyView(t);

                                listView.setAdapter(Editfinalcaremployeesadapter);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }.start();
       /* done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < X; i++) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Employee's Rank")
                            .child(ids.get(i))
                            .child("Rank")
                            .setValue(ranks.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Editfinalemployeedetails.this, "Done !", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        finish();
                    }
                }.start();
            }
        });*/

    }
}
