package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Atonesightempty extends AppCompatActivity {
    String carId;
    ArrayList<String> ranks = new ArrayList<String>();
    ArrayList<String> ids = new ArrayList<String>();
    int X = 0;
    String text1 = null;

    String driverid;
    String driverid1;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atonesightempty);
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Please wait...");
        progressDialog1.setCancelable(false);
        progressDialog1.show();
        textView = new TextView(this);
        textView.setText("");
        carId = getIntent().getStringExtra("carid");
        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Rank")
                .child(carId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                if (map != null) {
                                    ids.add(String.valueOf(map.get("id")));
                                    ranks.add(String.valueOf(map.get("rank")));
                                    X++;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                for (int i = 0; i < X; i++) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Final Rank of employee")
                            .child(ids.get(i))
                            .child("Rank")
                            .setValue(ranks.get(i));
                    FirebaseDatabase.getInstance().getReference()
                            .child("RanksfordistanceCAl")
                            .child(carId)
                            .child(ids.get(i))
                            .child("Rank")
                            .setValue(Integer.parseInt(ranks.get(i)));
                 /*   FirebaseDatabase.getInstance().getReference()
                            .child("Updates")
                            .child(ids.get(i))
                            .child("whattodo")
                            .setValue("update");
             */   }
                /*if (driverid != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("DriverUpdates")
                            .child(driverid)
                            .child("whattodo")
                            .setValue("update");

                }*/
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        progressDialog1.dismiss();
                        Intent intent = new Intent(Atonesightempty.this, CurrentCardetails.class);
                        intent.putExtra("carId", carId);
                        startActivity(intent);
                        finish();
                    }
                }.start();

            }
        }.start();

    }
}
/* FirebaseDatabase.getInstance().getReference()
                .child("RanksfordistanceCAl")
                .child(carId).orderByChild("Rank")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        textView.setText("");
                        if (dataSnapshot != null) {
                            for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                if (map != null) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Employeespickuptimes")
                                            .child(dataSnapshot1.getKey())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        final Map<String, Object> map2 = (HashMap<String, Object>) dataSnapshot.getValue();
                                                        if (map2 != null) {
                                                            //pickuptime = String.valueOf(map2.get("time"));
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("SignedEmployees")
                                                                    .child(dataSnapshot1.getKey())
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            final Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                                                            if (employeedetails != null) {
                                                                                //    employeename = employeedetails.getUsername();
                                                                                FirebaseDatabase.getInstance().getReference()
                                                                                        .child("Final Rank of employee")
                                                                                        .child(dataSnapshot1.getKey())
                                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                if (dataSnapshot != null) {
                                                                                                    Map<String, Object> map1 = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                                                    if (map1 != null) {
                                                                                                        text1 = "  \n (" + String.valueOf(map1.get("Rank")) + "  ,  " + String.valueOf(map2.get("time")) +")   |   "+ employeedetails.getUsername() + "\n";
                                                                                                        textView.append(text1);
                                                                                                    }
                                                                                                }

                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                                            }
                                                                                        });

                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                }

                            }
                            new CountDownTimer(3000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Atonesight")
                                            .child(carId)
                                            .child("employees")
                                            .setValue(textView.getText().toString());


                                }
                            }.start();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
*/