package com.example.praful.workmanager;

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
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MyService extends JobService {
    public MyService() {
    }

    @Override
    public void onCreate() {
        FirebaseDatabase.getInstance().getReference()
                .child("JobService")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        String test = String.valueOf(map.get("allow"));
                        if (test != null) {
                            if (test != null) {
                                if (test.equals("yes")) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("JobService")
                                            .child("allow")
                                            .setValue("no");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                        final NotificationHelper1 notificationHelper = new NotificationHelper1(getApplicationContext());

                                        Intent cancelI = new Intent(getApplicationContext(), Main2Activity.class);
                                        final PendingIntent cancelIp = PendingIntent.getActivity(getApplicationContext(), 0,
                                                cancelI, PendingIntent.FLAG_UPDATE_CURRENT);
                                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                        Notification.Builder builder = notificationHelper.getUberNotification("Testing",
                                                "Ok", cancelIp, sound);
                                        builder.setOnlyAlertOnce(true);
                                        Notification notification = builder.build();
                                        notificationHelper.getManager().notify(1, notification);


                                    } else {

                                        Intent cancelI = new Intent(getApplicationContext(), Main2Activity.class);
                                        final PendingIntent cancelIp = PendingIntent.getActivity(getApplicationContext(), 0,
                                                cancelI, PendingIntent.FLAG_UPDATE_CURRENT);

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                                        builder.setAutoCancel(true)
                                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                .setWhen(System.currentTimeMillis())
                                                .setOnlyAlertOnce(true)
                                                .setSmallIcon(R.drawable.camera)
                                                .setContentTitle("Testing")
                                                .setContentText("Ok")
                                                .setContentIntent(cancelIp);

                                        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                        manager.notify(1, builder.build());
                                    }
                                }

                                //   jobFinished(params, false);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        super.onCreate();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
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
}
     /*   FirebaseDatabase.getInstance().getReference()
                .child("JobService")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            if (map.get("allow") != null) {
                                if (String.valueOf(map.get("allow")).equals("yes")) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("JobService")
                                            .child("allow")
                                            .setValue("no");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                        final NotificationHelper1 notificationHelper = new NotificationHelper1(getApplicationContext());

                                        Intent cancelI = new Intent(getApplicationContext(), Main2Activity.class);
                                        final PendingIntent cancelIp = PendingIntent.getActivity(getApplicationContext(), 0,
                                                cancelI, PendingIntent.FLAG_UPDATE_CURRENT);
                                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                        Notification.Builder builder = notificationHelper.getUberNotification("Testing",
                                                "Ok", cancelIp, sound);
                                        Notification notification = builder.build();
                                        notificationHelper.getManager().notify(1, notification);


                                    } else {

                                        Intent cancelI = new Intent(getApplicationContext(), Main2Activity.class);
                                        final PendingIntent cancelIp = PendingIntent.getActivity(getApplicationContext(), 0,
                                                cancelI, PendingIntent.FLAG_UPDATE_CURRENT);

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                                        builder.setAutoCancel(true)
                                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                                .setWhen(System.currentTimeMillis()).
                                                setSmallIcon(R.drawable.camera)
                                                .setContentTitle("Testing")
                                                .setContentText("Ok")
                                                .setContentIntent(cancelIp);

                                        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                        manager.notify(1, builder.build());
                                    }
                                }

                                //   jobFinished(params, false);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
*/