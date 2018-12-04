package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Selectemployees extends AppCompatActivity {

    List<Orderedemployeedetails> orderedemployeedetails = new ArrayList<>();
    ListView employeeslist;
    double distance = 10000;
    GeoFire geoFire;
    String carId;
    String driverid;
    Selectemployeesadapter adapter;
    ArrayList<String> ids = new ArrayList<String>();
    int X = 0;

    @Override
    public void onBackPressed() {
        if (carId != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Car employees")
                    .child(carId)
                    .removeValue();

        }
        Intent intent = new Intent(Selectemployees.this, CurrentCardetails.class);
        intent.putExtra("carId", carId);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (carId != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Car employees")
                        .child(carId)
                        .removeValue();

            }
            Intent intent = new Intent(Selectemployees.this, CurrentCardetails.class);
            intent.putExtra("carId", carId);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectemployees);
        CommonSwitch.switch1 = (Switch) findViewById(R.id.switch1);
        CommonSwitch.switch1.setChecked(false);
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("locations of Dummy Employees Info"));
        employeeslist = (ListView) findViewById(R.id.employeeslist);
        getWindow().getAttributes().windowAnimations = R.style.Style;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        carId = getIntent().getStringExtra("carid");

        updatelist();
        final FloatingActionButton done = (FloatingActionButton) findViewById(R.id.done);
        if (carId != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Car employees")
                    .child(carId)
                    .removeValue();

        }
        FloatingActionButton seeonmap = (FloatingActionButton) findViewById(R.id.seeonmap);
        seeonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Selectemployees.this, Seeonmap.class);
                intent.putExtra("carId", carId);
                startActivity(intent);
            }
        });
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (carId != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Car employees")
                            .child(carId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                ids.add(String.valueOf(map.get("id")));
                                X++;

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                CommonSwitch.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (carId != null) {
                            X = 0;
                            ids.clear();
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Car employees")
                                    .child(carId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                        ids.add(String.valueOf(map.get("id")));
                                        X++;

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                });
                dialog.dismiss();

            }
        }.start();


        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null && newText != null) {
                    if (TextUtils.isEmpty(newText)) {
                        adapter.filter("");
                        employeeslist.clearTextFilter();
                    } else {
                        adapter.filter(newText);
                    }
                }
                return true;
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(this);

        FirebaseDatabase.getInstance().getReference()
                .child("Driver's Car")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                            if (map != null) {
                                if (carId.equals(String.valueOf(map.get("car id")))) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("SignedDrivers")
                                            .child(dataSnapshot1.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                        if (Driverdetails != null)
                                                            driverid = Driverdetails.getId();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Please wait");
                progressDialog.show();

                for (int i = 0; i < X; i++) {

                    final int finalI = i;

                    FirebaseDatabase.getInstance().getReference()
                            .child("Car employees")
                            .child(carId).child(ids.get(finalI))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();

                                    if(map != null) {
                                        if (String.valueOf(map.get("selection")).equals("Selected")) {

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Employee's Car")
                                                    .child(ids.get(finalI))
                                                    .child("car id")
                                                    .setValue(carId);

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Employee's Car")
                                                    .child(ids.get(finalI))
                                                    .child("checkselection")
                                                    .setValue("selected");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    final int finalI1 = i;
                    FirebaseDatabase.getInstance().getReference().child("employeeselection").child(ids.get(i))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        String check = dataSnapshot.getValue(String.class);
                                        if (check != null) {
                                            if (check.equals("unselected")) {
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Employee's Car")
                                                        .child(ids.get(finalI1))
                                                        .removeValue();

                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Employee's Rank")
                                                        .child(carId)
                                                        .child(ids.get(finalI))
                                                        .removeValue();
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("RanksfordistanceCAl")
                                                        .child(carId).child(ids.get(finalI))
                                                        .removeValue();

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                }


                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(Selectemployees.this, CurrentCardetails.class);
                        intent.putExtra("carId", carId);
                        startActivity(intent);
                        finish();
                    }
                }.start();
            }
        });


    }

    private void updatelist() {
        FirebaseDatabase.getInstance().getReference()
                .child("Final Ordered Employees").orderByChild("distance")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (orderedemployeedetails.size() > 0)
                            orderedemployeedetails.clear();


                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Orderedemployeedetails orderedemployeedetails1
                                    = dataSnapshot1.getValue(Orderedemployeedetails.class);

                            orderedemployeedetails.add(orderedemployeedetails1);

                        }

                        adapter = new Selectemployeesadapter(Selectemployees.this, orderedemployeedetails, carId);

                        TextView t = (TextView) findViewById(R.id.t);

                        employeeslist.setEmptyView(t);

                        employeeslist.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}
