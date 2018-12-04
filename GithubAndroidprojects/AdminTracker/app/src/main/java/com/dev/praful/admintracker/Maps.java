package com.dev.praful.admintracker;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.Toolbar;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.asksira.loopingviewpager.LoopingViewPager;
import com.firebase.geofire.GeoFire;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rd.PageIndicatorView;
import com.rengwuxian.materialedittext.MaterialEditText;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import retrofit2.Call;
import retrofit2.Callback;

import static android.view.View.GONE;

public class Maps extends AppCompatActivity implements DuoMenuView.OnMenuClickListener, OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    BottomSheetBehavior bottomSheetBehavior;
    String time_txt;
    String parsedDistance;
    String response;
    static final int My_PERMISSION = 7271;
    LocationRequest locationRequest;
    List<Slideitem> slideitems = new ArrayList<>();
    Location location;
    double distance = 10000;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    Marker current;
    int rank = 1;
    Driverdetails driverdetails;
    GeoFire geoFire;
    String name;
    IGoogleAPI Services;
    String end_address;
    String distance_textE, time_textE;
    String end_addressD;
    String distance_textD, time_textD;

    private DuoMenuAdapter mMenuAdapter;
    DuoDrawerLayout mDuoDrawerLayout;
    DuoMenuView mDuoMenuView;
    Toolbar mToolbar;
    ArrayList<String> mTitles = new ArrayList<>();

    String address1;
    Double lat2, lng2;
    java.util.List<Address> addresses = null;
    java.util.List<Address> addresses1 = null;
    PlaceAutocompleteFragment place_location;
    String Placelocation;
    double lat1, lng1;
    Geocoder geocoder, geocoder1;
    String address;
    String id1;
    Switch switch2;
    LoopingViewPager viewPager;
    LoopInfiniteAdapter adapter;
    PageIndicatorView indicatorView;

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    protected void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }

    private ArrayList<Integer> createDummyItems() {
        ArrayList<Integer> items = new ArrayList<>();
        items.add(0, 1);
        items.add(1, 2);
        items.add(2, 3);
        return items;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //  geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("locations of Dummy Employees Info"));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       */
        viewPager = findViewById(R.id.viewpager);
        indicatorView = findViewById(R.id.indicator);

        adapter = new LoopInfiniteAdapter(this, createDummyItems(), true);
        viewPager.setAdapter(adapter);

        //Custom bind indicator
        indicatorView.setCount(viewPager.getIndicatorCount());
        viewPager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {
                indicatorView.setProgress(selectingPosition, progress);
            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {
                indicatorView.setSelection(newIndicatorPosition);
            }
        });



        mapFragment.getMapAsync(this);
        final ImageView up = (ImageView) findViewById(R.id.up);
        final ImageView down = (ImageView) findViewById(R.id.down);

        View bottom = findViewById(R.id.bottomsheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom);
        up.setVisibility(GONE);
        down.setVisibility(View.VISIBLE);
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
        final FloatingActionButton currentlocation = (FloatingActionButton) findViewById(R.id.currentlocation1);
        final FloatingActionButton refresh = (FloatingActionButton) findViewById(R.id.refresh);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        // Toast.makeText(Maps.this, "collapsed", Toast.LENGTH_SHORT).show();
                        down.setVisibility(GONE);
                        up.setVisibility(View.VISIBLE);
                        hideandreveal(refresh);
                        hideandreveal(currentlocation);
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
        getWindow().getAttributes().windowAnimations = R.style.Dialogslide1;
        Slideitem slideitem = new Slideitem(R.drawable.car1, "CAB ROSTING");
        Slideitem slideitem1 = new Slideitem(R.drawable.time, "TIME OFF");
        Slideitem slideitem2 = new Slideitem(R.drawable.comments, "COMMENTS");
        slideitems.add(slideitem);
        slideitems.add(slideitem1);
        slideitems.add(slideitem2);

        // HorizontalInfiniteCycleViewPager pager = (HorizontalInfiniteCycleViewPager) findViewById(R.id.cycle);
        Slideadapter slideadapter = new Slideadapter(slideitems, getBaseContext());
        // pager.setAdapter(slideadapter);
        Services = CommonSwitch.getGoogleService();
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


     */
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maps.this, Maps.class);
                startActivity(intent);
                finish();
            }
        });
        mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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

      /*  View navigationHeaderView = navigationView.getHeaderView(0);
        final TextView numberofdrivers = (TextView) navigationHeaderView.findViewById(R.id.noofdrivers);
        final TextView numberofemployees = (TextView) navigationHeaderView.findViewById(R.id.noofemployees);
*/
        final TextView numberofdrivers = (TextView) mDuoMenuView.getHeaderView().findViewById(R.id.noofdrivers);
        final TextView numberofemployees = (TextView) mDuoMenuView.getHeaderView().findViewById(R.id.noofemployees);

        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            String number = String.valueOf(dataSnapshot.getChildrenCount());
                            numberofemployees.setText("Number of Employees - " + number);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("SignedDrivers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            String number = String.valueOf(dataSnapshot.getChildrenCount());
                            numberofdrivers.setText("Number of Drivers - " + number);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        switch2 = (Switch) findViewById(R.id.switch2);
        FirebaseDatabase.getInstance().getReference()
                .child("Adminforegroundserivces").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {

                                if (String.valueOf(map.get("want")).equals("yes")) {
                                    switch2.setChecked(true);
                                } else {
                                    switch2.setChecked(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch2.isChecked()) {
                    startService(new Intent(Maps.this, MyService.class));
                    FirebaseDatabase.getInstance().getReference()
                            .child("Adminforegroundserivces")
                            .child("want").setValue("yes");
                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Adminforegroundserivces")
                            .child("want").setValue("no");

                }
            }
        });


        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);

                current = mMap.addMarker(new MarkerOptions().
                        position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Main Office")
                );

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            }
        });
        place_location = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_location);
        place_location.setHint("");

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{

                    Manifest.permission.CALL_PHONE

            }, My_PERMISSION);
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we load all employees and drivers");
        progressDialog.setCancelable(false);
        progressDialog.show();

        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
               /* if (current != null)
                    current.remove();

                current = mMap.addMarker(new MarkerOptions().position(place.getLatLng()
                ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Pick up here"));
                */
                down.setVisibility(GONE);
                up.setVisibility(View.VISIBLE);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                //  Toast.makeText(Map.this, String.format("Address for pick up is %s", Placelocation), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });


        FirebaseDatabase.getInstance().getReference()
                .child("Employees Info")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mMap.clear();
                        showcurrentmarker();
                        setmarkersfordrivers();
                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            final EmployeeInfo EmployeeInfo = dataSnapshot1.getValue(EmployeeInfo.class);
                            final LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(EmployeeInfo.getLat())),
                                    Double.parseDouble(String.valueOf(EmployeeInfo.getLng()))
                            );
                            final LatLng latLng1 = new LatLng(17.434810272796604, 78.38469553738832);


                            FirebaseDatabase.getInstance().getReference()
                                    .child("SignedEmployees")
                                    .child(dataSnapshot1.getKey())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                            if (employeedetails != null) {

                                                mMap.addMarker(new MarkerOptions().position(latLng)
                                                        .title("Employee : " + employeedetails.getUsername())
                                                        .snippet(dataSnapshot1.getKey())
                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

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
                .child("Drivers Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                showcurrentmarker();
                setmarkersforemployess();
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    DriversInfo driversInfo = dataSnapshot1.getValue(DriversInfo.class);
                    final LatLng latLng = new    LatLng(Double.parseDouble(String.valueOf(driversInfo.getLat())),
                            Double.parseDouble(String.valueOf(driversInfo.getLng()))
                    );
                    FirebaseDatabase.getInstance().getReference()
                            .child("SignedDrivers")
                            .child(dataSnapshot1.getKey())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                    if (driverdetails != null) {

                                        mMap.addMarker(new MarkerOptions().position(latLng)
                                                .title("Driver : " + driverdetails.getUsername().toString())
                                                .icon(bitmapDescriptorFromVector(getBaseContext(), R.drawable.car1))

                                        );

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

        LinearLayout sos = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.sos);
        LinearLayout read = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.read);
        LinearLayout employees = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.employeeslist);
        LinearLayout driverlist = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.driverslist);
        LinearLayout create = (LinearLayout) mDuoMenuView.getHeaderView().findViewById(R.id.create);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        showsosdialog();

                    }
                }.start();

            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        shownotidialog();

                    }
                }.start();

            }
        });
        employees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                final Intent intent = new Intent(Maps.this, Employeeslist.class);
                editor.putString("anim", "yes");
                editor.apply();
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        startActivity(intent);

                    }
                }.start();


            }
        });
        driverlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                editor.putString("anim", "yes");
                editor.apply();
                final Intent intent1 = new Intent(Maps.this, Driverslist.class);
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        startActivity(intent1);

                    }
                }.start();

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDuoDrawerLayout.closeDrawer();
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        showadddialog();
                    }
                }.start();

            }
        });
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!Maps.this.isFinishing())
                    progressDialog.dismiss();
            }
        }.start();
        new CountDownTimer(3300, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                up.setVisibility(View.VISIBLE);
                down.setVisibility(GONE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }.start();

    }

    private void hideandreveal(final View V) {
        int cx = V.getWidth() / 2;
        int cy = V.getHeight() / 2;
        float initialradius = (float) Math.hypot(cx, cy);
        Animator animator = ViewAnimationUtils.createCircularReveal(V, cx, cy, initialradius, 0);
        animator.addListener(new AnimatorListenerAdapter() {
                                 @Override
                                 public void onAnimationEnd(Animator animation) {
                                     super.onAnimationEnd(animation);
                                     V.setVisibility(View.INVISIBLE);
                                     reveal(V);
                                 }
                             }
        );
        animator.start();
    }

    private void reveal(View V) {

        int cx = V.getWidth() / 2;
        int cy = V.getHeight() / 2;
        float finalradius = (float) Math.hypot(cx, cy);
        Animator animator = ViewAnimationUtils.createCircularReveal(V, cx, cy, 0, finalradius);
        V.setVisibility(View.VISIBLE);
        animator.start();
    }

    private void hide(final View V) {

        int cx = V.getWidth() / 2;
        int cy = V.getHeight() / 2;
        float initialradius = (float) Math.hypot(cx, cy);
        Animator animator = ViewAnimationUtils.createCircularReveal(V, cx, cy, initialradius, 0);
        animator.addListener(new AnimatorListenerAdapter() {
                                 @Override
                                 public void onAnimationEnd(Animator animation) {
                                     super.onAnimationEnd(animation);
                                     V.setVisibility(View.INVISIBLE);
                                 }
                             }
        );
        animator.start();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {

       /* mMenuAdapter.setViewSelected(position, true);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        mDuoDrawerLayout.closeDrawer();

        // Navigate to the right fragment
        switch (position) {
            case 0:
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        showsosdialog();

                    }
                }.start();

                break;
            case 1:
                Intent intent = new Intent(Maps.this, Employeeslist.class);
                editor.putString("anim", "yes");
                editor.apply();
                startActivity(intent);
                break;
            case 2:
                editor.putString("anim", "yes");
                editor.apply();
                Intent intent1 = new Intent(Maps.this, Driverslist.class);
                startActivity(intent1);
                break;
            case 3:
                editor.putString("anim", "yes");
                editor.apply();
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        showadddialog();
                    }
                }.start();
                break;
            case 4:
                new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        shownotidialog();

                    }
                }.start();
                break;
        }*/


    }

    private void showadddialog() {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.createdialog, null);
        builder1.setView(view);
        final AlertDialog alertDialog = builder1.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;

        alertDialog.show();
        Button createdemployees = (Button) view.findViewById(R.id.createdemployees);
        Button createdDrivers = (Button) view.findViewById(R.id.createddriver);
        createdemployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maps.this, Addemployee.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        createdDrivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maps.this, CreatedDrivers.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
    }

    private void getDT(final double lat1, final double lng1, final double lat2, final double lng2) {


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String requestUrl = null;
                try {
            /* requestUrl = "https://maps.googleapis.com/maps/api/directions/json?"
                    +"mode=driving&"
                    +"transit_routing_preference=less_driving&"
                    +"origin="+Location+"&"+"destination+"+Destination+"&"
                    + "key="+getResources().getString(R.string.google_direction_api); */
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
                                distance_textE = distance.getString("text");

                                Double distance_value = Double.parseDouble(distance_textE.replaceAll("[^0-9\\\\.]+", ""));

                                JSONObject time = legsobject.getJSONObject("duration");
                                time_textE = time.getString("text");
                                Integer time_value = Integer.parseInt(time_textE.replaceAll("\\D+", ""));
                                String start_address = legsobject.getString("start_address");
                                end_address = legsobject.getString("end_address");

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

                                //GET DISTANCE
                                JSONObject distance = legsobject.getJSONObject("distance");
                                distance_textD = distance.getString("text");

                                Double distance_value = Double.parseDouble(distance_textE.replaceAll("[^0-9\\\\.]+", ""));

                                JSONObject time = legsobject.getJSONObject("duration");
                                time_textD = time.getString("text");
                                Integer time_value = Integer.parseInt(time_textE.replaceAll("\\D+", ""));
                                String start_address = legsobject.getString("start_address");
                                end_addressD = legsobject.getString("end_address");

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

   /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    private void shownotidialog() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Maps.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.shownoti, null);
        TextView notificationmessage = (TextView) view1.findViewById(R.id.notificationmessage);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications , even when the app is working in background or it is killed . Like if Employee has changed his/her profile , you will be notified ." +
                    "\nAnd for those whose Android version is lesser to 8, no need to switch ON.<font color=\"#42f471\"><bold>"
                    + "Yours is more or equal to 8 "
                    + "</bold></font>" + ", So please switch it ON.";


            notificationmessage.setText(Html.fromHtml(text));

        } else {
            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications , even when the app is working in background or it is killed .  Like if Employee has changed his/her profile , you will be notified .   " +
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

    private void showsosdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.sos, null);
        final MaterialEditText number = (MaterialEditText) view.findViewById(R.id.number);
        final MaterialEditText sms = (MaterialEditText) view.findViewById(R.id.smsnumber);
        Button submit = (Button) view.findViewById(R.id.submit);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;

        alertDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child("SOS")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                number.setHint(String.valueOf(map.get("Number")));
                                sms.setHint(String.valueOf(map.get("SmsNumber")));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(number.getText().toString())) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("SOS")
                            .child("Number")
                            .setValue(number.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Maps.this, "Done !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (!TextUtils.isEmpty(sms.getText().toString())) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("SOS")
                            .child("SmsNumber")
                            .setValue(sms.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Maps.this, "Done !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                alertDialog.dismiss();

            }
        });
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }


    private void setmarkersforemployess() {
        FirebaseDatabase.getInstance().getReference()
                .child("Employees Info")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            final EmployeeInfo EmployeeInfo = dataSnapshot1.getValue(EmployeeInfo.class);
                            final LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(EmployeeInfo.getLat())),
                                    Double.parseDouble(String.valueOf(EmployeeInfo.getLng()))
                            );
                            FirebaseDatabase.getInstance().getReference()
                                    .child("SignedEmployees")
                                    .child(dataSnapshot1.getKey())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                            if (employeedetails != null) {
                                                mMap.addMarker(new MarkerOptions().position(latLng)
                                                        .title("Employee : " + employeedetails.getUsername())
                                                        .snippet(dataSnapshot1.getKey())
                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

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

    private void setmarkersfordrivers() {
        FirebaseDatabase.getInstance().getReference()
                .child("Drivers Info")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            DriversInfo driversInfo = dataSnapshot1.getValue(DriversInfo.class);
                            final LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(driversInfo.getLat())),
                                    Double.parseDouble(String.valueOf(driversInfo.getLng()))
                            );
                            final LatLng latLng1 = new LatLng(17.434810272796604, 78.38469553738832);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("SignedDrivers")
                                    .child(dataSnapshot1.getKey())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                            if (driverdetails != null) {

                                                mMap.addMarker(new MarkerOptions().position(latLng)
                                                        .title("Driver : " + driverdetails.getUsername().toString())
                                                        .icon(bitmapDescriptorFromVector(getBaseContext(), R.drawable.car1))

                                                );

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

    private void showcurrentmarker() {
        LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);

        current = mMap.addMarker(new MarkerOptions().
                position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Main Office")
        );

        //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)

        {

            case My_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }

            }
            break;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                showcurrentmarker();
                return true;
            }
        });
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);

        current = mMap.addMarker(new MarkerOptions().
                position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Main Office")
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getSnippet() != null) {
                    String id = marker.getSnippet();
                    Intent intent = new Intent(Maps.this, Employeedetailspage.class);
                    intent.putExtra("employeeid", id);
                    startActivity(intent);
                }
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


    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance = distance.getString("text");
                    JSONObject time = steps.getJSONObject("duration");
                    time_txt = time.getString("text");


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
        return parsedDistance;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
/*

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (id == R.id.nav_employees) {

            Intent intent = new Intent(Maps.this, Employeeslist.class);
            editor.putString("anim", "yes");
            editor.apply();
            startActivity(intent);

        } else if (id == R.id.nav_create) {
            editor.putString("anim", "yes");
            editor.apply();
            new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    showadddialog();
                }
            }.start();


        } else if (id == R.id.nav_drivers) {
            editor.putString("anim", "yes");
            editor.apply();
            Intent intent = new Intent(Maps.this, Driverslist.class);
            startActivity(intent);

        } else if (id == R.id.nav_sos) {
            new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    showsosdialog();

                }
            }.start();

        } else if (id == R.id.nav_read) {
            new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    shownotidialog();

                }
            }.start();
        }
*/

        return false;
    }

    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

}
  /*  FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            final Employeedetails employeedetails = dataSnapshot1.getValue(Employeedetails.class);
                            if (employeedetails.getAddresslat() != null && employeedetails.getAddresslng() != null && employeedetails.getAddresslng().length() != 0
                                    && employeedetails.getAddresslat().length() != 0) {
                                lat2 = Double.parseDouble(employeedetails.getAddresslat());

                                lng2 = Double.parseDouble(employeedetails.getAddresslng());
                                final LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);
                              //  String DT =  getDistance(lat2, lng2, latLng.latitude, latLng.longitude);

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latLng.latitude + "," + latLng.longitude + "&destination=" + Double.parseDouble(employeedetails.getAddresslat()) + "," + Double.parseDouble(employeedetails.getAddresslng()) + "&sensor=false&units=metric&mode=driving");
                                            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                            conn.setRequestMethod("POST");
                                            InputStream in = new BufferedInputStream(conn.getInputStream());
                                            response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray array = jsonObject.getJSONArray("routes");
                                            JSONObject routes = array.getJSONObject(0);
                                            JSONArray legs = routes.getJSONArray("legs");
                                            JSONObject steps = legs.getJSONObject(0);
                                            JSONObject distance = steps.getJSONObject("distance");
                                            parsedDistance = distance.getString("text");
                                            JSONObject time = steps.getJSONObject("duration");
                                            time_txt = time.getString("text");


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
                                final Orderedemployeedetails orderedemployeedetails = new
                                        Orderedemployeedetails(employeedetails.getUsername(),
                                        parsedDistance
                                        , dataSnapshot1.getKey());
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Final Ordered Employees")
                                        .child(dataSnapshot1.getKey())
                                        .setValue(orderedemployeedetails);
                                Map<String, Object> map = new HashMap<>();
                                map.put("time", time_txt);
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Final Ordered Employees")
                                        .child(dataSnapshot1.getKey())
                                        .updateChildren(map);


                                Toast.makeText(Maps.this, lat2 + " " + lng2, Toast.LENGTH_SHORT).show();


                            } else {
                                Orderedemployeedetails orderedemployeedetails = new
                                        Orderedemployeedetails("",
                                        String.format("Address is Not Updated")
                                        , dataSnapshot1.getKey());
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Final Ordered Employees")
                                        .child(dataSnapshot1.getKey())
                                        .setValue(orderedemployeedetails);

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
*/
//    getDTD(latLng1.latitude, latLng1.longitude, latLng.latitude, latLng.longitude);
                   /* new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            String requestUrl = null;
                            try {
                                requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                                        "transit_routing_preference=less_driving&" + "origin=" + latLng1.latitude + "," + latLng1.longitude + "&" +
                                        "destination=" + latLng.latitude + "," + latLng.longitude + "&" +
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
                                            final JSONObject distance = legsobject.getJSONObject("distance");
                                            distance_textD = distance.getString("text");

                                            Double distance_value = Double.parseDouble(distance_textE.replaceAll("[^0-9\\\\.]+", ""));

                                            final JSONObject time = legsobject.getJSONObject("duration");
                                            time_textD = time.getString("text");
                                            Integer time_value = Integer.parseInt(time_textE.replaceAll("\\D+", ""));
                                            String start_address = legsobject.getString("start_address");
                                            end_addressD = legsobject.getString("end_address");

                                            // Toast.makeText(Map.this, distance_textD + " " + time_textD, Toast.LENGTH_SHORT).show();
                                            new CountDownTimer(1000, 1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {

                                                }

                                                @Override
                                                public void onFinish() {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("SignedDrivers")
                                                            .child(dataSnapshot1.getKey())
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                                    if (driverdetails != null) {
                                                                        try {
                                                                            mMap.addMarker(new MarkerOptions().position(latLng)
                                                                                    .title("Driver :" + driverdetails.getUsername().toString())
                                                                                    .snippet(distance.getString("text") + " " + time.getString("text") + " far from Office")
                                                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

                                                                            );
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

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


*/
//    getDT(latLng1.latitude, latLng1.longitude, latLng.latitude, latLng.longitude);
                      /*      new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    String requestUrl = null;
                                    try {
                                        requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                                                "transit_routing_preference=less_driving&" + "origin=" + latLng1.latitude + "," + latLng1.longitude + "&" +
                                                "destination=" + latLng.latitude + "," + latLng.longitude + "&" +
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
                                                    final JSONObject distance = legsobject.getJSONObject("distance");
                                                    distance_textE = distance.getString("text");

                                                    Double distance_value = Double.parseDouble(distance_textE.replaceAll("[^0-9\\\\.]+", ""));

                                                    final JSONObject time = legsobject.getJSONObject("duration");
                                                    time_textE = time.getString("text");
                                                    Integer time_value = Integer.parseInt(time_textE.replaceAll("\\D+", ""));
                                                    String start_address = legsobject.getString("start_address");
                                                    end_address = legsobject.getString("end_address");

                                                    // Toast.makeText(Map.this, distance_textD + " " + time_textD, Toast.LENGTH_SHORT).show();

                                                    new CountDownTimer(1000, 1000) {
                                                        @Override
                                                        public void onTick(long millisUntilFinished) {

                                                        }

                                                        @Override
                                                        public void onFinish() {
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("SignedEmployees")
                                                                    .child(dataSnapshot1.getKey())
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                                                            if (employeedetails != null) {

                                                                                try {
                                                                                    mMap.addMarker(new MarkerOptions().position(latLng)
                                                                                            .title("Employee : " + employeedetails.getUsername())
                                                                                            .snippet(distance.getString("text") + " " + time.getString("text") + " far from Office")
                                                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

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
*/
//   getDT(latLng1.latitude, latLng1.longitude, latLng.latitude, latLng.longitude);
                          /*  new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    String requestUrl = null;
                                    try {
                                        requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                                                "transit_routing_preference=less_driving&" + "origin=" + latLng1.latitude + "," + latLng1.longitude + "&" +
                                                "destination=" + latLng.latitude + "," + latLng.longitude + "&" +
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
                                                    final JSONObject distance = legsobject.getJSONObject("distance");
                                                    distance_textE = distance.getString("text");

                                                    Double distance_value = Double.parseDouble(distance_textE.replaceAll("[^0-9\\\\.]+", ""));

                                                    final JSONObject time = legsobject.getJSONObject("duration");
                                                    time_textE = time.getString("text");
                                                    Integer time_value = Integer.parseInt(time_textE.replaceAll("\\D+", ""));
                                                    String start_address = legsobject.getString("start_address");
                                                    end_address = legsobject.getString("end_address");

                                                    // Toast.makeText(Map.this, distance_textD + " " + time_textD, Toast.LENGTH_SHORT).show();

                                                    new CountDownTimer(1000, 1000) {
                                                        @Override
                                                        public void onTick(long millisUntilFinished) {

                                                        }

                                                        @Override
                                                        public void onFinish() {
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("SignedEmployees")
                                                                    .child(dataSnapshot1.getKey())
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                                                            if (employeedetails != null) {
                                                                                try {
                                                                                    mMap.addMarker(new MarkerOptions().position(latLng)
                                                                                            .title("Employee : " + employeedetails.getUsername())
                                                                                            .snippet(distance.getString("text") + " " + time.getString("text") + " far from Office")
                                                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

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
                            });*/

//    getDTD(latLng1.latitude, latLng1.longitude, latLng.latitude, latLng.longitude);

                         /*   new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    String requestUrl = null;
                                    try {
                                        requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                                                "transit_routing_preference=less_driving&" + "origin=" + latLng1.latitude + "," + latLng1.longitude + "&" +
                                                "destination=" + latLng.latitude + "," + latLng.longitude + "&" +
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
                                                    final JSONObject distance = legsobject.getJSONObject("distance");
                                                    distance_textD = distance.getString("text");

                                                    Double distance_value = Double.parseDouble(distance_textE.replaceAll("[^0-9\\\\.]+", ""));

                                                    final JSONObject time = legsobject.getJSONObject("duration");
                                                    time_textD = time.getString("text");
                                                    Integer time_value = Integer.parseInt(time_textE.replaceAll("\\D+", ""));
                                                    String start_address = legsobject.getString("start_address");
                                                    end_addressD = legsobject.getString("end_address");

                                                    // Toast.makeText(Map.this, distance_textD + " " + time_textD, Toast.LENGTH_SHORT).show();
                                                    new CountDownTimer(1000, 1000) {
                                                        @Override
                                                        public void onTick(long millisUntilFinished) {

                                                        }

                                                        @Override
                                                        public void onFinish() {
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child(dataSnapshot1.getKey())
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                                            if (driverdetails != null) {
                                                                                try {
                                                                                    mMap.addMarker(new MarkerOptions().position(latLng)
                                                                                            .title("Driver : " + driverdetails.getUsername().toString())
                                                                                            .snippet(distance.getString("text") + " " + time.getString("text") + " far from Office")
                                                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

                                                                                    );
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }

                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

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
                            });*/
                         /*   Button ridedetails = (Button) findViewById(R.id.ridedetails);
        ridedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maps.this, Carslist.class);
                startActivity(intent);
            }
        });
        Button timeoffs = (Button) findViewById(R.id.timeoffs);
        timeoffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maps.this, EmployeesTimeoff.class);
                startActivity(intent);
            }
        });
*/
