package com.example.praful.doctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Important extends AppCompatActivity {

    String[] string = {

           "Chemistry Panel and Complete Blood Count","Fibrinogen","Heamoglobin AIC","DHEA","Prostate-Specific Antigen","Homocysteine", "C-reactive protein (CRP)" , "Thyroid Stimulating Hormone (TSH)","Testosterone","Estradiol"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important);

        ListView listView = (ListView)findViewById(R.id.listimpo);

        ArrayAdapter arrayAdapter = new ArrayAdapter(Important.this , android.R.layout.simple_list_item_1 , string);

        listView.setAdapter(arrayAdapter);
    }
}
