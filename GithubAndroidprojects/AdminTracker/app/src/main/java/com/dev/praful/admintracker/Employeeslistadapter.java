package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.ecommerce.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Employeeslistadapter extends BaseAdapter {

    List<Orderedemployeedetails> Orderedemployeedetails;
    Activity activity;
    String name;
    ArrayList<Orderedemployeedetails> arrayList;

    public Employeeslistadapter(Activity activity, List<Orderedemployeedetails> Orderedemployeedetails) {
        this.activity = activity;
        this.Orderedemployeedetails = Orderedemployeedetails;
        this.arrayList = new ArrayList<Orderedemployeedetails>();
        this.arrayList.addAll(Orderedemployeedetails);

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

        inflater = (LayoutInflater) activity.getLayoutInflater();
        View view = convertView;
        view = inflater.inflate(R.layout.employeeitem, null);
        final TextView employeename = (TextView) view.findViewById(R.id.name);
        final TextView CS = (TextView) view.findViewById(R.id.currentstatus);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slider_l);
        if (sharedPreferences.getString("anim", "").equals("yes")) {

            view.startAnimation(animation);
            if (String.valueOf(getItemId(position)).equals(String.valueOf(Orderedemployeedetails.size() - 1))) {
                editor.putString("anim", "no");
                editor.apply();
            }

        }
        LinearLayout card = (LinearLayout) view.findViewById(R.id.card);
        FirebaseDatabase.getInstance().getReference()
                    .child("checkinandout")
                .child(Orderedemployeedetails.get(position).getId())
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
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Employeedetailspage.class);
                intent.putExtra("employeeid", Orderedemployeedetails.get(position).getId());
                activity.startActivity(intent);
            }
        });
        if (Orderedemployeedetails.get(position).getId() != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("SignedEmployees")
                    .child(Orderedemployeedetails.get(position).getId())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                            if (employeedetails != null) {
                                employeename.setText(employeedetails.getUsername());
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Final Ordered Employees")
                                        .child(Orderedemployeedetails.get(position).getId())
                                        .child("name")
                                        .setValue(employeedetails.getUsername());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        final TextView distance = (TextView) view.findViewById(R.id.distance);
        if (Orderedemployeedetails.get(position).getId() != null) {
            if (Orderedemployeedetails.size() > 0) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Final Ordered Employees")
                        .child(Orderedemployeedetails.get(position).getId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                if (map != null) {
                                    if (map.size() > 0)
                                        distance.setText(Orderedemployeedetails.get(position).getDistance() + " " + String.valueOf(map.get("time")) + " far from Office");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

        }

        TextView rank = (TextView) view.findViewById(R.id.rank);
        rank.setText(String.valueOf(getItemId(position) + 1));

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
