package com.dev.praful.admintracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Employeeslist extends AppCompatActivity {


    List<Orderedemployeedetails> orderedemployeedetails = new ArrayList<>();
    ListView employeeslist;
    double distance = 10000;
    GeoFire geoFire;
    Employeeslistadapter adapter;

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
        setContentView(R.layout.activity_employeeslist);
        employeeslist = (ListView) findViewById(R.id.employeeslist);
        updatelist();
        getWindow().getAttributes().windowAnimations = R.style.Style;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                        employeeslist.clearTextFilter();
                    } else {
                        adapter.filter(newText);
                    }
                }
                return true;
            }

        });
      /*  FloatingActionButton refresh = (FloatingActionButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Employeeslist.this, Employeeslist.class);
                startActivity(intent);
                finish();
            }
        });*/
    }


    private void updatelist() {
        FirebaseDatabase.getInstance().getReference()
                .child("Final Ordered Employees").orderByChild("distance1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (orderedemployeedetails.size() > 0)
                            orderedemployeedetails.clear();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Orderedemployeedetails orderedemployeedetails1 = dataSnapshot1.getValue(Orderedemployeedetails.class);

                            orderedemployeedetails.add(orderedemployeedetails1);
                        }

                        adapter = new Employeeslistadapter(Employeeslist.this, orderedemployeedetails);

                        TextView t = (TextView) findViewById(R.id.t);

                        employeeslist.setEmptyView(t);

                        employeeslist.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}
