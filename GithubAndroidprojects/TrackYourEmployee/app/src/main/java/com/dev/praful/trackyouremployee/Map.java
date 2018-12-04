package com.dev.praful.trackyouremployee;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialOverlayLayout;
import com.leinardi.android.speeddial.SpeedDialView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class Map extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, OnMapReadyCallback, DuoMenuView.OnMenuClickListener {

    private GoogleMap mMap;
    static final int My_PERMISSION = 7271;
    double distance = 1000;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    String key;
    String carid;
    String[] rosters;
    String currenroster;
    final List<CarsInfo> carsInfos = new ArrayList<>();
    private View locationButton;


    //Location location;
    GeoFire geoFire;
    double lat1, lng1;
    Geocoder geocoder, geocoder1;
    String address;
    String address1;
    List<Address> addresses = null;
    List<Address> addresses1 = null;
    Marker current;
    PlaceAutocompleteFragment place_location;
    String Placelocation;
    String id;
    Switch switch2;
    ImageView imageView;
    LocationManager lm;

    LatLng location;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference, imagefolder;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    android.support.v7.app.AlertDialog alertDialog;


    private DuoMenuAdapter mMenuAdapter;
    DuoDrawerLayout mDuoDrawerLayout;
    DuoMenuView mDuoMenuView;
    Toolbar mToolbar;
    ArrayList<String> mTitles = new ArrayList<>();

    SpeedDialOverlayLayout speedDialOverlayLayout;
    SpeedDialView speedDialView;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Drivers"));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        CommonSwitch.aSwitch = (Switch) findViewById(R.id.switch1);
        CommonSwitch.switch3 = (Switch) findViewById(R.id.switch3);
        CommonSwitch.switch3.setChecked(false);
        CommonSwitch.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (CommonSwitch.aSwitch.isChecked()) {
                    showpickupaddress();
                    CommonSwitch.aSwitch.setChecked(false);
                }
            }
        });
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
        mDuoMenuView.setOnMenuClickListener(this);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        mMenuAdapter = new DuoMenuAdapter(mTitles);
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mDuoDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

        final TextView username = (TextView) mDuoMenuView.getHeaderView().findViewById(R.id.username);
        FirebaseDatabase.getInstance().getReference()
                .child("SignedDrivers")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {

                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                            if (employeedetails != null) {
                                username.setText(employeedetails.getUsername());

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        LinearLayout profile = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.profilepage);
        LinearLayout login = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.Loginpage);
        LinearLayout logout = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.Logoutpage);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Map.this, Profile.class);
                startActivity(intent);

            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Map.this, Rideslist.class);
                intent.putExtra("state", "login");
                editor.putString("state", "login");
                editor.apply();
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Rideslist.class);
                intent.putExtra("state", "logout");
                editor.putString("state", "logout");
                editor.apply();
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        Button getnearestone = (Button) findViewById(R.id.getnearestone);
        getnearestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Mapfornearestone.class);
                intent.putExtra("name", pref.getString("name", ""));
                startActivity(intent);
            }
        });
        lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.ld, null);
        dialog.setView(view1);
        alertDialog = dialog.create();

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        Button go = (Button) view1.findViewById(R.id.go);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);

            }
        });
        switch2 = (Switch) findViewById(R.id.switch2);
        switch2.setChecked(false);


        final Button ride = (Button) findViewById(R.id.ride);
        ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showrideinoutdialog();
            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("RidestartandendD")
                .child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                if (map != null) {
                    if (String.valueOf(map.get("status")).equals("RideStarted")) {
                        ride.setBackground(getDrawable(R.drawable.checkedin));

                    } else {
                        ride.setBackground(getDrawable(R.drawable.checkedout));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("Driversforegroundserivces")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            if (String.valueOf(map.get("want")).equals("yes")) {
                                switch2.setChecked(true);
                            } else {
                                switch2.setChecked(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch2.isChecked()) {
                    startService(new Intent(Map.this, MyService.class));
                    FirebaseDatabase.getInstance().getReference()
                            .child("Driversforegroundserivces")
                            .child(id)
                            .child("want").setValue("yes");
                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Driversforegroundserivces")
                            .child(id)
                            .child("want").setValue("no");

                }
            }
        });
        place_location = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_location);

        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
               /* if (current != null)
                    current.remove();

*/
                Placelocation = place.getAddress().toString();
                lat1 = place.getLatLng().latitude;
                lng1 = place.getLatLng().longitude;


              /*  current = mMap.addMarker(new MarkerOptions().position(place.getLatLng()
                ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Pick up around here"));
              */
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

                //  Toast.makeText(Map.this, String.format("Address for pick up is %s", Placelocation), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{

                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CALL_PHONE


            }, My_PERMISSION);
        }
        FirebaseDatabase.getInstance().getReference()
                .child("Drivers Info")
                .child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DriversInfo driversInfo = dataSnapshot.getValue(DriversInfo.class);
                if (driversInfo != null) {
                    String lat = driversInfo.getLat();
                    String lng = driversInfo.getLng();

                    location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                    if (location != null) {
                        if (!CommonSwitch.switch3.isChecked()) {
                                if (current != null)
                                //current.remove();
                                {
                                    Location location1 = new Location("");
                                    location1.setLatitude(location.latitude);
                                    location1.setLongitude(location.longitude);
                                    // current.setPosition(location);
                                    Location location0 = new Location("");
                                    location0.setLatitude(current.getPosition().latitude);
                                    location0.setLongitude(current.getPosition().longitude);
                                    moveVechile(current, location1);
                                    current.setRotation(location0.bearingTo(location1));
                                } else {

                                current = mMap.addMarker(new MarkerOptions().
                                        position(location)
                                        // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("You")
                                        .icon(bitmapDescriptorFromVector(getBaseContext(), R.drawable.car2))
                                        .title("You"));
                            }

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));


                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        speedDialOverlayLayout = (SpeedDialOverlayLayout) findViewById(R.id.overlay);
        speedDialView = findViewById(R.id.speedDial);

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_refresh, R.drawable.refresh)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Refresh")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_read, R.drawable.readlines)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Read")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );
        /*speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_rideinout, R.drawable.car)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Ride in/out")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );
*/
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.nav_read:
                        shownotidialog();
                        return false;
                    case R.id.nav_refresh:
                        Intent i = new Intent(Map.this, Map.class);
                        startActivity(i);
                        finish();
                        return false;
                    case R.id.nav_rideinout:
                        showrideinoutdialog();
                        return false;

                    default:
                        return false;
                }
            }
        });


        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.profiledialog, null);
        imageView = (ImageView) view.findViewById(R.id.drivinglicense);


        FirebaseDatabase.getInstance().getReference()
                .child("SignedDrivers")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                            String company = Driverdetails.getCompany();
                            String phone = Driverdetails.getPhone();
                            final String address = Driverdetails.getAddress();

                            if (TextUtils.isEmpty(company) || TextUtils.isEmpty(phone)) {
                                new CountDownTimer(5000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        showprofiledialog();


                                    }
                                }.start();
                            }
                            if (TextUtils.isEmpty(address)) {
                                new CountDownTimer(3000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        Toast.makeText(Map.this, "You haven't set your pick up address , So please set your pick up address in Profile page", Toast.LENGTH_LONG).show();

                                    }
                                }.start();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FloatingActionButton CL = (FloatingActionButton)findViewById(R.id.currentlocation);
        CL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location != null) {

                    if(locationButton != null)
                        locationButton.callOnClick();
                }
            }
        });

    }

    public void moveVechile(final Marker myMarker, final Location finalPosition) {

        final LatLng startPosition = myMarker.getPosition();

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 2000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + (finalPosition.getLatitude()) * t,
                        startPosition.longitude * (1 - t) + (finalPosition.getLongitude()) * t);
                myMarker.setPosition(currentPosition);
                // myMarker.setRotation(finalPosition.getBearing());


                // Repeat till progress is completeelse
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                    // handler.postDelayed(this, 100);
                } else {
                    if (hideMarker) {
                        myMarker.setVisible(false);
                    } else {
                        myMarker.setVisible(true);
                    }
                }
            }
        });


    }

    private void showrideinoutdialog() {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.rideinoutdialog, null);
        builder1.setView(view1);
        final AlertDialog alertDialog = builder1.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog.show();

        final LinearLayout escortlayout = (LinearLayout) view1.findViewById(R.id.escortlayout);
        EditText escortname = (EditText) view1.findViewById(R.id.escortname);
        ImageView cancelescort = (ImageView) view1.findViewById(R.id.cancelescort);
        ImageView appointescort = (ImageView) view1.findViewById(R.id.appointescort);
        cancelescort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        appointescort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        Button ridein = (Button) view1.findViewById(R.id.checkin);
        Button rideout = (Button) view1.findViewById(R.id.checkout);
        final TextView CS = (TextView) view1.findViewById(R.id.currentstatus);

        spin = (Spinner) view1.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        if (carsInfos.size() > 0)
            carsInfos.clear();

        FirebaseDatabase.getInstance().getReference()
                .child("Driver's Car")
                .child("login")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()) {
                                if (dataSnapshot11.getKey().equals(id)) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Cars Info")
                                            .child(dataSnapshot1.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    CarsInfo carsInfo1 = dataSnapshot.getValue(CarsInfo.class);
                                                    if (carsInfo1 != null) {
                                                        carsInfos.add(carsInfo1);
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
                .child("logout")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()) {
                                if (dataSnapshot11.getKey().equals(id)) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Cars Info")
                                            .child(dataSnapshot1.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    CarsInfo carsInfo1 = dataSnapshot.getValue(CarsInfo.class);
                                                    if (carsInfo1 != null) {
                                                        carsInfos.add(carsInfo1);
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


        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                rosters = new String[carsInfos.size()];
                for (int i = 0; i < carsInfos.size(); i++) {
                    rosters[i] = carsInfos.get(i).getName();
                }
                final ArrayAdapter aa = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, rosters);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);
            }
        }.start();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final TextView currentroster = (TextView) view1.findViewById(R.id.currentroster);

        final String CR = sharedPreferences.getString("currentroster", "");
        if (!CR.equals("nothing")) {
            if (!CR.equals("")) {
                currentroster.setText("Current Roster : " + CR);
                currentroster.setVisibility(View.VISIBLE);

            } else {
                currentroster.setVisibility(View.GONE);
            }
        }

        FirebaseDatabase.getInstance().getReference()
                .child("RidestartandendD")
                .child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                if (map != null) {
                    if (String.valueOf(map.get("status")).equals("RideStarted")) {
                        SpannableStringBuilder builder = new SpannableStringBuilder();

                        String BLACK = "Your current status is :";
                        SpannableString whiteSpannable = new SpannableString(BLACK);
                        whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, BLACK.length(), 0);
                        builder.append(whiteSpannable);
                        String green;
                        green = "  RIDE STARTED";

                        SpannableString redSpannable = new SpannableString(green);
                        redSpannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, green.length(), 0);
                        builder.append(redSpannable);
                        String time = "\nLast Ride Time : ";
                        SpannableString timesp = new SpannableString(time);
                        timesp.setSpan(new ForegroundColorSpan(Color.BLACK), 0, time.length(), 0);
                        builder.append(timesp);

                        if (String.valueOf(map.get("time")) != null) {
                            String Time = String.valueOf(map.get("time"));
                            SpannableString Timesp = new SpannableString(Time);
                            Timesp.setSpan(new ForegroundColorSpan(Color.BLUE), 0, Time.length(), 0);
                            builder.append(Timesp);

                        }

                        CS.setText(builder, TextView.BufferType.SPANNABLE);

                    } else {
                        SpannableStringBuilder builder = new SpannableStringBuilder();

                        String BLACK = "Your current status is :";
                        SpannableString whiteSpannable = new SpannableString(BLACK);
                        whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, BLACK.length(), 0);
                        builder.append(whiteSpannable);
                        String red;
                        red = "  RIDE ENDED";
                        SpannableString redSpannable = new SpannableString(red);
                        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
                        builder.append(redSpannable);
                        String time = "\nLast Ride Time : ";
                        SpannableString timesp = new SpannableString(time);
                        timesp.setSpan(new ForegroundColorSpan(Color.BLACK), 0, time.length(), 0);
                        builder.append(timesp);

                        if (String.valueOf(map.get("time")) != null) {
                            String Time = String.valueOf(map.get("time"));
                            SpannableString Timesp = new SpannableString(Time);
                            Timesp.setSpan(new ForegroundColorSpan(Color.BLUE), 0, Time.length(), 0);
                            builder.append(Timesp);

                        }
                        CS.setText(builder, TextView.BufferType.SPANNABLE);


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_left_to_right);

        ridein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Map<String, Object> map = new HashMap<>();
                Calendar c = Calendar.getInstance();

                int hrs = c.get(Calendar.HOUR_OF_DAY);//24
                int min = c.get(Calendar.MINUTE);//59
                String AMPM;
                if (c.get(Calendar.AM_PM) == 0) {
                    AMPM = "AM";
                } else {
                    AMPM = "PM";
                }
                if (hrs > 12) {
                    hrs = hrs - 12;
                }

                String minute1 = null;
                if (min < 10) {

                    minute1 = "0" + String.valueOf(min);
                } else
                    minute1 = String.valueOf(min);

                String hour1 = null;
                if (hrs < 10) {
                    hour1 = "0" + String.valueOf(hrs);
                } else
                    hour1 = String.valueOf(hrs);

                String time = hour1 + ":" + minute1 + " " + AMPM;

                //  String time = hrs + ":" + min + " " + AMPM;
                if (carid != null) {
                    map.put("carid", carid);
                    map.put("status", "RideStarted");
                    map.put("time", time);
                    FirebaseDatabase.getInstance().getReference()
                            .child("RidestartandendD")
                            .child(id)
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Map.this, "Updated your status", Toast.LENGTH_SHORT).show();
                                    escortlayout.setVisibility(View.VISIBLE);
                                    escortlayout.startAnimation(animation);
                                    currentroster.setText("Current Roster : " + currenroster);
                                    currentroster.setVisibility(View.VISIBLE);
                                    editor.putString("currentroster", currenroster);
                                    editor.apply();
                                    // alertDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Map.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Map.this, "Please Select a Car", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new android.support.v7.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new android.support.v7.app.AlertDialog.Builder(this);


        rideout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                java.util.Map<String, Object> map = new HashMap<>();

                int hrs = c.get(Calendar.HOUR_OF_DAY);//24
                int min = c.get(Calendar.MINUTE);//59
                String AMPM;
                if (c.get(Calendar.AM_PM) == 0) {
                    AMPM = "AM";
                } else {
                    AMPM = "PM";
                }
                if (hrs > 12) {
                    hrs = hrs - 12;
                }

                String minute1 = null;
                if (min < 10) {

                    minute1 = "0" + String.valueOf(min);
                } else
                    minute1 = String.valueOf(min);

                String hour1 = null;
                if (hrs < 10) {
                    hour1 = "0" + String.valueOf(hrs);
                } else
                    hour1 = String.valueOf(hrs);

                String time = hour1 + ":" + minute1 + " " + AMPM;

                //  String time = hrs + ":" + min + " " + AMPM;
                if (carid != null) {
                    map.put("carid", carid);
                    map.put("status", "RideEnded");
                    map.put("time", time);
                    FirebaseDatabase.getInstance().getReference()
                            .child("RidestartandendD")
                            .child(id)
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Map.this, "Updated your status", Toast.LENGTH_SHORT).show();
                                    editor.putString("currentroster", "nothing");
                                    editor.apply();
                                    builder.setMessage("Do you want turn off your locations ?")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                                    startActivity(myIntent);
                                                    dialog.dismiss();
                                                }
                                            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                                    alertDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Map.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

    }

    private void showprofiledialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.profiledialog, null);
        builder.setView(view);
        final MaterialEditText phone = (MaterialEditText) view.findViewById(R.id.phone);
        final MaterialEditText company = (MaterialEditText) view.findViewById(R.id.company);
        Button button = (Button) view.findViewById(R.id.Continue);
        final AlertDialog alertDialog = builder.create();
        imageView = (ImageView) view.findViewById(R.id.drivinglicense);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });
        imagefolder = storageReference.child(id + "/" + "DL");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!phone.getText().toString().equals("")) && (!company.getText().toString().equals(""))) {
                    java.util.Map<String, Object> map = new HashMap<>();
                    map.put("phone", phone.getText().toString());
                    map.put("company", company.getText().toString());

                    FirebaseDatabase.getInstance().getReference()
                            .child("SignedDrivers")
                            .child(id)
                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {

                                    java.util.Map<String, Object> drivinglicense = new HashMap<>();
                                    drivinglicense.put("drivinglicense", uri.toString());
                                    FirebaseDatabase.getInstance().getReference("SignedDrivers")
                                            .child(id)
                                            .updateChildren(drivinglicense)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Map.this, "Image Updated !", Toast.LENGTH_SHORT).show();

                                                    } else
                                                        Toast.makeText(Map.this, "Image Update error !", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });
                            Toast.makeText(Map.this, "Done !", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            showpickupaddress();
                        }
                    });


                } else {
                    Toast.makeText(Map.this, "Please fill all", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog.show();


    }


    private void chooseimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture: "), CommonSwitch.PICK_IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonSwitch.PICK_IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final Uri uri = data.getData();
            if (uri != null) {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Please wait..");
                dialog.setCancelable(false);
                dialog.show();

                String imagename = UUID.randomUUID().toString();
                imagefolder = storageReference.child(id + "/" + "DL");
                imagefolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Picasso.with(Map.this).load(uri).into(imageView);
                        Toast.makeText(Map.this, "Uploaded your image", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        dialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
            }
        }
    }

    private void showtaponmapdialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.showtaponmapdialog, null);
        Button done = (Button) view1.findViewById(R.id.done);

        builder.setView(view1);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showlocationdailog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.showlocation, null);
        Button done = (Button) view1.findViewById(R.id.done);
        final Button cancel = (Button) view1.findViewById(R.id.cancel);


        final TextView addresstap = (TextView) view1.findViewById(R.id.address);
        addresstap.setText(address1);
        builder.setView(view1);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        dialog.setCancelable(false);
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Map<String, Object> map = new HashMap<>();
                map.put("address", addresstap.getText().toString());
                map.put("addresslat", String.valueOf(lat1));
                map.put("addresslng", String.valueOf(lng1));
                FirebaseDatabase.getInstance().getReference()
                        .child("SignedDrivers")
                        .child(id)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonSwitch.switch3.setChecked(false);
                        dialog.dismiss();
                        Toast.makeText(Map.this, "Done !", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void shownotidialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Map.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.shownoti, null);
        TextView notificationmessage = (TextView) view1.findViewById(R.id.notificationmessage);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications , even when the app is working in background or it is killed . Like if Admin has changed your ride , you will be notified ." +
                    "\nAnd for those whose Android version is lesser to 8, no need to switch ON.<font color=\"#42f471\"><bold>"
                    + "Yours is more or equal to 8 "
                    + "</bold></font>" + ", So please switch it ON.";


            notificationmessage.setText(Html.fromHtml(text));

        } else {
            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications , even when the app is working in background or it is killed .  Like if Admin has changed your ride , you will be notified .   " +
                    "And for those whose Android version is lesser to 8, no need to switch ON.  <font color=\"#42f471\"><bold>" + "Yours is lesser than 8" + "</bold></font>" + " , So there is no need to Switch it On . But incase your not getting notifications , please Switch it On . When it is switched On there is 0% of failure .";
            notificationmessage.setText(Html.fromHtml(text));
        }*/
        String text = "If your running the device with power saving mode ON , Please open Battery Optimization dialog box by clicking the Settings button below and accept it by clicking 'OK' to get Notifications even when the app is removed from background or killed .";

        notificationmessage.setText(text);
        Button settings = (Button) view1.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String packageName = getPackageName();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        });
        Button ok = (Button) view1.findViewById(R.id.ok);
        builder.setView(view1);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

    }

    private void showpickupaddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.pickupaddressdialog, null);
        builder.setView(view);
        Button currentlocation = (Button) view.findViewById(R.id.currentlocation);
        Button taponmap = (Button) view.findViewById(R.id.taponmap);
        final AlertDialog alertDialog1 = builder.create();

        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }
                if (!gps_enabled && !network_enabled) {
                    if (!(Map.this.isFinishing())) {
                        alertDialog.show();
                        Toast.makeText(Map.this, "Please turn on your location and update your pick up address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (location != null) {
                        lat1 = location.latitude;
                        lng1 = location.longitude;
                        //txtAddress.setText(String.format("%f %f", Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));

                        Double lat = location.latitude;
                        Double lng = location.longitude;
                        geocoder = new Geocoder(Map.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(lat, lng, 1);

                            if (addresses != null && addresses.size() > 0) {
                                String addressc = addresses.get(0).getAddressLine(0);
                                address1 = addressc;
                                alertDialog1.dismiss();
                                showlocationdailog();
                            } else {
                                Toast.makeText(Map.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();
                                alertDialog1.dismiss();

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        taponmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
                CommonSwitch.switch3.setChecked(true);
                showtaponmapdialog();

            }
        });
        alertDialog1.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog1.show();

    }


    private void loadallemployess(final LatLng location) {
        mMap.clear();
        current = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).
                position(location).title("You")
        );


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));


        GeoFire gf = new GeoFire(FirebaseDatabase.getInstance().getReference("Employess"));

        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(location.latitude, location.longitude), distance);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {
               /* if (rideinoutdialog.equals("notfound")) {
                    rideinoutdialog = "found";
               */
                FirebaseDatabase.getInstance().getReference()
                        .child("Employees Info")
                        .child(key.toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                EmployeeInfo employeeInfo = dataSnapshot.getValue(EmployeeInfo.class);
                                if (employeeInfo != null) {
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                                            .flat(true).title(employeeInfo.getName())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                        /*java.util.Map<String, String> currentemployee = new HashMap<>();
                                        currentemployee.put("latitude", String.valueOf(location.latitude));
                                        currentemployee.put("longitude", String.valueOf(location.longitude));
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Driver's current Employee")
                                                .child(getIntent().getStringExtra("name").toString())
                                                .setValue(currentemployee);
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Driver's Employee found")
                                                .child(getIntent().getStringExtra("name").toString())
                                                .setValue("found");
                          */
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                  /*  FirebaseDatabase.getInstance().getReference()
                            .child("Employess")
                            .child(key)
                            .removeValue();*/
            }

            @Override
            public void onKeyExited(String key) {


            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

               /* if (rideinoutdialog.equals("notfound") && distance < limit) {

                    distance = distance + .1;
                    loadnearestemployess(location);
                }*/

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)

        {

            case My_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ComponentName serviceName = new ComponentName(this, MyService.class);
                    JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                    JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .setPersisted(true)
                            .build();
                    jobScheduler.schedule(startMySerivceJobInfo);
// startService(new Intent(Map.this, MyService.class));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);

                }
            }
            break;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 70, 40, 0);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }
                if (!gps_enabled && !network_enabled) {
                    if (!(Map.this.isFinishing())) {
                        alertDialog.show();
                        Toast.makeText(Map.this, "Please turn on your location and update your pick up address", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if (location != null) {
                        if (current != null)
                        //current.remove();
                        {
                            Location location1 = new Location("");
                            location1.setLatitude(location.latitude);
                            location1.setLongitude(location.longitude);
                            // current.setPosition(location);
                            Location location0 = new Location("");
                            location0.setLatitude(current.getPosition().latitude);
                            location0.setLongitude(current.getPosition().longitude);
                            moveVechile(current, location1);
                            current.setRotation(location0.bearingTo(location1));
                        } else {
                            current = mMap.addMarker(new MarkerOptions().position(location)
                                    .title("You")
                                    .icon(bitmapDescriptorFromVector(getBaseContext(), R.drawable.car2))
                            );
                        }
                        CommonSwitch.switch3.setChecked(false);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));

                        return true;
                    }
                }
                return true;
            }
        });
        // Add a marker in Sydney and move the camera

        locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        if(locationButton != null)
            locationButton.setVisibility(View.GONE);

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        CommonSwitch.switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (CommonSwitch.switch3.isChecked()) {
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {

                            lat1 = latLng.latitude;
                            lng1 = latLng.longitude;
                            Double lat1 = latLng.latitude;
                            Double lng1 = latLng.longitude;
                            geocoder1 = new Geocoder(Map.this, Locale.getDefault());

                            try {
                                addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                                if (addresses1 != null && addresses1.size() > 0) {

                                    address1 = addresses1.get(0).getAddressLine(0);

                                    Toast.makeText(Map.this, "TAPPED LOCATION : " + address1, Toast.LENGTH_LONG).show();
                                } else {

                                    Toast.makeText(Map.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();
                                    CommonSwitch.switch3.setChecked(false);

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addresses1 != null && addresses1.size() > 0) {

                                if (current != null)
                                    current.remove();

                                current = mMap.addMarker(new MarkerOptions().draggable(true)
                                        .icon(bitmapDescriptorFromVector(getBaseContext(), R.drawable.car2))
                                        .position(latLng).title("Tapped location").snippet(address1.toString()));

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                                current.showInfoWindow();
                            }

                        }
                    });

                } else {
                    mMap.setOnMapClickListener(null);
                }
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                showlocationdailog();
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng markerLocation = marker.getPosition();
                lat1 = markerLocation.latitude;
                lng1 = markerLocation.longitude;
                final Double lat1 = markerLocation.latitude;
                final Double lng1 = markerLocation.longitude;
                geocoder1 = new Geocoder(Map.this, Locale.getDefault());

                try {
                    addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                    if (addresses1 != null && addresses1.size() > 0) {

                        address1 = addresses1.get(0).getAddressLine(0);

                        Toast.makeText(Map.this, "DRAGGED LOCATION : " + address1, Toast.LENGTH_LONG).show();
                    } else {
                        CommonSwitch.switch3.setChecked(false);

                        Toast.makeText(Map.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses1 != null && addresses1.size() > 0) {

                    if (current != null)
                        current.remove();


                    current = mMap.addMarker(new MarkerOptions()
                                .draggable(true)
                                .icon(bitmapDescriptorFromVector(getBaseContext(), R.drawable.car2))
                                .position(markerLocation).title("Dragged location").snippet(address1.toString()));

                    current.showInfoWindow();
                    //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 15.0f));

                }
            }
        });

    }


    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        for (int k = 0; k < carsInfos.size(); k++) {
            if (carsInfos.get(k).getName().equals(rosters[i])) {
                carid = carsInfos.get(k).getId();
                currenroster = rosters[i];

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}
 /* FirebaseDatabase.getInstance().getReference().child("Driver's Employee found")
                .child(getIntent().getStringExtra("name").toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null && dataSnapshot.getValue().equals("found")) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Driver's current Employee").child(getIntent().getStringExtra("name").toString())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null) {
                                                java.util.Map<String, Object> currentemployee = (HashMap<String, Object>) dataSnapshot.getValue();
                                                LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(currentemployee.get("latitude"))),
                                                        Double.parseDouble(String.valueOf(currentemployee.get("longitude")))
                                                );
                                                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), .1);
                                                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                                    @Override
                                                    public void onKeyEntered(String key, GeoLocation location1) {

                                                        if (key.toString().equals(getIntent().getStringExtra("name").toString()))
                                                            loadnearestemployess(location);
                                                    }

                                                    @Override
                                                    public void onKeyExited(String key) {

                                                    }

                                                    @Override
                                                    public void onKeyMoved(String key, GeoLocation location) {

                                                    }

                                                    @Override
                                                    public void onGeoQueryReady() {

                                                    }

                                                    @Override
                                                    public void onGeoQueryError(DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Driver's Employee found")
                                    .child(getIntent().getStringExtra("name").toString())
                                    .setValue("Nfound");

                        }
                        else {
                            distance = distance + .1;
                            loadnearestemployess(location);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
   */   /* Uri navigationIntentUri = Uri.parse("google.navigation:q=" + location.getLatitude() + " , " + location.getLongitude() + "&mode=d");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
*/

/*  geoFire.setLocation(id, new GeoLocation(Double.parseDouble(lat), Double.parseDouble(lng))
                            , new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {


                                   *//* FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("Employess")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    loadallemployess(location);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });*//*
                                   // loadallemployess(location);


                                }
                            }
                    );*/
    /*       imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                java.util.Map<String, Object> drivinglicense = new HashMap<>();
                                drivinglicense.put("drivinglicense", uri.toString());
                                FirebaseDatabase.getInstance().getReference("SignedDrivers")
                                        .child(id)
                                        .updateChildren(drivinglicense)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Map.this, "Image Updated !", Toast.LENGTH_SHORT).show();
                                                    Picasso.with(Map.this).load(uri).into(imageView);

                                                } else
                                                    Toast.makeText(Map.this, "Image Update error !", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        });*/
    /*

        FirebaseDatabase.getInstance().getReference()
                .child("Driver's Car")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null)
                            carid = String.valueOf(map.get("car id"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (carid != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Employee's Car")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                        if (String.valueOf(map.get("car id")).equals(carid)) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Employees Info")
                                                    .child(dataSnapshot1.getKey())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            DriversInfo driversInfo = dataSnapshot.getValue(DriversInfo.class);
                                                            final LatLng latLng = new LatLng(Double.parseDouble(driversInfo.getLat()),
                                                                    Double.parseDouble(driversInfo.getLng()));

                                                            if (location != null) {
                                                                mMap.clear();
                                                                current = mMap.addMarker(new MarkerOptions().
                                                                        position(location)
                                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("You")
                                                                );
                                                            }

                                                            if (!CommonSwitch.switch3.isChecked())
                                                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));

                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("SignedEmployees")
                                                                    .child(dataSnapshot1.getKey())
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);

                                                                            if (employeedetails != null) {
                                                                                mMap.addMarker(new MarkerOptions()
                                                                                        .position(latLng)
                                                                                        .title(employeedetails.getUsername())
                                                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                                                                );
                                                                            }
                                                                        }


                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                            new CountDownTimer(1000, 1000) {
                                                                @Override
                                                                public void onTick(long millisUntilFinished) {

                                                                }

                                                                @Override
                                                                public void onFinish() {

                                                                }
                                                            }.start();

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
