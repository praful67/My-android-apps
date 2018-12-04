package com.dev.praful.admintracker;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

public class ListofRostersets extends AppCompatActivity {

    List<Rostersetlistitem> rostersetlistitems = new ArrayList<>();
    ListView listView;
    ListofRosterAdapter listofRosterAdapter;
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
        setContentView(R.layout.activity_listof_rostersets);
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showrostersetitemadddailog();
            }
        });
        listView = (ListView) findViewById(R.id.rostersetslist);
        getWindow().getAttributes().windowAnimations = R.style.Style;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        FirebaseDatabase.getInstance().getReference()
                .child("List of Rosters Sets")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (rostersetlistitems.size() > 0)
                            rostersetlistitems.clear();

                        if (dataSnapshot != null) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Rostersetlistitem rostersetlistitem = dataSnapshot1.getValue(Rostersetlistitem.class);
                                rostersetlistitems.add(rostersetlistitem);
                            }
                        }


                        TextView t = (TextView) findViewById(R.id.t);

                        listView.setEmptyView(t);

                        listofRosterAdapter = new ListofRosterAdapter(rostersetlistitems, ListofRostersets.this);
                        listView.setAdapter(listofRosterAdapter);

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
                if (listofRosterAdapter != null && newText != null) {
                    if (TextUtils.isEmpty(newText)) {
                        listofRosterAdapter.filter("");
                        listView.clearTextFilter();
                    } else {
                        listofRosterAdapter.filter(newText);
                    }
                }
                return true;
            }
        });

    }

    private void showrostersetitemadddailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.rostersetitemadddailog, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText rostersetname = (EditText) view.findViewById(R.id.rostersetname);
        Button add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rostersetlistitem rostersetinfo = new Rostersetlistitem(rostersetname.getText().toString(), UUID.randomUUID().toString(), "", "");
                FirebaseDatabase.getInstance().getReference()
                        .child("List of Rosters Sets")
                        .child(rostersetinfo.getId())
                        .setValue(rostersetinfo);
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
