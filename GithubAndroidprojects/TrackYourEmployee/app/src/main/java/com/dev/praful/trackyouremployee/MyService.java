package com.dev.praful.trackyouremployee;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

    @Override
    public boolean onStartJob(JobParameters params) {
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Drivers"));
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    Location location;

    @Override
    public void onCreate() {
        super.onCreate();
        final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());
        final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseDatabase.getInstance().getReference()
                .child("ProfileEditedbyadminD").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (String.valueOf(map.get("changed")).equals("yes")) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        Intent notificationIntent = new Intent(getBaseContext(), Profile.class);
                                        final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        ;
                                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Notification.Builder builder = notificationHelper.getUberNotification("RYANTransport", "Admin has edited your profile", pendingIntent, sound);

                                        builder.setOnlyAlertOnce(true);
                                        final Notification notification = builder.build();
                                        notificationHelper.getManager().notify(1, notification);

                                    } else {
                                        Intent notificationIntent = new Intent(getBaseContext(), Profile.class);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                notificationIntent, 0);
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                        builder.setAutoCancel(true)
                                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                .setWhen(System.currentTimeMillis())
                                                .setOnlyAlertOnce(true)
                                                .setSmallIcon(R.drawable.car)
                                                .setContentTitle("RYANTransport")
                                                .setContentText("Admin has edited your profile")
                                                .setContentIntent(pendingIntent);

                                        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                        manager.notify(1, builder.build());
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("ProfileEditedbyadminD")
                                            .child(id)
                                            .child("changed")
                                            .setValue("No");
                                }
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            ;
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification.Builder builder = notificationHelper.getUberNotification("RYANTransport", "Have a nice trip", pendingIntent, sound);

            final Notification notification = builder.build();
            //notificationHelper.getManager().notify(1, notification);

            FirebaseDatabase.getInstance().getReference()
                    .child("Driversforegroundserivces")
                    .child(id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (String.valueOf(map.get("want")).equals("yes")) {
                                    startForeground(1337, notification);

                                } else if (String.valueOf(map.get("want")).equals("no")) {
                                    stopForeground(true);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        } else
            showNotif();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        buildLocationreq();
        buildLocationCallback();
        displaylocation();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        FirebaseDatabase.getInstance().goOnline();
        FirebaseDatabase.getInstance().getReference()
                .child("DriverUpdates")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            if (String.valueOf(map.get("whattodo")).equals("update")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                    Intent arrivI = new Intent(MyService.this, Loginout.class);

                                    PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                            arrivI, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                    Notification.Builder builder = notificationHelper.getUberNotification("UPDATED YOUR RIDE",
                                            "Admin has upated your ride details", arrivIp, sound);

                                    builder.setOnlyAlertOnce(true);
                                    Notification notification = builder.build();
                                    notificationHelper.getManager().notify(1, notification);

                                } else {

                                    Intent arrivI = new Intent(getBaseContext(), Loginout.class);

                                    PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                            arrivI, PendingIntent.FLAG_UPDATE_CURRENT);
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                    builder.setAutoCancel(true)
                                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                            .setWhen(System.currentTimeMillis())
                                            .setOnlyAlertOnce(true)
                                            .setSmallIcon(R.drawable.admin)
                                            .setContentTitle("UPDATED YOUR RIDE")
                                            .setContentText("Admin has upated your ride details")
                                            .setContentIntent(arrivIp);

                                    NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(1, builder.build());


                                }

                                FirebaseDatabase.getInstance().getReference()
                                        .child("DriverUpdates")
                                        .child(id)
                                        .child("whattodo")
                                        .setValue("updated");


                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void buildLocationCallback() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location1 : locationResult.getLocations()) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("latitude", String.valueOf(location1.getLatitude()));
                    editor.putString("longitude", String.valueOf(location1.getLongitude()));
                    editor.apply();
                    location = location1;
                }


                displaylocation();
            }
        };
    }


    private void buildLocationreq() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(1);
    }


    private void displaylocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {

            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location1) {
                location = location1;

                if (location != null) {
                    final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    geoFire.setLocation(id, new GeoLocation(location.getLatitude(), location.getLongitude())
                            , new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {



                                    final Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("id", id);
                                    map.put("lat", String.valueOf(location.getLatitude()));
                                    map.put("lng", String.valueOf(location.getLongitude()));
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("SignedDrivers")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.child(id).exists()) {
                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("Drivers Info")
                                                                .child(id)
                                                                .updateChildren(map);
                                                    }


                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                }
                            }
                    );

                }
            }
        });
    }

/*

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());


        return START_STICKY;
    }
*/

    private void showNotif() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis()).
                setSmallIcon(R.drawable.car)
                .setContentTitle("RYANTransport")
                .setContentText("Have a nice trip")
                .setContentIntent(pendingIntent);

        final Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // manager.notify(1, builder.build());
        FirebaseDatabase.getInstance().getReference()
                .child("Driversforegroundserivces")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            if (String.valueOf(map.get("want")).equals("yes")) {
                                startForeground(1337, notification);

                            } else if (String.valueOf(map.get("want")).equals("no")) {
                                stopForeground(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //startForeground(1337, notification);


    }

 /*   @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
*/
    @Override
    public void onDestroy() {

/*

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {

            startService(new Intent(MyService.this , MyService.class));
        }

*/

        ComponentName serviceName = new ComponentName(this, MyService.class);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(startMySerivceJobInfo);


        super.onDestroy();
    }
}
/* FirebaseDatabase.getInstance().getReference()
                    .child("Driver's current Employee").child(pref.getString("name", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                java.util.Map<String, Object> currentemployee = (HashMap<String, Object>) dataSnapshot.getValue();
                                LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(currentemployee.get("latitude"))),
                                        Double.parseDouble(String.valueOf(currentemployee.get("longitude")))
                                );
                                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), .1);
                                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                    @Override
                                    public void onKeyEntered(String key, GeoLocation location1) {

                                        if (key.toString().equals(pref.getString("name", ""))) {
                                       *//* Intent intent1 = new Intent(getBaseContext(),Mapfornearestone.class);
                                        startActivity(intent1);
                                   *//*
                                            Toast.makeText(getBaseContext(), "Reached SSS", Toast.LENGTH_SHORT).show();

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

                                    }

                                    @Override
                                    public void onGeoQueryError(DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
          */

    /*    FirebaseDatabase.getInstance().getReference().child("Driver's click")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null && dataSnapshot.getValue().toString().equals("clicked")) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Driver's click")
                                    .child(id)
                                    .setValue("notclicked");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Driver's confirmed employee").child(id)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null) {
                                                java.util.Map<String, Object> currentemployee = (HashMap<String, Object>) dataSnapshot.getValue();
                                                LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(currentemployee.get("latitude"))),
                                                        Double.parseDouble(String.valueOf(currentemployee.get("longitude")))
                                                );

                                                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), .1);
                                                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                                    @Override
                                                    public void onKeyEntered(String key, GeoLocation location1) {

                                                        if ((key.toString().equals(id))) {
                                                            Toast.makeText(getBaseContext(), "Reached", Toast.LENGTH_SHORT).show();
                                                                *//*Intent intent1 = new Intent(MyService.this, Map.class);
                                                                startActivity(intent1);
                                                                *//*
                                                            stopSelf();
                                                            Toast.makeText(getBaseContext(), "Reached N SS", Toast.LENGTH_SHORT).show();
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

                                                    }

                                                    @Override
                                                    public void onGeoQueryError(DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
*/