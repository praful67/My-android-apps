package com.dev.praful.admintracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Employeedetailspage extends AppCompatActivity {

    String id;
    String carid;
    String lat, lng;
    LatLng latLng;
    double distance = 0.1;
    boolean gotnearestone = false;
    final double limit = 10000;
    TextView nameofNE;
    TextView addressofNE;
    ProgressDialog progressDialog;
    TextView distanceNE;
    TextView eidNE;
    ProgressDialog progressDialog1;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeedetailspage);
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Please wait while we Remove");
        getWindow().getAttributes().windowAnimations = R.style.Style;
        final TextView CS = (TextView) findViewById(R.id.currentstatus);

        Button ok = (Button) findViewById(R.id.ok);
        Button edit = (Button) findViewById(R.id.edit);
        if (getIntent().getStringExtra("employeeid") != null) {
            id = getIntent().getStringExtra("employeeid");
            FirebaseDatabase.getInstance().getReference()
                    .child("checkinandout")
                    .child(id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (String.valueOf(map.get("status")).equals("checkedin")) {
                                    SpannableStringBuilder builder = new SpannableStringBuilder();

                                    String BLACK = "Current status is :";
                                    SpannableString whiteSpannable = new SpannableString(BLACK);
                                    whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, BLACK.length(), 0);
                                    builder.append(whiteSpannable);

                                    String green = " CHECKED IN";
                                    SpannableString redSpannable = new SpannableString(green);
                                    redSpannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, green.length(), 0);
                                    builder.append(redSpannable);
                                    String time = "\nLastly Checked Time : ";
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

                                    String red = "  CHECKED OUT";
                                    SpannableString redSpannable = new SpannableString(red);
                                    redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
                                    builder.append(redSpannable);
                                    String time = "\nLastly Checked Time : ";
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

        }
        final TextView cardetails = (TextView) findViewById(R.id.cardetails);
        Button findinmap = (Button) findViewById(R.id.findinmap);

        findinmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Employeedetailspage.this, Trackemployee.class);
                intent.putExtra("employeeid", id);
                startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Car")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null)
                            carid = String.valueOf(map.get("car id"));
                        if (carid != null) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Cars Info")
                                    .child(carid)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null) {
                                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                if (map != null) {
                                                    cardetails.setText("Roster : " + String.valueOf(map.get("name")));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Employeedetailspage.this, Editemployeeprofile.class);
                intent.putExtra("employeeid", id);
                startActivity(intent);
            }
        });
        // CommonSwitch.aSwitch = (Switch)findViewById(R.id.switch1);
        Button editaddress = (Button) findViewById(R.id.editaddress);

        editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Employeedetailspage.this, Editemployeeaddress.class);
                intent.putExtra("employeeid", id);
                startActivity(intent);
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        final TextView bg = (TextView) findViewById(R.id.bg);
        final TextView phone = (TextView) findViewById(R.id.phone);
        final TextView Eid = (TextView) findViewById(R.id.Eid);
        final TextView address = (TextView) findViewById(R.id.address);
        final TextView gender = (TextView) findViewById(R.id.gender);

        final TextView pickuptime = (TextView) findViewById(R.id.pickuptime);
        FirebaseDatabase.getInstance().getReference()
                .child("Employeespickuptimes")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                pickuptime.setText(String.valueOf(map.get("time")));
                                pickuptime.setTextColor(Color.BLUE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                            if (employeedetails != null) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                if (map != null)
                                    gender.setText(String.valueOf(map.get("gender")));
                                if (employeedetails.getUsername() != null)
                                    username.setText(employeedetails.getUsername());
                                if (employeedetails.getPassword() != null)
                                    password.setText(employeedetails.getPassword());
                                if (employeedetails.getBloodgroup() != null)
                                    bg.setText(employeedetails.getBloodgroup());
                                if (employeedetails.getPhone() != null)
                                    phone.setText(employeedetails.getPhone());
                                if (employeedetails.getEmployee_Id() != null)
                                    Eid.setText(employeedetails.getEmployee_Id());
                                if (employeedetails.getPick_up_address() != null)
                                    if (TextUtils.isEmpty(employeedetails.getPick_up_address()))
                                        address.setText("No address");
                                    else
                                        address.setText(employeedetails.getPick_up_address());

                                if (employeedetails.getAddresslat() != null && employeedetails.getAddresslat().length() != 0) {
                                    lat = employeedetails.getAddresslat();
                                    lng = employeedetails.getAddresslng();
                                    latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                    //   loadnearestemployess(latLng);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
        final android.support.v7.app.AlertDialog.Builder builder1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder1 = new android.support.v7.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder1 = new android.support.v7.app.AlertDialog.Builder(this);

        Button remove = (Button) findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                builder1.setMessage("Do you want Remove this employee from whole app ? ")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog1.show();
                                if (carid != null) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Car employees")
                                            .child(carid)
                                            .child(id)
                                            .removeValue();
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Employee's Rank")
                                            .child(carid)
                                            .child(id)
                                            .removeValue();

                                }
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Employee's Car")
                                        .child(id)
                                        .removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Employees Info")
                                        .child(id)
                                        .removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Final Ordered Employees")
                                        .child(id)
                                        .removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Final Rank of employee")
                                        .child(id)
                                        .removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("SignedEmployees")
                                        .child(id)
                                        .removeValue();

                                FirebaseDatabase.getInstance().getReference()
                                        .child("checkinandout")
                                        .child(id).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("Updates")
                                        .child(id).removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("employeeselection")
                                        .child(id).removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("sent")
                                        .child(id).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("foregroundserivces")
                                        .child(id).removeValue();

                                new CountDownTimer(3000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        progressDialog1.dismiss();
                                        finish();
                                    }
                                }.start();

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

    private void loadnearestemployess(final LatLng location) {


        GeoFire gf = new GeoFire(FirebaseDatabase.getInstance().getReference("Employee's addresses"));

        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(location.latitude, location.longitude), distance);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location1) {
                if (!gotnearestone) {
                    if (!key.equals(id)) {
                        gotnearestone = true;

                        progressDialog.dismiss();
                        FirebaseDatabase.getInstance().getReference().child("SignedEmployees")
                                .child(key)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {

                                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                            if (employeedetails != null) {
                                               /* nameofNE.setText(employeedetails.getUsername());
                                                addressofNE.setText(employeedetails.getPick_up_address());
                                                eidNE.setText(employeedetails.getEmployee_Id());
                                        */
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                        FirebaseDatabase.getInstance().getReference()
                                .child("Final Ordered Employees")
                                .child(key)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                        if (map != null) {
                                            // distanceNE.setText(String.valueOf(map.get("distance")) + " " + String.valueOf(map.get("time")) + " far from Office");

                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                    } else {
                        distance = distance + .1;
                        loadnearestemployess(location);
                    }
                }

            }

            @Override
            public void onKeyExited(String key) {


            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!gotnearestone && distance < limit) {
                    distance = distance + .1;
                    loadnearestemployess(location);
                } else {
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

}
