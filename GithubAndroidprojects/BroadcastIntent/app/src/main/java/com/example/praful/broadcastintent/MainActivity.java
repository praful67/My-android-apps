package com.example.praful.broadcastintent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String text1;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText text = (EditText) findViewById(R.id.text);
        Button submit = (Button) findViewById(R.id.submit);
        broadcastReceiver = new MyReceiver();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1 = text.getText().toString();
               // broadcast(v);
                sendBroadcastCompat(getBaseContext() , );
            }
        });
    }

    public static void sendBroadcastCompat(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            context.sendBroadcast(intent);
            return;
        }

        Intent broadcastIntent = new Intent(intent);
        PackageManager pm = context.getPackageManager();

        List<ResolveInfo> broadcastReceivers  = pm.queryBroadcastReceivers(broadcastIntent, 0);
        for(ResolveInfo info : broadcastReceivers) {
            broadcastIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
            context.sendBroadcast(broadcastIntent);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.broadcastreceiver");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void broadcast(View view) {
        Intent intent = new Intent();
        intent.setAction("com.example.praful.broadcastintent");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("message", text1);
        sendBroadcast(intent);

    }
}
