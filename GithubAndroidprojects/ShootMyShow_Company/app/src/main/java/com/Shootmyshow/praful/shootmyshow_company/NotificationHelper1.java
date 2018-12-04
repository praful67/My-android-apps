package com.Shootmyshow.praful.shootmyshow_company;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;


public class NotificationHelper1 extends ContextWrapper {
    private static final String UBER_CHANNEL_ID = "com.example.praful.shootmyshow_company";
    private static final String UBER_CHANNEL_NAME = "ShootMyShow";

    private NotificationManager manager;

    public NotificationHelper1(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            creatchannels();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void creatchannels() {

        NotificationChannel notificationChannel = new NotificationChannel(UBER_CHANNEL_ID, UBER_CHANNEL_NAME
                , NotificationManager.IMPORTANCE_DEFAULT);

        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getUberNotification(String title, String content, PendingIntent contentIntent, Uri soundUri) {
        return new Notification.Builder(getApplicationContext(), UBER_CHANNEL_ID
        )       .setContentText(content)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.camera);
    }
}
