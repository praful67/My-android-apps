package com.example.praful.fcmoreo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

public class NotificationHelper extends ContextWrapper {

    private  static final String CHANNEL_ID= "com.example.praful.fcm.PRAFUL";
    private  static final String CHANNEL_NAME= "PRAFUL Channel";
    private NotificationManager manager;
    public NotificationHelper(Context base) {
        super(base);
        createchannel();
    }

    private void createchannel() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID , CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableVibration(true);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);

    }

    public NotificationManager getManager() {
        if (manager == null )
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        return manager;
    }

    public Notification.Builder getchannelNoti(String title , String body){

        return  new Notification.Builder(getApplicationContext() , CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);
    }

}
