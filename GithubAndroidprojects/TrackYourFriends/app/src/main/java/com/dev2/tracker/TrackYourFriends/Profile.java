package com.dev2.tracker.TrackYourFriends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final CircleImageView face = (CircleImageView) findViewById(R.id.face);
        final TextView username = (TextView) findViewById(R.id.username);
        final TextView about = (TextView) findViewById(R.id.about);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

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
        id = pref.getString("id", "");
        TextView myid = (TextView) findViewById(R.id.id);
        if (id != null) {
            myid.setText("Your ID : " + id);
            FirebaseDatabase.getInstance().getReference()
                    .child("Signedusers")
                    .child(id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Userprofile userprofile = dataSnapshot.getValue(Userprofile.class);
                                if (userprofile != null) {
                                    if (!userprofile.getFace().toString().equals("")) {
                                        Uri uri = Uri.parse(userprofile.getFace());
                                        Picasso.with(Profile.this).load(uri).into(face);
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    if (userprofile.getAbout().toString().equals("")) {
                                        about.setText(" ----Please write about yourself something by editing----");
                                    } else {
                                        about.setText(userprofile.getAbout());
                                    }
                                    username.setText(userprofile.getUsername());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
            }
        });
    }
}
