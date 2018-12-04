package com.Shootmyshow.praful.shootmyshow;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 * This is a simple job to take a number and toast that number of random numbers, one every 10 seconds.
 */
public class MyJobIntentService extends JobIntentService {
    String bid;
    int x = 0;
    final Handler mHandler = new Handler();


    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;
    final String TAG = "MyJobIntenetService";

    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobIntentService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    protected void onHandleWork(Intent intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);


        FirebaseDatabase.getInstance().goOnline();


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


        }
        toast("started");


        for (int i = 2; i > 1; i++) {
            Log.wtf(TAG, "Random number is " + i);

        }


    }

    @Override
    public void onDestroy() {
       /* Intent i = new Intent(this, MyJobIntentService.class);
        MyJobIntentService.enqueueWork(this,i);
       */
        toast("over");
        super.onDestroy();
    }


    // Helper for showing tests
    void toast(final CharSequence text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyJobIntentService.this, text, Toast.LENGTH_SHORT).show();
            }
        });

    }

}