package com.dev2.tracker.TrackYourFriends;

import android.Manifest;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MyService extends JobService {
    public MyService() {
    }

    GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Userslocations"));
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    Location location;
    String id;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        if (!pref.getString("id", "").equals(""))
            id = pref.getString("id", "");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        buildLocationreq();
        buildLocationCallback();
        displaylocation();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }

    private void buildLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location1 : locationResult.getLocations()) {
                    location = location1;
                }


                displaylocation();
            }
        };
    }

    @Override
    public void onDestroy() {

        ComponentName serviceName = new ComponentName(this, MyService.class);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(startMySerivceJobInfo);


        super.onDestroy();
    }

    private void buildLocationreq() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void displaylocation() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location1) {
                if (location1 != null) {
                    location = location1;

                    if (location != null) {

                        if (!pref.getString("id", "").equals("")) {
                            id = pref.getString("id", "");

                  /*  geoFire.setLocation(id, new GeoLocation(location.getLatitude(), location.getLongitude())
                            , new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {



                                }


                            });*/
                            if (location != null) {
                                final Map<String, Object> map = new HashMap<String, Object>();
                                map.put("lat", String.valueOf(location.getLatitude()));
                                map.put("lng", String.valueOf(location.getLongitude()));

                                if (id != null) {
                                    if (!id.equals("")) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Signedusers")
                                                .child(id)
                                                .updateChildren(map);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
