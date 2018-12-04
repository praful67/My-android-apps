package com.Shootmyshow.praful.shootmyshow;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Howtouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtouse);
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdView adView1 = (AdView) findViewById(R.id.ad_banner1);
        AdView adView2 = (AdView) findViewById(R.id.ad_banner2);
        AdView adView3 = (AdView) findViewById(R.id.ad_banner3);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);
        adView2.loadAd(adRequest);
        adView3.loadAd(adRequest);
    }
}
