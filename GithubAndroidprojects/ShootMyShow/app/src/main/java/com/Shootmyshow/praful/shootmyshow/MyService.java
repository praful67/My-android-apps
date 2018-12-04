package com.Shootmyshow.praful.shootmyshow;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow.Model.Trackingmessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.Shootmyshow.praful.shootmyshow.App.CHANNEL_ID;

public class MyService extends Service {

    public MyService() {
    }

    Trackingmessage bid1;

    public static Boolean serviceRunning = false;
    String bid;
    Trackingmessage trackingmessage;
    String message;

    @Override
    public void onCreate() {

        BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.ubercoustomer");
        registerReceiver(broadcastReceiver, intentFilter);


        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        FirebaseDatabase.getInstance().getReference()
                .child("foregroundserivces")
                .child(pref.getString("Id", ""))
                .child("want").setValue("yes");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());


            FirebaseDatabase.getInstance().getReference().child("cancelBooking")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        bid = dataSnapshot1.getValue().toString();

                        if (dataSnapshot != null) {
                            Intent cancelI = new Intent(getBaseContext(), CompanycancelledtheBooking.class);
                            cancelI.putExtra("Id", bid);
                            final PendingIntent cancelIp = PendingIntent.getActivity(getBaseContext(), 0,
                                    cancelI, PendingIntent.FLAG_UPDATE_CURRENT);
                            ;
                            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                            Notification.Builder builder = notificationHelper.getUberNotification("BOOKING CANCELLED",
                                    "Please check your cancelled bookings", cancelIp, sound);

                            Notification notification = builder.build();
                            notificationHelper.getManager().notify(1, notification);

                            Intent intent = new Intent(getBaseContext(), CompanycancelledtheBooking.class);
                            intent.putExtra("Id", bid);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            FirebaseDatabase.getInstance().getReference().child("verifyingBooking")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        bid = dataSnapshot1.getValue().toString();

                        if (dataSnapshot != null) {

                            FirebaseDatabase.getInstance().getReference().child("coustomerBookings").child(pref.getString("Id", ""))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.getKey().equals(bid)) {

                                                    Intent verifyingI = new Intent(getBaseContext(), VerifyingCompletedBooking.class);
                                                    verifyingI.putExtra("BookingIdC", bid);
                                                    final PendingIntent verifyingIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                            verifyingI, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    ;
                                                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                                    Notification.Builder builder = notificationHelper.getUberNotification("VERIFY YOUR BOOKING",
                                                            "Company wants to complete this booking . Please verify it.", verifyingIp, sound);

                                                    Notification notification = builder.build();
                                                    notificationHelper.getManager().notify(1, notification);
                                                    Intent intent = new Intent(getBaseContext(), VerifyingCompletedBooking.class);
                                                    intent.putExtra("BookingIdC", bid);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);

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


            FirebaseDatabase.getInstance().getReference()
                    .child("Responses")
                    .child(pref.getString("Id", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String response = dataSnapshot1.getValue(String.class);

                                Intent responseI = new Intent(getBaseContext(), Response.class);
                                responseI.putExtra("type", "response");
                                responseI.putExtra("response", response);

                                final PendingIntent responseIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        responseI, PendingIntent.FLAG_UPDATE_CURRENT);
                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                Notification.Builder builder = notificationHelper.getUberNotification("Company Response",
                                        response, responseIp, sound);

                                Notification notification = builder.build();
                                notificationHelper.getManager().notify(1, notification);

                                Intent intent = new Intent(getBaseContext(), Response.class);
                                intent.putExtra("response", response);
                                startActivity(intent);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            Intent notificationIntent = new Intent(this, Home.class);
            notificationIntent.putExtra("B", "i got this");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            ;
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            Notification.Builder builder = notificationHelper.getUberNotification("SHOOT MY SHOW", "Lets book some cool experts", pendingIntent, sound);
            final Notification notification = builder.build();
            FirebaseDatabase.getInstance().getReference()
                    .child("foregroundserivces")
                    .child(pref.getString("Id", ""))
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


        } else {

            FirebaseDatabase.getInstance().getReference().child("cancelBooking")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        bid = dataSnapshot1.getValue().toString();


                        if (dataSnapshot != null) {
                            Intent cancelI = new Intent(getBaseContext(), CompanycancelledtheBooking.class);
                            cancelI.putExtra("Id", bid);
                            final PendingIntent cancelIp = PendingIntent.getActivity(getBaseContext(), 0,
                                    cancelI, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                            builder.setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                    .setWhen(System.currentTimeMillis()).
                                    setSmallIcon(R.drawable.camera)
                                    .setContentTitle("BOOKING CANCELLED")
                                    .setContentText("Please check your cancelled bookings")
                                    .setContentIntent(cancelIp);

                            NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(1, builder.build());
                            Intent intent = new Intent(getBaseContext(), CompanycancelledtheBooking.class);
                            intent.putExtra("Id", bid);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("verifyingBooking")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        bid = dataSnapshot1.getValue(String.class);

                        if (dataSnapshot != null) {

                            FirebaseDatabase.getInstance().getReference().child("coustomerBookings").child(pref.getString("Id", ""))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.getKey().equals(bid)) {
                                                    Intent verifyI = new Intent(getBaseContext(), VerifyingCompletedBooking.class);
                                                    verifyI.putExtra("BookingIdC", bid);

                                                    final PendingIntent verifyIp = PendingIntent.getActivity(getBaseContext(), 0,
                                                            verifyI, PendingIntent.FLAG_UPDATE_CURRENT);

                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                                    builder.setAutoCancel(true)
                                                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                            .setWhen(System.currentTimeMillis()).
                                                            setSmallIcon(R.drawable.camera)
                                                            .setContentTitle("VERIFY YOUR BOOKING")
                                                            .setContentText("Company wants to complete this booking . Please verify it.")
                                                            .setContentIntent(verifyIp);

                                                    NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                                    manager.notify(1, builder.build());
                                                    Intent intent = new Intent(getBaseContext(), VerifyingCompletedBooking.class);
                                                    intent.putExtra("BookingIdC", bid);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);

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


            FirebaseDatabase.getInstance().getReference()
                    .child("Responses")
                    .child(pref.getString("Id", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String response = dataSnapshot1.getValue(String.class);

                                Intent responseI = new Intent(getBaseContext(), Home.class);
                                responseI.putExtra("type", "response");
                                responseI.putExtra("response", response);

                                final PendingIntent responseIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        responseI, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                builder.setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                        .setWhen(System.currentTimeMillis()).
                                        setSmallIcon(R.drawable.camera)
                                        .setContentTitle("Company Response")
                                        .setContentText(response)
                                        .setContentIntent(responseIp);

                                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(1, builder.build());
                                Intent intent = new Intent(getBaseContext(), Response.class);
                                intent.putExtra("response", response);
                                startActivity(intent);


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis()).
                    setSmallIcon(R.drawable.camera)
                    .setContentTitle("SHOOT MY SHOW")
                    .setContentText("This is for reminding you about your booking")
                    .setContentIntent(pendingIntent);

            final Notification notification = builder.build();
            FirebaseDatabase.getInstance().getReference()
                    .child("foregroundserivces")
                    .child(pref.getString("Id", ""))
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

        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FirebaseDatabase.getInstance().goOnline();
       /* BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.ubercoustomer");
        registerReceiver(broadcastReceiver, intentFilter);

        if (serviceRunning == false) {
            serviceRunning = true;


        }

*/
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


      /*  BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.ubercoustomer");
        registerReceiver(broadcastReceiver, intentFilter);


        serviceRunning = false;

        Intent intent = new Intent();
        intent.setAction("com.example.praful.ubercoustomer");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        sendBroadcast(intent);

     */   //Toast.makeText(getApplicationContext(), "Destroid", Toast.LENGTH_SHORT).show();

    }


    private void showNotif() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis()).
                setSmallIcon(R.drawable.camera)
                .setContentTitle("SHOOT MY SHOW")
                .setContentText("This is for reminding you about your booking")
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

        //startForeground(1337, notification);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotifAPI26(final Context context) {
        Intent notificationIntent = new Intent(this, Home.class);
        notificationIntent.putExtra("B", "i got this");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        ;
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());
        Notification.Builder builder = notificationHelper.getUberNotification("SHOOT MY SHOW", "Lets book some cool experts", pendingIntent, sound);

        notificationHelper.getManager().notify(1, builder.build());
        Notification notification = builder.build();

        //  startForeground(1337, notification);

    }


    public void noti() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle("Welcome everyone")
                .setContentText("HEY")
                .setSmallIcon(R.drawable.camera)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        // startForeground(1337, notification);


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

   /*    FirebaseDatabase.getInstance().getReference().child("trackingNotification")
                    .child(pref.getString("Id", "")).child("Notification").addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        trackingmessage = dataSnapshot.getValue(Trackingmessage.class);

                        if (trackingmessage != null) {
                            Intent trackingI = new Intent(getBaseContext(), Onthewayandimreached.class);
                            trackingI.putExtra("type", "tracking");
                            trackingI.putExtra("BookingIdT", trackingmessage.getBookingid());
                            trackingI.putExtra("message", trackingmessage.getMessage());

                            if (trackingmessage.getMessage() != null)
                                message = trackingmessage.getMessage();

                            final PendingIntent trackingIp = PendingIntent.getActivity(getBaseContext(), 0,
                                    trackingI, PendingIntent.FLAG_UPDATE_CURRENT);
                            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                            Notification.Builder builder = notificationHelper.getUberNotification("Tracking Notification",
                                    message, trackingIp, sound);

                            Notification notification = builder.build();
                            notificationHelper.getManager().notify(1, notification);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

*/
    /*   FirebaseDatabase.getInstance().getReference().child("trackingNotification")
                    .child(pref.getString("Id", "")).child("Notification").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        bid1 = dataSnapshot.getValue(Trackingmessage.class);

                        if (dataSnapshot != null) {
                            Intent trackingI = new Intent(getBaseContext(), Onthewayandimreached.class);
                            trackingI.putExtra("BookingIdT", bid1.getBookingid());
                            trackingI.putExtra("message", bid1.getMessage());
                            final PendingIntent trackingIp = PendingIntent.getActivity(getBaseContext(), 0,
                                    trackingI, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                            builder.setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                    .setWhen(System.currentTimeMillis()).
                                    setSmallIcon(R.drawable.camera)
                                    .setContentTitle("Tracking Notification")
                                    .setContentText(bid1.getMessage())
                                    .setContentIntent(trackingIp);

                            NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(1, builder.build());


                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
*/
    /* ComponentName componentName = new ComponentName(this, com.Shootmyshow.praful.shootmyshow.JobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(700, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresCharging(false);
        builder.setPersisted(true);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = builder.build();
        int result = jobScheduler.schedule(jobInfo);
        if (result == jobScheduler.RESULT_SUCCESS) {
            Toast.makeText(this, "yesss!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Noo", Toast.LENGTH_SHORT).show();
        }*/




 /*  if (serviceRunning == false) {
            serviceRunning = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent notificationIntent = new Intent(this, MainActivity.class);
                    final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    ;
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification.Builder builder = notificationHelper.getUberNotification("SHOOT MY SHOW", "This is for reminding you about booking", pendingIntent, sound);

                    Notification notification = builder.build();
                    notificationHelper.getManager().notify(1, notification);

                    startForeground(1337, notification);
                } else
                    showNotif();

        }
*/
 /* Intent i = new Intent(this, MyJobIntentService.class);
        MyJobIntentService.enqueueWork(this,i);
*/