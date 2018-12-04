package com.dev2.tracker.TrackYourFriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LiveT extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_t);
        Button find = (Button) findViewById(R.id.findF);
        final EditText f = (EditText) findViewById(R.id.idF);
        final ProgressDialog progressDialog = new ProgressDialog(this);
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
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(f.getText().toString())) {
                    progressDialog.setMessage("Please wait..");
                    progressDialog.show();

                    String s = f.getText().toString();
                    FirebaseDatabase.getInstance().getReference()
                            .child("Signedusers")
                            .child(s)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                        if (map != null) {

                                            Intent intent = new Intent(LiveT.this, LiveTrack.class);
                                            progressDialog.dismiss();
                                            intent.putExtra("userid", String.valueOf(map.get("id")));
                                            startActivity(intent);

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(LiveT.this, "invalid ID", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LiveT.this, "invalid ID", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });


                } else {
                    Toast.makeText(LiveT.this, "Please fill ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
