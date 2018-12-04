package com.example.praful.firebasedemo;

import android.content.Intent;
import android.media.MediaPlayer;
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
    Firebase firebase , firebase0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt = (Button)findViewById(R.id.button);

        final EditText editText = (EditText)findViewById(R.id.editText);
        Firebase.setAndroidContext(this);

        firebase = new Firebase("https://fir-c7264.firebaseio.com/Name");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string = editText.getText().toString();
                Firebase firebase1 = firebase.child("Name1");
                firebase1.setValue(string);
                Toast.makeText(MainActivity.this,"sent", Toast.LENGTH_SHORT).show();

            }
        });
        Button bt1 = (Button)findViewById(R.id.button2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , See.class);
                startActivity(intent);
            }
        });


        final TextView textView =(TextView)findViewById(R.id.textView3);
        Firebase.setAndroidContext(getApplicationContext());

        firebase0 = new Firebase("https://fir-c7264.firebaseio.com/Name/Name1");
        firebase0.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                textView.setText(string);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }
}
