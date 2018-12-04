package com.example.praful.fd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Open extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        FirebaseDatabase.getInstance().getReference()
                .child("Test")
                .child("1")
                .child("name")
                .setValue("Praful");

        final TextView text = (TextView) findViewById(R.id.text);
        final EditText edittext = (EditText) findViewById(R.id.edittext);
        Button change = (Button) findViewById(R.id.change);
        FirebaseDatabase.getInstance().getReference()
                .child("Test")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1.getKey().equals("2")) {
                                // text.setText(dataSnapshot1.child("for").getValue().toString());
                                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot1.getValue();
                                if (objectMap.get("id") != null)
                                    text.setText(objectMap.get("id").toString() + " " + objectMap.get("name").toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference()
                        .child("Test")
                        .child("2")
                        .child("id")
                        .removeValue();
                Map<String, Object>content = new HashMap<>();
                content.put("id" , edittext.getText().toString());
                FirebaseDatabase.getInstance().getReference()
                        .child("Test")
                        .child("2")
                        .updateChildren(content);

            }
        });
        String id = "2";
        FirebaseDatabase.getInstance().getReference()
                .child("Test").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(Open.this, "changed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
