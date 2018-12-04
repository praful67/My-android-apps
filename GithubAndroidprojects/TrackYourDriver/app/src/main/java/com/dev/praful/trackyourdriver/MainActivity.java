package com.dev.praful.trackyourdriver;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    String id;
    private GoogleApiClient googleApiClient;
    SpotsDialog waitingDailog;
    final static int REQUEST_CHECK_SETTINGS = 199;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        waitingDailog = new SpotsDialog(this);

        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            //  startService(new Intent(MainActivity.this, MyService.class));
            ComponentName serviceName = new ComponentName(this, MyService.class);
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
            jobScheduler.schedule(startMySerivceJobInfo);


        }

        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.locationdialog, null);
        dialog.setView(view);
        final AlertDialog alertDialog = dialog.create();

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
                if (googleApiClient == null) {

                    googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
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
                                        status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
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
        });

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            if (!(MainActivity.this.isFinishing())) {
                alertDialog.show();
            }
        } else {

            FirebaseDatabase.getInstance().getReference()
                    .child("SignedEmployees")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                //we are connected to a network
                                if (dataSnapshot.child(id).exists()) {
                                    if (!MainActivity.this.isFinishing()) {
                                        waitingDailog.show();
                                        waitingDailog.setMessage("Signing..");
                                        waitingDailog.setCancelable(false);

                                        new CountDownTimer(2000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                waitingDailog.dismiss();
                                                Intent intent = new Intent(MainActivity.this, Map.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }.start();

                                    }
                                } else {
                                    new CountDownTimer(2000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            Intent intent = new Intent(MainActivity.this, Signup.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }.start();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "No Internet connection !", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }


        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        Toast.makeText(this, "PLEASE REFRESH", Toast.LENGTH_LONG).show();

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "PLEASE REFRESH", Toast.LENGTH_LONG).show();
                        if (!states.isGpsUsable()) {
                            // Degrade gracefully depending on what is available
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
    }

}
/*

        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.locationdialog, null);
        dialog.setView(view);
        final AlertDialog alertDialog = dialog.create();

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

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            if (!(MainActivity.this.isFinishing())) {
                alertDialog.show();
            }
        } else {
            FirebaseDatabase.getInstance().getReference()
                    .child("SignedEmployees")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                //we are connected to a network
                                if (dataSnapshot.child(id).exists()) {
                                    if (!MainActivity.this.isFinishing()) {
                                        waitingDailog.show();
                                        waitingDailog.setMessage("Signing..");
                                        waitingDailog.setCancelable(false);

                                        new CountDownTimer(2000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                waitingDailog.dismiss();
                                                Intent intent = new Intent(MainActivity.this, Map.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }.start();

                                    }
                                } else {
                                    new CountDownTimer(2000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            Intent intent = new Intent(MainActivity.this, Signup.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }.start();
                                }
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "No Internet connection !", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
*/
