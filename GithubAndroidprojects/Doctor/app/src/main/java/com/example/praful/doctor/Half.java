package com.example.praful.doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Half extends AppCompatActivity {

    String[] string = {

            "C-reactive protein (CRP)" , "Thyroid"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half);

        ListView listView = (ListView)findViewById(R.id.listhalf);

        ArrayAdapter arrayAdapter = new ArrayAdapter(Half.this , android.R.layout.simple_list_item_1 , string);

        listView.setAdapter(arrayAdapter);

    }
}
