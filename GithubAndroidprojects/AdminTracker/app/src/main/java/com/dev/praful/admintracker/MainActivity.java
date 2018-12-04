package com.dev.praful.admintracker;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.Fabric;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);
        final SpotsDialog waitingDailog = new SpotsDialog(this);
        waitingDailog.show();
        waitingDailog.setMessage("Please wait..");
        waitingDailog.setCancelable(false);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ComponentName serviceName = new ComponentName(this, MyService.class);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(startMySerivceJobInfo);



        //  startService(new Intent(MainActivity.this, MyService.class));

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!MainActivity.this.isFinishing())
                    waitingDailog.dismiss();

                Intent intent = new Intent(MainActivity.this, Maps.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}