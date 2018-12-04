package com.dev.praful.admintracker;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

import retrofit2.Call;
import retrofit2.Callback;

public class Trackdriver extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String id;
    String time_txt;
    LatLng latLng;
    String parsedDistance;
    String response;
    String DT;
    IGoogleAPI Services;
    String end_address;
    String distance_textD, time_textD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackdriver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getIntent().getStringExtra("Driverid") != null) {
            id = getIntent().getStringExtra("Driverid");
        }
        Services = CommonSwitch.getGoogleService();

        final TextView info = (TextView) findViewById(R.id.info);

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (id != null) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Drivers Info")
                    .child(id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final DriversInfo DriversInfo = dataSnapshot.getValue(DriversInfo.class);
                            latLng = new LatLng(Double.parseDouble(String.valueOf(DriversInfo.getLat())),
                                    Double.parseDouble(String.valueOf(DriversInfo.getLng()))
                            );
                            LatLng latLng1 = new LatLng(17.434810272796604, 78.38469553738832);

                            getDT(latLng1.latitude, latLng1.longitude, latLng.latitude, latLng.longitude);
                            new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("SignedDrivers")
                                            .child(id)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                    if (Driverdetails != null) {

                                                        mMap.addMarker(new MarkerOptions().position(latLng)
                                                                .title(Driverdetails.getUsername())
                                                                .snippet(distance_textD + " " + time_textD +  " far from Office")
                                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                                        info.setText("Driver : " + Driverdetails.getUsername() + "\n\n" + distance_textD+ " " + time_textD  + " far from Office" + "\n\n" + "Lastly updated Address : " + end_address);

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                }
                            }.start();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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
                    String start_address = steps.getString("start_address");
                    end_address = steps.getString("end_address");


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
        return parsedDistance + " " + time_txt;
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
                                distance_textD = distance.getString("text");

                                Double distance_value = Double.parseDouble(distance_textD.replaceAll("[^0-9\\\\.]+", ""));

                                JSONObject time = legsobject.getJSONObject("duration");
                                time_textD = time.getString("text");
                                Integer time_value = Integer.parseInt(time_textD.replaceAll("\\D+", ""));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
