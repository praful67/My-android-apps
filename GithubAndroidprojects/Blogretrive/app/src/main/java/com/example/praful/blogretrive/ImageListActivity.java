package com.example.praful.blogretrive;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class    ImageListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseref;
    private List<ImageUpload> imgList;
    private ListView lv;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final InterstitialAd interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(getString(R.string.ad_mob1));
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
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


        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewImage);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait , loading Tip posts...");
        progressDialog.show();

        mDatabaseref = FirebaseDatabase.getInstance().getReference(MainActivity.FB_DATABASE_PATH);

        mDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }

                adapter = new ImageListAdapter(ImageListActivity.this , R.layout.image_item , imgList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();


            }
        });


    }
}
