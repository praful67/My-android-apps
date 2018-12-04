package com.dev2.tracker.TrackYourFriends;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;

import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");


        final String id = pref.getString("id", "");
        if (id.equals("")) {
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
            finish();
        } else {
            progressDialog.show();
            FirebaseDatabase.getInstance().getReference()
                    .child("Signedusers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(id).exists()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, Welcome.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                pref.getAll().clear();
                                Intent intent = new Intent(MainActivity.this, SignUp.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }

    }


}
