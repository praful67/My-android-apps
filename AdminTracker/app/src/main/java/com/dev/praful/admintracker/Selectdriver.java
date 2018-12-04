package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Selectdriver extends AppCompatActivity {
    ListView listdata;
    String carId;
    List<DriversInfo> driversInfoList = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<String>();
    int X = 0;
    String state;
    Selectdriverlistadapter adapter;

    @Override
    public void onBackPressed() {
        if (carId != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Car Drivers")
                    .child(state)
                    .child(carId)
                    .removeValue();

        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (carId != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Car Drivers")
                        .child(state)
                        .child(carId)
                        .removeValue();

            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectdriver);
        CommonSwitch.switch2 = (Switch) findViewById(R.id.switch2);
        CommonSwitch.switch2.setChecked(false);
        getWindow().getAttributes().windowAnimations = R.style.Style;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        state = getIntent().getStringExtra("state");
        carId = getIntent().getStringExtra("carid");
        FloatingActionButton done = (FloatingActionButton) findViewById(R.id.done);
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
                        listdata.clearTextFilter();
                    } else {
                        adapter.filter(newText);
                    }
                }
                return true;
            }
        });
        listdata = (ListView) findViewById(R.id.driverslist);
        if (carId != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Car Drivers")
                    .child(state)
                    .child(carId)
                    .removeValue();

        }
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

                dialog.dismiss();
                if (carId != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Car Drivers")
                            .child(state)
                            .child(carId).
                            addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                        if (map != null) {
                                            ids.add(String.valueOf(map.get("id")));
                                            X++;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                    CommonSwitch.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (carId != null) {
                                X = 0;
                                ids.clear();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Car Drivers")
                                        .child(state)
                                        .child(carId).
                                        addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                                    if (map != null) {
                                                        ids.add(String.valueOf(map.get("id")));
                                                        X++;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            }

                        }
                    });
                }
            }
        }.start();
        final ProgressDialog progressDialog = new ProgressDialog(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Please wait");
                progressDialog.show();

                for (int i = 0; i < X; i++) {

                    final int finalI1 = i;
                    FirebaseDatabase.getInstance().getReference()
                            .child("Car Drivers")
                            .child(state)
                            .child(carId).child(ids.get(i))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();

                                    if (map != null) {
                                        if (String.valueOf(map.get("selection")).equals("Selected")) {

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Driver's Car")
                                                    .child(state)
                                                    .child(carId)
                                                    .child(ids.get(finalI1))
                                                    .child("car id")
                                                    .setValue(carId);
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Driver's Car")
                                                    .child(state)
                                                    .child(carId)
                                                    .child(ids.get(finalI1))
                                                    .child("checkselection")
                                                    .setValue("selected")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(Selectdriver.this, "Done !", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                            ;
                                           /* FirebaseDatabase.getInstance().getReference()
                                                    .child("DriverUpdates")
                                                    .child(ids.get(finalI1))
                                                    .child("whattodo")
                                                    .setValue("update")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(Selectdriver.this, "Notification sent !", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                            ;*/

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    final int finalI = i;
                    FirebaseDatabase.getInstance().getReference()
                            .child("Driverselection")
                            .child(state)
                            .child(ids.get(i))
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        String check = dataSnapshot.getValue(String.class);
                                        if (check != null) {
                                            if (check.equals("unselected")) {
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Driver's Car")
                                                        .child(state)
                                                        .child(carId)
                                                        .child(ids.get(finalI))
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
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                        finish();

                    }
                }.start();
            }
        });


        addEventFirebaselistener();

    }

    private void addEventFirebaselistener() {

        FirebaseDatabase.getInstance().getReference().child("Drivers Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (driversInfoList.size() > 0)
                    driversInfoList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DriversInfo DriversInfo = dataSnapshot1.getValue(DriversInfo.class);
                    driversInfoList.add(DriversInfo);
                }
                adapter = new Selectdriverlistadapter(Selectdriver.this, driversInfoList, carId, state);

                TextView t = (TextView) findViewById(R.id.t);

                listdata.setEmptyView(t);

                listdata.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
