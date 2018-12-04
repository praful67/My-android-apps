package com.Shootmyshow.praful.shootmyshow_company;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResponseYES extends AppCompatActivity {

    Button done;

    TextView id, key;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String BookingIdC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_yes);
        done = (Button) findViewById(R.id.Done11);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent() != null) {
            BookingIdC = getIntent().getStringExtra("BookingIdC");

        }
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        FirebaseDatabase.getInstance().getReference().child("YES")
                .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                {
                    finish();
                    Toast.makeText(ResponseYES.this, "You have responded", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
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
                FirebaseDatabase.getInstance().getReference()
                        .child("YES")
                        .child(pref.getString("Id", ""))
                        .removeValue();
            }
        });
       /* FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Bookings").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(BookingIdC)) {
                        Bookings bookings = snapshot.getValue(Bookings.class);
                        databaseReference.child("CompletedBookings").child(userid).child(BookingIdC).setValue(bookings);
                        databaseReference.child("Bookings").child(userid).child(BookingIdC).removeValue();

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


    }
}
