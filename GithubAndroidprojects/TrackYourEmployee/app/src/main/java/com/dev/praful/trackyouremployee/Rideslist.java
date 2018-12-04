package com.dev.praful.trackyouremployee;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rideslist extends AppCompatActivity {

    List<CarsInfo> carsInfos = new ArrayList<>();
    ListView listView;
    Carslistadapter carslistadapter;

    String id, state;


    android.support.v7.widget.Toolbar mToolbar;

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
        setContentView(R.layout.activity_rideslist);
        listView = (ListView) findViewById(R.id.carslist);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        state = getIntent().getStringExtra("state");

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child("Driver's Car")
                .child(state)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (carsInfos.size() > 0)
                            carsInfos.clear();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()) {
                                if (dataSnapshot11.getKey().equals(id)) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Cars Info")
                                            .child(dataSnapshot1.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    CarsInfo carsInfo1 = dataSnapshot.getValue(CarsInfo.class);
                                                    if (carsInfo1 != null)
                                                        carsInfos.add(carsInfo1);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                }
                            }
                        }
                        new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                carslistadapter = new Carslistadapter(carsInfos, Rideslist.this);
                                listView.setAdapter(carslistadapter);
                                TextView t = (TextView) findViewById(R.id.t);

                                listView.setEmptyView(t);

                                progressDialog.dismiss();

                            }
                        }.start();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (carslistadapter != null && newText != null) {
                    if (TextUtils.isEmpty(newText)) {
                        carslistadapter.filter("");
                        listView.clearTextFilter();
                    } else {
                        carslistadapter.filter(newText);
                    }
                }
                return true;
            }
        });
    }
}
