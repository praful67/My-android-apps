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

public class Finalcaremployeesadapter extends BaseAdapter {


    List<Finalcaremployees> finalcaremployees;
    Activity activity;
    String time;
    String carid;

    public Finalcaremployeesadapter(List<Finalcaremployees> finalcaremployees, Activity activity) {
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
        view = inflater.inflate(R.layout.finalcaremployeeitem, null);
        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView distance = (TextView) view.findViewById(R.id.distance);
        final TextView rank = (TextView) view.findViewById(R.id.rank);
        final TextView editedrank = (TextView) view.findViewById(R.id.editedrank);
        final TextView pickuptime = (TextView) view.findViewById(R.id.pickuptime);
        CardView detailslayout = (CardView) view.findViewById(R.id.detailslayout);
        detailslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Employeedetailspage.class);
                intent.putExtra("employeeid", finalcaremployees.get(position).getId());
                activity.startActivity(intent);
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
                .child("current selected car")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            carid = String.valueOf(map.get("carId"));
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Employee's Rank").child(carid).child(finalcaremployees.get(position).getId())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                            if (map != null) {
                                                editedrank.setText(String.valueOf(map.get("rank")));
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
        FirebaseDatabase.getInstance().getReference()
                .child("Final Rank of employee").child(finalcaremployees.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            rank.setText(String.valueOf(map.get("Rank")));
                            rank.setTextColor(Color.GREEN);
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
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (orderedemployeedetails != null)
                                distance.setText(orderedemployeedetails.getDistance().toString() + " " + String.valueOf(map.get("time")) + " far from Office ");
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
                    Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                    name.setText(Driverdetails.getUsername());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
