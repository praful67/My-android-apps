package com.dev.praful.admintracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FloatingActionButton back = (FloatingActionButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button driverslist = (Button)findViewById(R.id.drivers);
        Button employeeslist = (Button)findViewById(R.id.employees);
        Button carslist = (Button)findViewById(R.id.cars);

        driverslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(List.this , Driverslist.class);
                startActivity(intent);
            }
        });

        employeeslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this , Employeeslist.class);
                startActivity(intent);
            }
        });

        carslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this , Carslist.class);
                startActivity(intent);

            }
        });


    }
}
