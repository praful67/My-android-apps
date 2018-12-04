package com.Shootmyshow.praful.shootmyshow;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Whentouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whentouse);

        final KenBurnsView F1 = (KenBurnsView) findViewById(R.id.F1);
        final KenBurnsView F2 = (KenBurnsView) findViewById(R.id.F2);
        final KenBurnsView F3 = (KenBurnsView) findViewById(R.id.F3);
        final KenBurnsView F4 = (KenBurnsView) findViewById(R.id.F4);
        final KenBurnsView P1 = (KenBurnsView) findViewById(R.id.p1);
        final KenBurnsView P2 = (KenBurnsView) findViewById(R.id.p2);
        final KenBurnsView P1h = (KenBurnsView) findViewById(R.id.PH1);
        final KenBurnsView P2h = (KenBurnsView) findViewById(R.id.PH2);
        final KenBurnsView P3h = (KenBurnsView) findViewById(R.id.PH3);
        final KenBurnsView P4h = (KenBurnsView) findViewById(R.id.PH4);
        final GabrielleViewFlipper F = (GabrielleViewFlipper) findViewById(R.id.VFF);
        final GabrielleViewFlipper P = (GabrielleViewFlipper) findViewById(R.id.VFP);
        final GabrielleViewFlipper PH = (GabrielleViewFlipper) findViewById(R.id.VFPH);
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdView adView1 = (AdView) findViewById(R.id.ad_banner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);

        F.setFlipInterval(5000);
        F.setInAnimation(getBaseContext(), R.anim.slide_left_to_right);
        F.setOutAnimation(getBaseContext(), R.anim.slide_right_to_left);
        F.startFlipping();

        P.setFlipInterval(5000);
        P.setInAnimation(getBaseContext(), R.anim.slide_left_to_right);
        P.setOutAnimation(getBaseContext(), R.anim.slide_right_to_left);
        P.startFlipping();

        PH.setFlipInterval(5000);
        PH.setInAnimation(getBaseContext(), R.anim.slide_left_to_right);
        PH.setOutAnimation(getBaseContext(), R.anim.slide_right_to_left);
        PH.startFlipping();

        AccelerateDecelerateInterpolator a = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000, a);

        F1.setTransitionGenerator(generator);
        F2.setTransitionGenerator(generator);
        F3.setTransitionGenerator(generator);
        F4.setTransitionGenerator(generator);
        P1.setTransitionGenerator(generator);
        P2.setTransitionGenerator(generator);
        P1h.setTransitionGenerator(generator);
        P2h.setTransitionGenerator(generator);
        P3h.setTransitionGenerator(generator);
        P4h.setTransitionGenerator(generator);


    }
}
