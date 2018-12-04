package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetRosterdetails extends AppCompatActivity {

    String rostersetid;
    private DateRangeCalendarView calendarA;

    android.support.v7.widget.Toolbar mToolbar;
    String ok = "yes";

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
        setContentView(R.layout.activity_set_rosterdetails);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rostersetid = getIntent().getStringExtra("rostersetid");
        if (rostersetid != null) {
            Button cancel = (Button) findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            Button done = (Button) findViewById(R.id.done);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
            month = month + 1;
            String monthh = null;
            switch (month)

            {
                case 1:
                    monthh = "January";
                    break;
                case 2:
                    monthh = "February";
                    break;
                case 3:
                    monthh = "March";
                    break;
                case 4:
                    monthh = "April";
                    break;
                case 5:
                    monthh = "May";
                    break;
                case 6:
                    monthh = "June";
                    break;
                case 7:
                    monthh = "July";
                    break;
                case 8:
                    monthh = "August";
                    break;
                case 9:
                    monthh = "September";
                    break;
                case 10:
                    monthh = "October";
                    break;
                case 11:
                    monthh = "November";
                    break;
                case 12:
                    monthh = "December";

                    break;


            }

            Button editname = (Button) findViewById(R.id.editit);
            final EditText editedname = (EditText) findViewById(R.id.editedname);
            editname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(editedname.getText().toString())) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("List of Rosters Sets")
                                .child(rostersetid)
                                .child("name")
                                .setValue(editedname.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SetRosterdetails.this, "Edited", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(SetRosterdetails.this, "Please type a name", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            final TextView text1 = new TextView(this);
            calendarA = findViewById(R.id.calendar);
            final TextView finaldates = (TextView) findViewById(R.id.finaldates);
            ImageView clear = (ImageView) findViewById(R.id.clear);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finaldates.setText("");
                    text1.setText("");
                }
            });
            Calendar now = Calendar.getInstance();
            now.add(Calendar.MONTH, 0);
            Calendar later = (Calendar) now.clone();
            later.add(Calendar.MONTH, 50);

            calendarA.setVisibleMonthRange(now, later);

            calendarA.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
                @Override
                public void onFirstDateSelected(Calendar startDate) {

                }

                @Override
                public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                    Calendar calendar = Calendar.getInstance();

                    String startdate = startDate.getTime().toString();
                    String enddate = endDate.getTime().toString();

                    if (startDate.get(Calendar.DATE) >= calendar.get(Calendar.DATE)) {
                        String st = startdate.substring(0, 10) + " , " + startdate.substring(30, 34);
                        String end = enddate.substring(0, 10) + " , " + enddate.substring(30, 34);
                        if (st.equals(end))
                            text1.append(st + " | \n");
                        else {

                            text1.append(st + " to " + end + " | \n");
                        }
                        finaldates.setText(text1.getText().toString());

                    } else {
                        Toast.makeText(SetRosterdetails.this, "Please select valid Dates", Toast.LENGTH_SHORT).show();
                    }
                }

            });
            final Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String currentDateTimeString = sdf.format(d);
            final String current_time = (currentDateTimeString);


            final String dateofsubmit = monthh + " " + day_of_month + " , " + year;
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait");
            done.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    if (!finaldates.getText().toString().equals("")) {
                        progressDialog.show();
                        Map<String, Object> map = new HashMap<>();
                        map.put("dates", finaldates.getText());
                        map.put("dateofchange", dateofsubmit + " " + current_time);
                        FirebaseDatabase.getInstance().getReference()
                                .child("List of Rosters Sets")
                                .child(rostersetid)
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SetRosterdetails.this, "Done", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    } else {
                        Toast.makeText(SetRosterdetails.this, "Please select date", Toast.LENGTH_SHORT).show();
                    }
                }


            });

            final TextView currentdates = (TextView) findViewById(R.id.currentdates);
            final TextView editedon = (TextView) findViewById(R.id.editedon);
            FirebaseDatabase.getInstance().getReference()
                    .child("List of Rosters Sets")
                    .child(rostersetid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                if (map != null) {
                                    editedname.setHint(String.valueOf(map.get("name")));
                                    if (map.get("dates") != null) {
                                        currentdates.setText(String.valueOf(map.get("dates")));
                                        editedon.setText(String.valueOf(map.get("dateofchange")));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            Button publish = (Button) findViewById(R.id.publishnow);
            publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cars Info")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            CarsInfo carsInfo1 = dataSnapshot1.getValue(CarsInfo.class);

                                            if (carsInfo1 != null && carsInfo1.getListid() != null) {
                                                if (carsInfo1.getListid().equals(rostersetid)) {
                                                    final String carid = carsInfo1.getId();
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Employee's Car")
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();

                                                                        if (map != null) {
                                                                            if (String.valueOf(map.get("car id")).equals(carid)) {
                                                                                FirebaseDatabase.getInstance().getReference()
                                                                                        .child("SignedEmployees")
                                                                                        .child(dataSnapshot1.getKey())
                                                                                        .addValueEventListener(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                Employeedetails employeedetails1 = dataSnapshot.getValue(Employeedetails.class);
                                                                                                if (employeedetails1 != null) {
                                                                                                    String id = employeedetails1.getId();
                                                                                                    FirebaseDatabase.getInstance().getReference()
                                                                                                            .child("Updates")
                                                                                                            .child(id)
                                                                                                            .child("whattodo")
                                                                                                            .setValue("update").addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                            ok = "no";
                                                                                                        }
                                                                                                    });

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
                                                            .child(carid).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (dataSnapshot != null) {
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                                                    if (map != null) {
                                                                        if (String.valueOf(map.get("car id")).equals(carid)) {
                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("SignedDrivers")
                                                                                    .child(dataSnapshot1.getKey())
                                                                                    .addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                                                            if (Driverdetails != null) {
                                                                                                String driverid = Driverdetails.getId();
                                                                                                FirebaseDatabase.getInstance().getReference()
                                                                                                        .child("DriverUpdates")
                                                                                                        .child(driverid)
                                                                                                        .child("whattodo")
                                                                                                        .setValue("update");


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
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Driver's Car")
                                                            .child("login")
                                                            .child(carid).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (dataSnapshot != null) {
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                                                    if (map != null) {
                                                                        if (String.valueOf(map.get("car id")).equals(carid)) {
                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("SignedDrivers")
                                                                                    .child(dataSnapshot1.getKey())
                                                                                    .addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                                                            if (Driverdetails != null) {
                                                                                                String driverid = Driverdetails.getId();
                                                                                                FirebaseDatabase.getInstance().getReference()
                                                                                                        .child("DriverUpdates")
                                                                                                        .child(driverid)
                                                                                                        .child("whattodo")
                                                                                                        .setValue("update");


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
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            }
                                        }
                                    }


                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    new CountDownTimer(4000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {

                            progressDialog.dismiss();
                            if (ok.equals("yes")) {
                                Toast.makeText(SetRosterdetails.this, "Publication done", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SetRosterdetails.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.start();


                }
            });

        }
    }
}
