package com.dev.praful.trackyouremployee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class Profile extends AppCompatActivity {

    String id;
    ImageView drivinglicense;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference, imagefolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button ok = (Button) findViewById(R.id.ok);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile.this, Editprofile.class);
                startActivity(intent);
            }
        });
        // CommonSwitch.aSwitch = (Switch)findViewById(R.id.switch1);
        Button editaddress = (Button) findViewById(R.id.editaddress);
        editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonSwitch.aSwitch.setChecked(true);
                finish();
            }
        });
        Button  car = (Button)findViewById(R.id.car);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile.this , Cardetails.class);
                intent.putExtra("id" , id);
                startActivity(intent);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        final TextView phone = (TextView) findViewById(R.id.phone);
        final TextView company = (TextView) findViewById(R.id.company);
        final TextView address = (TextView) findViewById(R.id.address);
        drivinglicense = (ImageView) findViewById(R.id.drivinglicense);
        FirebaseDatabase.getInstance().getReference()
                .child("SignedDrivers")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                            username.setText(Driverdetails.getUsername());
                            password.setText(Driverdetails.getPassword());
                            phone.setText(Driverdetails.getPhone());
                            company.setText(Driverdetails.getCompany());
                            if (Driverdetails.getAddress() != null) {
                                if (Driverdetails.getAddress().length() != 0) {
                                    if (TextUtils.isEmpty(Driverdetails.getAddress()))
                                        address.setText("No address");
                                    else
                                        address.setText(Driverdetails.getAddress());
                                }
                            } else
                                address.setText("No address");

                            if (Driverdetails.getDrivinglicense() != null) {
                                if (Driverdetails.getDrivinglicense().length() != 0) {

                                    String DL = Driverdetails.getDrivinglicense();
                                    if (!TextUtils.isEmpty(DL)) {
                                        Picasso.with(Profile.this).load(DL)
                                                .into(drivinglicense);
                                    } else
                                        drivinglicense.setImageResource(R.drawable.add);
                                }
                            } else
                                drivinglicense.setImageResource(R.drawable.add);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        drivinglicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

    }

    private void chooseimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture: "), CommonSwitch.PICK_IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonSwitch.PICK_IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final Uri uri = data.getData();
            if (uri != null) {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Please wait..");
                dialog.setCancelable(false);
                dialog.show();

                imagefolder = storageReference.child(id + "/" + "DL");
                imagefolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Picasso.with(Profile.this).load(uri).into(drivinglicense);
                        Toast.makeText(Profile.this, "Uploaded your image", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                final java.util.Map<String, Object> drivinglicense1 = new HashMap<>();
                                drivinglicense1.put("drivinglicense", uri.toString());
                                FirebaseDatabase.getInstance().getReference("SignedDrivers")
                                        .child(id)
                                        .updateChildren(drivinglicense1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Profile.this, "Image Updated !", Toast.LENGTH_SHORT).show();
                                                    Picasso.with(Profile.this).load(uri).into(drivinglicense);

                                                } else
                                                    Toast.makeText(Profile.this, "Image Update error !", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        dialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
            }
        }
    }

}
