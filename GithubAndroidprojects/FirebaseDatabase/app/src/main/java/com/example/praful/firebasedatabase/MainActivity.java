package com.example.praful.firebasedatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    Toolbar toolbar;
    EditText input_name, input_email;
    ListView listdata;
    Users selecteduser;
    List<Users> listusers = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Firebase Database");
        setSupportActionBar(toolbar);

        input_email = (EditText)findViewById(R.id.input_email);
        input_name = (EditText)findViewById(R.id.input_name);
        listdata = (ListView)findViewById(R.id.listusers);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        addEventFirebaselistener();


        listdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users user = (Users)parent.getItemAtPosition(position);
                selecteduser = user;
                input_name.setText(user.getName());
                input_email.setText(user.getEmail());
            }
        });

    }

    private void addEventFirebaselistener() {

        databaseReference.child("users1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listusers.size() > 0)
                    listusers.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Users users = dataSnapshot1.getValue(Users.class);
                    listusers.add(users);
                }
                ListAdapter adapter = new ListAdapter(MainActivity.this , listusers);
                listdata.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout ,menu );
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.btn_add)
        {
            createUser();
        }
        else if (item.getItemId() == R.id.btn_save){
            Users users = new Users(selecteduser.getUid() , input_name.getText().toString() , input_email.getText().toString());
            saveit(users);
        }
        else if (item.getItemId() == R.id.btn_remove){
            removeit(selecteduser);
        }
        return true;
    }

    private void removeit(Users selecteduser) {

        databaseReference.child("users1").child(selecteduser.getUid()).removeValue();
        clearedittext();



    }

    private void saveit(Users users) {
        databaseReference.child("users1").child(users.getUid()).child("name").setValue(users.getName());
        databaseReference.child("users1").child(users.getUid()).child("email").setValue(users.getEmail());
        clearedittext();

    }

    private void clearedittext() {
        input_name.setText("");
        input_email.setText("");
    }

    private void createUser() {
        Users user = new Users(UUID.randomUUID().toString() ,input_name.getText().toString() ,input_email.getText().toString());
        databaseReference.child("users1").child(user.getUid()).setValue(user);
        clearedittext();

    }
}