package com.dev.praful.admintracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Editfinalcaremployeesadapter extends BaseAdapter {
    List<Finalcaremployees> finalcaremployees;
    Activity activity;
    String carid;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    String time;

    public Editfinalcaremployeesadapter(List<Finalcaremployees> finalcaremployees, Activity activity) {
        this.finalcaremployees = finalcaremployees;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return finalcaremployees.size();
    }

    @Override
    public Object getItem(int position) {
        return finalcaremployees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = convertView;
        view = inflater.inflate(R.layout.editfinalcaremployeeitem, null);
        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView number = (TextView) view.findViewById(R.id.number);
        final TextView distance = (TextView) view.findViewById(R.id.distance);
        FirebaseDatabase.getInstance().getReference()
                .child("Final Rank of employee").child(finalcaremployees.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            number.setText(String.valueOf(map.get("Rank")));
                            number.setTextColor(Color.GREEN);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Button settime = (Button) view.findViewById(R.id.settime);
        final TextView pickuptime = (TextView) view.findViewById(R.id.pickuptime);
        CardView cardView = (CardView) view.findViewById(R.id.details);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Employeedetailspage.class);
                intent.putExtra("employeeid", finalcaremployees.get(position).getId());
                activity.startActivity(intent);
            }
        });
        Button confirmtime = (Button) view.findViewById(R.id.confirmtime);
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
                if (hourOfDay > 12)
                    hourOfDay = hourOfDay - 12;

                String minute1 = null;
                if (minute < 10) {

                    minute1 = "0" + String.valueOf(minute);
                } else
                    minute1 = String.valueOf(minute);

                String hour1 = null;
                if (hourOfDay < 10) {
                    hour1 = "0" + String.valueOf(hourOfDay);
                } else
                    hour1 = String.valueOf(hourOfDay);

                time = hour1 + ":" + minute1 + " " + AM_PM;
                //time = hourOfDay + ":" + minute + " " + AM_PM;
                Toast.makeText(activity, time, Toast.LENGTH_SHORT).show();
                pickuptime.setText(time);
                pickuptime.setTextColor(Color.GRAY);


            }
        };
        Calendar calendar = Calendar.getInstance();
        final TimePickerDialog dialog = new TimePickerDialog(
                activity, android.R.style.Theme_Holo_Dialog_MinWidth, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
        );

        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.show();

            }
        });

        final android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new android.support.v7.app.AlertDialog.Builder(activity);
        final ProgressDialog progressDialog = new ProgressDialog(activity);

        confirmtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("Confirm pick up time ? ")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                progressDialog.setMessage("Please wait..");
                                progressDialog.show();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Employeespickuptimes")
                                        .child(finalcaremployees.get(position).getId())
                                        .child("time")
                                        .setValue(time)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(activity, "Done !", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                dialog.dismiss();

                                            }
                                        });
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                builder.show();


            }
        });
        final TextView timeoffE;
        final LinearLayout TF;
        timeoffE = (TextView) view.findViewById(R.id.timeoffE);
        TF = (LinearLayout) view.findViewById(R.id.timeofflayout);

        Button cancel_timeoff = (Button) view.findViewById(R.id.canceltimeoff);

        FirebaseDatabase.getInstance().getReference()
                .child("StatusE")
                .child(finalcaremployees.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (!String.valueOf(map.get("status")).equals("Active")) {
                                    TF.setVisibility(View.VISIBLE);
                                    timeoffE.setText(String.valueOf(map.get("status")));
                                } else {
                                    TF.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        cancel_timeoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                else
                    builder = new android.support.v7.app.AlertDialog.Builder(activity);

                builder.setMessage("Do you want to cancel this timeoff ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("StatusE")
                                        .child(finalcaremployees.get(position).getId())
                                        .child("status")
                                        .setValue("Active")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show();
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("CancelledTimeoff")
                                                        .child(finalcaremployees.get(position).getId())
                                                        .child("timeoff")
                                                        .setValue("cancelled");
                                                dialog.dismiss();
                                            }
                                        });

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

        FirebaseDatabase.getInstance().getReference()
                .child("Employeespickuptimes")
                .child(finalcaremployees.get(position).getId())
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
                .child("Final Ordered Employees")
                .child(finalcaremployees.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Orderedemployeedetails orderedemployeedetails = dataSnapshot.getValue(Orderedemployeedetails.class);
                            if (orderedemployeedetails != null)
                                distance.setText(orderedemployeedetails.getDistance().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(finalcaremployees.get(position).getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Employeedetails Employeedetails = dataSnapshot.getValue(Employeedetails.class);
                    name.setText(Employeedetails.getUsername());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
 /* changerank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", finalcaremployees.get(position).getId());
                map.put("rank", rankedit.getText().toString());
                FirebaseDatabase.getInstance().getReference()
                        .child("Employee's Rank")
                        .child(carid)
                        .child(finalcaremployees.get(position).getId())
                        .setValue(map);
                rank.setText(rankedit.getText().toString());
            }
        });*/