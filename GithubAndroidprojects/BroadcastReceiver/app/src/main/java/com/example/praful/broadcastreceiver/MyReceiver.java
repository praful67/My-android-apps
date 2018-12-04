package com.example.praful.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String messsage = intent.getStringExtra("message");
        Toast.makeText(context , action + " " +messsage ,Toast.LENGTH_LONG).show();

         }
}
