package com.example.praful.fd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText input_name, input_email;
    ListView listView;
    List<Users> list_of_Users = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Users selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FD");
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        input_email = (EditText) findViewById(R.id.input_email);
        input_name = (EditText) findViewById(R.id.input_name);
        listView = (ListView) findViewById(R.id.listView);

        Showusers();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users user = (Users) parent.getItemAtPosition(position);
                selectedUser = user;
                input_email.setText(user.getEmail());
                input_name.setText(user.getName());
            }
        });

        Button open = (Button) findViewById(R.id.open);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Open.class);
                startActivity(intent);
            }
        });


    }

    private void Showusers() {

        databaseReference.child("users2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (list_of_Users.size() > 0)
                    list_of_Users.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users users = dataSnapshot1.getValue(Users.class);
                    list_of_Users.add(users);
                }

                Adapter adapter = new Adapter(MainActivity.this, list_of_Users);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.btn_add) {
            createuser();
        } else if (item.getItemId() == R.id.btn_update) {

            Users user = new Users(selectedUser.getId(), input_name.getText().toString(), input_email.getText().toString());
            updateuser(user);

        } else if (item.getItemId() == R.id.btn_remove) {

            removeuser(selectedUser);
        }
        return true;
    }

    private void removeuser(Users selectedUser) {
        databaseReference.child("users2").child(selectedUser.getId()).removeValue();
        clearEdittext();
    }

    private void updateuser(Users user) {

        databaseReference.child("users2").child(user.getId()).child("name").setValue(user.getName());
        databaseReference.child("users2").child(user.getId()).child("email").setValue(user.getEmail());
        clearEdittext();
    }

    private void createuser() {

        Users user = new Users(UUID.randomUUID().toString(), input_name.getText().toString(), input_email.getText().toString());
        databaseReference.child("users2").child(user.getId()).setValue(user);
        clearEdittext();
    }

    private void clearEdittext() {
        input_name.setText("");
        input_email.setText("");
    }

}
