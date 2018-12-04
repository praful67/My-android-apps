package com.dev.praful.trackyourdriver;

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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.view.View;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MyService extends JobService {
    public MyService() {
    }


    GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Drivers"));
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LatLng latLng;
    GeoQuery geoQuery, geoQuery1, geoQuery2;
    double lat, lng;
    Location location;
    String carid;
    String id;
    Notification notification;
    boolean seen = false;
    boolean seen1 = false;
    boolean seen2 = false;

    final ArrayList<String> dummytext = new ArrayList<>();
    final ArrayList<String> date = new ArrayList<>();

    @Override
    public boolean onStartJob(JobParameters params) {
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public void onDestroy() {

       /* if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {

            startService(new Intent(MyService.this, MyService.class));
        }*/
        ComponentName serviceName = new ComponentName(this, MyService.class);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(startMySerivceJobInfo);


        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());

        FirebaseDatabase.getInstance().getReference()
                .child("CancelledTimeoff")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (String.valueOf(map.get("timeoff")).equals("cancelled")) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        Intent notificationIntent = new Intent(getBaseContext(), RideDetails.class);
                                        final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        ;
                                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Notification.Builder builder = notificationHelper.getUberNotification("RYANTransport", "Admin has cancelled your timeoff", pendingIntent, sound);

                                        builder.setOnlyAlertOnce(true);
                                        final Notification notification = builder.build();
                                        notificationHelper.getManager().notify(1, notification);

                                    } else {
                                        Intent notificationIntent = new Intent(getBaseContext(), RideDetails.class);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                notificationIntent, 0);
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                        builder.setAutoCancel(true)
                                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                .setWhen(System.currentTimeMillis())
                                                .setOnlyAlertOnce(true)
                                                .setSmallIcon(R.drawable.car)
                                                .setContentTitle("RYANTransport")
                                                .setContentText("Admin has cancelled your timeoff")
                                                .setContentIntent(pendingIntent);

                                        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                        manager.notify(1, builder.build());
                                    }
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("CancelledTimeoff")
                                            .child(id)
                                            .child("timeoff")
                                            .setValue("nothing");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        

        FirebaseDatabase.getInstance().getReference()
                .child("ProfileEditedbyadmin").child(id)
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
                                    FirebaseDatabase.getInstance().getReference().child("ProfileEditedbyadmin")
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
        FirebaseDatabase.getInstance().getReference()
                .child("StatusE")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (!String.valueOf(map.get("status")).equals("Active")) {

                                    new CountDownTimer(20000, 1000) {
                                        @Override
                                        public void onTick(long l) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            if (date.size() > 0)
                                                date.clear();

                                            final DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                                            for (int i = 0; i < 366; i++) {
                                                Calendar calendar1 = Calendar.getInstance();
                                                calendar1.add(Calendar.DATE, i);
                                                Date newDate = calendar1.getTime();
                                                date.add((format.format(newDate)));

                                            }
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("ETimeoffsDatesReminder")
                                                    .child(id)
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dummytext.size() > 0)
                                                                dummytext.clear();

                                                            if (dataSnapshot != null) {
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    Map<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                                                                    if (map != null) {

                                                                        dummytext.add(String.valueOf(map.get("date")));

                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                            new CountDownTimer(2000, 1000) {
                                                @Override
                                                public void onTick(long l) {

                                                }

                                                @Override
                                                public void onFinish() {
                                                    int k = 0;
                                                    for (int i = 0; i < dummytext.size(); i++) {

                                                        for (int j = 0; j < 366; j++) {
                                                            if (date.get(j).equals(dummytext.get(i))) {
                                                                k++;
                                                            }
                                                        }

                                                    }
                                                    if (k != dummytext.size()) {
                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("StatusE")
                                                                .child(id)
                                                                .child("status")
                                                                .setValue("Active");
                                                    }
                                                }
                                            }.start();
                                            start();
                                        }
                                    }.start();

                                } else {
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
            id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            FirebaseDatabase.getInstance().getReference()
                    .child("foregroundserivces")
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
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                FirebaseDatabase.getInstance().getReference()
                        .child("Updates")
                        .child(id)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                if (map != null) {
                                    if (String.valueOf(map.get("whattodo")).equals("update")) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Updates").child(id).child("whattodo")
                                                .setValue("updated");
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("SignedEmployees")
                                                .child(id)
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                                        if (employeedetails.getAddresslat().length() != 0 && employeedetails.getAddresslng().length() != 0) {
                                                            lat = Double.parseDouble(employeedetails.getAddresslat());
                                                            lng = Double.parseDouble(employeedetails.getAddresslng());
                                                            geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lng), 3);
                                                            geoQuery1 = geoFire.queryAtLocation(new GeoLocation(lat, lng), 0.5);
                                                            geoQuery2 = geoFire.queryAtLocation(new GeoLocation(lat, lng), 0.03);

                                                            sendNotification();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                            Intent arrivI = new Intent(MyService.this, RideDetails.class);

                                            PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                    arrivI, PendingIntent.FLAG_UPDATE_CURRENT);

                                            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                            Notification.Builder builder = notificationHelper.getUberNotification("UPDATED YOUR RIDE",
                                                    "Admin has upated your ride details", arrivIp, sound);

                                            builder.setOnlyAlertOnce(true);
                                            Notification notification = builder.build();
                                            notificationHelper.getManager().notify(1, notification);

                                        } else {

                                            Intent arrivI = new Intent(getBaseContext(), RideDetails.class);

                                            PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                    arrivI, PendingIntent.FLAG_UPDATE_CURRENT);
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                            builder.setAutoCancel(true)
                                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                    .setWhen(System.currentTimeMillis())
                                                    .setSmallIcon(R.drawable.car)
                                                    .setOnlyAlertOnce(true)
                                                    .setContentTitle("UPDATED YOUR RIDE")
                                                    .setContentText("Admin has upated your ride details")
                                                    .setContentIntent(arrivIp);

                                            NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                            manager.notify(1, builder.build());


                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                FirebaseDatabase.getInstance().getReference()
                        .child("SignedEmployees")
                        .child(id)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                if (employeedetails != null) {
                                    if (employeedetails.getAddresslat().length() != 0 && employeedetails.getAddresslng().length() != 0) {
                                        lat = Double.parseDouble(employeedetails.getAddresslat());
                                        lng = Double.parseDouble(employeedetails.getAddresslng());
                                        geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lng), 3);
                                        geoQuery1 = geoFire.queryAtLocation(new GeoLocation(lat, lng), 0.5);
                                        geoQuery2 = geoFire.queryAtLocation(new GeoLocation(lat, lng), 0.03);

                                        sendNotification();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        }.start();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


    }

    private void sendNotification() {
        final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());

        final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Car")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null)
                            carid = String.valueOf(map.get("car id"));
                        if (carid != null) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("sent")
                                    .child(id)
                                    .child("saw")
                                    .setValue("no");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("sent")
                                    .child(id)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final Map<String, Object> map1 = (HashMap<String, Object>) dataSnapshot.getValue();
                                            if (String.valueOf(map1.get("saw")).equals("no")) {
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Driver's Car")
                                                        .child("login")
                                                        .child(carid)
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                                                    if (map != null) {
                                                                        if (String.valueOf(map.get("car id")).equals(carid)) {
                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("Drivers Info")
                                                                                    .child(dataSnapshot1.getKey())
                                                                                    .addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            DriversInfo DriversInfo = dataSnapshot.getValue(DriversInfo.class);
                                                                                            if (DriversInfo != null) {

                                                                                                geoQuery.removeAllListeners();
                                                                                                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                                                                                    @Override
                                                                                                    public void onKeyEntered(String key, GeoLocation location) {

                                                                                                        if (key.equals(dataSnapshot1.getKey())) {
                                                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                                                    .child("sent")
                                                                                                                    .child(id).addValueEventListener(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                    Map<String, Object> map2 = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                                                                    if (map2 != null) {
                                                                                                                        if (String.valueOf(map2.get("saw")).equals("no") && !seen) {

                                                                                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                                                                                                seen = true;
                                                                                                                                Intent arrivI = new Intent(MyService.this, RideDetails.class);

                                                                                                                                PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                                                                                                        arrivI, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                                                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                                                                                                                Notification.Builder builder = notificationHelper.getUberNotification("DRIVER ON THE WAY",
                                                                                                                                        "Driver is within 3km and can arrive in 10min", arrivIp, sound);

                                                                                                                                builder.setOnlyAlertOnce(true);
                                                                                                                                notification = builder.build();
                                                                                                                                notificationHelper.getManager().notify(1, notification);
                                                                                                                                // startForeground(1,notification);
                                                                                                                                FirebaseDatabase.getInstance().getReference().child("sent")
                                                                                                                                        .child(id)
                                                                                                                                        .child("saw").setValue("yes");

                                                                                                                            } else {
                                                                                                                                Intent arrivI = new Intent(getBaseContext(), RideDetails.class);
                                                                                                                                seen = true;
                                                                                                                                PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                                                                                                        arrivI, PendingIntent.FLAG_UPDATE_CURRENT);
                                                                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                                                                                                                builder.setAutoCancel(true)
                                                                                                                                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                                                                                                        .setWhen(System.currentTimeMillis())
                                                                                                                                        .setSmallIcon(R.drawable.car)
                                                                                                                                        .setOnlyAlertOnce(true)
                                                                                                                                        .setContentTitle("DRIVER ON THE WAY")
                                                                                                                                        .setContentText("Driver is within 3km and can arrive in 10min")
                                                                                                                                        .setContentIntent(arrivIp);

                                                                                                                                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                                                                manager.notify(1, builder.build());
                                                                                                                                FirebaseDatabase.getInstance().getReference().child("sent")
                                                                                                                                        .child(id)
                                                                                                                                        .child("saw").setValue("yes");


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

                                                                                                    @Override
                                                                                                    public void onKeyExited(String key) {

                                                                                                        if (key.equals(dataSnapshot1.getKey())) {

                                                                                                            FirebaseDatabase.getInstance().getReference().child("sent")
                                                                                                                    .child(id)
                                                                                                                    .child("saw").setValue("no");
                                                                                                            new CountDownTimer(2000, 1000) {
                                                                                                                @Override
                                                                                                                public void onTick(long millisUntilFinished) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onFinish() {
                                                                                                                    seen = false;

                                                                                                                }
                                                                                                            }.start();
                                                                                                            stopForeground(true);
                                                                                                        }
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
                            FirebaseDatabase.getInstance().getReference()
                                    .child("sent1")
                                    .child(id)
                                    .child("saw")
                                    .setValue("no");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("sent1")
                                    .child(id)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final Map<String, Object> map1 = (HashMap<String, Object>) dataSnapshot.getValue();
                                            if (String.valueOf(map1.get("saw")).equals("no")) {
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Driver's Car")
                                                        .child("login")
                                                        .child(carid)
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                                                    if (map != null) {
                                                                        if (String.valueOf(map.get("car id")).equals(carid)) {
                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("Drivers Info")
                                                                                    .child(dataSnapshot1.getKey())
                                                                                    .addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            DriversInfo DriversInfo = dataSnapshot.getValue(DriversInfo.class);
                                                                                            if (DriversInfo != null) {

                                                                                                geoQuery1.removeAllListeners();
                                                                                                geoQuery1.addGeoQueryEventListener(new GeoQueryEventListener() {
                                                                                                    @Override
                                                                                                    public void onKeyEntered(String key, GeoLocation location) {

                                                                                                        if (key.equals(dataSnapshot1.getKey())) {
                                                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                                                    .child("sent1")
                                                                                                                    .child(id).addValueEventListener(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                    Map<String, Object> map2 = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                                                                    if (map2 != null) {
                                                                                                                        if (String.valueOf(map2.get("saw")).equals("no") && !seen1) {

                                                                                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                                                                                                seen1 = true;
                                                                                                                                Intent arrivI = new Intent(MyService.this, RideDetails.class);

                                                                                                                                PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                                                                                                        arrivI, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                                                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                                                                                                                Notification.Builder builder = notificationHelper.getUberNotification("DRIVER ON THE WAY",
                                                                                                                                        "Driver is within 0.5 km and can arrive in 3min", arrivIp, sound);

                                                                                                                                builder.setOnlyAlertOnce(true);
                                                                                                                                notification = builder.build();
                                                                                                                                notificationHelper.getManager().notify(2, notification);
                                                                                                                                // startForeground(1,notification);
                                                                                                                                FirebaseDatabase.getInstance().getReference().child("sent1")
                                                                                                                                        .child(id)
                                                                                                                                        .child("saw").setValue("yes");

                                                                                                                            } else {
                                                                                                                                Intent arrivI = new Intent(getBaseContext(), RideDetails.class);
                                                                                                                                seen1 = true;
                                                                                                                                PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                                                                                                        arrivI, PendingIntent.FLAG_UPDATE_CURRENT);
                                                                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                                                                                                                builder.setAutoCancel(true)
                                                                                                                                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                                                                                                        .setWhen(System.currentTimeMillis())
                                                                                                                                        .setSmallIcon(R.drawable.car)
                                                                                                                                        .setOnlyAlertOnce(true)
                                                                                                                                        .setContentTitle("DRIVER ON THE WAY")
                                                                                                                                        .setContentText("Driver is within 0.5 km and can arrive in 3min")
                                                                                                                                        .setContentIntent(arrivIp);

                                                                                                                                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                                                                manager.notify(2, builder.build());
                                                                                                                                FirebaseDatabase.getInstance().getReference().child("sent1")
                                                                                                                                        .child(id)
                                                                                                                                        .child("saw").setValue("yes");


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

                                                                                                    @Override
                                                                                                    public void onKeyExited(String key) {

                                                                                                        if (key.equals(dataSnapshot1.getKey())) {

                                                                                                            FirebaseDatabase.getInstance().getReference().child("sent1")
                                                                                                                    .child(id)
                                                                                                                    .child("saw").setValue("no");
                                                                                                            new CountDownTimer(2000, 1000) {
                                                                                                                @Override
                                                                                                                public void onTick(long millisUntilFinished) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onFinish() {
                                                                                                                    seen1 = false;

                                                                                                                }
                                                                                                            }.start();
                                                                                                            stopForeground(true);
                                                                                                        }
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

                            FirebaseDatabase.getInstance().getReference()
                                    .child("sent2")
                                    .child(id)
                                    .child("saw")
                                    .setValue("no");
                            FirebaseDatabase.getInstance().getReference()
                                    .child("sent2")
                                    .child(id)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final Map<String, Object> map1 = (HashMap<String, Object>) dataSnapshot.getValue();
                                            if (String.valueOf(map1.get("saw")).equals("no")) {
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Driver's Car")
                                                        .child("login")
                                                        .child(carid)
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                                                    if (map != null) {
                                                                        if (String.valueOf(map.get("car id")).equals(carid)) {
                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("Drivers Info")
                                                                                    .child(dataSnapshot1.getKey())
                                                                                    .addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            DriversInfo DriversInfo = dataSnapshot.getValue(DriversInfo.class);
                                                                                            if (DriversInfo != null) {

                                                                                                geoQuery2.removeAllListeners();
                                                                                                geoQuery2.addGeoQueryEventListener(new GeoQueryEventListener() {
                                                                                                    @Override
                                                                                                    public void onKeyEntered(String key, GeoLocation location) {

                                                                                                        if (key.equals(dataSnapshot1.getKey())) {
                                                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                                                    .child("sent2")
                                                                                                                    .child(id).addValueEventListener(new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                    Map<String, Object> map2 = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                                                                    if (map2 != null) {
                                                                                                                        if (String.valueOf(map2.get("saw")).equals("no") && !seen2) {

                                                                                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                                                                                                                seen2 = true;
                                                                                                                                Intent arrivI = new Intent(MyService.this, RideDetails.class);

                                                                                                                                PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                                                                                                        arrivI, PendingIntent.FLAG_UPDATE_CURRENT);

                                                                                                                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                                                                                                                Notification.Builder builder = notificationHelper.getUberNotification("DRIVER ON THE WAY",
                                                                                                                                        "Driver has arrived", arrivIp, sound);

                                                                                                                                builder.setOnlyAlertOnce(true);
                                                                                                                                notification = builder.build();
                                                                                                                                notificationHelper.getManager().notify(3, notification);
                                                                                                                                // startForeground(1,notification);
                                                                                                                                FirebaseDatabase.getInstance().getReference().child("sent2")
                                                                                                                                        .child(id)
                                                                                                                                        .child("saw").setValue("yes");

                                                                                                                            } else {
                                                                                                                                Intent arrivI = new Intent(getBaseContext(), RideDetails.class);
                                                                                                                                seen2 = true;
                                                                                                                                PendingIntent arrivIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                                                                                                        arrivI, PendingIntent.FLAG_UPDATE_CURRENT);
                                                                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                                                                                                                builder.setAutoCancel(true)
                                                                                                                                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                                                                                                        .setWhen(System.currentTimeMillis())
                                                                                                                                        .setSmallIcon(R.drawable.car)
                                                                                                                                        .setOnlyAlertOnce(true)
                                                                                                                                        .setContentTitle("DRIVER ON THE WAY")
                                                                                                                                        .setContentText("Driver has arrived")
                                                                                                                                        .setContentIntent(arrivIp);

                                                                                                                                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                                                                                                                manager.notify(3, builder.build());
                                                                                                                                FirebaseDatabase.getInstance().getReference().child("sent2")
                                                                                                                                        .child(id)
                                                                                                                                        .child("saw").setValue("yes");


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

                                                                                                    @Override
                                                                                                    public void onKeyExited(String key) {

                                                                                                        if (key.equals(dataSnapshot1.getKey())) {

                                                                                                            FirebaseDatabase.getInstance().getReference().child("sent2")
                                                                                                                    .child(id)
                                                                                                                    .child("saw").setValue("no");
                                                                                                            new CountDownTimer(2000, 1000) {
                                                                                                                @Override
                                                                                                                public void onTick(long millisUntilFinished) {

                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onFinish() {
                                                                                                                    seen2 = false;

                                                                                                                }
                                                                                                            }.start();
                                                                                                            stopForeground(true);
                                                                                                        }
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
        locationRequest.setSmallestDisplacement(10);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void displaylocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location1) {
                location = location1;
                if (location != null) {
                    final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    geoFire.setLocation(id, new GeoLocation(location.getLatitude(), location.getLongitude())
                            , new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {

                                    final Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("lat", String.valueOf(location.getLatitude()));
                                    map.put("lng", String.valueOf(location.getLongitude()));

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("SignedEmployees")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.child(id).exists()) {
                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("Employees Info")
                                                                .child(id)
                                                                .updateChildren(map);

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                }


                            });
                }
            }
        });
    }


    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseDatabase.getInstance().goOnline();


        return START_STICKY;
    }*/

    /*  @Override
      public IBinder onBind(Intent intent) {
          return null;
      }
  */
    private void showNotif() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

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
                .child("foregroundserivces")
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
}
 /*       FirebaseDatabase.getInstance().getReference()
                        .child("SignedEmployees")
                        .child(id)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                lat = Double.parseDouble(employeedetails.getAddresslat());
                                lng = Double.parseDouble(employeedetails.getAddresslng());
                                 geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lng), 3);
                                sendNotification();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
*/