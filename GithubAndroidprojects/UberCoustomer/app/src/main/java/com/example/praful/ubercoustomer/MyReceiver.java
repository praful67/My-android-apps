package com.example.praful.ubercoustomer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.praful.ubercoustomer.Service.MyFirebaseIdService;
import com.example.praful.ubercoustomer.Service.MyFirebaseMessaging;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, Myservice.class));
    }
}
