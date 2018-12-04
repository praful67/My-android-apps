
package com.dev.praful.admintracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Addemployee extends AppCompatActivity {

    ListView listView;
    List<Credentialmail> credentialmailList = new ArrayList<>();
    Addadpter addadpter;
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
        setContentView(R.layout.activity_addemployee);
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showadddialog();
            }
        });
        listView = (ListView) findViewById(R.id.employeeslist);

        getWindow().getAttributes().windowAnimations = R.style.Style;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseDatabase.getInstance().getReference()
                .child("Credentialmails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (credentialmailList.size() > 0)
                            credentialmailList.clear();


                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Credentialmail credentialmail = dataSnapshot1.getValue(Credentialmail.class);
                            credentialmailList.add(credentialmail);
                        }
                        addadpter = new Addadpter(credentialmailList, Addemployee.this);
                        TextView t = (TextView) findViewById(R.id.t);

                        listView.setEmptyView(t);
                        listView.setAdapter(addadpter);
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
                if (addadpter != null && newText != null) {

                    if (TextUtils.isEmpty(newText)) {
                        addadpter.filter("");
                        listView.clearTextFilter();
                    } else {
                        addadpter.filter(newText);
                    }
                }
                return true;
            }
        });

    }

    private void showadddialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.adddialog, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText employeemail = (EditText) view.findViewById(R.id.employeemail);
        Button add = (Button) view.findViewById(R.id.add);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                String e1 = employeemail.getText().toString().replace(".", ",");
                String e2 = e1.replace("@", ",");

                FirebaseDatabase.getInstance().getReference()
                        .child("Credentialmails")
                        .child(e2)
                        .child("email")
                        .setValue(employeemail.getText().toString());
                FirebaseDatabase.getInstance().getReference()
                        .child("Credentialmails1")
                        .child(e2)
                        .child("email")
                        .setValue(employeemail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(Addemployee.this, "Added !", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });


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
