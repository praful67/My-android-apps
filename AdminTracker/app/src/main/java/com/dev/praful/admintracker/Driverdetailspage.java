package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class Driverdetailspage extends AppCompatActivity {

    String driverid, carid;
    ImageView drivinglicense;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference, imagefolder;
    ProgressDialog progressDialog;
    String loginrosterid, logoutrosterid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverdetailspage);
        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        final TextView phone = (TextView) findViewById(R.id.phone);
        final TextView company = (TextView) findViewById(R.id.company);
        final TextView address = (TextView) findViewById(R.id.address);
        drivinglicense = (ImageView) findViewById(R.id.drivinglicense);
        final TextView CS = (TextView) findViewById(R.id.currentstatus);

        final TextView loginroster = (TextView) findViewById(R.id.loginroster);
        final TextView logoutroster = (TextView) findViewById(R.id.logoutroster);

        if (getIntent().getStringExtra("Driverid") != null) {

            FirebaseDatabase.getInstance().getReference()
                    .child("Driver's Car")
                    .child("login")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()) {
                                    if (dataSnapshot11.getKey().equals(driverid)) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Cars Info")
                                                .child(dataSnapshot1.getKey())
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        CarsInfo carsInfo1 = dataSnapshot.getValue(CarsInfo.class);
                                                        if (carsInfo1 != null) {
                                                            loginrosterid = carsInfo1.getId();
                                                            loginroster.setText(carsInfo1.getName());
                                                            if (loginrosterid != null) {
                                                                loginroster.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        Intent intent = new Intent(Driverdetailspage.this, CurrentCardetails.class);
                                                                        intent.putExtra("carId", loginrosterid);
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                    }
                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            FirebaseDatabase.getInstance().getReference()
                    .child("Driver's Car")
                    .child("logout")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot11 : dataSnapshot1.getChildren()) {
                                    if (dataSnapshot11.getKey().equals(driverid)) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Cars Info")
                                                .child(dataSnapshot1.getKey())
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        CarsInfo carsInfo1 = dataSnapshot.getValue(CarsInfo.class);
                                                        if (carsInfo1 != null) {
                                                            logoutrosterid = carsInfo1.getId();
                                                            logoutroster.setText(carsInfo1.getName());
                                                            if (logoutrosterid != null) {
                                                                logoutroster.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        Intent intent = new Intent(Driverdetailspage.this, CurrentCardetails.class);
                                                                        intent.putExtra("carId", logoutrosterid);
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                    }
                                }
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            driverid = getIntent().getStringExtra("Driverid");
            FirebaseDatabase.getInstance().getReference()
                    .child("RidestartandendD")
                    .child(driverid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (String.valueOf(map.get("status")).equals("RideStarted")) {
                                    SpannableStringBuilder builder = new SpannableStringBuilder();

                                    String BLACK = "Current status is :";
                                    SpannableString whiteSpannable = new SpannableString(BLACK);
                                    whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, BLACK.length(), 0);
                                    builder.append(whiteSpannable);

                                    String green = " RIDE STARTED";
                                    SpannableString redSpannable = new SpannableString(green);
                                    redSpannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, green.length(), 0);
                                    builder.append(redSpannable);
                                    String time = "\nLast Ride Time : ";
                                    SpannableString timesp = new SpannableString(time);
                                    timesp.setSpan(new ForegroundColorSpan(Color.BLACK), 0, time.length(), 0);
                                    builder.append(timesp);

                                    if (String.valueOf(map.get("time")) != null) {
                                        String Time = String.valueOf(map.get("time"));
                                        SpannableString Timesp = new SpannableString(Time);
                                        Timesp.setSpan(new ForegroundColorSpan(Color.BLUE), 0, Time.length(), 0);
                                        builder.append(Timesp);

                                    }

                                    CS.setText(builder, TextView.BufferType.SPANNABLE);

                                } else {
                                    SpannableStringBuilder builder = new SpannableStringBuilder();

                                    String BLACK = "Current status is :";
                                    SpannableString whiteSpannable = new SpannableString(BLACK);
                                    whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, BLACK.length(), 0);
                                    builder.append(whiteSpannable);

                                    String red = "  RIDE ENDED";
                                    SpannableString redSpannable = new SpannableString(red);
                                    redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
                                    builder.append(redSpannable);
                                    String time = "\nLast Ride Time : ";
                                    SpannableString timesp = new SpannableString(time);
                                    timesp.setSpan(new ForegroundColorSpan(Color.BLACK), 0, time.length(), 0);
                                    builder.append(timesp);

                                    if (String.valueOf(map.get("time")) != null) {
                                        String Time = String.valueOf(map.get("time"));
                                        SpannableString Timesp = new SpannableString(Time);
                                        Timesp.setSpan(new ForegroundColorSpan(Color.BLUE), 0, Time.length(), 0);
                                        builder.append(Timesp);

                                    }

                                    CS.setText(builder, TextView.BufferType.SPANNABLE);


                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference()
                    .child("Driver's Car")
                    .child(driverid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null)
                                carid = String.valueOf(map.get("car id"));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            FirebaseDatabase.getInstance().getReference()
                    .child("SignedDrivers")
                    .child(driverid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                if (Driverdetails != null) {
                                    username.setText(Driverdetails.getUsername());
                                    password.setText(Driverdetails.getPassword());
                                    phone.setText(Driverdetails.getPhone());
                                    company.setText(Driverdetails.getCompany());
                                    if (TextUtils.isEmpty(Driverdetails.getAddress()))
                                        address.setText("No address");
                                    else
                                        address.setText(Driverdetails.getAddress());
                                    String DL = Driverdetails.getDrivinglicense();

                                    if (!TextUtils.isEmpty(DL)) {
                                        Picasso.with(Driverdetailspage.this).load(DL)
                                                .into(drivinglicense);
                                    } else
                                        drivinglicense.setImageResource(R.drawable.add);


                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        } else {
            Toast.makeText(this, "No driver0", Toast.LENGTH_SHORT).show();
            finish();
        }
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we Remove");


        Button cardetails = (Button) findViewById(R.id.cardetails);
        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Driverdetailspage.this, Editdriverprofile.class);
                intent.putExtra("Driverid", driverid);
                startActivity(intent);
            }
        });
        Button editaddress = (Button) findViewById(R.id.editaddress);
        editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Driverdetailspage.this, Editdriveraddress.class);
                intent.putExtra("driverid", driverid);
                startActivity(intent);
            }
        });
        cardetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Driverdetailspage.this, Cardetails.class);
                intent.putExtra("driverid", driverid);
                startActivity(intent);
            }
        });
        Button findinmap = (Button) findViewById(R.id.findinmap);

        findinmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Driverdetailspage.this, Trackdriver.class);
                intent.putExtra("Driverid", driverid);
                startActivity(intent);
            }
        });
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        drivinglicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });
        Button call = (Button) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone.getText().toString()));
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new android.support.v7.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new android.support.v7.app.AlertDialog.Builder(this);


        Button remove = (Button) findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                builder.setMessage("Do you want remove this driver0 from whole app ? ")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.show();
                                if (carid != null) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Car Drivers")
                                            .child(carid)
                                            .child(driverid)
                                            .removeValue();
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Driver's Car")
                                            .child("logout")
                                            .child(carid)
                                            .child(driverid)
                                            .removeValue();
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Driver's Car")
                                            .child("login")
                                            .child(carid)
                                            .child(driverid)
                                            .removeValue();

                                }
                                FirebaseDatabase.getInstance().getReference()
                                        .child("DriverprivateCars")
                                        .child(driverid)
                                        .removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Driverselection")
                                        .child("login")
                                        .child(driverid).removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Driverselection")
                                        .child("logout")
                                        .child(driverid).removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Drivers Info")
                                        .child(driverid)
                                        .removeValue();
                                FirebaseDatabase.getInstance().
                                        getReference()
                                        .child("DriverUpdates").child(driverid).removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Drivers")
                                        .child(driverid).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("Driversforegroundserivces")
                                        .child(driverid).removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("SignedDrivers")
                                        .child(driverid)
                                        .removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Driverdetailspage.this, "Removed !!", Toast.LENGTH_SHORT).show();
                                                new CountDownTimer(1000, 1000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        progressDialog.dismiss();
                                                        finish();
                                                    }
                                                }.start();

                                            }
                                        });

                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();


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

                imagefolder = storageReference.child(driverid + "/" + "DL");
                imagefolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Picasso.with(Driverdetailspage.this).load(uri).into(drivinglicense);
                        Toast.makeText(Driverdetailspage.this, "Uploaded your image", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                final java.util.Map<String, Object> drivinglicense1 = new HashMap<>();
                                drivinglicense1.put("drivinglicense", uri.toString());
                                FirebaseDatabase.getInstance().getReference("SignedDrivers")
                                        .child(driverid)
                                        .updateChildren(drivinglicense1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Driverdetailspage.this, "Image Updated !", Toast.LENGTH_SHORT).show();
                                                    Picasso.with(Driverdetailspage.this).load(uri).into(drivinglicense);

                                                } else
                                                    Toast.makeText(Driverdetailspage.this, "Image Update error !", Toast.LENGTH_SHORT).show();

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
