package com.dev.praful.trackyourdriver;

import android.app.ProgressDialog;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class Editprofile extends AppCompatActivity {

    String id;

    android.support.v7.widget.Toolbar mToolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final MaterialEditText username = (MaterialEditText) findViewById(R.id.username);
        final MaterialEditText phone = (MaterialEditText) findViewById(R.id.phone);
        final MaterialEditText password = (MaterialEditText) findViewById(R.id.password);
        final MaterialEditText bg = (MaterialEditText) findViewById(R.id.bg);
        final MaterialEditText Eid = (MaterialEditText) findViewById(R.id.Eid);
        final MaterialEditText gender = (MaterialEditText) findViewById(R.id.gender);
        ImageView cancel = (ImageView) findViewById(R.id.cancel);
        ImageView done = (ImageView) findViewById(R.id.ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            username.setHint(employeedetails.getUsername());
                            password.setHint(employeedetails.getPassword());
                            bg.setHint(employeedetails.getBloodgroup());
                            phone.setHint(employeedetails.getPhone());
                            Eid.setHint(employeedetails.getEmployee_Id());
                            if (map != null)
                                gender.setHint(String.valueOf(map.get("gender")));

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String phone1 = phone.getText().toString();
                String password1 = password.getText().toString();
                String bg1 = bg.getText().toString();
                String eid = Eid.getText().toString();
                String gender1 = gender.getText().toString();
                Map<String, Object> map = new HashMap<>();
                if (!TextUtils.isEmpty(name))
                    map.put("username", name);

                if (!TextUtils.isEmpty(password1))
                    map.put("password", password1);

                if (!TextUtils.isEmpty(bg1))
                    map.put("bloodgroup", bg1);

                if (!TextUtils.isEmpty(phone1))
                    map.put("phone", phone1);

                if (!TextUtils.isEmpty(gender1))
                    map.put("gender", gender1);

                if (!TextUtils.isEmpty(eid))
                    map.put("employee_Id", eid);

                progressDialog.setMessage("Please wait");
                progressDialog.setCancelable(false);
                progressDialog.show();


                FirebaseDatabase.getInstance().
                        getReference().child("Profilechanged").child(id)
                        .child("changed").setValue("yes");
                  FirebaseDatabase.getInstance().getReference()
                        .child("SignedEmployees")
                        .child(id)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        finish();
                    }
                });

            }
        });

    }
}
