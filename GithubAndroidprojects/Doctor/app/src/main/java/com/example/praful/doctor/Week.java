package com.example.praful.doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Week extends AppCompatActivity {


    String[] string = {

            "Blood pressure checks"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);
        ListView listView = (ListView)findViewById(R.id.listweek);

        ArrayAdapter arrayAdapter = new ArrayAdapter(Week.this , android.R.layout.simple_list_item_1 , string);

        listView.setAdapter(arrayAdapter);
    }
}
