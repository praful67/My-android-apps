package com.praful.firebase;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    String string;
    Firebase firebase;
    Button bt1 = (Button)findViewById(R.id.button_3);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt = (Button)findViewById(R.id.button);

        final EditText editText = (EditText)findViewById(R.id.editText);
        Firebase.setAndroidContext(this);

        firebase = new Firebase("https://fir-24867.firebaseio.com");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string = editText.getText().toString();
                Firebase firebase1 = firebase.child("Name");
                firebase1.setValue(string);
                Toast.makeText(MainActivity.this,"sent", Toast.LENGTH_SHORT).show();

            }
        });




    }
}
