package com.example.praful.blogretrive;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "Tip";
    public static  final int REQUEST_CODE = 1234;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdRequest adRequest = new AdRequest.Builder().build();

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


        bt = (Button) findViewById(R.id.button3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , ImageListActivity.class);
                startActivity(i);
            }
        });

    }


}
