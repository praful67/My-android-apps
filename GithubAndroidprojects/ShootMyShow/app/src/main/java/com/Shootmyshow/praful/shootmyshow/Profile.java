package com.Shootmyshow.praful.shootmyshow;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.flaviofaria.kenburnsview.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;

public class  Profile extends AppCompatActivity {
    CircleImageView myphoto;
    boolean moving = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView myphone = (TextView) findViewById(R.id.myPhone);
        TextView myname = (TextView) findViewById(R.id.myName);
        final TextView myId = (TextView) findViewById(R.id.myId);
        myphoto = (CircleImageView) findViewById(R.id.myphoto);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
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
            }
        }.start();


        myname.setText(Common.currentUser.getName());
        myId.setText(Common.userid.toString());
        myphone.setText(Common.currentUser.getPhone());
        final KenBurnsView kenBurnsView = (KenBurnsView) findViewById(R.id.KBV);

        if (Common.currentUser.getAvatarUrl() != null && !TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
            Picasso.with(this).load(Common.currentUser.getAvatarUrl()).into(myphoto);
            Picasso.with(this).load(Common.currentUser.getAvatarUrl()).into(kenBurnsView);
        }
        getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);


        AccelerateDecelerateInterpolator a = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(3000, a);
        kenBurnsView.setTransitionGenerator(generator);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Profile.super.onBackPressed();
            }
        });


    }
}
