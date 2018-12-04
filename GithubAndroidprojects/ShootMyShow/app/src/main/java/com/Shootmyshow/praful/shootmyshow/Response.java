package com.Shootmyshow.praful.shootmyshow;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Response extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageView cancelimg = (ImageView)findViewById(R.id.cancelimg);
        ImageView acceptimg = (ImageView)findViewById(R.id.acceptimg);
        final Button ok = (Button) findViewById(R.id.ok);
        TextView message = (TextView) findViewById(R.id.response);
        if (getIntent().getStringExtra("response") != null) {
            message.setText(getIntent().getStringExtra("response") + "Thank you .");
            if (message.getText().toString().contains("ACCEPTED"))
            {
                acceptimg.setVisibility(View.VISIBLE);
                cancelimg.setVisibility(View.GONE);
            }
            else if (message.getText().toString().contains("DECLINED"))
            {
                cancelimg.setVisibility(View.VISIBLE);
                acceptimg.setVisibility(View.GONE);
            }
        }

        FirebaseDatabase.getInstance().getReference().child("Responses")
                .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                {
                    finish();
                    Toast.makeText(Response.this, "You have responded", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                FirebaseDatabase.getInstance().getReference()
                        .child("Responses")
                        .child(pref.getString("Id", "")).removeValue();

            }
        });
    }
}
