package com.dev.praful.admintracker;

import android.content.Intent;
import android.provider.Settings;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeesTimeoff extends AppCompatActivity {

    ListView listView;
    List<Timeoff> list = new ArrayList<>();
    Timeffadapter timeffadapter;

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
        setContentView(R.layout.activity_employees_timeoff);
        Button seepast = (Button) findViewById(R.id.seepast);
        seepast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeesTimeoff.this, PastTimeoffs.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.list);
        getWindow().getAttributes().windowAnimations = R.style.Style;
        FirebaseDatabase.getInstance().getReference()
                .child("Employee'sTimeoffs1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (list.size() > 0)
                            list.clear();
                        if (dataSnapshot != null){

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (!dataSnapshot1.getKey().equals("number"))
                                    {
                                        Timeoff timeoff = dataSnapshot1.getValue(Timeoff.class);
                                        list.add(timeoff);
                                    }
                            }

                        }
                        timeffadapter = new Timeffadapter(list, EmployeesTimeoff.this);
                        TextView t = (TextView) findViewById(R.id.t);

                        listView.setEmptyView(t);

                        listView.setAdapter(timeffadapter);

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
                if (newText != null) {
                    if (TextUtils.isEmpty(newText)) {
                        timeffadapter.filter("");
                        listView.clearTextFilter();
                    } else {
                        timeffadapter.filter(newText);
                    }
                }
                return true;
            }
        });
    }
}
