package com.dev.praful.trackyouremployee;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class Myconsent extends AppCompatActivity {

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myconsent);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final TextView consent = (TextView)findViewById(R.id.consent);
        Button sendconsent =(Button)findViewById(R.id.sendconsent);
        Button cancel =(Button)findViewById(R.id.cancel);
        sendconsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Driver's consents")
                        .child(id)
                        .child("consent")
                        .setValue(consent.getText().toString());
                finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
