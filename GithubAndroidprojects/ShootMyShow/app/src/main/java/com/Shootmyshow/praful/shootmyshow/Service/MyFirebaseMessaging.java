package com.Shootmyshow.praful.shootmyshow.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.Shootmyshow.praful.shootmyshow.AcceptedWindow;
import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.Shootmyshow.praful.shootmyshow.CompanycancelledtheBooking;
import com.Shootmyshow.praful.shootmyshow.DeclinedWindow;
import com.Shootmyshow.praful.shootmyshow.Helper.NotificationHelper;
import com.Shootmyshow.praful.shootmyshow.Onthewayandimreached;
import com.Shootmyshow.praful.shootmyshow.R;
import com.Shootmyshow.praful.shootmyshow.RateActivity;
import com.Shootmyshow.praful.shootmyshow.VerifyingCompletedBooking;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessaging extends FirebaseMessagingService {

    public MyFirebaseMessaging() {
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String companyName = data.get("CompanyName");
            String BookingIdC = data.get("BookingIdC");
            String BookingIdT = data.get("BookingIdT");
            String companyPhone = data.get("CompanyPhone");
            String companyRates = data.get("CompanyRates");
            String companyId = data.get("CompanyId");
            String Date = data.get("Date");
            String Time = data.get("Time");
            String Id = data.get("Id");
            String Address = data.get("Address");
            String Bookingid = data.get("Bookingid");
            String EventType = data.get("EventType");
            String messageCB = data.get("messageCB");
            String message = data.get("message");
            if (title != null && title.equals("Cancel")) {
                Intent intent = new Intent(MyFirebaseMessaging.this, DeclinedWindow.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Common.isCompanyFound = false;
                Common.companyId = "";
                // Toast.makeText(MyFirebaseMessaging.this, "" + message, Toast.LENGTH_SHORT).show();

            } else if (title != null && title.equals("cancelAdvanceBooking")) {
                Intent intent = new Intent(getBaseContext(), CompanycancelledtheBooking.class);
                intent.putExtra("messageCB", messageCB);
                intent.putExtra("Id", Id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

              //  startActivity(intent);

                Common.isCompanyFound = false;
                Common.companyId = "";
                // Toast.makeText(MyFirebaseMessaging.this, "" + messageCB, Toast.LENGTH_SHORT).show();


            } else if (title != null && title.equals("Accept")) {

                Intent intent = new Intent(MyFirebaseMessaging.this, AcceptedWindow.class);
                intent.putExtra("Date", Date);
                intent.putExtra("Time", Time);
                intent.putExtra("Address", Address);
                intent.putExtra("companyName", companyName);
                intent.putExtra("companyPhone", companyPhone);
                intent.putExtra("companyRates", companyRates);
                intent.putExtra("companyId", companyId);
                intent.putExtra("Bookingid", Bookingid);
                intent.putExtra("EventType", EventType);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

               // startActivity(intent);
                Common.isCompanyFound = false;
                Common.companyId = "";


//                Toast.makeText(MyFirebaseMessaging.this, "" + message, Toast.LENGTH_SHORT).show();

            } else if (title != null && title.equals("Arrived")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    showArrivedNotifAPI26(message);
                else
                    showArrivedNotif(message);


            } else if (title != null && title.equals("Completed")) {

                openRateactivity(message);
            } else if (title != null && title.equals("completedAdvancebooking")) {
                Intent intent = new Intent(MyFirebaseMessaging.this, VerifyingCompletedBooking.class);
                intent.putExtra("BookingIdC", BookingIdC);
                intent.putExtra("message", message);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //startActivity(intent);

            } else if (title != null && title.equals("Ontheway")) {
                Intent intent = new Intent(MyFirebaseMessaging.this, Onthewayandimreached.class);
                intent.putExtra("message", message);
                intent.putExtra("BookingIdT", BookingIdT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            } else if (title != null && title.equals("Reached")) {
                Intent intent = new Intent(MyFirebaseMessaging.this, Onthewayandimreached.class);
                intent.putExtra("message", message);
                intent.putExtra("BookingIdT", BookingIdT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction("com.example.praful.ubercoustomer");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("key", "F");

        sendBroadcast(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showArrivedNotifAPI26(String body) {
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0,
                new Intent(), PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getUberNotification("Arrived", body, contentIntent, defaultSound);
        notificationHelper.getManager().notify(1, builder.build());

    }

    private void openRateactivity(String body) {

        Intent intent = new Intent(this, RateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showArrivedNotif(String body) {
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0,
                new Intent(), PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis()).
                setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle("Arrived")
                .setContentText(body)
                .setContentIntent(contentIntent);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());


    }

}
