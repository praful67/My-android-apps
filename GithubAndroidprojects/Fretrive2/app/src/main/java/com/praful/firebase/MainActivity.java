package com.praful.firebase;

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

    Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView textView =(TextView)findViewById(R.id.textView2);
        Firebase.setAndroidContext(getApplicationContext());

        firebase = new Firebase("https://fir-24867.firebaseio.com/Name");
        firebase.addValueEventListener(new ValueEventListener() {
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
