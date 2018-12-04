package com.Shootmyshow.praful.shootmyshow_company;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.Shootmyshow.praful.shootmyshow_company.Model.Presentbooking;
import com.Shootmyshow.praful.shootmyshow_company.Service.MyFirebaseMessaging;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Myservices extends Service {
    public Myservices() {
    }

    public static Boolean serviceRunning = false;
    String cuscancelledBookingID;
    Presentbooking presentbooking;

    @Override

    public void onCreate() {
        super.onCreate();
        final NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());

        BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.uber");
        registerReceiver(broadcastReceiver, intentFilter);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        FirebaseDatabase.getInstance().getReference()
                .child("Studioforegroundserivces")
                .child(pref.getString("Id", ""))
                .child("want").setValue("yes");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotifAPI26(getBaseContext());
        } else
            showNotif();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            FirebaseDatabase.getInstance().getReference().child("cuscancelbooking")
                    .child(pref.getString("Id", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                cuscancelledBookingID = dataSnapshot1.getValue(String.class);

                                Intent cuscancelI = new Intent(Myservices.this, CustomercancelledtheBooking.class);
                                cuscancelI.putExtra("Id", cuscancelledBookingID);

                                PendingIntent cuscancelIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        cuscancelI, PendingIntent.FLAG_UPDATE_CURRENT);

                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                Notification.Builder builder = notificationHelper.getUberNotification("BOOKING CANCELLED",
                                        "Please check your cancelled bookings", cuscancelIp, sound);

                                Notification notification = builder.build();
                                notificationHelper.getManager().notify(1, notification);
                                Intent intent = new Intent(getBaseContext(), CustomercancelledtheBooking.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("Id", cuscancelledBookingID);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            FirebaseDatabase.getInstance().getReference()
                    .child("YES")
                    .child(pref.getString("Id", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String bookingId = dataSnapshot1.getValue(String.class);

                                Intent responseI = new Intent(Myservices.this, ResponseYES.class);
                                responseI.putExtra("BookingIdC", bookingId);

                                PendingIntent responseIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        responseI, PendingIntent.FLAG_UPDATE_CURRENT);

                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                Notification.Builder builder = notificationHelper.getUberNotification("Booking Completed",
                                        "Customer has verified and ACCEPTED your request to complete the booking", responseIp, sound);

                                Notification notification = builder.build();
                                notificationHelper.getManager().notify(1, notification);

                                Intent intent = new Intent(getBaseContext(), ResponseYES.class);
                                intent.putExtra("BookingIdC", bookingId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            FirebaseDatabase.getInstance().getReference()
                    .child("NO")
                    .child(pref.getString("Id", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String bookingId = dataSnapshot1.getValue(String.class);

                                Intent responseI = new Intent(Myservices.this, ResponseNO.class);
                                responseI.putExtra("BookingIdC", bookingId);

                                PendingIntent responseIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        responseI, PendingIntent.FLAG_UPDATE_CURRENT);

                                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                Notification.Builder builder = notificationHelper.getUberNotification("Booking Pending",
                                        "Customer has NOT verified your booking and DECLINED your request to complete the booking", responseIp, sound);

                                Notification notification = builder.build();
                                notificationHelper.getManager().notify(1, notification);
                                Intent intent = new Intent(getBaseContext(), ResponseNO.class);
                                intent.putExtra("BookingIdC", bookingId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            FirebaseDatabase.getInstance().getReference().child("PresentBookings")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Presentbooking presentbooking = dataSnapshot1.getValue(Presentbooking.class);

                        Intent PBI = new Intent(Myservices.this, CustomerCallForAdvanceBooking.class);
                        PBI.putExtra("N", "N");

                        PendingIntent PBIp = PendingIntent.getActivity(getBaseContext(), 0,
                                PBI, PendingIntent.FLAG_UPDATE_CURRENT);

                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        Notification.Builder builder = notificationHelper.getUberNotification("BOOKING REQUEST",
                                "You got a booking request from " + presentbooking.getCustomerName(), PBIp, sound);

                        Notification notification = builder.build();
                        notificationHelper.getManager().notify(1, notification);


                        Intent intent = new Intent(getBaseContext(), CustomerCallForAdvanceBooking.class);
                        intent.putExtra("N", "N");
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {

            FirebaseDatabase.getInstance().getReference().child("cuscancelbooking")
                    .child(pref.getString("Id", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                cuscancelledBookingID = dataSnapshot1.getValue(String.class);
                                Intent cuscancelI = new Intent(getBaseContext(), CustomercancelledtheBooking.class);
                                cuscancelI.putExtra("Id", cuscancelledBookingID);

                                PendingIntent cuscancelIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        cuscancelI, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                builder.setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                        .setWhen(System.currentTimeMillis()).
                                        setSmallIcon(R.drawable.camera)
                                        .setContentTitle("BOOKING CANCELLED")
                                        .setContentText("Please check your cancelled bookings")
                                        .setContentIntent(cuscancelIp);

                                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(1, builder.build());

                                Intent intent = new Intent(getBaseContext(), CustomercancelledtheBooking.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("Id", cuscancelledBookingID);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            FirebaseDatabase.getInstance().getReference()
                    .child("NO")
                    .child(pref.getString("Id", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String bookingId = dataSnapshot1.getValue(String.class);
                                Intent responseI = new Intent(Myservices.this, ResponseNO.class);
                                responseI.putExtra("BookingIdC", bookingId);

                                PendingIntent responseIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        responseI, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                builder.setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                        .setWhen(System.currentTimeMillis()).
                                        setSmallIcon(R.drawable.camera)
                                        .setContentTitle("Booking Pending")
                                        .setContentText("Customer has NOT verified your booking and DECLINED your request to complete the booking")
                                        .setContentIntent(responseIp);

                                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(1, builder.build());
                                Intent intent = new Intent(getBaseContext(), ResponseYES.class);
                                intent.putExtra("BookingIdC", bookingId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            FirebaseDatabase.getInstance().getReference()
                    .child("YES")
                    .child(pref.getString("Id", ""))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String bookingId = dataSnapshot1.getValue(String.class);
                                Intent responseI = new Intent(Myservices.this, ResponseYES.class);
                                responseI.putExtra("BookingIdC", bookingId);

                                PendingIntent responseIp = PendingIntent.getActivity(getBaseContext(), 0,
                                        responseI, PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                                builder.setAutoCancel(true)
                                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                        .setWhen(System.currentTimeMillis()).
                                        setSmallIcon(R.drawable.camera)
                                        .setContentTitle("Booking Completed")
                                        .setContentText("Customer has verified your booking and ACCEPTED your request to complete the booking")
                                        .setContentIntent(responseIp);

                                NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(1, builder.build());
                                Intent intent = new Intent(getBaseContext(), ResponseYES.class);
                                intent.putExtra("BookingIdC", bookingId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference().child("PresentBookings")
                    .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        presentbooking = dataSnapshot1.getValue(Presentbooking.class);
                        Intent PBI = new Intent(Myservices.this, CustomerCallForAdvanceBooking.class);
                        PBI.putExtra("N", "N");

                        PendingIntent PBIp = PendingIntent.getActivity(getBaseContext(), 0,
                                PBI, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
                        builder.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                .setWhen(System.currentTimeMillis()).
                                setSmallIcon(R.drawable.camera)
                                .setContentTitle("BOOKING REQUEST")
                                .setContentText("You got a booking request from " + presentbooking.getCustomerName())
                                .setContentIntent(PBIp);

                        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(1, builder.build());
                        Intent intent = new Intent(getBaseContext(), CustomerCallForAdvanceBooking.class);
                        intent.putExtra("N", "N");
                        startActivity(intent);

                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {
        FirebaseDatabase.getInstance().goOnline();
      /*  BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.uber");
        registerReceiver(broadcastReceiver, intentFilter);

        if (serviceRunning == false) {
            serviceRunning = true;

        }
*/

        return START_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotifAPI26(Context context) {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper1 notificationHelper = new NotificationHelper1(getBaseContext());
        Notification.Builder builder = notificationHelper.getUberNotification("SHOOT MY SHOW", "Get ready for your booking", pendingIntent, defaultSound);

      //  notificationHelper.getManager().notify(1, builder.build());
        final Notification notification = builder.build();
        FirebaseDatabase.getInstance().getReference()
                .child("Studioforegroundserivces")
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


    @Override
    public void onDestroy() {
        super.onDestroy();
/*
        serviceRunning = false;
        startService(new Intent(getBaseContext(), Myservices.class));
        BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.uber");
        registerReceiver(broadcastReceiver, intentFilter);


        Intent intent = new Intent();
        intent.setAction("com.example.praful.uber");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        sendBroadcast(intent);*/
    }

    private void showNotif() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis()).
                setSmallIcon(R.drawable.camera)
                .setContentTitle("SHOOT MY SHOW")
                .setContentText("Get ready for your booking")
                .setContentIntent(pendingIntent);

        final Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
      //  manager.notify(1, builder.build());

        FirebaseDatabase.getInstance().getReference()
                .child("Studioforegroundserivces")
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
