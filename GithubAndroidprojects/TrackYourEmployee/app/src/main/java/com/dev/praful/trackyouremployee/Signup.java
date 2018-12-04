package com.dev.praful.trackyouremployee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Signup extends AppCompatActivity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final MaterialEditText username = (MaterialEditText) findViewById(R.id.username);
        final MaterialEditText password = (MaterialEditText) findViewById(R.id.password);
        Button signup = (Button) findViewById(R.id.signup);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!(TextUtils.isEmpty(username.getText().toString()))) && (!(TextUtils.isEmpty(password.getText().toString())))) {
                    progressDialog.setMessage("Please wait");
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference()
                            .child("CredentialsDrivers1")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String e1 = password.getText().toString().replace(".", ",");
                                    String e2 = e1.replace("@", ",");
                                    if (dataSnapshot.child(e2).exists()) {
                                        Driverdetails Driverdetails = new Driverdetails(username.getText().toString(), password.getText().toString()
                                                , "", "", "", "", "", "", id);
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("SignedDrivers")
                                                .child(id)
                                                .setValue(Driverdetails);
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("CredentialsDrivers1")
                                                .child(e2)
                                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(Signup.this, Map.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });


                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Signup.this, "Sorry , invalid crendentials", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                } else {
                    Toast.makeText(Signup.this, "Please fill all", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
