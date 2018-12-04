package com.dev.praful.trackyourdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    String id;
    String lat, lng;
    LatLng latLng;
    double distance = 0.1;
    boolean gotnearestone = false;
    final double limit = 10000;
    TextView nameofNE;
    TextView addressofNE;
    TextView eidNE;
    ProgressDialog progressDialog;
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
        setContentView(R.layout.activity_profile);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        AccelerateDecelerateInterpolator a = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000, a);
        KenBurnsView image = (KenBurnsView) findViewById(R.id.image);
        image.setTransitionGenerator(generator);

       /* nameofNE = (TextView) findViewById(R.id.nameofnearestemployee);
        addressofNE = (TextView) findViewById(R.id.addreeofNE);
        eidNE = (TextView) findViewById(R.id.employeeidogNE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting your nearest employee");
        progressDialog.show();*/
        FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.edit);
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

        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        final TextView bg = (TextView) findViewById(R.id.bg);
        final TextView phone = (TextView) findViewById(R.id.phone);
        final TextView Eid = (TextView) findViewById(R.id.Eid);
        final TextView address = (TextView) findViewById(R.id.address);
        final TextView gender = (TextView) findViewById(R.id.gender);

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
                                username.setText(employeedetails.getUsername());
                                password.setText(employeedetails.getPassword());
                                bg.setText(employeedetails.getBloodgroup());
                                phone.setText(employeedetails.getPhone());
                                Eid.setText(employeedetails.getEmployee_Id());
                                if (map != null)
                                    gender.setText(String.valueOf(map.get("gender")));
                                if (employeedetails.getPick_up_address() != null) {
                                    if (employeedetails.getPick_up_address().length() != 0) {
                                        if (TextUtils.isEmpty(employeedetails.getPick_up_address()))
                                            address.setText("No address");
                                        else
                                            address.setText(employeedetails.getPick_up_address());
                                    }

                                }
                            }
                         /*   lat = employeedetails.getAddresslat();
                            lng = employeedetails.getAddresslng();
                            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                         */   //   loadnearestemployess(latLng);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
