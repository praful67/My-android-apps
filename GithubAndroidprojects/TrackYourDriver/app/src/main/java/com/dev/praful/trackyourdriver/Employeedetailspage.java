package com.dev.praful.trackyourdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Employeedetailspage extends AppCompatActivity {

    String employeeid;
    String lat, lng;
    LatLng latLng;
    double distance = 0.01;
    boolean gotnearestone = false;
    final double limit = 10000;
    TextView nameofNE;
    TextView addressofNE;
    ProgressDialog progressDialog;
    TextView eidNE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeedetailspage);
        Button ok = (Button) findViewById(R.id.ok);

      /*  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();*/

     /*   nameofNE = (TextView) findViewById(R.id.nameofnearestemployee);
        addressofNE = (TextView) findViewById(R.id.addreeofNE);
        eidNE = (TextView) findViewById(R.id.employeeidogNE);
*/
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
        final TextView CS = (TextView) findViewById(R.id.currentstatus);
        final TextView gender = (TextView) findViewById(R.id.gender);

        if (getIntent().getStringExtra("employeeid") != null) {
            employeeid = getIntent().getStringExtra("employeeid");
            String id = employeeid;
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
        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(employeeid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                            if (employeedetails != null) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                if (map != null)
                                    gender.setText(String.valueOf(map.get("gender")));

                                username.setText(employeedetails.getUsername());
                                password.setText(employeedetails.getPassword());
                                bg.setText(employeedetails.getBloodgroup());
                                phone.setText(employeedetails.getPhone());
                                Eid.setText(employeedetails.getEmployee_Id());
                                if (employeedetails.getPick_up_address() != null) {
                                    if (TextUtils.isEmpty(employeedetails.getPick_up_address()))
                                        address.setText("No address");
                                    else
                                        address.setText(employeedetails.getPick_up_address());
                                }
                            }
/*
                            lat = employeedetails.getAddresslat();
                            lng = employeedetails.getAddresslng();
                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        */    //  loadnearestemployess(latLng);

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


    }

    private void loadnearestemployess(final LatLng location) {


        GeoFire gf = new GeoFire(FirebaseDatabase.getInstance().getReference("Employee's addresses"));

        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), distance);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location1) {
                if (!gotnearestone) {
                    if (!key.equals(employeeid)) {
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
                                              /*  nameofNE.setText(employeedetails.getUsername());
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


                    } else {
                        distance = distance + .01;
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
                    distance = distance + .01;
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
