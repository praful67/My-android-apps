package com.example.praful.workmanager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

public class Myworker extends Worker {


    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Result doWork() {

       /* try {
            Thread.sleep(Long.MAX_VALUE);
            return Result.SUCCESS;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Result.FAILURE;
        }*/
        FirebaseDatabase.getInstance().getReference()
                .child("JobService")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
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
                                    Toast.makeText(getApplicationContext(), String.valueOf(map.get("test")), Toast.LENGTH_SHORT).show();
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

                                    setResult(Result.SUCCESS);


                                }


                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return getResult();


    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public ListenableFuture<Pair<Result, Data>> onStartWork() {
        FirebaseDatabase.getInstance().getReference()
                .child("JobService")
                .addValueEventListener(new ValueEventListener() {
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
                                    Toast.makeText(getApplicationContext(), String.valueOf(map.get("test")), Toast.LENGTH_SHORT).show();
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

        return super.onStartWork();
    }

    @Override
    public void onStopped(boolean cancelled) {
        FirebaseDatabase.getInstance().getReference()
                .child("Destroyedbyphone")
                .child(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + " " + Calendar.getInstance().get(Calendar.MINUTE)))
                .setValue(123);
       /* FirebaseDatabase.getInstance().getReference()
                .child("Destroyedbyemulator")
                .child(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + " " + Calendar.getInstance().get(Calendar.MINUTE)))
                .setValue(123);
       */ OneTimeWorkRequest OneTimeWorkRequest = new OneTimeWorkRequest.Builder(Worker.class
        )
                .build();
        WorkManager.getInstance().enqueue(OneTimeWorkRequest);

        super.onStopped(cancelled);
    }

}
