package com.dev.praful.trackyouremployee;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Mapfornearestone extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final int My_PERMISSION = 7271;
    double distance = .001;
    final double limit = 1000;
    String id;
    GeoFire geoFire;
    Marker current;
    String lat, lng;
    Switch switch1;
    boolean gotnearestone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapfornearestone);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Drivers"));
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        switch1 = (Switch) findViewById(R.id.switch1);
        Button getdirection = (Button) findViewById(R.id.getdirection);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,

            }, My_PERMISSION);
        }

        FirebaseDatabase.getInstance().getReference()
                .child("Drivers Info")
                .child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DriversInfo driversInfo = dataSnapshot.getValue(DriversInfo.class);
                lat = driversInfo.getLat();
                lng = driversInfo.getLng();
                final LatLng location  = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                geoFire.setLocation(id, new GeoLocation(Double.parseDouble(lat), Double.parseDouble(lng))
                        , new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (current != null)
                                    current.remove();

                                FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("Employess")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                gotnearestone = false;
                                                switch1.setChecked(false);
                                                loadnearestemployess(location);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                gotnearestone = false;
                                switch1.setChecked(false);
                                loadnearestemployess(location);


                                current = mMap.addMarker(new MarkerOptions().
                                        position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("You")
                                );

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));


                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Mapfornearestone.this.isFinishing()) {
                    Uri navigationIntentUri = Uri.parse("google.navigation:q=" + Double.parseDouble(lat) + " , " + Double.parseDouble(lng) + "&mode=d");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }


            }
        });

    }



    private void loadnearestemployess(final LatLng location) {
        mMap.clear();
        current = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).
                position(location).title("You")
        );


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));


        GeoFire gf = new GeoFire(FirebaseDatabase.getInstance().getReference("Employess"));
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(location.latitude,location.longitude), distance);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {
                if (!gotnearestone) {
                    gotnearestone = true;
                    switch1.setChecked(true);
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
                                        java.util.Map<String, String> currentemployee = new HashMap<>();
                                        currentemployee.put("name", key.toString());
                                        currentemployee.put("latitude", String.valueOf(location.latitude));
                                        currentemployee.put("longitude", String.valueOf(location.longitude));
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Driver's current Employee")
                                                .child(pref.getString("name", ""))
                                                .setValue(currentemployee);
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
            }

            @Override
            public void onKeyExited(String key) {


            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!gotnearestone && distance < limit) {
                    distance = distance + .1;
                    loadnearestemployess(location);
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

}