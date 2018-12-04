package com.dev.praful.admintracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class
Selectdriverlistadapter extends BaseAdapter {
    Activity activity;
    String carId, state;
    java.util.List<DriversInfo> DriversInfo;
    ArrayList<DriversInfo> arrayList;
    Driverdetails driverdetails;

    public Selectdriverlistadapter(Activity activity, List<DriversInfo> DriversInfo, String carId, String state) {
        this.activity = activity;
        this.DriversInfo = DriversInfo;
        this.arrayList = new ArrayList<DriversInfo>();
        this.arrayList.addAll(DriversInfo);
        this.carId = carId;
        this.state = state;
    }

    LayoutInflater inflater;


    @Override
    public int getCount() {
        return DriversInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return DriversInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        inflater = activity.getLayoutInflater();

        View itemView = convertView;
        itemView = inflater.inflate(R.layout.selectdriveritem, null);
        final TextView drivername = (TextView) itemView.findViewById(R.id.name);
        Button details = (Button) itemView.findViewById(R.id.details);
        final TextView textselectedD = (TextView) itemView.findViewById(R.id.textselectedD);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Driverdetailspage.class);
                intent.putExtra("Driverid", DriversInfo.get(position).getId());
                activity.startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.scale_in_slow);
        if (sharedPreferences.getString("anim", "").equals("yes")) {

            itemView.startAnimation(animation);
            if (String.valueOf(getItemId(position)).equals(String.valueOf(DriversInfo.size() - 1))) {
                editor.putString("anim", "no");
                editor.apply();
            }

        }

        FirebaseDatabase.getInstance().getReference()
                .child("SignedDrivers")
                .child(DriversInfo.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        driverdetails = dataSnapshot.getValue(Driverdetails.class);
                        if (driverdetails != null)
                            drivername.setText(driverdetails.getUsername());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final Button check = (Button) itemView.findViewById(R.id.check);
        final TextView textView = (TextView) itemView.findViewById(R.id.textselected);

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (DriversInfo != null && DriversInfo.size() > 0) {

                 /*   FirebaseDatabase.getInstance().getReference()
                            .child("Driver's Car")
                            .child(DriversInfo.get(position).getId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                    if (map != null) {
                                        if (String.valueOf(map.get("checkselection")).equals("selected")) {
                                            textView.setVisibility(View.VISIBLE);

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                */
                    if (carId != null && state != null) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Driver's Car")
                                .child(state)
                                .child(carId)
                                .child(DriversInfo.get(position).getId())
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

                                                } else if (!carId.equals(String.valueOf(map.get("car id")))) {

                                                    if (String.valueOf(map.get("checkselection")).equals("selected")) {
                                                        textselectedD.setVisibility(View.VISIBLE);
                                                        check.setText("SELECT");
                                                        check.setEnabled(true);

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
        }.start();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.getText().equals("SELECT")) {
                    android.support.v7.app.AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                    else
                        builder = new android.support.v7.app.AlertDialog.Builder(activity);

                    builder.setMessage("Do you want to Select this driver to this car ?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("selection", "Selected");
                                    map.put("id", DriversInfo.get(position).getId());
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Car Drivers")
                                            .child(state)
                                            .child(carId)
                                            .child(DriversInfo.get(position).getId())
                                            .setValue(map);
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Driverselection")
                                            .child(state)
                                            .child(DriversInfo.get(position).getId())
                                            .setValue("Selected");
                                    if (CommonSwitch.switch2.isChecked()) {
                                        CommonSwitch.switch2.setChecked(false);
                                    } else {
                                        CommonSwitch.switch2.setChecked(true);
                                    }

                                    dialog.dismiss();
                                    textView.setVisibility(View.VISIBLE);
                                    check.setText("UNSELECT");
                                    Toast.makeText(activity, "Selected the Driver", Toast.LENGTH_SHORT).show();

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

                    builder.setMessage("Do you want to Unselect this driver to this car ?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("selection", "UnSelected");
                                    map.put("id", DriversInfo.get(position).getId());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Car Drivers")
                                            .child(state)
                                            .child(carId)
                                            .child(DriversInfo.get(position).getId())
                                            .setValue(map);
                                    if (CommonSwitch.switch2.isChecked()) {
                                        CommonSwitch.switch2.setChecked(false);
                                    } else {
                                        CommonSwitch.switch2.setChecked(true);
                                    }

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Driverselection")
                                            .child(state)
                                            .child(DriversInfo.get(position).getId())
                                            .setValue("unselected");
                                    dialog.dismiss();
                                    textView.setVisibility(View.INVISIBLE);
                                    check.setText("SELECT");
                                    Toast.makeText(activity, "Unselected the Driver", Toast.LENGTH_SHORT).show();

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

        return itemView;

    }

    public void filter(String chartext) {
        chartext = chartext.toLowerCase(Locale.getDefault());
        DriversInfo.clear();
        if (chartext.length() == 0) {
            DriversInfo.addAll(arrayList);
        } else {
            for (DriversInfo driversInfo : arrayList) {
                if (driversInfo.getName().toLowerCase(Locale.getDefault()).contains(chartext)) {
                    DriversInfo.add(driversInfo);
                }
            }
        }
        notifyDataSetChanged();

    }

}


