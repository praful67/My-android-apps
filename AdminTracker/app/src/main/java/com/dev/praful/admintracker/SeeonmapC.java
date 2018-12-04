package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeonmapC extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String carId;
    private IGoogleAPI services;
    private Polyline directions;
    ArrayList<Double> lat = new ArrayList<>();
    ArrayList<Double> lng = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeonmap_c);
        services = CommonSwitch.getGoogleService();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lat.clear();
        lng.clear();
        getWindow().getAttributes().windowAnimations = R.style.Style;
        carId = getIntent().getStringExtra("carid");

        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Car")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot != null) {
                                final Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                if (map != null) {
                                    if (map.get("car id") != null) {
                                        if (carId.equals(String.valueOf(map.get("car id")))) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("RanksfordistanceCAl")
                                                    .child(carId).orderByChild("Rank")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                final Map<String, Object> map1 = (HashMap<String, Object>) dataSnapshot1.getValue();
                                                                if (map1 != null) {
                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("SignedEmployees")
                                                                            .child(dataSnapshot1.getKey())
                                                                            .addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                    if (dataSnapshot != null) {
                                                                                        final Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                                                                        if (employeedetails != null) {
                                                                                            if (employeedetails.getAddresslat() != null) {
                                                                                                mMap.addMarker(new MarkerOptions().title(employeedetails.getUsername())
                                                                                                        .snippet("Rank : " + String.valueOf(map1.get("Rank")))
                                                                                                        .position(new LatLng(Double.parseDouble(employeedetails.getAddresslat()), Double.parseDouble(employeedetails.getAddresslng())))
                                                                                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                                                                                lat.add(Double.parseDouble(employeedetails.getAddresslat()));
                                                                                                lng.add(Double.parseDouble(employeedetails.getAddresslng()));

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
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {

                if (lat.size() > 1) {
                    final LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);
                    getDirection(lat.get(lat.size() - 1), lng.get(lat.size() - 1), latLng.latitude, latLng.longitude);
                    /*for (int i = 0; i < lat.size() - 1; i++) {
                        getDirection(lat.get(i), lng.get(i), lat.get(i + 1), lng.get(i + 1));
                    }*/

                    int i = 0;
                    while (i < lat.size() - 1) {
                        getDirection(lat.get(i), lng.get(i), lat.get(i + 1), lng.get(i + 1));
                        i++;
                    }
                } else if (lat.size() == 1) {

                    final LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);
                    getDirection(lat.get(lat.size() - 1), lng.get(lat.size() - 1), latLng.latitude, latLng.longitude);

                } else {
                    Toast.makeText(SeeonmapC.this, "No Employess", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }.start();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        final LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);

        mMap.addMarker(new MarkerOptions().position(latLng)
                .title("Main Office")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

    }


    private class ParserTask extends AsyncTask<String, Integer, java.util.List<java.util.List<HashMap<String, String>>>> {

        ProgressDialog dialog = new ProgressDialog(SeeonmapC.this);

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
            services.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {

                        new ParserTask().execute(response.body().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(SeeonmapC.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
