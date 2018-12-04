package com.example.praful.demo;

import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final InterstitialAd interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(getString(R.string.ad_mob1));
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Toast.makeText(MainActivity.this , "Ad closed" , Toast.LENGTH_SHORT).show();
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Toast.makeText(MainActivity.this , "Ad failed" , Toast.LENGTH_SHORT).show();

                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(MainActivity.this , "Ad left" , Toast.LENGTH_SHORT).show();

                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(MainActivity.this , "Ad opnend" , Toast.LENGTH_SHORT).show();

                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                interstitialAd.show();
                Toast.makeText(MainActivity.this , "Ad loaded" , Toast.LENGTH_SHORT).show();

                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                Toast.makeText(MainActivity.this , "Ad clicked" , Toast.LENGTH_SHORT).show();

                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                Toast.makeText(MainActivity.this , "Ad Impreesion" , Toast.LENGTH_SHORT).show();

                super.onAdImpression();
            }
        });
    }
}
