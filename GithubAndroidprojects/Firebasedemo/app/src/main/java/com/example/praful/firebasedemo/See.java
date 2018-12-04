package com.example.praful.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class See extends AppCompatActivity {

    Firebase firebase0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            final TextView textView =(TextView)findViewById(R.id.textView);
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
