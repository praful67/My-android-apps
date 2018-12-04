package com.example.praful.doctor;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button important , week , quater , half,year , about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        important= (Button)findViewById(R.id.important);
        week = (Button)findViewById(R.id.week);
        quater = (Button)findViewById(R.id.quater);
        half = (Button)findViewById(R.id.half);
        year = (Button)findViewById(R.id.year);
        about = (Button)findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , About.class);
                startActivity(i);
            }
        });

        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , Important.class);
                startActivity(i);
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , Week.class);
                startActivity(i);
            }
        });
        quater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , Quater.class);
                startActivity(i);
            }
        }); year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , Year.class);
                startActivity(i);
            }
        }); half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , Half.class);
                startActivity(i);
            }
        });

    }
}
