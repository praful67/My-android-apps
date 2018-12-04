package com.dev.praful.trackyouremployee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class Loginout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginout);
        Button login = (Button) findViewById(R.id.login);
        Button logout = (Button) findViewById(R.id.logout);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor  = preferences.edit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loginout.this, Rideslist.class);
                intent.putExtra("state", "login");
                editor.putString("state" , "login");
                editor.apply();
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loginout.this, Rideslist.class);
                intent.putExtra("state", "logout");
                editor.putString("state" , "logout");
                editor.apply();
                startActivity(intent);
            }
        });
    }
}
