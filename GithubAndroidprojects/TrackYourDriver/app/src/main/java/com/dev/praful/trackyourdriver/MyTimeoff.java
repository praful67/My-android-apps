package com.dev.praful.trackyourdriver;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
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


public class MyTimeoff extends AppCompatActivity {

    String id;
    String name;
    private DateRangeCalendarView calendarA;


    android.support.v7.widget.Toolbar mToolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timeoff);
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Button cancel = (Button) findViewById(R.id.cancel);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button submit = (Button) findViewById(R.id.submit);
        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                            if (employeedetails != null) {
                                name = employeedetails.getUsername();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final MaterialEditText message = (MaterialEditText) findViewById(R.id.message);

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
        final TextView text1 = new TextView(this);
        final ArrayList<String> date = new ArrayList<>();
        if (date.size() > 0)
            date.clear();
        final ArrayList<String> dummytext = new ArrayList<>();
        final DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        for (int i = 0; i < 366; i++) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.DATE, i);
            Date newDate = calendar1.getTime();

            date.add((format.format(newDate)));

        }
        calendarA = findViewById(R.id.calendar);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.exoreg);
        calendarA.setFonts(typeface);
        TextView mydayoff = (TextView) findViewById(R.id.mydayoff);
        ImageView clear = (ImageView) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydayoff.setText("");
                text1.setText("");
                dummytext.clear();
            }
        });
        calendarA.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {

            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                Calendar calendar = Calendar.getInstance();

                String startdate = startDate.getTime().toString();
                String enddate = endDate.getTime().toString();

                if (startDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                    if (startDate.get(Calendar.DATE) >= calendar.get(Calendar.DATE)) {
                        String date1 = format.format(startDate.getTime());
                        String date2 = format.format(endDate.getTime());
                        String st = startdate.substring(0, 10) + " , " + startdate.substring(30, 34);
                        String end = enddate.substring(0, 10) + " , " + enddate.substring(30, 34);
                        if (st.equals(end))
                            text1.append(st + " | \n");
                        else {

                            text1.append(st + " to " + end + " | \n");
                         }
                        if (st.equals(end))

                            dummytext.add((date1));
                        else {
                            dummytext.add((date1));
                            dummytext.add((date2));
                        }
                        int k = 0;
                        for (int i = 0; i < dummytext.size(); i++) {

                            for (int j = 0; j < 366; j++) {
                                if (date.get(j).equals(dummytext.get(i))) {
                                    k++;
                                }
                            }

                        }
                        if (k == dummytext.size()) {
                            mydayoff.setText(text1.getText().toString());
                        } else {
                            Toast.makeText(MyTimeoff.this, "Invalid Dates", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MyTimeoff.this, "Please select valid Dates", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    String date1 = format.format(startDate.getTime());
                    String date2 = format.format(endDate.getTime());
                    String st = startdate.substring(0, 10) + " , " + startdate.substring(30, 34);
                    String end = enddate.substring(0, 10) + " , " + enddate.substring(30, 34);
                    if (st.equals(end))
                        text1.append(st + " | \n");
                    else {

                        text1.append(st + " to " + end + " | \n");
                    }
                    if (st.equals(end))

                        dummytext.add((date1));
                    else {
                        dummytext.add((date1));
                        dummytext.add((date2));
                    }
                    int k = 0;
                    for (int i = 0; i < dummytext.size(); i++) {

                        for (int j = 0; j < 366; j++) {
                            if (date.get(j).equals(dummytext.get(i))) {
                                k++;
                            }
                        }

                    }
                    if (k == dummytext.size()) {
                        mydayoff.setText(text1.getText().toString());
                    } else {
                        Toast.makeText(MyTimeoff.this, "Invalid Dates", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        String current_time = (currentDateTimeString);


        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, 0);
        Calendar later = (Calendar) now.clone();
        later.add(Calendar.MONTH, 50);

        calendarA.setVisibleMonthRange(now, later);

        Calendar startSelectionDate = Calendar.getInstance();
        startSelectionDate.add(Calendar.DATE, -1);
        Calendar endSelectionDate = (Calendar) startSelectionDate.clone();
        endSelectionDate.add(Calendar.DATE, 40);

        // calendarA.setSelectedDateRange(startSelectionDate, endSelectionDate);
        FirebaseDatabase.getInstance().getReference().child("Employee'sTimeoffs1")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map1 = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map1 != null) {
                                String number = String.valueOf(map1.get("number"));
                                num = Integer.parseInt(number);
                                num++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final String dateofsubmit = monthh + " " + day_of_month + " , " + year;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        submit.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (!mydayoff.getText().toString().equals("")) {
                    if (!(MyTimeoff.this.isFinishing()))
                        progressDialog.show();
                    FirebaseDatabase.getInstance().
                            getReference().child("Employee'sTimeoffs1").child("number").setValue(String.valueOf(num));


                    Map<String, Object> map = new HashMap<>();
                    map.put("date", mydayoff.getText().toString());
                    map.put("message", message.getText().toString());
                    map.put("dateofsubmit", dateofsubmit + " " + current_time);
                    map.put("name", name);
                    map.put("id", id);
                    FirebaseDatabase.getInstance().getReference()
                            .child("Employee'sTimeoffs1")
                            .child(id)
                            .setValue(map);
                    FirebaseDatabase.getInstance().getReference()
                            .child("ETimeoff")
                            .child(id)
                            .child("Updated")
                            .setValue("yes");
                    FirebaseDatabase.getInstance().getReference()
                            .child("ETimeoffsDatesReminder")
                            .child(id).removeValue();
                    for (int i = 0; i < dummytext.size(); i++) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("ETimeoffsDatesReminder")
                                .child(id)
                                .child(UUID.randomUUID().toString())
                                .child("date")
                                .setValue(dummytext.get(i));


                    }
                    FirebaseDatabase.getInstance().getReference()
                            .child("StatusE")
                            .child(id)
                            .child("status")
                            .setValue(mydayoff.getText().toString());

                    FirebaseDatabase.getInstance().getReference()
                            .child("Employee'sTimeoffs")
                            .child(id)
                            .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            new CountDownTimer(3000, 1000) {
                                @Override
                                public void onTick(long l) {

                                }

                                @Override
                                public void onFinish() {
                                    if (!(MyTimeoff.this.isFinishing()))
                                        progressDialog.dismiss();
                                    Toast.makeText(MyTimeoff.this, "Submited", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }.start();

                        }
                    });

                } else {
                    Toast.makeText(MyTimeoff.this, "Please select date", Toast.LENGTH_SHORT).show();
                }
            }


        });

        final TextView previoustimeoff = (TextView) findViewById(R.id.previoustimeoff);

        FirebaseDatabase.getInstance().

                getReference()
                .
                        child("Employee'sTimeoffs")
                .

                        child(id)
                .
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                    if (map != null) {
                                        String date = String.valueOf(map.get("date"));
                                        String dateofsubmit = String.valueOf(map.get("dateofsubmit"));
                                        String message = String.valueOf(map.get("message"));

                                        previoustimeoff.setText("Date of submit : " + dateofsubmit + "\nTime off : " + date + "\nMessage : " + message);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

    }
}
