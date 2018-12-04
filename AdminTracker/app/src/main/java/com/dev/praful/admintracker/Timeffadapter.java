package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Timeffadapter extends BaseAdapter {

    List<Timeoff> timeoffList;
    Activity activity;
    ArrayList<Timeoff> arrayList;

    public Timeffadapter(List<Timeoff> timeoffList, Activity activity) {
        this.timeoffList = timeoffList;
        this.activity = activity;
        this.arrayList = new ArrayList<Timeoff>();
        this.arrayList.addAll(timeoffList);

    }

    @Override
    public int getCount() {
        return timeoffList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeoffList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.timeoffitem, null);
            final TextView name = (TextView) view.findViewById(R.id.employeename);
            final ExpandableTextView timeoff = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
            name.setText(timeoffList.get(position).getName());
            String date = timeoffList.get(position).getDate();
            String dateofsubmit = timeoffList.get(position).getDateofsubmit();
            String message = timeoffList.get(position).getMessage();

            timeoff.setText("Date of submit : " + dateofsubmit + "\n\n" + "Time off Date : " + date + "\n\n Message : " + message);

            Button details = (Button) view.findViewById(R.id.employeedetails);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity.getBaseContext(), Employeedetailspage.class);
                    intent.putExtra("employeeid", timeoffList.get(position).getId());
                    activity.startActivity(intent);
                }
            });
            ImageView done = (ImageView) view.findViewById(R.id.done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                    else
                        builder = new android.support.v7.app.AlertDialog.Builder(activity);

                    builder.setMessage("Do you want move this to past time offs ? ")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    Timeoff timeoff1 = new Timeoff(timeoffList.get(position).getDate(), timeoffList.get(position).getName()
                                            , timeoffList.get(position).getDateofsubmit(), timeoffList.get(position).getMessage(),
                                            timeoffList.get(position).getId()
                                    );
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("PastTImeoffs")
                                            .child(timeoffList.get(position).getId())
                                            .setValue(timeoff1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialog.dismiss();
                                                    Toast.makeText(activity, "Moved !!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Employee'sTimeoffs1")
                                            .child(timeoffList.get(position).getId())
                                            .removeValue();


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
        return view;
    }

    public void filter(String chartext) {
        chartext = chartext.toLowerCase(Locale.getDefault());
        timeoffList.clear();
        if (chartext.length() == 0) {
            timeoffList.addAll(arrayList);
        } else {
            for (Timeoff Timeoff : arrayList) {
                if (Timeoff.getName().toLowerCase(Locale.getDefault()).contains(chartext)) {
                    timeoffList.add(Timeoff);
                }
            }
        }
        notifyDataSetChanged();

    }
}
