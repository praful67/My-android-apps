package com.example.praful.presencesystem;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
        , LocationListener {

    List<User> users = new ArrayList<>();
    static final int My_PERMISSION = 7271;
    int play_services = 7271;
    LocationRequest locationRequest;
    Location location;
    EditText name;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button online = (Button) findViewById(R.id.online);
        Button offline = (Button) findViewById(R.id.offline);
        final ListView listView = (ListView) findViewById(R.id.list_data);
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().goOffline();
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().goOffline();

                FirebaseDatabase.getInstance().goOnline();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,

            }, My_PERMISSION);
        } else {
            if (checkplayservice()) {
                buildgoogleapiclient();
                createLocationrequest();
                displaylocation();
            }
        }

        name = (EditText) findViewById(R.id.name);
        FirebaseDatabase.getInstance().getReference()
                .child(".info/connected")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class) == true) {

                            User currentuser = new User(name.getText().toString(), "online");
                            if (!TextUtils.isEmpty(name.getText().toString())) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("OnlineUsers")
                                        .child(name.getText().toString())
                                        .setValue(currentuser);
                            }
                        }/* else {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("OnlineUsers")
                                    .child(name.getText().toString()).onDisconnect()
                                    .removeValue();

                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("OnlineUsers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (users.size() > 0)
                            users.clear();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            User user = dataSnapshot1.getValue(User.class);
                            if (user != null)
                                users.add(user);

                        }

                        LstViewAdapter adapter = new LstViewAdapter(MainActivity.this, users);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void displaylocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {

            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {

            final Tracking userlocations = new Tracking(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            final SharedPreferences.Editor editor = pref.edit();
            editor.putString("lat", String.valueOf(location.getLatitude()));
            editor.putString("lng" , String.valueOf(location.getLongitude()));
            editor.apply();
            FirebaseDatabase.getInstance().getReference()
                    .child("locations")
                    .child(name.getText().toString())
                    .setValue(userlocations);

            FirebaseDatabase.getInstance().getReference()
                    .child("OnlineUsers")
                    .child(name.getText().toString())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                              /* FirebaseDatabase.getInstance().getReference()
                                    .child("locations")
                                    .child(name.getText().toString()).onDisconnect().removeValue();
*/
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        } else {
            Toast.makeText(this, "Couldn't get your location", Toast.LENGTH_SHORT).show();
        }
    }

    private void createLocationrequest() {
        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(10);
        locationRequest.setFastestInterval(3000);
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)

        {

            case My_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkplayservice()) {
                        buildgoogleapiclient();
                        createLocationrequest();
                        displaylocation();
                    }
                }
            }
            break;
        }
    }

    private void buildgoogleapiclient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this).addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private boolean checkplayservice() {

        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode)) {
                GooglePlayServicesUtil.getErrorDialog(resultcode, this, play_services).show();
            } else {
                Toast.makeText(this, "Device is not supported", Toast.LENGTH_SHORT).show();

                finish();
            }

            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location1) {

        location = location1;
        displaylocation();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displaylocation();
        startlocationupdates();
    }

    private void startlocationupdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {

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
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkplayservice();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null)
            googleApiClient.disconnect();

        super.onStop();
    }
}
