package com.example.praful.doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Year extends AppCompatActivity {

    String[] string = {

            "Urine check for microalbumin" , "Lipid profile (blood test)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        ListView listView = (ListView)findViewById(R.id.listyear);

        ArrayAdapter arrayAdapter = new ArrayAdapter(Year.this , android.R.layout.simple_list_item_1 , string);

        listView.setAdapter(arrayAdapter);
    }
}
