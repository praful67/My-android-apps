package com.example.praful.fireappretrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Firebase firebase;

    ArrayList<String> arrayList = new ArrayList<>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase = new Firebase("https://fireapp1-cb370.firebaseio.com/Users");
        listView = (ListView)findViewById(R.id.list);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1 ,arrayList);
        listView.setAdapter(arrayAdapter);

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String string = dataSnapshot.getValue(String.class);
                arrayList.add(string);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
