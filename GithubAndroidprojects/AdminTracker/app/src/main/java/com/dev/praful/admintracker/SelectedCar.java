package com.dev.praful.admintracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class SelectedCar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_car);
        Button selectdriver = (Button) findViewById(R.id.selectdriver);
        Button current = (Button) findViewById(R.id.current);
        Button selectemployees = (Button) findViewById(R.id.selectemployees);

        FloatingActionButton back = (FloatingActionButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String carId = getIntent().getStringExtra("carId");

        Button details = (Button)findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedCar.this, Cardetails.class);
                intent.putExtra("carid", carId.toString());
                startActivity(intent);
            }
        });
        selectdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedCar.this, Selectdriver.class);
                startActivity(intent);
            }
        });

        selectemployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedCar.this, Selectemployees.class);
                intent.putExtra("carId", carId.toString());
                startActivity(intent);

            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedCar.this , CurrentCardetails.class);
                startActivity(intent);
            }
        });
    }
}
