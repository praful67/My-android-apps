package com.example.praful.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter =  new IntentFilter("com.example.praful.broadcastreceiver");
        registerReceiver(broadcastReceiver ,intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broadcastReceiver = new MyReceiver();

    }
}
