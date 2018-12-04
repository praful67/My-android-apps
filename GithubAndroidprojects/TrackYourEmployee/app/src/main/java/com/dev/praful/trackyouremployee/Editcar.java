package com.dev.praful.trackyouremployee;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class Editcar extends AppCompatActivity {

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcar);
        final MaterialEditText carname = (MaterialEditText) findViewById(R.id.Carname);
        final MaterialEditText carnumber = (MaterialEditText) findViewById(R.id.CarNumber);

        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
            FirebaseDatabase.getInstance().getReference()
                    .child("DriverprivateCars")
                    .child(id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Privatecarsinfo carsInfo = dataSnapshot.getValue(Privatecarsinfo.class);
                                if (carsInfo != null) {
                                    carname.setHint(carsInfo.getName());
                                    carnumber.setHint(carsInfo.getCarnumber());
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            Button cancel = (Button) findViewById(R.id.cancel);
            Button done = (Button) findViewById(R.id.ok);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final ProgressDialog progressDialog = new ProgressDialog(this);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String carname1 = carname.getText().toString();
                    String carnumber1 = carnumber.getText().toString();
                    Map<String, Object> map = new HashMap<>();
                    if (!TextUtils.isEmpty(carname1))
                        map.put("name", carname1);

                    if (!TextUtils.isEmpty(carnumber1))
                        map.put("carnumber", carnumber1);


                    progressDialog.setMessage("Please wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    FirebaseDatabase.getInstance().getReference()
                            .child("DriverprivateCars")
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
    }

