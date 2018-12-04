package com.dev2.tracker.TrackYourFriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendProfile extends AppCompatActivity {

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        final CircleImageView face = (CircleImageView) findViewById(R.id.face);
        final TextView username = (TextView) findViewById(R.id.username);
        final TextView about = (TextView) findViewById(R.id.about);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        TextView myid = (TextView) findViewById(R.id.id);
        if (getIntent().getStringExtra("userid") != null) {
            userid = getIntent().getStringExtra("userid");
            myid.setText("User ID : " + userid);
            FirebaseDatabase.getInstance().getReference()
                    .child("Signedusers")
                    .child(userid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Userprofile userprofile = dataSnapshot.getValue(Userprofile.class);
                                if (userprofile != null) {
                                    if (!userprofile.getFace().toString().equals("")) {
                                        Uri uri = Uri.parse(userprofile.getFace());
                                        Picasso.with(FriendProfile.this).load(uri).into(face);
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    if (userprofile.getAbout().toString().equals("")) {
                                        about.setText(" ---Not updated----");
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
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait..");
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userid != null) {
                    progressDialog.show();
                    String id = pref.getString("id", "");
                    FirebaseDatabase.getInstance().getReference()
                            .child("Savedusers")
                            .child(id)
                            .child(userid)
                            .child("id")
                            .setValue(userid)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    progressDialog.dismiss();
                                    Toast.makeText(FriendProfile.this, "Saved!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(FriendProfile.this, "Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                }
            }
        });
        Button livetrack = (Button) findViewById(R.id.LiveTrack);
        livetrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userid != null) {
                    Intent intent = new Intent(FriendProfile.this , LiveTrack.class);
                    intent.putExtra("userid" , userid);
                    startActivity(intent);
                }
            }
        });

    }
}
