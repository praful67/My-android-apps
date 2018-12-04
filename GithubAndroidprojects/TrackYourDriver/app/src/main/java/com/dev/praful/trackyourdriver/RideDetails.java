package com.dev.praful.trackyourdriver;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hlab.fabrevealmenu.enums.Direction;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static android.view.View.GONE;


public class RideDetails extends BaseFragment implements OnFABMenuSelectedListener {


    ListView employeeslist;
    String id;
    List<Employeedetails> employeedetails = new ArrayList<>();
    IGoogleAPI Services;
    String carid;
    String result;
    String distance_textD, time_textD;
    String driverid, driverid1;
    ArrayList<String> ordered_ids = new ArrayList<String>();
    LatLng latLng, latLng2, latLng1;
    TextView currentdistance;
    TextView currentdistance1;
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
    public void onMenuItemSelected(View view, int id) {
        if (id == R.id.menu_more) {
            new CountDownTimer(700, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    showmoredialog();

                }
            }.start();
        } else if (id == R.id.menu_map) {
            new CountDownTimer(700, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                    Intent intent = new Intent(RideDetails.this, SeeonmapC.class);
                    intent.putExtra("carid", carid);
                    startActivity(intent);
                }
            }.start();

        }
    }

    /* public boolean onBackPressed() {
         if (fabMenu != null) {
             if (fabMenu.isShowing()) {
                 fabMenu.closeMenu();
                 return false;
             }
         }
         return true;
     }*/
    FABRevealMenu fabMenu;

    @Override
    public void onBackPressed() {
        if (fabMenu != null) {
            if (fabMenu.isShowing()) {
                fabMenu.closeMenu();
            }
        }

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);


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

        final FloatingActionButton fab = findViewById(R.id.fab);
        fabMenu = findViewById(R.id.fabMenu);

        try {
            if (fab != null && fabMenu != null) {
                setFabMenu(fabMenu);
                //attach menu to fab
                fabMenu.bindAnchorView(fab);
                //set menu selection listener
                fabMenu.setOnFABMenuSelectedListener(this);
                fabMenu.setMenuDirection(Direction.UP);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Services = CommonSwitch.getGoogleService();

        currentdistance = (TextView) findViewById(R.id.currentdistance);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        //currentdistance.setText(pref.getString("DT" , ""));

        final TextView drivername = (TextView) findViewById(R.id.drivername);
        employeeslist = (ListView) findViewById(R.id.employeeslist);
        LinearLayout driverlogout = (LinearLayout) findViewById(R.id.driverlogout);
        LinearLayout driverlogin = (LinearLayout) findViewById(R.id.driverlogin);

        final TextView drivername1 = (TextView) findViewById(R.id.drivername1);

        final TextView textView = (TextView) findViewById(R.id.noemployee);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                progressDialog.dismiss();
            }
        }.start();
        final TextView dates = (TextView) findViewById(R.id.dates);

        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Car")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null)
                            carid = String.valueOf(map.get("car id"));
                        if (carid != null) {
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

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Employee's Car")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (employeedetails.size() > 0)
                                                employeedetails.clear();

                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();

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

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Driver's Car")
                                    .child("logout")
                                    .child(carid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot != null) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                            if (map != null) {
                                                if (String.valueOf(map.get("car id")).equals(carid)) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("SignedDrivers")
                                                            .child(dataSnapshot1.getKey())
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                                    if (Driverdetails != null) {
                                                                        drivername.setText("Logout Driver : " + Driverdetails.getUsername());
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
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Driver's Car")
                                    .child("login")
                                    .child(carid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot != null) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                            if (map != null) {
                                                if (String.valueOf(map.get("car id")).equals(carid)) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("SignedDrivers")
                                                            .child(dataSnapshot1.getKey())
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                                    if (Driverdetails != null) {
                                                                        drivername1.setText("Login Driver : " + Driverdetails.getUsername());
                                                                        driverid1 = Driverdetails.getId();


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


        FirebaseDatabase.getInstance().getReference()
                .child("Employees Info").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            EmployeeInfo employeeInfo = dataSnapshot.getValue(EmployeeInfo.class);
                            if (employeeInfo != null) {
                                double lat = Double.parseDouble(employeeInfo.getLat());
                                double lng = Double.parseDouble(employeeInfo.getLng());
                                latLng = new LatLng(lat, lng);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        driverlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideDetails.this, Driverdetailspage.class);
                intent.putExtra("driverid", driverid);
                startActivity(intent);
            }
        });
        driverlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideDetails.this, Driverdetailspage.class);
                intent.putExtra("driverid", driverid1);
                startActivity(intent);
            }
        });

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (driverid != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Drivers Info")
                            .child(driverid)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DriversInfo driversInfo = dataSnapshot.getValue(DriversInfo.class);
                                    if (driversInfo != null) {
                                        latLng1 = new LatLng(Double.parseDouble(driversInfo.getLat()),
                                                Double.parseDouble(driversInfo.getLng()));
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
                if (driverid1 != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Drivers Info")
                            .child(driverid1)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DriversInfo driversInfo = dataSnapshot.getValue(DriversInfo.class);
                                    if (driversInfo != null) {
                                        latLng2 = new LatLng(Double.parseDouble(driversInfo.getLat()),
                                                Double.parseDouble(driversInfo.getLng()));
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }
        }.start();
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (latLng1 != null && driverid != null)
                    getDT(latLng.latitude, latLng.longitude, latLng1.latitude, latLng1.longitude);
                if (latLng2 != null && driverid1 != null)
                    getDT1(latLng.latitude, latLng.longitude, latLng2.latitude, latLng2.longitude);

            }
        }.start();


        currentdistance1 = (TextView) findViewById(R.id.currentdistance1);
    }

    private void showmoredialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.morepopup, null);
        builder.setView(view);
        Button ok = (Button) view.findViewById(R.id.ok);
        final TextView text = (TextView) view.findViewById(R.id.text);
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

    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    String distanceString = distance.getString("text");
                    JSONObject time = steps.getJSONObject("duration");
                    String timeString = time.getString("text");
                    result = distanceString + " " + timeString;


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void getDT(final double lat1, final double lng1, final double lat2, final double lng2) {

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = pref.edit();
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
                                currentdistance.setText(distance_textD + " " + time_textD + " far from you");
                             /*   Integer time_value = Integer.parseInt(time_textD.replaceAll("\\D+", ""));
                                String start_address = legsobject.getString("start_address");
                                String end_address = legsobject.getString("end_address");

                               editor.putString("DT", distance_textD + " " + time_textD + " far from you");
                                editor.apply();
                                Double distance_value = Double.parseDouble(distance_textD.replaceAll("[^0-9\\\\.]+", ""));



                                 Toast.makeText(Map.this, distance_textD + " " + time_textD, Toast.LENGTH_SHORT).show();
*/
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

    private void getDT1(final double lat1, final double lng1, final double lat2, final double lng2) {

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = pref.edit();
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
                                currentdistance1.setText(distance_textD + " " + time_textD + " far from you");
                             /*   Integer time_value = Integer.parseInt(time_textD.replaceAll("\\D+", ""));
                                String start_address = legsobject.getString("start_address");
                                String end_address = legsobject.getString("end_address");

                               editor.putString("DT", distance_textD + " " + time_textD + " far from you");
                                editor.apply();
                                Double distance_value = Double.parseDouble(distance_textD.replaceAll("[^0-9\\\\.]+", ""));



                                 Toast.makeText(Map.this, distance_textD + " " + time_textD, Toast.LENGTH_SHORT).show();
*/
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

/*

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (carid != null) {
                  */
/*  FirebaseDatabase.getInstance().getReference()
                            .child("Employee's Car")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (employeedetails.size() > 0)
                                        employeedetails.clear();

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();

                                        if (map != null) {
                                            if (String.valueOf(map.get("car id")).equals(carid)) {
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("SignedEmployees")
                                                        .child(dataSnapshot1.getKey())
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Employeedetails employeedetails1 = dataSnapshot.getValue(Employeedetails.class);
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

                    FirebaseDatabase.getInstance().getReference()
                            .child("Driver's Car")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                        if (String.valueOf(map.get("car id")).equals(carid)) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("SignedDrivers")
                                                    .child(dataSnapshot1.getKey())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                            if (Driverdetails != null) {
                                                                drivername.setText(Driverdetails.getUsername());
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

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                    FirebaseDatabase.getInstance().getReference().child("Cars Info")
                            .child(carid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                CarsInfo carsInfo = dataSnapshot.getValue(CarsInfo.class);
                                if (carsInfo != null) {
                                    carname.setText(carsInfo.getName());
                                    carmodel.setText(carsInfo.getModel());
                                    carnumber.setText(carsInfo.getCarnumber());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        *//*
        }
            }
        }.start();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                FirebaseDatabase.getInstance().getReference()
                        .child("Employees Info").child(id)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    EmployeeInfo employeeInfo = dataSnapshot.getValue(EmployeeInfo.class);
                                    if (employeeInfo != null) {
                                        double lat = Double.parseDouble(employeeInfo.getLat());
                                        double lng = Double.parseDouble(employeeInfo.getLng());
                                        latLng = new LatLng(lat, lng);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        }.start();
        final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                            String eid = employeedetails.getEmployee_Id();
                            String bg = employeedetails.getBloodgroup();
                            String phone = employeedetails.getPhone();
                            final String address = employeedetails.getPick_up_address();


                            if (TextUtils.isEmpty(address)) {
                                new CountDownTimer(3000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                }.start();
                            } else {
                                latLng2 = new LatLng(Double.parseDouble(employeedetails.getAddresslat()), Double.parseDouble(employeedetails.getAddresslng()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (carid != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Driver's Car")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                        if (String.valueOf(map.get("car id")).equals(carid)) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Drivers Info")
                                                    .child(dataSnapshot1.getKey())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            DriversInfo driversInfo = dataSnapshot.getValue(DriversInfo.class);
                                                            if (driversInfo != null) {
                                                                LatLng latLng1 = new LatLng(Double.parseDouble(driversInfo.getLat()),
                                                                        Double.parseDouble(driversInfo.getLng()));

                                                           */
/*     currentdistance.setText(getDistance(latLng1.latitude,
                                                                        latLng1.longitude, latLng2.latitude, latLng2.longitude) + " far from your pick-up address");
*//*



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
        }.start();
*/
