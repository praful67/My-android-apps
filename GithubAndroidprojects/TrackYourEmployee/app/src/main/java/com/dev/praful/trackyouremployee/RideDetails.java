package com.dev.praful.trackyouremployee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class RideDetails extends AppCompatActivity {


    ListView employeeslist;
    String id;
    List<Employeedetails> employeedetails = new ArrayList<>();
    String carid;
    ArrayList<String> ordered_ids = new ArrayList<String>();
    String state;

    BottomSheetBehavior bottomSheetBehavior;


    android.support.v7.widget.Toolbar mToolbar;

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
        setContentView(R.layout.activity_ride_details);

        Button getofficedirection = (Button) findViewById(R.id.getofficedirection);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ImageView up = (ImageView) findViewById(R.id.up);
        final ImageView down = (ImageView) findViewById(R.id.down);

        View bottom = findViewById(R.id.bottomsheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom);
        final ImageView comeup = (ImageView) findViewById(R.id.comeup);
        comeup.setVisibility(GONE);
        comeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                comeup.setVisibility(GONE);
            }
        });
        comeup.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                comeup.setVisibility(GONE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up.setVisibility(GONE);
                down.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down.setVisibility(GONE);
                up.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // Toast.makeText(Maps.this, "collapsed", Toast.LENGTH_SHORT).show();
                        down.setVisibility(GONE);
                        up.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        up.setVisibility(GONE);
                        down.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        comeup.setVisibility(View.VISIBLE);
                        comeup.startAnimation(animation);
                        break;


                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        state = preferences.getString("state", "");
        carid = getIntent().getStringExtra("carId");
        getofficedirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri navigationIntentUri = Uri.parse("google.navigation:q=" + 17.434810272796604 + " , " + 78.38469553738832 + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });
        employeeslist = (ListView) findViewById(R.id.employeeslist);
        final TextView textView = (TextView) findViewById(R.id.noemployee);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final TextView dates = (TextView) findViewById(R.id.dates);
        if (carid != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Cars Info")
                    .child(carid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                CarsInfo carsInfo1 = dataSnapshot.getValue(CarsInfo.class);
                                if (carsInfo1 != null && carsInfo1.getListid() != null) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("List of Rosters Sets")
                                            .child(carsInfo1.getListid())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                        if (map != null) {
                                                            dates.setText(String.valueOf(map.get("dates")));
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
            }
        }.start();

        FirebaseDatabase.getInstance().getReference()
                .child("RanksfordistanceCAl")
                .child(carid).orderByChild("Rank")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ordered_ids.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            ordered_ids.add(dataSnapshot1.getKey().toString());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        if (carid != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Employee's Car")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (employeedetails.size() > 0)
                                employeedetails.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();

                                if (map != null) {
                                    if (String.valueOf(map.get("car id")).equals(carid)) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("SignedEmployees")
                                                .child(dataSnapshot1.getKey())
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Employeedetails employeedetails1 = dataSnapshot.getValue(Employeedetails.class);
                                                        if (employeedetails1 != null)
                                                            employeedetails.add(employeedetails1);

                                                    }


                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                    }
                                }
                            }

                            new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    Adapteremployeeslist adapter = new Adapteremployeeslist(employeedetails, RideDetails.this);
                                    employeeslist.setAdapter(adapter);

                                    employeeslist.setEmptyView(textView);


                                }
                            }.start();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        Button more = (Button) findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.morepopup, null);
                Button ok = (Button) view.findViewById(R.id.ok);
                final TextView text = (TextView) view.findViewById(R.id.text);
                if (carid != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("CarrideDT")
                            .child(carid)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                        if (map != null) {
                                            text.setText("\nTotal distance and time from 1st employee to Office --- " + String.valueOf(map.get("DT")) + "\n");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    for (int i = 0; i < ordered_ids.size() - 1; i++) {
                        final int finalI = i;
                        FirebaseDatabase.getInstance().getReference()
                                .child("RideDTforeachEmp")
                                .child(carid)
                                .child("DT")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot != null) {
                                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                            if (map != null) {
                                                String text1 = String.valueOf(map.get("Rank" + String.valueOf(finalI + 1) + "to" + String.valueOf(finalI + 2)));
                                                text.append("\nDistance and Time between Employee of Rank " + String.valueOf(finalI + 1) + " and " + String.valueOf(finalI + 2) + " --- " + text1 + "\n");
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
                alertDialog.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


            }
        });

    }
}
