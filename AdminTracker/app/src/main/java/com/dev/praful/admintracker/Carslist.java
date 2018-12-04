package com.dev.praful.admintracker;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Carslist extends AppCompatActivity {

    List<CarsInfo> carsInfos = new ArrayList<>();
    ListView listView;
    Carslistadapter carslistadapter;
    Toolbar toolbar;

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
        setContentView(R.layout.activity_carslist);
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcaradddialog();
            }
        });
        listView = (ListView) findViewById(R.id.carslist);
        getWindow().getAttributes().windowAnimations = R.style.Style;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final String listid;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listid = sharedPreferences.getString("rostersetid", "");
        FirebaseDatabase.getInstance().getReference()
                .child("Cars Info")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (carsInfos.size() > 0)
                            carsInfos.clear();

                        if (dataSnapshot != null) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                CarsInfo carsInfo1 = dataSnapshot1.getValue(CarsInfo.class);

                                if (carsInfo1 != null && carsInfo1.getListid() != null) {
                                    if (carsInfo1.getListid().equals(listid)) {
                                        carsInfos.add(carsInfo1);

                                    }

                                }
                            }
                        }

                        TextView t = (TextView) findViewById(R.id.t);

                        listView.setEmptyView(t);

                        carslistadapter = new Carslistadapter(carsInfos, Carslist.this);
                        listView.setAdapter(carslistadapter);

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


    private void showcaradddialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.addcardialog, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText carname = (EditText) view.findViewById(R.id.carname);
        Button add = (Button) view.findViewById(R.id.add);
        final String listid = getIntent().getStringExtra("rostersetid");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarsInfo carsInfo = new CarsInfo(carname.getText().toString(), UUID.randomUUID().toString(), "", listid);
                FirebaseDatabase.getInstance().getReference()
                        .child("Cars Info")
                        .child(carsInfo.getId().toString())
                        .setValue(carsInfo);
                alertDialog.dismiss();

            }
        });

        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }


}
