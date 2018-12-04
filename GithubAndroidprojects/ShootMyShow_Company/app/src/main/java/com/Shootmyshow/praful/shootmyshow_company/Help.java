package com.Shootmyshow.praful.shootmyshow_company;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);
        adView2.loadAd(adRequest);
    }
}
