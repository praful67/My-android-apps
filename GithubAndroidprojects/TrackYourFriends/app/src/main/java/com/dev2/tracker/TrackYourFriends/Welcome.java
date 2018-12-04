package com.dev2.tracker.TrackYourFriends;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import dmax.dialog.SpotsDialog;

public class Welcome extends AppCompatActivity {

    static final int My_PERMISSION = 7271;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_CHECK_SETTINGS = 199;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        LinearLayout findF = (LinearLayout) findViewById(R.id.findfriends);
        LinearLayout invite = (LinearLayout) findViewById(R.id.invite);
        LinearLayout instructions = (LinearLayout) findViewById(R.id.instructions);
        LinearLayout savedF = (LinearLayout) findViewById(R.id.savefriends);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LinearLayout profile = (LinearLayout) findViewById(R.id.profile);
        LinearLayout LiveT = (LinearLayout) findViewById(R.id.livetracking);

        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        final InterstitialAd interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial));
        interstitialAd.loadAd(adRequest);
        interstitialAd.show();

        interstitialAd.setAdListener(new AdListener()

        {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                interstitialAd.show();
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, Instructions.class);
                startActivity(intent);

            }
        });
        savedF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, SavedFriends.class);
                startActivity(intent);

            }
        });
        LiveT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, LiveT.class);
                startActivity(intent);

            }
        });

        findF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, Findfriends.class);
                startActivity(intent);


            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, Profile.class);
                startActivity(intent);
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My app name");
                    String strShareMessage = "\nTrack Your Friends Now ! \n My ID is : " + pref.getString("id", "") + "\n\n";
                    strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
                    startActivity(Intent.createChooser(i, "Share via"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,


            }, My_PERMISSION);
        }
        if (googleApiClient == null) {

            googleApiClient = new GoogleApiClient.Builder(Welcome.this)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            builder.setNeedBle(true);


            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final com.google.android.gms.common.api.Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(Welcome.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });

        }
        if (!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            ComponentName serviceName = new ComponentName(this, MyService.class);
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
            jobScheduler.schedule(startMySerivceJobInfo);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)

        {

            case My_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ComponentName serviceName = new ComponentName(this, MyService.class);
                    JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                    JobInfo startMySerivceJobInfo = new JobInfo.Builder(123, serviceName)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .setPersisted(true)
                            .build();
                    jobScheduler.schedule(startMySerivceJobInfo);

                }

            }
            break;
        }
    }


}
