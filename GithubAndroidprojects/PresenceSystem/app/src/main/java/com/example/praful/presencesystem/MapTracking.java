package com.example.praful.presencesystem;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MapTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String name;
    String lat, lng;
    LatLng userlocation;
    GeoFire geoFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().getStringExtra("name") != null) {

            name = getIntent().getStringExtra("name");
        }
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference()
                .child("Geofire"));

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        FirebaseDatabase.getInstance().getReference()
                .child("locations").child(name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mMap != null)
                            mMap.clear();


                        Tracking tracking = dataSnapshot.getValue(Tracking.class);

                        if (tracking != null) {
                            lat = tracking.getLat();
                            lng = tracking.getLng();

                           /* Uri navigationIntentUri = Uri.parse("google.navigation:q=" + Float.parseFloat(lat) + " , " + Float.parseFloat(lng) + "&mode=d");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                           */
                            userlocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                            Location user = new Location("");
                            user.setLongitude(userlocation.longitude);
                            user.setLatitude(userlocation.latitude);

                            mMap.addMarker(new MarkerOptions().position(userlocation)
                                    .title(name)
                                    .snippet("Im here")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 15.0f));

                        }
                        LatLng current = new LatLng(Double.parseDouble(pref.getString("lat", "")), Double.parseDouble(pref.getString("lng", "")));
                        mMap.addMarker(new MarkerOptions()
                                .position(current)
                                .icon(BitmapDescriptorFactory.defaultMarker())
                                .title("Me"));


                        geoFire.setLocation(name.toString(), new GeoLocation(userlocation.latitude, userlocation.longitude),
                                new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {
                                        Toast.makeText(MapTracking.this, "Updated to Firebase", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(32.8917771, 74.8967406);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        //0.5f = 0.5km = 500 m
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 0.5f);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Toast.makeText(MapTracking.this, key.toString() + "Entered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onKeyExited(String key) {
                Toast.makeText(MapTracking.this, key.toString() + "Exited", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Toast.makeText(MapTracking.this, key.toString() + "Moved", Toast.LENGTH_SHORT).show();

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
