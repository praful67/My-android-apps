package com.example.praful.fabspeeddial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leinardi.android.speeddial.SpeedDialView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeedDialView speedDialView = findViewById(R.id.speedDial);
        speedDialView.inflate(R.menu.menu);

    }
}
