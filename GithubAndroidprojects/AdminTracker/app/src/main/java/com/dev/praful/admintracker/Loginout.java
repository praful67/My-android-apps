package com.dev.praful.admintracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class Loginout extends AppCompatActivity {

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
        setContentView(R.layout.activity_loginout);
        Button login = (Button) findViewById(R.id.login);
        Button logout = (Button) findViewById(R.id.logout);
        getWindow().getAttributes().windowAnimations = R.style.Style;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final String carid = getIntent().getStringExtra("carid");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loginout.this, Selectdriver.class);
                intent.putExtra("carid", carid);
                intent.putExtra("state", "login");
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loginout.this, Selectdriver.class);
                intent.putExtra("carid", carid);
                intent.putExtra("state", "logout");
                startActivity(intent);
            }
        });
    }
}
