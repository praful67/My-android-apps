package com.example.praful.presencesystem;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
