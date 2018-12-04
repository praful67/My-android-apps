package com.example.praful.doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Quater extends AppCompatActivity {

    String[] string = {

            "A1c (Glycosylated heamoglobin" , "Electrocardiogram" , "Fasting blood glucose level"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quater);

        ListView listView = (ListView)findViewById(R.id.listquater);

        ArrayAdapter arrayAdapter = new ArrayAdapter(Quater.this , android.R.layout.simple_list_item_1 , string);

        listView.setAdapter(arrayAdapter);
    }
}
