package com.example.praful.ubercoustomer;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Helper.NotificationHelper;
import com.example.praful.ubercoustomer.Model.DataMessage;
import com.example.praful.ubercoustomer.Model.FCMResponse;
import com.example.praful.ubercoustomer.Model.Token;
import com.example.praful.ubercoustomer.Model.Trackingmessage;
import com.example.praful.ubercoustomer.Remote.FCMClient;
import com.example.praful.ubercoustomer.Remote.IFCMSerives;
import com.example.praful.ubercoustomer.Service.MyFirebaseMessaging;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.praful.ubercoustomer.App.CHANNEL_ID;


public class Myservice extends Service {

    public Myservice() {
    }

    Trackingmessage bid1;
    public static Boolean serviceRunning = false;
    String bid;
    Trackingmessage trackingmessage;

    @Override
    public void onCreate() {


        BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.ubercoustomer");
        registerReceiver(broadcastReceiver, intentFilter);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());


            FirebaseDatabase.getInstance().getReference().child("cancelBooking")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        bid = dataSnapshot1.getValue().toString();

                    Intent cancelI = new Intent(getBaseContext(), Home.class);
                    cancelI.putExtra("type", "cancel");
                    cancelI.putExtra("BID", bid);
                    final PendingIntent cancelIp = PendingIntent.getActivity(getBaseContext(), 0,
                            cancelI, PendingIntent.FLAG_UPDATE_CURRENT);
                    ;
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    Notification.Builder builder = notificationHelper.getUberNotification("BOOKING CANCELLED",
                            "Please check your cancelled bookings", cancelIp, sound);

                    Notification notification = builder.build();
                    notificationHelper.getManager().notify(1, notification);


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

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        bid = dataSnapshot1.getValue().toString();


                    Intent verifyingI = new Intent(getBaseContext(), Home.class);
                    verifyingI.putExtra("type", "verify");
                    verifyingI.putExtra("BID", bid);
                    final PendingIntent verifyingIp = PendingIntent.getActivity(getBaseContext(), 0,
                            verifyingI, PendingIntent.FLAG_UPDATE_CURRENT);
                    ;
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    Notification.Builder builder = notificationHelper.getUberNotification("VERIFY YOUR BOOKING",
                            "Company wants to complete this booking . Please verify it.", verifyingIp, sound);

                    Notification notification = builder.build();
                    notification.sound = Uri.parse("android.resource://"
                            + getBaseContext().getPackageName() + "/" + R.raw.s);
                    notificationHelper.getManager().notify(1, notification);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            FirebaseDatabase.getInstance().getReference().child("trackingNotification")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        trackingmessage = dataSnapshot.getValue(Trackingmessage.class);

                    Intent trackingI = new Intent(getBaseContext(), Home.class);
                    trackingI.putExtra("type", "tracking");
                    trackingI.putExtra("BID", trackingmessage.getBookingid());
                    trackingI.putExtra("message", trackingmessage.getMessage());

                    final PendingIntent trackingIp = PendingIntent.getActivity(getBaseContext(), 0,
                            trackingI, PendingIntent.FLAG_UPDATE_CURRENT);
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    Notification.Builder builder = notificationHelper.getUberNotification("Tracking Notification",
                            trackingmessage.getMessage(), trackingIp, sound);

                    Notification notification = builder.build();
                    notificationHelper.getManager().notify(1, notification);


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

                                Intent responseI = new Intent(getBaseContext(), Home.class);
                                responseI.putExtra("type", "response");
                                responseI.putExtra("response", response);

                                final PendingIntent responseIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        responseI, PendingIntent.FLAG_UPDATE_CURRENT);
                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                Notification.Builder builder = notificationHelper.getUberNotification("Company Response",
                                        response, responseIp, sound);

                                Notification notification = builder.build();
                                notificationHelper.getManager().notify(1, notification);

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            Intent welcomeI = new Intent(this, Home.class);
            welcomeI.putExtra("B", "i got this");
            final PendingIntent welcomeIp = PendingIntent.getActivity(this, 0,
                    welcomeI, PendingIntent.FLAG_UPDATE_CURRENT);
            ;
            FirebaseDatabase.getInstance().getReference().child("Tests")
                    .addValueEventListener(new ValueEventListener() {

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                            Notification.Builder builder = notificationHelper.getUberNotification("Welcome",
                                    "Lets start booking.", welcomeIp, sound);

                            Notification notification = builder.build();
                            notificationHelper.getManager().notify(1, notification);


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
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        bid = dataSnapshot1.getValue().toString();

                    Intent cancelI = new Intent(getBaseContext(), Home.class);
                    cancelI.putExtra("type", "cancel");
                    cancelI.putExtra("BID", bid);
                    final PendingIntent cancelIp = PendingIntent.getActivity(getBaseContext(), 0,
                            cancelI, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                    builder.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setWhen(System.currentTimeMillis()).
                            setSmallIcon(R.drawable.ic_menu_camera)
                            .setContentTitle("BOOKING CANCELLED")
                            .setContentText("Please check your cancelled bookings")
                            .setContentIntent(cancelIp);

                    NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(1, builder.build());


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
                        bid = dataSnapshot1.getValue().toString();

                        Intent verifyI = new Intent(getBaseContext(), Home.class);
                        verifyI.putExtra("type", "verify");
                        verifyI.putExtra("BID", bid);

                        final PendingIntent verifyIp = PendingIntent.getActivity(getBaseContext(), 0,
                                verifyI, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                        builder.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                .setWhen(System.currentTimeMillis()).
                                setSmallIcon(R.drawable.ic_menu_camera)
                                .setContentTitle("VERIFY YOUR BOOKING")
                                .setContentText("Company wants to complete this booking . Please verify it.")
                                .setContentIntent(verifyIp);

                        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(1, builder.build());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("trackingNotification")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        bid1 = dataSnapshot1.getValue(Trackingmessage.class);

                        Intent trackingI = new Intent(getBaseContext(), Home.class);
                        trackingI.putExtra("type", "tracking");
                        trackingI.putExtra("BID", bid1.getBookingid());
                        trackingI.putExtra("message", bid1.getMessage());
                        final PendingIntent trackingIp = PendingIntent.getActivity(getBaseContext(), 0,
                                trackingI, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                        builder.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                .setWhen(System.currentTimeMillis()).
                                setSmallIcon(R.drawable.ic_menu_camera)
                                .setContentTitle("Tracking Notification")
                                .setContentText(bid1.getMessage())
                                .setContentIntent(trackingIp);

                        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(1, builder.build());


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
                                        setSmallIcon(R.drawable.ic_menu_camera)
                                        .setContentTitle("Company Response")
                                        .setContentText(response)
                                        .setContentIntent(responseIp);

                                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(1, builder.build());


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            FirebaseDatabase.getInstance().getReference().child("Tests")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Intent welcomeI = new Intent(getBaseContext(), Home.class);

                            final PendingIntent welcomeIp = PendingIntent.getActivity(getBaseContext(), 0,
                                    welcomeI, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                            builder.setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                    .setWhen(System.currentTimeMillis()).
                                    setSmallIcon(R.drawable.ic_menu_camera)
                                    .setContentTitle("Welcome")
                                    .setContentText("Lets start booking.")
                                    .setContentIntent(welcomeIp);

                            Notification notification = builder.build();
                            NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(1, builder.build());

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
        final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());
        FirebaseDatabase.getInstance().goOnline();
        BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.ubercoustomer");
        registerReceiver(broadcastReceiver, intentFilter);


        if (serviceRunning == false) {
            serviceRunning = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent notificationIntent = new Intent(this, Home.class);
                final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                ;
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Notification.Builder builder = notificationHelper.getUberNotification("SHOOT MY SHOW", "Lets book some cool experts", pendingIntent, sound);
                Notification notification = builder.build();
                notification.sound = Uri.parse("android.resource://"
                        + getBaseContext().getPackageName() + "/" + R.raw.s);
                notificationHelper.getManager().notify(1, notification);
                startForeground(1337, notification);
            } else
                showNotif();
        }


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.s);
        startForeground(1337, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.ubercoustomer");
        registerReceiver(broadcastReceiver, intentFilter);


        serviceRunning = false;
        Intent intent = new Intent();
        intent.setAction("com.example.praful.ubercoustomer");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        sendBroadcast(intent);

    }

    private void showNotif() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis()).
                setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle("SHOOT MY SHOW")
                .setContentText("Get ready for your booking")
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

        startForeground(1337, notification);


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

        startForeground(1337, notification);


    }


}
/*

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationChannel notificationChannel = new NotificationChannel(
                "prafulservices" , "prafulservice", NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);

        Notification notification = new NotificationCompat.Builder(this, "prafulservices")
                .setContentIntent(pendingIntent)
                .setContentTitle("Welcome everyone")
                .setContentText("HEY")
                .setSmallIcon(R.drawable.camera)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

       startForeground(2 ,notification);

*/
