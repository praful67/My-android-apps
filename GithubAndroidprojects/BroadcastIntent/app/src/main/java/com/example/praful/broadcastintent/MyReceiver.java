package com.example.praful.broadcastintent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String messsage = intent.getStringExtra("message");
        Toast.makeText(context , action + " " +messsage ,Toast.LENGTH_SHORT).show();

    }
}
