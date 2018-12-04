package com.dev.praful.admintracker;

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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

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

    @Override
    public void onCreate() {
        super.onCreate();
        final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());

        FirebaseDatabase.getInstance().getReference()
                .child("Profilechanged")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1 != null) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                if (map != null) {
                                    if (String.valueOf(map.get("changed")).equals("yes")) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            Intent notificationIntent = new Intent(getBaseContext(), Employeedetailspage.class);
                                            notificationIntent.putExtra("employeeid", dataSnapshot1.getKey());
                                            final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            ;
                                            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            Notification.Builder builder = notificationHelper.getUberNotification("RYANTransport", "A Employee has edited his/her profile", pendingIntent, sound);

                                            builder.setOnlyAlertOnce(true);
                                            final Notification notification = builder.build();
                                            notificationHelper.getManager().notify(1, notification);

                                        } else {
                                            Intent notificationIntent = new Intent(getBaseContext(), Employeedetailspage.class);
                                            notificationIntent.putExtra("employeeid", dataSnapshot1.getKey());
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                    notificationIntent, 0);
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                            builder.setAutoCancel(true)
                                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                    .setWhen(System.currentTimeMillis())
                                                    .setOnlyAlertOnce(true)
                                                    .setSmallIcon(R.drawable.car)
                                                    .setContentTitle("RYANTransport")
                                                    .setContentText("A Employee has edited his/her profile")
                                                    .setContentIntent(pendingIntent);

                                            NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                            manager.notify(1, builder.build());
                                        }
                                        FirebaseDatabase.getInstance().getReference().child("Profilechanged")
                                                .child(dataSnapshot1.getKey())
                                                .child("changed")
                                                .setValue("No");
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("ETimeoff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1 != null) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                if (map != null) {
                                    if (String.valueOf(map.get("Updated")).equals("yes")) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            Intent notificationIntent = new Intent(getBaseContext(), EmployeesTimeoff.class);
                                            final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            ;
                                            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            Notification.Builder builder = notificationHelper.getUberNotification("RYANTransport", "A Employee has updated his/her Timeoff", pendingIntent, sound);

                                            builder.setOnlyAlertOnce(true);
                                            final Notification notification = builder.build();
                                            notificationHelper.getManager().notify(1, notification);

                                        } else {
                                            Intent notificationIntent = new Intent(getBaseContext(), EmployeesTimeoff.class);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                    notificationIntent, 0);
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                            builder.setAutoCancel(true)
                                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                    .setWhen(System.currentTimeMillis()).
                                                    setSmallIcon(R.drawable.car)
                                                    .setContentTitle("RYANTransport")
                                                    .setOnlyAlertOnce(true)
                                                    .setContentText("A Employee has updated his/her Timeoff")
                                                    .setContentIntent(pendingIntent);

                                            NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                            manager.notify(1, builder.build());
                                        }
                                        FirebaseDatabase.getInstance().getReference().child("ETimeoff")
                                                .child(dataSnapshot1.getKey())
                                                .child("Updated")
                                                .setValue("No");
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("EComment")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1 != null) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                if (map != null) {
                                    if (String.valueOf(map.get("Updated")).equals("yes")) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            Intent notificationIntent = new Intent(getBaseContext(), Employeescomment.class);
                                            final PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            ;
                                            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            Notification.Builder builder = notificationHelper.getUberNotification("RYANTransport", "A Employee has commented", pendingIntent, sound);

                                            builder.setOnlyAlertOnce(true);
                                            final Notification notification = builder.build();
                                            notificationHelper.getManager().notify(1, notification);

                                        } else {
                                            Intent notificationIntent = new Intent(getBaseContext(), Employeescomment.class);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                                    notificationIntent, 0);
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                            builder.setAutoCancel(true)
                                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                    .setWhen(System.currentTimeMillis()).
                                                    setSmallIcon(R.drawable.car)
                                                    .setContentTitle("RYANTransport")
                                                    .setOnlyAlertOnce(true)
                                                    .setContentText("A Employee has commented")
                                                    .setContentIntent(pendingIntent);

                                            NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                            manager.notify(1, builder.build());
                                        }
                                        FirebaseDatabase.getInstance().getReference().child("EComment")
                                                .child(dataSnapshot1.getKey())
                                                .child("Updated")
                                                .setValue("No");
                                    }
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
            Notification.Builder builder = notificationHelper.getUberNotification("RYANTransport", "This is for receivig notifications", pendingIntent, sound);

            final Notification notification = builder.build();
            //notificationHelper.getManager().notify(1, notification);
            FirebaseDatabase.getInstance().getReference()
                    .child("Adminforegroundserivces")
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
    }

    /*  @Override
      public int onStartCommand(Intent intent, int flags, int startId) {

          super.onStartCommand(intent, flags, startId);

          return START_STICKY;
      }
  */
    @Override
    public void onDestroy() {

      /*  if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {

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
/*

    @Override
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
                .setContentText("This is for receivig notifications")
                .setContentIntent(pendingIntent);

        final Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // manager.notify(1, builder.build());
        FirebaseDatabase.getInstance().getReference()
                .child("Adminforegroundserivces")
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
