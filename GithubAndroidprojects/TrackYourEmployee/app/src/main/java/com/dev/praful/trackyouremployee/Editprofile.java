package com.dev.praful.trackyouremployee;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Editprofile extends AppCompatActivity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        final MaterialEditText username = (MaterialEditText) findViewById(R.id.username);
        final MaterialEditText phone = (MaterialEditText) findViewById(R.id.phone);
        final MaterialEditText password = (MaterialEditText) findViewById(R.id.password);
        final MaterialEditText company = (MaterialEditText) findViewById(R.id.company);
        Button cancel = (Button) findViewById(R.id.cancel);
        Button done = (Button) findViewById(R.id.ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseDatabase.getInstance().getReference()
                .child("SignedDrivers")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                            username.setHint(Driverdetails.getUsername());
                            password.setHint(Driverdetails.getPassword());
                            phone.setHint(Driverdetails.getPhone());
                            company.setHint(Driverdetails.getCompany());


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
                String company1 = company.getText().toString();
                Map<String, Object> map = new HashMap<>();
                if (!TextUtils.isEmpty(name))
                    map.put("username", name);

                if (!TextUtils.isEmpty(password1))
                    map.put("password", password1);

                if (!TextUtils.isEmpty(company1))
                    map.put("company", company1);

                if (!TextUtils.isEmpty(phone1))
                    map.put("phone", phone1);


                FirebaseDatabase.getInstance().getReference()
                        .child("SignedDrivers")
                        .child(id)
                        .updateChildren(map);
                finish();
            }
        });

    }
}
