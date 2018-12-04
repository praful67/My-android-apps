package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialOverlayLayout;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class CurrentCardetails extends AppCompatActivity {

    String carId;
    List<Finalcaremployees> finalcaremployees = new ArrayList<>();
    RecyclerView listView;
    ArrayList<String> ranks = new ArrayList<String>();
    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> lat = new ArrayList<String>();
    ArrayList<String> lng = new ArrayList<String>();
    ArrayList<String> ordered_ids = new ArrayList<String>();
    int X = 0;
    IGoogleAPI Services;
    String distance_textD, time_textD;
    double distance;
    int time;
    String text1 = null;

    String driverid;
    String driverid1;
    String rank, employeename, pickuptime;
    SpeedDialOverlayLayout speedDialOverlayLayout;
    SpeedDialView speedDialView;
    TextView textView;
    BottomSheetBehavior bottomSheetBehavior;
    FinalRecyclerAdapter finalcaremployeesrecycleradapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            speedDialView.setOrientation(LinearLayout.HORIZONTAL);

        } else {
            speedDialView.setOrientation(LinearLayout.VERTICAL);
        }

    }*/

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_cardetails);
        final TextView drivername = (TextView) findViewById(R.id.drivername);
        listView = (RecyclerView) findViewById(R.id.employeeslist);
        listView.setLayoutManager(new LinearLayoutManager(this));
        carId = getIntent().getStringExtra("carId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Employees");
        setSupportActionBar(toolbar);

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

        getWindow().getAttributes().windowAnimations = R.style.Style;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Services = CommonSwitch.getGoogleService();
        final TextView drivername1 = (TextView) findViewById(R.id.drivername2);
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

        speedDialOverlayLayout = (SpeedDialOverlayLayout) findViewById(R.id.overlay);
        speedDialView = findViewById(R.id.speedDial);

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_cal, R.drawable.calculate)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Calculate")
                        .setLabelColor(Color.BLACK)
                        .setLabelClickable(true)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(false)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_edit, R.drawable.edit)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Edit")
                        .setLabelColor(Color.BLACK)
                        .setLabelClickable(true)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(false)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_map, R.drawable.line)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .setLabel("Map")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(false)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_more, R.drawable.sets)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("More")
                        .setLabelColor(Color.BLACK)
                        .setLabelClickable(true)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(false)
                        .create()
        );
        final TextView totalDT = (TextView) findViewById(R.id.totalDT);

        final ProgressDialog progressDialog2 = new ProgressDialog(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.nav_cal:
                        progressDialog2.setMessage("Please wait");
                        progressDialog2.show();
                        lat.clear();
                        lng.clear();
                        distance = 0;
                        time = 0;
                        for (int i = 0; i < ordered_ids.size(); i++) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("SignedEmployees")
                                    .child(ordered_ids.get(i))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                            if (employeedetails != null) {
                                                if (employeedetails.getAddresslat() != null) {
                                                    lat.add(employeedetails.getAddresslat());
                                                    lng.add(employeedetails.getAddresslng());

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        }
                        if (ordered_ids.size() > 0) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("RideDTforeachEmp")
                                    .child(carId)
                                    .child("DT").removeValue();

                            new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    for (int i = 0; i < ordered_ids.size() - 1; i++) {
                                        final double lat1 = Double.parseDouble(lat.get(i));
                                        final double lng1 = Double.parseDouble(lng.get(i));
                                        final int ia = i;
                                        if (lat.get(i + 1) != null) {
                                            final double lat2 = Double.parseDouble(lat.get(i + 1));
                                            final double lng2 = Double.parseDouble(lng.get(i + 1));
                                            final int finalI = i;
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String requestUrl = null;
                                                    try {
                                                        requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                                                                "transit_routing_preference=less_driving&" + "origin=" + lat1 + "," + lng1 + "&" +
                                                                "destination=" + lat2 + "," + lng2 + "&" +
                                                                "key=" + getResources().getString(R.string.google_maps_key);
                                                        Log.e("LINK", requestUrl);
                                                        Services.getPath(requestUrl).enqueue(new Callback<String>() {
                                                            @Override
                                                            public void onResponse(Call<String> call, Response<String> response) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                                                    JSONArray routes = jsonObject.getJSONArray("routes");
                                                                    JSONObject object = routes.getJSONObject(0);

                                                                    JSONArray legs = object.getJSONArray("legs");
                                                                    JSONObject legsobject = legs.getJSONObject(0);

                                                                    JSONObject distance1 = legsobject.getJSONObject("distance");
                                                                    // distance_textD = distance.getString("text");
                                                                    distance = distance + distance1.getDouble("value");

                                                                    JSONObject time1 = legsobject.getJSONObject("duration");
                                                                    //  time_textD = time.getString("text");

                                                                    time = time + time1.getInt("value");
                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("RideDTforeachEmp")
                                                                            .child(carId)
                                                                            .child("DT")
                                                                            .child("Rank" + String.valueOf(finalI + 1) + "to" + String.valueOf(finalI + 2))
                                                                            .setValue(distance1.getString("text") + " " + time1.getString("text"));


                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }


                                                            }

                                                            @Override
                                                            public void onFailure(Call<String> call, Throwable t) {

                                                                Log.e("ERROR", t.getMessage());
                                                            }
                                                        });
                                           /* final Handler handler = new Handler();
                                            final String finalRequestUrl = requestUrl;
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                }
                                            }, 3000);
*/
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            });
                                        } else {
                                            progressDialog2.dismiss();
                                            totalDT.setText("Not enough Employess");
                                        }
                                    }
                                }
                            }.start();


                            new CountDownTimer(5000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    progressDialog2.setMessage("Getting distance and time..");

                                    final LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);

                                    final double lat1 = Double.parseDouble(lat.get(ordered_ids.size() - 1));
                                    final double lng1 = Double.parseDouble(lng.get(ordered_ids.size() - 1));

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            String requestUrl = null;
                                            try {
                                                requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                                                        "transit_routing_preference=less_driving&" + "origin=" + lat1 + "," + lng1 + "&" +
                                                        "destination=" + latLng.latitude + "," + latLng.longitude + "&" +
                                                        "key=" + getResources().getString(R.string.google_maps_key);
                                                Log.e("LINK", requestUrl);
                                                Services.getPath(requestUrl).enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response.body().toString());
                                                            JSONArray routes = jsonObject.getJSONArray("routes");
                                                            JSONObject object = routes.getJSONObject(0);

                                                            JSONArray legs = object.getJSONArray("legs");
                                                            JSONObject legsobject = legs.getJSONObject(0);

                                                            JSONObject distance1 = legsobject.getJSONObject("distance");
                                                            // distance_textD = distance.getString("text");
                                                            distance = distance + distance1.getDouble("value");

                                                            JSONObject time1 = legsobject.getJSONObject("duration");
                                                            //  time_textD = time.getString("text");

                                                            time = time + time1.getInt("value");
                                                            new CountDownTimer(1000, 1000) {
                                                                @Override
                                                                public void onTick(long millisUntilFinished) {

                                                                }

                                                                @Override
                                                                public void onFinish() {
                                                                    progressDialog2.dismiss();

                                                                    String finaltime = ConvertSectoDay(time);
                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("CarrideDT")
                                                                            .child(carId)
                                                                            .child("DT")
                                                                            .setValue(String.valueOf((int) (distance / 1000)) + " km" + " " + finaltime);


                                                                }
                                                            }.start();

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }


                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {

                                                        Log.e("ERROR", t.getMessage());
                                                    }
                                                });

                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }.start();
                        } else {
                            progressDialog2.dismiss();
                            Toast.makeText(CurrentCardetails.this, "No Employess", Toast.LENGTH_SHORT).show();
                        }

                        return false; // true to keep the Speed Dial open
                    case R.id.nav_more:
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.morepopup, null);
                        builder.setView(view);
                        Button ok = (Button) view.findViewById(R.id.ok);
                        final TextView text = (TextView) view.findViewById(R.id.text);
                        for (int i = 0; i < ordered_ids.size() - 1; i++) {
                            final int finalI = i;
                            FirebaseDatabase.getInstance().getReference()
                                    .child("RideDTforeachEmp")
                                    .child(carId)
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
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        return false;
                    case R.id.nav_edit:
                        Intent intent1 = new Intent(CurrentCardetails.this, Editfinalemployeedetails.class);
                        intent1.putExtra("carid", carId);
                        startActivity(intent1);

                        return false;
                    case R.id.nav_map:
                        Intent intent = new Intent(CurrentCardetails.this, SeeonmapC.class);
                        intent.putExtra("carid", carId);
                        startActivity(intent);
                        return false;

                    default:
                        return false;
                }
            }
        });
        textView = new TextView(this);
        textView.setText("");
        Button done = (Button) findViewById(R.id.done);
        FirebaseDatabase.getInstance().getReference()
                .child("CarrideDT")
                .child(carId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                totalDT.setText(String.valueOf(map.get("DT")));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Rank")
                .child(carId)
                .addValueEventListener(new ValueEventListener() {
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
        FirebaseDatabase.getInstance().getReference()
                .child("RanksfordistanceCAl")
                .child(carId).orderByChild("Rank")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null) {
                            ordered_ids.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                ordered_ids.add(dataSnapshot1.getKey().toString());
                            }
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
                                                                                                        text1 = "  \n (" + String.valueOf(map1.get("Rank")) + "  ,  " + String.valueOf(map2.get("time")) + ")   |   " + employeedetails.getUsername() + "\n";
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final TextView dates = (TextView) findViewById(R.id.dates);
        final String listid = sharedPreferences.getString("rostersetid", "");
        if (carId != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Cars Info")
                    .child(carId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                CarsInfo carsInfo1 = dataSnapshot.getValue(CarsInfo.class);
                                if (carsInfo1 != null && carsInfo1.getListid() != null) {
                                    if (carsInfo1.getListid().equals(listid)) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("List of Rosters Sets")
                                                .child(listid)
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
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Button UD = (Button) findViewById(R.id.UD);
        Button UE = (Button) findViewById(R.id.UE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        UD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentCardetails.this, Loginout.class);
                intent.putExtra("carid", carId);
                editor.putString("anim", "yes");
                editor.apply();
                startActivity(intent);

            }
        });
        UE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentCardetails.this, Selectemployees.class);
                intent.putExtra("carid", carId);
                editor.putString("anim", "yes");
                editor.apply();
                startActivity(intent);
                finish();
            }
        });
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
                FirebaseDatabase.getInstance().getReference()
                        .child("Driver's Car")
                        .child("logout")
                        .child(carId)
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
                                                                if (Driverdetails != null) {
                                                                    drivername.setText(Driverdetails.getUsername());

                                                                    driverid = Driverdetails.getId();
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

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                FirebaseDatabase.getInstance().getReference()
                        .child("Driver's Car")
                        .child("login")
                        .child(carId)
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
                                                                if (Driverdetails != null) {
                                                                    drivername1.setText(Driverdetails.getUsername());
                                                                    driverid1 = Driverdetails.getId();
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

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                FirebaseDatabase.getInstance().getReference()
                        .child("Employee's Car")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (finalcaremployees.size() > 0)
                                    finalcaremployees.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                    if (map != null) {
                                        if (carId.equals(String.valueOf(map.get("car id")))) {

                                            Finalcaremployees finalcaremployees1 = new Finalcaremployees("", dataSnapshot1.getKey());
                                            if (finalcaremployees1 != null)
                                                finalcaremployees.add(finalcaremployees1);

                                        }
                                    }

                                }
                                new CountDownTimer(1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        finalcaremployeesrecycleradapter = new FinalRecyclerAdapter(finalcaremployees, CurrentCardetails.this, carId);
                                        TextView t = (TextView) findViewById(R.id.t);

                                        if (finalcaremployees.isEmpty()) {
                                            t.setVisibility(View.VISIBLE);
                                        }
                                        listView.setAdapter(finalcaremployeesrecycleradapter);
                                    }
                                }.start();


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




            }
        }.start();


        drivername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentCardetails.this, Driverdetailspage.class);
                intent.putExtra("Driverid", driverid);
                startActivity(intent);
            }
        });
        drivername1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentCardetails.this, Driverdetailspage.class);
                intent.putExtra("Driverid", driverid1);
                startActivity(intent);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference()
                        .child("RanksfordistanceCAl")
                        .child(carId).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(CurrentCardetails.this, Atonesightempty.class);
                                intent.putExtra("carid", carId);
                                startActivity(intent);
                                finish();
                            }
                        });


            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                // int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder dragged, RecyclerView.ViewHolder target) {


                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();
                int d = position_dragged + 1;
                int t = position_target + 1;
                FirebaseDatabase.getInstance().getReference()
                        .child("Employee's Rank").child(carId).child(finalcaremployees.get(position_dragged).getId())
                        .child("rank")
                        .setValue(String.valueOf(t));
                FirebaseDatabase.getInstance().getReference()
                        .child("Employee's Rank").child(carId).child(finalcaremployees.get(position_target).getId())
                        .child("id").setValue(finalcaremployees.get(position_target).getId());

                FirebaseDatabase.getInstance().getReference()
                        .child("Employee's Rank").child(carId).child(finalcaremployees.get(position_dragged).getId())
                        .child("id").setValue(finalcaremployees.get(position_dragged).getId());

                FirebaseDatabase.getInstance().getReference()
                        .child("Employee's Rank").child(carId).child(finalcaremployees.get(position_target).getId())
                        .child("rank")
                        .setValue(String.valueOf(d));
                Collections.swap(finalcaremployees, position_dragged, position_target);
                finalcaremployeesrecycleradapter.notifyItemMoved(position_dragged, position_target);

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                finalcaremployeesrecycleradapter.notifyDataSetChanged();
            }
        });
        helper.attachToRecyclerView(listView);

    }

    private String ConvertSectoDay(int n) {

        int day = n / (24 * 3600);

        n = n % (24 * 3600);
        int hour = n / 3600;

        n %= 3600;
        int minutes = n / 60;

        n %= 60;
        int seconds = n;
        String result = null;
        if (day != 0) {
            result = day + " " + "days " + hour
                    + " " + "hours " + minutes + " "
                    + "minutes ";
            return result;

        } else if (hour != 0) {

            result = hour
                    + " " + "hours " + minutes + " "
                    + "minutes ";
            return result;

        } else {
            result = minutes + " "
                    + "minutes ";
            return result;

        }

    }

    private void getDTD(final double lat1, final double lng1, final double lat2, final double lng2) {


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String requestUrl = null;
                try {
                    requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                            "transit_routing_preference=less_driving&" + "origin=" + lat1 + "," + lng1 + "&" +
                            "destination=" + lat2 + "," + lng2 + "&" +
                            "key=" + getResources().getString(R.string.google_maps_key);
                    Log.e("LINK", requestUrl);
                    Services.getPath(requestUrl).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray routes = jsonObject.getJSONArray("routes");
                                JSONObject object = routes.getJSONObject(0);

                                JSONArray legs = object.getJSONArray("legs");
                                JSONObject legsobject = legs.getJSONObject(0);

                                JSONObject distance = legsobject.getJSONObject("distance");
                                distance_textD = distance.getString("text");


                                JSONObject time = legsobject.getJSONObject("duration");
                                time_textD = time.getString("text");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Log.e("ERROR", t.getMessage());
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

}
 /*FirebaseDatabase.getInstance().getReference()
                        .child("RanksfordistanceCAl")
                        .child(carId).orderByChild("Rank")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
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
                                                                                                                text1 = "  \n Pick up - " + String.valueOf(map1.get("Rank")) + "  |  " + employeedetails.getUsername() + "  |  " + String.valueOf(map2.get("time")) + "\n";
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
 /*if (ordered_ids.size() > 0) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("RanksfordistanceCAl")
                            .child(carId).orderByChild("Rank")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        if (finalcaremployees.size() > 0)
                                            finalcaremployees.clear();

                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            Finalcaremployees finalcaremployees1 = new Finalcaremployees("", dataSnapshot1.getKey());
                                            if (finalcaremployees1 != null)
                                                finalcaremployees.add(finalcaremployees1);
                                        }
                                        new CountDownTimer(1000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                finalcaremployeesrecycleradapter = new FinalRecyclerAdapter(finalcaremployees, CurrentCardetails.this, carId);
                                                TextView t = (TextView) findViewById(R.id.t);

                                                if (finalcaremployees.isEmpty()) {
                                                    t.setVisibility(View.VISIBLE);
                                                }
                                                listView.setAdapter(finalcaremployeesrecycleradapter);
                                            }
                                        }.start();


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                } else {

                }*/