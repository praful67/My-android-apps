package com.dev.praful.admintracker;

import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

public class Driverslist extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listdata;
    Driverslistadapter adapter;
    List<DriversInfo> driversInfoList = new ArrayList<>();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverslist);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        getWindow().getAttributes().windowAnimations = R.style.Style;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listdata = (ListView) findViewById(R.id.driverslist);

        addEventFirebaselistener();

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null && newText != null) {

                    if (TextUtils.isEmpty(newText)) {

                        adapter.filter("");
                        listdata.clearTextFilter();
                    } else {
                        adapter.filter(newText);
                    }
                }
                return true;
            }

        });
    }

    private void addEventFirebaselistener() {

        databaseReference.child("Drivers Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (driversInfoList.size() > 0)
                    driversInfoList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DriversInfo DriversInfo = dataSnapshot1.getValue(DriversInfo.class);
                    driversInfoList.add(DriversInfo);
                }
                adapter = new Driverslistadapter(Driverslist.this, driversInfoList);

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
