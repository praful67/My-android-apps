package com.Shootmyshow.praful.shootmyshow;

import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Listofavailablecompanies extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listdata;
    List<String> ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofavailablecompanies);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getWindow().getAttributes().windowAnimations = R.style.Dialogslide2;
        listdata = (ListView) findViewById(R.id.listofavailablecompanies);

        addEventFirebaselistener();
    }

    private void addEventFirebaselistener() {

        databaseReference.child("Availablecompanies").child(Common.userid).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (ids.size() > 0)
                    ids.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String id = dataSnapshot1.getKey();
                    ids.add(id);
                }
                ListAdapterofAvailablecompanies adapter = new ListAdapterofAvailablecompanies(Listofavailablecompanies.this, ids);
                adapter.notifyDataSetChanged();

                TextView t = (TextView) findViewById(R.id.t);

                listdata.setEmptyView(t);
                listdata.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
