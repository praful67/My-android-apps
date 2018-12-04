package com.Shootmyshow.praful.shootmyshow_company;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow_company.Common.Common;
import com.Shootmyshow.praful.shootmyshow_company.Helper.DirectionJSONParser;
import com.Shootmyshow.praful.shootmyshow_company.Model.DataMessage;
import com.Shootmyshow.praful.shootmyshow_company.Model.FCMResponse;
import com.Shootmyshow.praful.shootmyshow_company.Model.Token;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IFCMSerives;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IGoogleAPI;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

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

public class Companytracking extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener

{

    private GoogleMap mMap;
    String customerlat, customerlng;

    String customerId;
    Location pickuplocation;
    Button btnStart;

    private static final int PLAY_SERVICE_RES_REQUEST = 7001;

    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;


    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    private Circle customerMarker;
    private IGoogleAPI services;
    private Marker companyMarker;

    IFCMSerives ifcmSerives;
    GeoFire geoFire;
    private Polyline directions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companytracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent() != null) {
            customerlat = getIntent().getStringExtra("lat");
            customerlng = getIntent().getStringExtra("lng");
            customerId = getIntent().getStringExtra("customerId");
        }

        services = Common.getGoogleAPI();
        ifcmSerives = Common.getFCMService();
        setUploation();

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStart.getText().equals("START")) {

                    pickuplocation = Common.Lastlocation;
                    btnStart.setText("ENDED");
                } else if (btnStart.getText().equals("ENDED")) {
                    calculate(pickuplocation, Common.Lastlocation);
                }
            }
        });

    }

    private void calculate(final Location pickuplocation, Location lastlocation) {

        String requestApi = null;
        try {

            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" + pickuplocation.getLatitude() + "," + pickuplocation.getLongitude() + "&" +
                    "destination=" + lastlocation.getLatitude() + "," + lastlocation.getLongitude() + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            services.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray routes = jsonObject.getJSONArray("routes");
                        JSONObject object = routes.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject legsobject = legs.getJSONObject(0);

                        JSONObject distance = legsobject.getJSONObject("distance");
                        String distance_text = distance.getString("text");
                        Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));

                        sendCompletedNotify(customerId);
                        Intent intent = new Intent(Companytracking.this, WorkDetails.class);
                        intent.putExtra("start_address", legsobject.getString("start_address"));
                        intent.putExtra("end_address", legsobject.getString("end_address"));
                        intent.putExtra("distance", String.valueOf(distance_value));
                        intent.putExtra("location_start", String.format("%f %f", pickuplocation.getLatitude(), pickuplocation.getLongitude()));
                        intent.putExtra("location_end", String.format("%f %f", Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));

                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(Companytracking.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        customerMarker = mMap.addCircle(new CircleOptions().center(new LatLng(Double.parseDouble(customerlat), Double.parseDouble(customerlng))).radius(50).

                strokeColor(Color.BLUE).fillColor(0x220000FF).strokeWidth(5.0f));

        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference(Common.companies_table));


        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(Double.parseDouble(customerlat), Double.parseDouble(customerlng)), 0.05f);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                sendArrivedNotif(customerId);
                //   btnStart.setEnabled(true);
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

    private void sendArrivedNotif(String customerId) {

        Token token = new Token(customerId);

        // Notification notification = new Notification("Arrived",String.format("The %s has arrived at  your location" ,Common.currentUser.getName()));
        // Sender sender =new Sender(token.getToken(), notification);
        Map<String, String> content = new HashMap<>();
        content.put("title", "Arrived");
        content.put("message", String.format("The %s has arrived at  your location", Common.currentUser.getName()));
        DataMessage dataMessage = new DataMessage(token.getToken(), content);

        ifcmSerives.sendMessage(dataMessage).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.body().success != 1)
                    Toast.makeText(Companytracking.this, "Failed", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {

            }
        });
    }

    private void sendCompletedNotify(String customerId) {

        Token token = new Token(customerId);

        // Notification notification = new Notification("Completed",customerId);

        // Sender sender =new Sender(token.getToken(), notification);
        Map<String, String> content = new HashMap<>();
        content.put("title", "Completed");
        content.put("message", customerId);
        DataMessage dataMessage = new DataMessage(token.getToken(), content);

        ifcmSerives.sendMessage(dataMessage).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.body().success != 1)
                    Toast.makeText(Companytracking.this, "Failed", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displaylocation();
        startLocationUpdates();
    }

    private void displaylocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        Common.Lastlocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (Common.Lastlocation != null) {
            final double latitude = Common.Lastlocation.getLatitude();
            final double logitude = Common.Lastlocation.getLongitude();

            if (companyMarker != null)
                companyMarker.remove();
            companyMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, logitude))
                    .title("You")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
            );

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, logitude), 17.0f));

            if (directions != null)
                directions.remove();
            getDirection();

        } else {
            Log.d("ERROR", "CANNOT GET YOUR LOCATION");
        }
    }

    private void getDirection() {
        LatLng currentposition = new LatLng(Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude());
        String requestApi = null;
        try {

            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" + currentposition.latitude + "," + currentposition.longitude + "&" +
                    "destination=" + customerlat + "," + customerlng + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            Log.d("C A M E R A N", requestApi);
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

                    Toast.makeText(Companytracking.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setUploation() {
        if (checkPlayServices()) {
            buildGoogleApiclient();
            createlocationreq();
            displaylocation();

        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Common.Lastlocation = location;
        displaylocation();
    }


    private void createlocationreq() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiclient() {

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

    private boolean checkPlayServices() {
        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode))
                GooglePlayServicesUtil.getErrorDialog(resultcode, this, PLAY_SERVICE_RES_REQUEST).show();
            else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        ProgressDialog dialog = new ProgressDialog(Companytracking.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Please wait...");
            dialog.show();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
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
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
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
}
