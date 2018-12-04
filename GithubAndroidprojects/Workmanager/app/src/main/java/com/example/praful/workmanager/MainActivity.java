package com.example.praful.workmanager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().getReference()
                .child("JobService")
                .child("test")
                .setValue("Test me");
       /* OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(Myworker.class)
                .build();
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

     */
        ComponentName serviceName = new ComponentName(this, MyService.class);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        jobScheduler.schedule(startMySerivceJobInfo);
        Intent intent = new Intent();
        String packageName = getPackageName();
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }
}
