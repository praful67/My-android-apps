package com.dev2.tracker.TrackYourFriends;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.dev2.tracker.TrackYourFriends.Welcome.REQUEST_CHECK_SETTINGS;

public class Findfriends extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker current;
    String currentid;
    LatLng currentlatlng;
    private GoogleApiClient googleApiClient;
    PlaceAutocompleteFragment place_location;
    String Placelocation;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationManager lm;
    String check = "ok";
    Marker f, f1, f2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findfriends);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        final InterstitialAd interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial));
        interstitialAd.loadAd(adRequest);
        interstitialAd.show();

        interstitialAd.setAdListener(new AdListener()

        {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                interstitialAd.show();
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        currentid = pref.getString("id", "");
        lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        FirebaseDatabase.getInstance().getReference()
                .child("Signedusers")
                .child(currentid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (current != null)
                                    current.remove();

                                currentlatlng = new LatLng(Double.parseDouble(String.valueOf(map.get("lat"))), Double.parseDouble(String.valueOf(map.get("lng"))));
                                if (check.equals("ok")) {
                                    current = mMap.addMarker(new MarkerOptions()
                                            .position(currentlatlng)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                            .title("You")
                                    );
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatlng, 15.0f));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        place_location = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_location);

        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (current != null)
                    current.remove();


                // Placelocation = place.getAddress().toString();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

            }

            @Override
            public void onError(Status status) {

            }
        });
        final Button findF = (Button) findViewById(R.id.findF);
        final EditText idF = (EditText) findViewById(R.id.idF);
        findF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(idF.getText().toString()))
                    addmarker(idF.getText().toString());
                else {
                    Toast.makeText(Findfriends.this, "Please fill ID", Toast.LENGTH_SHORT).show();
                }

            }
        });
        FloatingActionButton clear = (FloatingActionButton) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                FirebaseDatabase.getInstance().getReference()
                        .child("Signedusers")
                        .child(currentid)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                    if (map != null) {
                                        if (current != null)
                                            current.remove();

                                        currentlatlng = new LatLng(Double.parseDouble(String.valueOf(map.get("lat"))), Double.parseDouble(String.valueOf(map.get("lng"))));

                                        if (check.equals("ok")) {
                                            current = mMap.addMarker(new MarkerOptions()
                                                    .position(currentlatlng)
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                                    .title("You")
                                            );
                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatlng, 15.0f));

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        FloatingActionButton addf = (FloatingActionButton) findViewById(R.id.add);
        addf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogadd();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showDialogadd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.adddialog, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText idf1 = (EditText) view.findViewById(R.id.idF1);
        final EditText idf2 = (EditText) view.findViewById(R.id.idF2);
        Button button = (Button) view.findViewById(R.id.findF);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait..");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(idf1.getText().toString())) {
                    final String s = idf1.getText().toString();
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference()
                            .child("Signedusers")
                            .child(s)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                        if (map != null) {

                                            if (f1 != null)
                                                f1.remove();

                                            LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(map.get("lat"))), Double.parseDouble(String.valueOf(map.get("lng"))));
                                            f1 = mMap.addMarker(new MarkerOptions()
                                                    .position(latLng)
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                                    .title(String.valueOf(map.get("username")) + " | Tap to see profile")
                                                    .snippet(s)
                                            );
                                            check = "notok";
                                            f1.showInfoWindow();
                                            progressDialog.dismiss();
                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Findfriends.this, "invalid ID", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Findfriends.this, "invalid ID", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });


                }
                if (!TextUtils.isEmpty(idf2.getText().toString())) {
                    final String s = idf2.getText().toString();
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference()
                            .child("Signedusers")
                            .child(s)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                        if (map != null) {

                                            if (f2 != null)
                                                f2.remove();

                                            LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(map.get("lat"))), Double.parseDouble(String.valueOf(map.get("lng"))));
                                            f2 = mMap.addMarker(new MarkerOptions()
                                                    .position(latLng)
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                                    .title(String.valueOf(map.get("username")) + " | Tap to see profile")
                                                    .snippet(s)
                                            );
                                            f2.showInfoWindow();
                                            check = "notok";
                                            progressDialog.dismiss();

                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Findfriends.this, "invalid ID", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Findfriends.this, "invalid ID", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });

                } else if (TextUtils.isEmpty(idf2.getText().toString()) && TextUtils.isEmpty(idf1.getText().toString())) {
                    Toast.makeText(Findfriends.this, "Please type a ID", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(idf2.getText().toString()) || !TextUtils.isEmpty(idf1.getText().toString())) {
                    Toast.makeText(Findfriends.this, "Please close this dialog and try to zoom in-out to see the results", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void addmarker(final String s) {
        if (!TextUtils.isEmpty(s)) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();

            FirebaseDatabase.getInstance().getReference()
                    .child("Signedusers")
                    .child(s)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                if (map != null) {

                                    if (f != null)
                                        f.remove();

                                    LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(map.get("lat"))), Double.parseDouble(String.valueOf(map.get("lng"))));
                                    f = mMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                            .title(String.valueOf(map.get("username")) + " | Tap to see profile")
                                            .snippet(s)
                                    );
                                    check = "notok";
                                    f.showInfoWindow();
                                    progressDialog.dismiss();
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Findfriends.this, "invalid ID", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Findfriends.this, "invalid ID", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 350, 0, 0);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    if (!(Findfriends.this.isFinishing())) {
                        if (googleApiClient == null) {

                            googleApiClient = new GoogleApiClient.Builder(Findfriends.this)
                                    .addApi(LocationServices.API).build();
                            googleApiClient.connect();
                            LocationRequest locationRequest = LocationRequest.create();
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            locationRequest.setInterval(30 * 1000);
                            locationRequest.setFastestInterval(5 * 1000);
                            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                            builder.setAlwaysShow(true);
                            builder.setNeedBle(true);


                            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                                @Override
                                public void onResult(LocationSettingsResult result) {
                                    final com.google.android.gms.common.api.Status status = result.getStatus();
                                    final LocationSettingsStates state = result.getLocationSettingsStates();
                                    switch (status.getStatusCode()) {
                                        case LocationSettingsStatusCodes.SUCCESS:
                                            break;
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            try {
                                                status.startResolutionForResult(Findfriends.this, REQUEST_CHECK_SETTINGS);
                                            } catch (IntentSender.SendIntentException e) {
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                            break;
                                    }
                                }
                            });
                        }
                    }
                } else {
                    if (currentlatlng != null) {
                        if (current != null)
                            current.remove();

                        check = "ok";

                        current = mMap.addMarker(new MarkerOptions().position(currentlatlng)
                                .title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        );

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatlng, 15.0f));
                        return true;
                    } else
                        return false;

                }
                return true;
            }
        });
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);


        LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getSnippet() != null) {
                    if (!marker.getTitle().equals("You")) {
                        Intent intent = new Intent(Findfriends.this, FriendProfile.class);
                        intent.putExtra("userid", marker.getSnippet());
                        startActivity(intent);
                    }
                }
            }
        });

    }
}
