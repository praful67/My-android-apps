package com.example.praful.list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String[] string = {

            "Tracter" , "Machine" , "Pesticides" , "Weeds" , "Soil"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.list);

        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this , android.R.layout.simple_list_item_1 , string);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                Toast.makeText(MainActivity.this, "You have selected " + textView.getText(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
