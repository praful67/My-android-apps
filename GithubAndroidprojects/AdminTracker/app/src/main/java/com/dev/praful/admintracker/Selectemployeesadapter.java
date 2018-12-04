package com.dev.praful.admintracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Selectemployeesadapter extends BaseAdapter {

    String carId;
    View view;
    ArrayList<Orderedemployeedetails> arrayList;
    List<Orderedemployeedetails> Orderedemployeedetails;
    Activity activity;

    public Selectemployeesadapter(Activity activity, List<Orderedemployeedetails> Orderedemployeedetails, String carId) {
        this.activity = activity;
        this.Orderedemployeedetails = Orderedemployeedetails;
        this.arrayList = new ArrayList<Orderedemployeedetails>();
        this.arrayList.addAll(Orderedemployeedetails);
        this.carId = carId;

    }

    LayoutInflater inflater;

    @Override
    public int getCount() {
        return Orderedemployeedetails.size();
    }

    @Override
    public Object getItem(int position) {
        return Orderedemployeedetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = convertView;
        view = inflater.inflate(R.layout.selectemployeeitem, null);
        final Button check = (Button) view.findViewById(R.id.checked);
        final TextView textView = (TextView) view.findViewById(R.id.textselected);
        final TextView textselectedD = (TextView) view.findViewById(R.id.textselectedD);
        if (Orderedemployeedetails.get(position) != null && Orderedemployeedetails.size() > 0) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Employee's Car")
                    .child(Orderedemployeedetails.get(position).getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();

                                if (map != null) {
                                    if (map.get("car id") != null) {
                                        if (carId.equals(String.valueOf(map.get("car id")))) {
                                            textView.setVisibility(View.VISIBLE);
                                            textView.setTextColor(Color.GREEN);
                                            check.setText("UNSELECT");
                                            check.setEnabled(true);
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("employeeselection")
                                                    .child(Orderedemployeedetails.get(position).getId())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot != null) {
                                                                String value = dataSnapshot.getValue(String.class);
                                                                if (value != null) {
                                                                    if (value.equals("Selected")) {
                                                                        check.setText("UNSELECT");
                                                                        textView.setVisibility(View.VISIBLE);
                                                                        textView.setTextColor(Color.GRAY);


                                                                    } else {
                                                                        check.setText("SELECT");
                                                                        textView.setVisibility(View.INVISIBLE);

                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                        } else if (!carId.equals(String.valueOf(map.get("car id")))) {

                                            if (String.valueOf(map.get("checkselection")).equals("selected")) {
                                                textselectedD.setVisibility(View.VISIBLE);
                                                check.setText("SELECT");
                                                check.setEnabled(false);
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("employeeselection")
                                                        .child(Orderedemployeedetails.get(position).getId())
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot != null) {
                                                                    String value = dataSnapshot.getValue(String.class);
                                                                    if (value != null) {
                                                                        if (value.equals("Selected")) {
                                                                            check.setText("UNSELECT");
                                                                            textView.setVisibility(View.VISIBLE);
                                                                            textView.setTextColor(Color.GRAY);


                                                                        } else {
                                                                            check.setText("SELECT");
                                                                            textView.setVisibility(View.INVISIBLE);

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
                                }  else {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("employeeselection")
                                            .child(Orderedemployeedetails.get(position).getId())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        String value = dataSnapshot.getValue(String.class);
                                                        if (value != null) {
                                                            if (value.equals("Selected")) {
                                                                check.setText("UNSELECT");
                                                                textView.setVisibility(View.VISIBLE);
                                                                textView.setTextColor(Color.GRAY);


                                                            } else {
                                                                check.setText("SELECT");
                                                                textView.setVisibility(View.INVISIBLE);

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

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.scale_in_slow);
        if (sharedPreferences.getString("anim", "").equals("yes")) {

            view.startAnimation(animation);
            if (String.valueOf(getItemId(position)).equals(String.valueOf(Orderedemployeedetails.size() - 1))) {
                editor.putString("anim", "no");
                editor.apply();
            }

        }
        final TextView employeename = (TextView) view.findViewById(R.id.name);

        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(Orderedemployeedetails.get(position).getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Employeedetails Employeedetails = dataSnapshot.getValue(Employeedetails.class);
                    if (Employeedetails != null) {
                        employeename.setText(Employeedetails.getUsername());
                        FirebaseDatabase.getInstance().getReference()
                                .child("Final Ordered Employees")
                                .child(Orderedemployeedetails.get(position).getId())
                                .child("name")
                                .setValue(Employeedetails.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final TextView distance = (TextView) view.findViewById(R.id.distance);

        FirebaseDatabase.getInstance().getReference()
                .child("Final Ordered Employees")
                .child(Orderedemployeedetails.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        distance.setText(Orderedemployeedetails.get(position).getDistance() + " " + String.valueOf(map.get("time")) + " far from Office");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        TextView rank = (TextView) view.findViewById(R.id.rank);
        rank.setText(String.valueOf(getItemId(position) + 1));
        Button details = (Button) view.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Employeedetailspage.class);
                intent.putExtra("employeeid", Orderedemployeedetails.get(position).getId());
                activity.startActivity(intent);
            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.getText().equals("SELECT")) {
                    android.support.v7.app.AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                    else
                        builder = new android.support.v7.app.AlertDialog.Builder(activity);

                    builder.setMessage("Do you want Select this employee to this car ? ")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", employeename.getText().toString());
                                    map.put("selection", "Selected");
                                    map.put("id", Orderedemployeedetails.get(position).getId());
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Car employees")
                                            .child(carId)
                                            .child(Orderedemployeedetails.get(position).getId())
                                            .setValue(map);
                                    if (CommonSwitch.switch1.isChecked()) {
                                        CommonSwitch.switch1.setChecked(false);
                                    } else {
                                        CommonSwitch.switch1.setChecked(true);
                                    }

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("employeeselection")
                                            .child(Orderedemployeedetails.get(position).getId())
                                            .setValue("Selected").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            check.setText("UNSELECT");
                                            dialog.dismiss();
                                            textView.setVisibility(View.VISIBLE);
                                            Toast.makeText(activity, "Selected the Employee", Toast.LENGTH_SHORT).show();

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

                } else if (check.getText().equals("UNSELECT")) {
                    android.support.v7.app.AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                    else
                        builder = new android.support.v7.app.AlertDialog.Builder(activity);

                    builder.setMessage("Do you want Unselect this employee to this car ? ")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", employeename.getText().toString());
                                    map.put("selection", "NotSelected");
                                    map.put("id", Orderedemployeedetails.get(position).getId());
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Car employees")
                                            .child(carId)
                                            .child(Orderedemployeedetails.get(position).getId())
                                            .setValue(map);
                                    if (CommonSwitch.switch1.isChecked()) {
                                        CommonSwitch.switch1.setChecked(false);
                                    } else {
                                        CommonSwitch.switch1.setChecked(true);
                                    }
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("employeeselection")
                                            .child(Orderedemployeedetails.get(position).getId())
                                            .setValue("unselected")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialog.dismiss();
                                                    check.setText("SELECT");
                                                    textView.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(activity, "Unselected the Employee", Toast.LENGTH_SHORT).show();

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
            }
        });


        return view;
    }

    public void filter(String chartext) {

        chartext = chartext.toLowerCase(Locale.getDefault());
        Orderedemployeedetails.clear();
        if (chartext.length() == 0) {
            Orderedemployeedetails.addAll(arrayList);
        } else {
            for (Orderedemployeedetails orderedemployeedetails : arrayList) {
                if (orderedemployeedetails.getName().toLowerCase(Locale.getDefault()).contains(chartext)) {
                    Orderedemployeedetails.add(orderedemployeedetails);
                }
            }
        }
        notifyDataSetChanged();

    }

}
/*
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
              */
/*  if (Orderedemployeedetails.get(position) != null && Orderedemployeedetails.size() > 0) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Employee's Car")
                            .child(Orderedemployeedetails.get(position).getId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();

                                        if (map != null) {
                                            if (carId.equals(String.valueOf(map.get("car id"))) && map.get("car id") != null) {
                                                textView.setVisibility(View.VISIBLE);
                                                textView.setTextColor(Color.GREEN);
                                                check.setText("UNSELECT");
                                                check.setEnabled(true);
                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }*//*

            }
        }.start();
*/
