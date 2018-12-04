package com.dev2.tracker.TrackYourFriends;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dev2.tracker.TrackYourFriends.Welcome.REQUEST_CHECK_SETTINGS;

public class LiveTrack extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String userid, id;
    Marker current;
    Marker friend;
    private Polyline directions;
    LatLng currentlatlng;
    private GoogleApiClient googleApiClient;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationManager lm;
    IGoogleAPI Services;
    LatLng friendlatlng;
    String check = "notok";
    String end_address;
    TextView text;
    int x = 0;
    String distance_textE, time_textE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_track);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        mapFragment.getMapAsync(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Services = Common.getGoogleService();
        text = (TextView) findViewById(R.id.text);
        id = preferences.getString("id", "");
        FirebaseDatabase.getInstance().getReference()
                .child("Signedusers")
                .child(id)
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
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        final AdRequest adRequest = new AdRequest.Builder().build();
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
        if (getIntent().getStringExtra("userid") != null) {
            userid = getIntent().getStringExtra("userid");
            FirebaseDatabase.getInstance().getReference()
                    .child("Signedusers")
                    .child(userid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {

                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                if (map != null) {

                                    if (friend != null)
                                        friend.remove();

                                    friendlatlng = new LatLng(Double.parseDouble(String.valueOf(map.get("lat"))), Double.parseDouble(String.valueOf(map.get("lng"))));
                                    friend = mMap.addMarker(new MarkerOptions()
                                            .position(friendlatlng)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                            .title(String.valueOf(map.get("username")))
                                            .snippet(userid)
                                    );
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(friendlatlng, 15.0f));


                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("please wait");
            progressDialog.show();
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                    progressDialog.dismiss();
                    if (currentlatlng != null && friendlatlng != null) {
                        getDirection(currentlatlng.latitude, currentlatlng.longitude, friendlatlng.latitude, friendlatlng.longitude);

                    }


                }
            }.start();
        }
        FloatingActionButton refresh = (FloatingActionButton) findViewById(R.id.refresh);
        FloatingActionButton getdirection = (FloatingActionButton) findViewById(R.id.getdirection);
        getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friendlatlng != null) {
                    if (currentlatlng != null) {
                        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + friendlatlng.latitude + " , " + friendlatlng.longitude + "&mode=d");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }
            }
        });
        FloatingActionButton friend = (FloatingActionButton) findViewById(R.id.friend);
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (friendlatlng != null) {
                    check = "notok";
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(friendlatlng, 15.0f));

                }

            }
        });
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentlatlng != null && friendlatlng != null) {
                    x++;
                    getDirection(currentlatlng.latitude, currentlatlng.longitude, friendlatlng.latitude, friendlatlng.longitude);
                    if (x == 3) {
                        x = 0;
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
                    }
                }


            }
        });


    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        ProgressDialog dialog = new ProgressDialog(LiveTrack.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected java.util.List<java.util.List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jObject;
            java.util.List<java.util.List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(strings[0]);

                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(jObject);
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return routes;
        }

        @Override
        protected void onPostExecute(java.util.List<java.util.List<HashMap<String, String>>> lists) {
            dialog.dismiss();
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = lists.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                }
                polylineOptions.addAll(points);
                polylineOptions.width(10);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);
            }
            if (directions != null)
                directions.remove();

            directions = mMap.addPolyline(polylineOptions);
        }
    }

    private void getDirection(double lat1, double lng1, double lat2, double lng2) {
        String requestApi = null;
        try {

            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" + lat1 + "," + lng1 + "&" +
                    "destination=" + lat2 + "," + lng2 + "&" +
                    "key=" + getResources().getString(R.string.google_api_key);
            Log.d("URL", requestApi);
            Services.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray routes = jsonObject.getJSONArray("routes");
                        JSONObject object = routes.getJSONObject(0);

                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject legsobject = legs.getJSONObject(0);

                        JSONObject distance = legsobject.getJSONObject("distance");
                        distance_textE = distance.getString("text");

                        JSONObject time = legsobject.getJSONObject("duration");
                        time_textE = time.getString("text");
                        end_address = legsobject.getString("end_address");
                        text.setText("\nYour friend is " + distance_textE + " " + time_textE + " far from you" + "\n\n" + "Lastly Updated Address of your friend : " + end_address);


                        new ParserTask().execute(response.body().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(LiveTrack.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDT(final double lat1, final double lng1, final double lat2, final double lng2) {


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
                                distance_textE = distance.getString("text");

                                Double distance_value = Double.parseDouble(distance_textE.replaceAll("[^0-9\\\\.]+", ""));

                                JSONObject time = legsobject.getJSONObject("duration");
                                time_textE = time.getString("text");
                                Integer time_value = Integer.parseInt(time_textE.replaceAll("\\D+", ""));
                                String start_address = legsobject.getString("start_address");
                                end_address = legsobject.getString("end_address");
                                text.setText("\n" + distance_textE + " " + time_textE + " far from you" + "\n\n" + "Lastly Updated Address : " + end_address);

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
                    if (!(LiveTrack.this.isFinishing())) {
                        if (googleApiClient == null) {

                            googleApiClient = new GoogleApiClient.Builder(LiveTrack.this)
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
                                                status.startResolutionForResult(LiveTrack.this, REQUEST_CHECK_SETTINGS);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
