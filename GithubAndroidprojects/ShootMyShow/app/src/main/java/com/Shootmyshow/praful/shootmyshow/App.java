package com.Shootmyshow.praful.shootmyshow;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application{
    public static final String CHANNEL_ID = "prafulservices";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationchannel();
    }

    private void createNotificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID , "prafulservice", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }

    }
}
