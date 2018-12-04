package com.example.praful.fireapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    Button bt;
    Firebase firebase;
    EditText EditText;String s1,s2;
    EditText EditText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button)findViewById(R.id.button);
        EditText = (EditText)findViewById(R.id.editText);
        EditText2 = (EditText)findViewById(R.id.editText2);
        firebase = new Firebase("https://fireapp1-cb370.firebaseio.com/");

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 = EditText.getText().toString();
                s2 = EditText2.getText().toString();


                Firebase fchild = firebase.child("Country");
                fchild.setValue(s2);

            }
        });
    }
}
