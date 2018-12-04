package com.dev.praful.trackyourdriver;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialOverlayLayout;
import com.leinardi.android.speeddial.SpeedDialView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.shadowfax.proswipebutton.ProSwipeButton;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import retrofit2.Call;
import retrofit2.Callback;

public class Map extends AppCompatActivity implements OnMapReadyCallback, DuoMenuView.OnMenuClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    static final int My_PERMISSION = 7271;
    LocationRequest locationRequest;
    Location location;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;

    IGoogleAPI Services;

    String time;

    String smsMessage;
    java.util.Map<String, Object> map1;
    String distance_textD, time_textD;
    String destinationAddress;


    String carid;
    String time_txt = "";
    String parsedDistance = "";
    String response;
    String sosnumber;
    GeoFire geoFireaddress;
    String result;
    Marker driver1;
    Marker driver;
    String snippet;
    LatLng latLng, latLng2;
    LatLng latLng1, latLng3;
    String id;
    GeoFire geoFire;
    String address1;
    List<Address> addresses = null;
    List<Address> addresses1 = null;
    LocationManager lm;
    Marker current;
    PlaceAutocompleteFragment place_location;
    String Placelocation;
    ProgressDialog progressDialog;
    double lat1, lng1;
    Geocoder geocoder, geocoder1;
    String address;
    Switch switch2;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    android.support.v7.app.AlertDialog alertDialog;
    SupportMapFragment mapFragment;
    private View locationButton;
    private DuoMenuAdapter mMenuAdapter;
    DuoDrawerLayout mDuoDrawerLayout;
    DuoMenuView mDuoMenuView;
    Toolbar mToolbar;
    ArrayList<String> mTitles = new ArrayList<>();

    SpeedDialOverlayLayout speedDialOverlayLayout;
    SpeedDialView speedDialView;
    AlertDialog alertDialogCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Employess"));
        CommonSwitch.aSwitch = (Switch) findViewById(R.id.switch1);
        CommonSwitch.switch3 = (Switch) findViewById(R.id.switch3);
        CommonSwitch.switch3.setChecked(false);
        CommonSwitch.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (CommonSwitch.aSwitch.isChecked()) {
                    showpickupaddresstrue();
                    CommonSwitch.aSwitch.setChecked(false);
                }
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseDatabase.getInstance().getReference()
                .child("checkinandout")
                .child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                if (map != null) {
                    Drawable mDrawable = ContextCompat.getDrawable(getBaseContext(), R.drawable.check);

                    if (String.valueOf(map.get("status")).equals("checkedin")) {
                        mDrawable.setColorFilter(new
                                PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY));
                    } else {
                        mDrawable.setColorFilter(new
                                PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY));

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
                new SpeedDialActionItem.Builder(R.id.nav_check, R.drawable.check)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Check in & out")
                        .setLabelColor(Color.BLACK)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_driverlogin, R.drawable.driver1)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Login Driver")
                        .setLabelColor(Color.GREEN)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.nav_driverlogout, R.drawable.driver1)
                        .setFabBackgroundColor(Color.WHITE)
                        .setLabel("Logout Driver")
                        .setLabelColor(Color.RED)
                        .setLabelBackgroundColor(Color.WHITE)
                        .setLabelClickable(true)
                        .create()
        );

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.nav_check:
                        showcheckinout();
                        return false;
                    case R.id.nav_refresh:
                        Intent i = new Intent(Map.this, Map.class);
                        startActivity(i);
                        finish();
                        return false;
                    case R.id.nav_driverlogout:
                        if (latLng1 != null)
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15.0f));
                        else
                            Toast.makeText(Map.this, "No Logout driver available", Toast.LENGTH_SHORT).show();
                        return false;
                    case R.id.nav_driverlogin:
                        if (latLng3 != null)
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng3, 15.0f));
                        else
                            Toast.makeText(Map.this, "No Login driver available", Toast.LENGTH_SHORT).show();
                        return false;
                    default:
                        return false;
                }
            }
        });

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
        //   mDuoMenuView.setAdapter(mMenuAdapter);

        final TextView username = (TextView) mDuoMenuView.getHeaderView().findViewById(R.id.username);

        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
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
        LinearLayout readdialog = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.readdialog);
        LinearLayout ridedetails = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.ridedetailspage);
        LinearLayout timeoff = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.timeoffpage);
        LinearLayout commentfeedback = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.commentfeedbackpage);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(200, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(Map.this, Profile.class);
                        startActivity(intent);

                    }
                }.start();
            }
        });

        readdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(200, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        shownotidialog();

                    }
                }.start();
            }
        });

        ridedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(200, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(Map.this, RideDetails.class);
                        startActivity(intent);

                    }
                }.start();
            }
        });

        timeoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(200, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(Map.this, MyTimeoff.class);
                        startActivity(intent);

                    }
                }.start();

            }
        });
        commentfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(200, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(Map.this, CommentandFeedbacksection.class);
                        startActivity(intent);

                    }
                }.start();

            }
        });

        Services = CommonSwitch.getGoogleService();
        lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.ld, null);
        dialog.setView(view);
        alertDialog = dialog.create();

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button go = (Button) view.findViewById(R.id.go);
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


        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                        if (employeedetails != null) {
                            smsMessage = "Employee : " + employeedetails.getUsername() + " has tapped SOS !!";
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("SOS")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                sosnumber = String.valueOf(map.get("Number"));
                                destinationAddress = String.valueOf(map.get("SmsNumber"));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Button sos = (Button) findViewById(R.id.sos);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sosnumber != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + sosnumber));
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
                if (destinationAddress != null) {
                    String scAddress = null;
                    PendingIntent sentIntent = null, deliveryIntent = null;

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage,
                            sentIntent, deliveryIntent);

                }
            }
        });
        progressDialog = new ProgressDialog(this);

        geoFireaddress = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Employee's addresses"));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //   fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


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
                ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Pick up here"));
              */
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

                //  Toast.makeText(Map.this, String.format("Address for pick up is %s", Placelocation), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });
        checkForSmsPermission();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                ) {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CALL_PHONE


            }, My_PERMISSION);
        }
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

                            if (TextUtils.isEmpty(eid) || TextUtils.isEmpty(bg) || TextUtils.isEmpty(phone)) {
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
                            } else {
                                latLng2 = new LatLng(Double.parseDouble(employeedetails.getAddresslat()), Double.parseDouble(employeedetails.getAddresslng()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Car")
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
                                        if (!CommonSwitch.switch3.isChecked())

                                        {
                                         /*   if (current != null)
                                                current.remove();
*/
                                            if (current != null)
                                            //current.remove();
                                            {
                                                Location location1 = new Location("");
                                                location1.setLatitude(latLng.latitude);
                                                location1.setLongitude(latLng.longitude);
                                                // current.setPosition(location);
                                              /*  Location location0 = new Location("");
                                                location0.setLatitude(current.getPosition().latitude);
                                                location0.setLongitude(current.getPosition().longitude);
                                            */    moveVechile(current, location1);
                                                //current.setRotation(location0.bearingTo(location1));
                                            } else {

                                                current = mMap.addMarker(new MarkerOptions().position(latLng)
                                                        .title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                                );
                                            }
                                           /* CameraPosition cameraPosition = new CameraPosition.Builder()
                                                    .bearing(0)                // Sets the orientation of the camera to east
                                                    .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                                                    .target(latLng)
                                                    .zoom(15.0f)// Sets the center of the map to Mountain View
                                                    .build();                   // Creates a CameraPosition from the builder
                                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/
                                          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        }.start();

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (carid != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Driver's Car")
                            .child("logout")
                            .child(carid)
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
                                                                latLng1 = new LatLng(Double.parseDouble(driversInfo.getLat()),
                                                                        Double.parseDouble(driversInfo.getLng()));
                                                                if (latLng1 != null) {

                                                                    if (driver != null)
                                                                    //current.remove();
                                                                    {
                                                                        Location location1 = new Location("");
                                                                        location1.setLatitude(latLng1.latitude);
                                                                        location1.setLongitude(latLng1.longitude);
                                                                        // current.setPosition(location);
                                                                        Location location0 = new Location("");
                                                                        location0.setLatitude(driver.getPosition().latitude);
                                                                        location0.setLongitude(driver.getPosition().longitude);
                                                                        moveVechile(driver, location1);
                                                                        driver.setRotation(location0.bearingTo(location1));
                                                                    } else {


                                                                        driver = mMap.addMarker(new MarkerOptions()
                                                                            .position(latLng1)
                                                                            .title("Your Logout Driver")
                                                                            .icon(bitmapDescriptorFromVector(getBaseContext(), R.drawable.car2)));

                                                                }}
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

                    FirebaseDatabase.getInstance().getReference()
                            .child("Driver's Car")
                            .child("login")
                            .child(carid)
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
                                                                latLng3 = new LatLng(Double.parseDouble(driversInfo.getLat()),
                                                                        Double.parseDouble(driversInfo.getLng()));

                                                                if (latLng3 != null) {

                                                                    if (driver1 != null)
                                                                    //current.remove();
                                                                    {
                                                                        Location location1 = new Location("");
                                                                        location1.setLatitude(latLng3.latitude);
                                                                        location1.setLongitude(latLng3.longitude);
                                                                        // current.setPosition(location);
                                                                        Location location0 = new Location("");
                                                                        location0.setLatitude(driver1.getPosition().latitude);
                                                                        location0.setLongitude(driver1.getPosition().longitude);
                                                                        moveVechile(driver1, location1);
                                                                        driver1.setRotation(location0.bearingTo(location1));
                                                                    } else {


                                                                        driver1 = mMap.addMarker(new MarkerOptions()
                                                                                .position(latLng3)
                                                                                .title("Your Login Driver")
                                                                                .icon(bitmapDescriptorFromVector(getBaseContext(), R.drawable.car2)));

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
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                }
            }
        }.start();


        FloatingActionButton CL = (FloatingActionButton)findViewById(R.id.currentlocation);
        CL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latLng != null) {

                    if(locationButton != null)
                        locationButton.callOnClick();
                }
            }
        });

        /*else {
            buildLocationreq();
            buildLocationCallback();
            displaylocation();
        }*/
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

    private void showcheckinout() {

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        final android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new android.support.v7.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.check, null);
        builder1.setView(view1);
        alertDialogCheck = builder1.create();
        alertDialogCheck.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialogCheck.show();
        final TextView CS = (TextView) view1.findViewById(R.id.currentstatus);
        final ProSwipeButton proSwipeBtn = view1.findViewById(R.id.proswipebutton_main);
        proSwipeBtn.setSwipeDistance(0.5f);
        proSwipeBtn.setArrowColor(Color.LTGRAY);

        FirebaseDatabase.getInstance().getReference()
                .child("checkinandout")
                .child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                if (map != null) {
                    if (String.valueOf(map.get("status")).equals("checkedin")) {
                        SpannableStringBuilder builder = new SpannableStringBuilder();

                        String BLACK = "Your current status is :";
                        SpannableString whiteSpannable = new SpannableString(BLACK);
                        whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, BLACK.length(), 0);
                        builder.append(whiteSpannable);
                        String green;
                        green = " CHECKED IN";

                        proSwipeBtn.setText("Check Out");
                        proSwipeBtn.setBackgroundColor(Color.RED);

                        SpannableString redSpannable = new SpannableString(green);
                        redSpannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, green.length(), 0);
                        builder.append(redSpannable);
                        String time = "\nLastly Checked Time : ";
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
                        red = "  CHECKED OUT";
                        SpannableString redSpannable = new SpannableString(red);
                        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
                        builder.append(redSpannable);
                        String time = "\nLastly Checked Time : ";
                        SpannableString timesp = new SpannableString(time);
                        timesp.setSpan(new ForegroundColorSpan(Color.BLACK), 0, time.length(), 0);
                        builder.append(timesp);

                        if (String.valueOf(map.get("time")) != null) {
                            String Time = String.valueOf(map.get("time"));
                            SpannableString Timesp = new SpannableString(Time);
                            Timesp.setSpan(new ForegroundColorSpan(Color.BLUE), 0, Time.length(), 0);
                            builder.append(Timesp);

                        }
                        proSwipeBtn.setText("Check In");
                        // proSwipeBtn.setBackgroundColor(Color.GREEN);
                        proSwipeBtn.setBackgroundColor(getColor(R.color.green));


                        CS.setText(builder, TextView.BufferType.SPANNABLE);


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        TextView comment = (TextView) view1.findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCheck.dismiss();
                Intent intent = new Intent(Map.this, CommentandFeedbacksection.class);
                startActivity(intent);
            }
        });

        proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
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

                time = hour1 + ":" + minute1 + " " + AMPM;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (proSwipeBtn.getText().toString().equals("Check In")) {
                            java.util.Map<String, Object> map = new HashMap<>();
                            map.put("status", "checkedin");
                            map.put("time", time);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("checkinandout")
                                    .child(id)
                                    .setValue(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Map.this, "Updated your status", Toast.LENGTH_SHORT).show();
                                            proSwipeBtn.showResultIcon(true);
                                            new CountDownTimer(1000, 1000) {
                                                @Override
                                                public void onTick(long l) {

                                                }

                                                @Override
                                                public void onFinish() {
                                                    alertDialogCheck.dismiss();

                                                }
                                            }.start();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Map.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            java.util.Map<String, Object> map = new HashMap<>();
                            map.put("status", "checkedout");
                            map.put("time", time);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("checkinandout")
                                    .child(id)
                                    .setValue(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Map.this, "Updated your status", Toast.LENGTH_SHORT).show();
                                            proSwipeBtn.showResultIcon(true);

                                            new CountDownTimer(1000, 1000) {
                                                @Override
                                                public void onTick(long l) {

                                                }

                                                @Override
                                                public void onFinish() {
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

                                                    alertDialogCheck.dismiss();
                                                }
                                            }.start();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Map.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }, 2000);
            }
        });


    }


    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    My_PERMISSION);
        }
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
                    parsedDistance = distanceString;
                    time_txt = timeString;


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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void showmylocation() {
        if (current != null)
            current.remove();

        current = mMap.addMarker(new MarkerOptions().position(latLng)
                .title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

    }

    private void showprofiledialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.profiledialog, null);
        builder.setView(view);
        final MaterialEditText phone = (MaterialEditText) view.findViewById(R.id.phone);
        final MaterialEditText BG = (MaterialEditText) view.findViewById(R.id.bg);
        final MaterialEditText Eid = (MaterialEditText) view.findViewById(R.id.Eid);
        final MaterialEditText gender = (MaterialEditText) view.findViewById(R.id.gender);
        Button button = (Button) view.findViewById(R.id.Continue);
        final AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!phone.getText().toString().equals("")) && (!gender.getText().toString().equals("")) && (!BG.getText().toString().equals("")) && (!Eid.getText().toString().equals(""))) {
                    java.util.Map<String, Object> map = new HashMap<>();
                    map.put("bloodgroup", BG.getText().toString());
                    map.put("phone", phone.getText().toString());
                    map.put("employee_Id", Eid.getText().toString());
                    map.put("gender", gender.getText().toString());
                    FirebaseDatabase.getInstance().getReference()
                            .child("SignedEmployees")
                            .child(id)
                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
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

    private void showpickupaddresstrue() {
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

                    }
                } else {
                    if (latLng != null) {

                        lat1 = latLng.latitude;
                        lng1 = latLng.longitude;
                        //txtAddress.setText(String.format("%f %f", Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));

                        Double lat = latLng.latitude;
                        Double lng = latLng.longitude;
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

    private void shownotidialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Map.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.shownoti, null);
        TextView notificationmessage = (TextView) view1.findViewById(R.id.notificationmessage);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications , even when the app is working in background or it is killed . Like if Admin has changed your ride or when driver is within the range of 3km from your pickup address , you will be notified ." +
//                    "\nAnd for those whose Android version is lesser to 8, no need to switch ON.<font color=\"#42f471\"><bold>"
//                    + "Yours is more or equal to 8 "
//                    + "</bold></font>" + ", So please switch it ON,  atleast for driver's notification";
//
//
//            notificationmessage.setText(Html.fromHtml(text));
//
//        } else {
//            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications , even when the app is working in background or it is killed .  Like if Admin has changed your ride or when driver is within the range of 3km from your pickup address , you will be notified .   " +
//                    "And for those whose Android version is lesser to 8, no need to switch ON.  <font color=\"#42f471\"><bold>" + "Yours is lesser than 8" + "</bold></font>" + " , So there is no need to Switch it On . But incase your not getting notifications , please Switch it On . When it is switched On there is 0% of failure .";
//            notificationmessage.setText(Html.fromHtml(text));
//        }
        String text = "If your running the device with power saving mode ON , Please open Battery Optimization dialog box by clicking the Settings button below and accept it by clicking 'OK' to get Notifications even when the app is removed from background or killed .";

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
        notificationmessage.setText(text);
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
                    if (latLng != null) {
                        lat1 = latLng.latitude;
                        lng1 = latLng.longitude;
                        //txtAddress.setText(String.format("%f %f", Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));

                        Double lat = latLng.latitude;
                        Double lng = latLng.longitude;
                        geocoder = new Geocoder(Map.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(lat, lng, 1);

                            if (addresses != null && addresses.size() > 0) {
                                String addressc = addresses.get(0).getAddressLine(0);
                                alertDialog1.dismiss();
                                address1 = addressc;
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
        alertDialog1.setCancelable(false);
        alertDialog1.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog1.show();

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
                    //startService(new Intent(Map.this, MyService.class));
                    /* buildLocationCallback();
                    buildLocationreq();
                    displaylocation();
                */

                }

            }
            break;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.setPadding(left, top, right, bottom);
        mMap.setPadding(0, 50, 41, 0);
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
                    }
                } else {
                    if (latLng != null) {
                        if (current != null)
                        //  current.remove();
                        {
                            Location location1 = new Location("");
                            location1.setLatitude(latLng.latitude);
                            location1.setLongitude(latLng.longitude);
                            // current.setPosition(location);
                            Location location0 = new Location("");
                            location0.setLatitude(current.getPosition().latitude);
                            location0.setLongitude(current.getPosition().longitude);
                            moveVechile(current, location1);

                        } else {

                            current = mMap.addMarker(new MarkerOptions().position(latLng)
                                    .title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            );
                        }
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .bearing(0)                // Sets the orientation of the camera to east
                                .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                                .target(latLng)
                                .zoom(15.0f)// Sets the center of the map to Mountain View
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                      //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                        CommonSwitch.switch3.setChecked(false);
                        return true;
                    } else
                        return false;

                }
                return true;
            }
        });

      locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        if(locationButton != null)
            locationButton.setVisibility(View.GONE);

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .target(sydney)
                .zoom(15.0f)// Sets the center of the map to Mountain View
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CommonSwitch.switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (CommonSwitch.switch3.isChecked()) {
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            progressDialog.setMessage("Please wait");
                            progressDialog.show();
                            lat1 = latLng.latitude;
                            lng1 = latLng.longitude;
                            final Double lat1 = latLng.latitude;
                            final Double lng1 = latLng.longitude;
                            geocoder1 = new Geocoder(Map.this, Locale.getDefault());

                            try {
                                addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                                if (addresses1 != null && addresses1.size() > 0) {

                                    address1 = addresses1.get(0).getAddressLine(0);

                                    Toast.makeText(Map.this, "TAPPED LOCATION : " + address1, Toast.LENGTH_LONG).show();
                                } else {
                                    CommonSwitch.switch3.setChecked(false);

                                    Toast.makeText(Map.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();


                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addresses1 != null && addresses1.size() > 0) {

                                if (current != null) {
                                    current.remove();
                                }

                                current = mMap.addMarker(new MarkerOptions()
                                        .draggable(true)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(latLng).title("Tapped location").snippet(address1.toString()));

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                                current.showInfoWindow();
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
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

                    if (current != null) {
                        current.remove();
                    }

                    current = mMap.addMarker(new MarkerOptions()
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(markerLocation).title("Dragged location").snippet(address1.toString()));

                    current.showInfoWindow();
                    //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 15.0f));

                }
            }
        });

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

                                //GET DISTANCE
                                JSONObject distance = legsobject.getJSONObject("distance");
                                distance_textD = distance.getString("text");

                                Double distance_value = Double.parseDouble(distance_textD.replaceAll("[^0-9\\\\.]+", ""));

                                JSONObject time = legsobject.getJSONObject("duration");
                                time_textD = time.getString("text");
                                Integer time_value = Integer.parseInt(time_textD.replaceAll("\\D+", ""));
                              /*  String start_address = legsobject.getString("start_address");
                                String end_address = legsobject.getString("end_address");
                             */
                                editor.putString("DT", distance_textD + " " + time_textD + " far from you");
                                editor.apply();


                                // Toast.makeText(Map.this, distance_textD + " " + time_textD, Toast.LENGTH_SHORT).show();

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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final LatLng latLng1 = new LatLng(17.434810272796604, 78.38469553738832);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("please wait..");
                progressDialog.setCancelable(false);
                if (!Map.this.isFinishing())
                    progressDialog.show();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        String requestUrl = null;
                        try {
                            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                                    "transit_routing_preference=less_driving&" + "origin=" + latLng1.latitude + "," + latLng1.longitude + "&" +
                                    "destination=" + lat1 + "," + lng1 + "&" +
                                    "key=" + getResources().getString(R.string.google_maps_key);
                            Log.e("LINK", requestUrl);
                            Services.getPath(requestUrl).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().toString());
                                        String result = jsonObject.getString("status");

                                        if (!result.equals("OVER_QUERY_LIMIT")) {

                                            JSONArray routes = jsonObject.getJSONArray("routes");
                                            JSONObject object = routes.getJSONObject(0);


                                            JSONArray legs = object.getJSONArray("legs");
                                            JSONObject legsobject = legs.getJSONObject(0);

                                            //GET DISTANCE
                                            JSONObject distance = legsobject.getJSONObject("distance");
                                            final String distance_text = distance.getString("text");

                                            final Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));

                                            parsedDistance = distance_text;
                                            JSONObject time = legsobject.getJSONObject("duration");
                                            final String time_text1 = time.getString("text");
                                            Integer time_value = Integer.parseInt(time_text1.replaceAll("\\D+", ""));

                                            time_txt = time_text1;
                                      /*  String start_address = legsobject.getString("start_address");
                                        String end_address = legsobject.getString("end_address");
*/
                                            Toast.makeText(Map.this, distance_text + " " + time_text1, Toast.LENGTH_SHORT).show();
                                            new CountDownTimer(3000, 1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    if (!parsedDistance.equals("")) {
                                                        if (!time_txt.equals("")) {
                                                            FirebaseDatabase.getInstance()
                                                                    .getReference().child("SignedEmployees")
                                                                    .child(id)
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            map1 = (HashMap<String, Object>) dataSnapshot.getValue();


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
                                                                    final Orderedemployeedetails orderedemployeedetails = new
                                                                            Orderedemployeedetails(String.valueOf(map1.get("username")),
                                                                            distance_text
                                                                            , id);

                                                                    //  Scanner sc = new Scanner(parsedDistance);

                                                                    //   .setValue(String.valueOf(sc.nextDouble()))
                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("Final Ordered Employees")
                                                                            .child(id)
                                                                            .setValue(orderedemployeedetails)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    final java.util.Map<String, Object> map = new HashMap<>();
                                                                                    map.put("pick_up_address", addresstap.getText().toString());
                                                                                    map.put("addresslat", String.valueOf(lat1));
                                                                                    map.put("addresslng", String.valueOf(lng1));
                                                                                    FirebaseDatabase.getInstance().getReference()
                                                                                            .child("SignedEmployees")
                                                                                            .child(id)
                                                                                            .updateChildren(map);
                                                                                    FirebaseDatabase.getInstance().getReference()
                                                                                            .child("Final Ordered Employees")
                                                                                            .child(id)
                                                                                            .child("distance1")
                                                                                            .setValue(distance_value);
                                                                                    CommonSwitch.switch3.setChecked(false);

                                                                                    FirebaseDatabase.getInstance().getReference()
                                                                                            .child("Final Ordered Employees")
                                                                                            .child(id)
                                                                                            .child("time")
                                                                                            .setValue(time_text1);
                                                                                    new CountDownTimer(1000, 1000) {
                                                                                        @Override
                                                                                        public void onTick(long millisUntilFinished) {

                                                                                        }

                                                                                        @Override
                                                                                        public void onFinish() {
                                                                                            progressDialog.dismiss();
                                                                                            geoFireaddress.setLocation(id, new GeoLocation(lat1, lng1));
                                                                                            dialog.dismiss();
                                                                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                                                @Override
                                                                                                public void run() {
                                                                                                    Toast.makeText(Map.this, "Done !", Toast.LENGTH_SHORT).show();

                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }.start();


                                                                                }
                                                                            })
                                                                    ;
                                                                }
                                                            }.start();
                                                            time_txt = "";
                                                            parsedDistance = "";

                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFinish() {

                                                }
                                            }.start();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Map.this, "Failed ! ,Sorry Quota Completed", Toast.LENGTH_LONG).show();
                                        }
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
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

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
}
    /*private void buildLocationCallback() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location1 : locationResult.getLocations()) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("latitude", String.valueOf(location1.getLatitude()));
                    editor.putString("longitude", String.valueOf(location1.getLongitude()));
                    editor.apply();
                    location = location1;
                }


                displaylocation();
            }
        };
    }

    private void buildLocationreq() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void displaylocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location1) {
                location = location1;

                if (location != null) {

                    geoFire.setLocation(getIntent().getStringExtra("name").toString(), new GeoLocation(location.getLatitude(), location.getLongitude())
                            , new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if (current != null)
                                        current.remove();

                           *//* Uri navigationIntentUri = Uri.parse("google.navigation:q=" + location.getLatitude() + " , " + location.getLongitude() + "&mode=d");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
*//*
                                    EmployeeInfo employeeInfo = new EmployeeInfo(getIntent().getStringExtra("name").toString(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), UUID.randomUUID().toString());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Employees Info")
                                            .child(getIntent().getStringExtra("name").toString())
                                            .setValue(employeeInfo);

                                    current = mMap.addMarker(new MarkerOptions().
                                            position(new LatLng(location.getLatitude(), location.getLongitude()))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("You")
                                    );

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));


                                }
                            }
                    );

                }
            }
        });
    }



*/
/*  @Override
protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }*/
 /* if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        buildLocationreq();
        buildLocationCallback();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
*/


 /*  HandlerThread handlerThread = new HandlerThread("URLConnection");
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Map.this, "started", Toast.LENGTH_SHORT).show();
                StringBuilder stringBuilder = new StringBuilder();
                Double dist = 0.0;
                try {

                    String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + latLng1.latitude + "," + latLng1.longitude + "&destination=" + lat1 + "," + lng1 + "&mode=driving&sensor=false";

                    HttpPost httppost = new HttpPost(url);

                    HttpClient client = new HttpClient();
                    HttpResponse response;
                    stringBuilder = new StringBuilder();


                    response = client.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    InputStream stream = entity.getContent();
                    int b;
                    while ((b = stream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject = new JSONObject(stringBuilder.toString());

                    JSONArray array = jsonObject.getJSONArray("routes");

                    JSONObject routes = array.getJSONObject(0);

                    JSONArray legs = routes.getJSONArray("legs");

                    JSONObject steps = legs.getJSONObject(0);

                    JSONObject distance = steps.getJSONObject("distance");

                    Log.i("Distance", distance.toString());
                    dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]", ""));
                    final Double finalDist = dist;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Map.this, String.valueOf(finalDist), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });*/
 /* switch2 = (Switch) findViewById(R.id.switch2);

        switch2.setChecked(false);
        FirebaseDatabase.getInstance().getReference()
                .child("foregroundserivces")
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
                    //   startService(new Intent(Map.this, MyService.class));
                    FirebaseDatabase.getInstance().getReference()
                            .child("foregroundserivces")
                            .child(id)
                            .child("want").setValue("yes");
                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("foregroundserivces")
                            .child(id)
                            .child("want").setValue("no");

                }
            }
        });
*/