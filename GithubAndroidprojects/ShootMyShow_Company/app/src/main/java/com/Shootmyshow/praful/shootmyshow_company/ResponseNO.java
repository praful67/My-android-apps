
package com.Shootmyshow.praful.shootmyshow_company;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResponseNO extends AppCompatActivity {

    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_no);
        done = (Button) findViewById(R.id.Done12);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        FirebaseDatabase.getInstance().getReference().child("NO")
                .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null)
                {
                    finish();
                    Toast.makeText(ResponseNO.this, "You have responded", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                FirebaseDatabase.getInstance().getReference()
                        .child("NO")
                        .child(pref.getString("Id", ""))
                        .removeValue();
            }
        });
    }
}
