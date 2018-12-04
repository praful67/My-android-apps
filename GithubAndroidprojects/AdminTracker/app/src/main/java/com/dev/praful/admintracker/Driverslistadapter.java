package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Driverslistadapter extends BaseAdapter {

    Activity activity;
    List<DriversInfo> DriversInfo;
    ArrayList<DriversInfo> arrayList;
    Driverdetails driverdetails;


    public Driverslistadapter(Activity activity, List<DriversInfo> DriversInfo) {
        this.activity = activity;
        this.DriversInfo = DriversInfo;
        this.arrayList = new ArrayList<DriversInfo>();
        this.arrayList.addAll(DriversInfo);

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


        //   inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = activity.getLayoutInflater();
        View itemView = convertView;
        itemView = inflater.inflate(R.layout.driveritem, null);
        final TextView drivername = (TextView) itemView.findViewById(R.id.name);
        LinearLayout card = (LinearLayout)itemView.findViewById(R.id.card);
        final TextView CS = (TextView)itemView.findViewById(R.id.currentstatus);
        FirebaseDatabase.getInstance().getReference()
                .child("RidestartandendD")
                .child(DriversInfo.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            if (String.valueOf(map.get("status")).equals("RideStarted")) {
                                SpannableStringBuilder builder = new SpannableStringBuilder();

                                String BLACK = "Current status is :";
                                SpannableString whiteSpannable = new SpannableString(BLACK);
                                whiteSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, BLACK.length(), 0);
                                builder.append(whiteSpannable);

                                String green = " RIDE STARTED";
                                SpannableString redSpannable = new SpannableString(green);
                                redSpannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, green.length(), 0);
                                builder.append(redSpannable);
                                String time = "\nLast Ride Time : ";
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

                                String red = "  RIDE ENDED";
                                SpannableString redSpannable = new SpannableString(red);
                                redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
                                builder.append(redSpannable);
                                String time = "\nLast Ride Time : ";
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
                Intent intent = new Intent(activity.getBaseContext(), Driverdetailspage.class);
                intent.putExtra("Driverid", DriversInfo.get(position).getId());
                activity.startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference()
                .child("SignedDrivers")
                .child(DriversInfo.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        driverdetails = dataSnapshot.getValue(Driverdetails.class);
                        if (driverdetails != null) {
                            drivername.setText(driverdetails.getUsername());
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Drivers Info")
                                    .child(DriversInfo.get(position).getId())
                                    .child("name")
                                    .setValue(driverdetails.getUsername());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slider_l);
        if (sharedPreferences.getString("anim", "").equals("yes")) {

            itemView.startAnimation(animation);
            if (String.valueOf(getItemId(position)).equals(String.valueOf(DriversInfo.size() - 1))) {
                editor.putString("anim", "no");
                editor.apply();
            }

        }

        return itemView;

    }

    public void filter(String chartext) {
        if (arrayList != null) {
            chartext = chartext.toLowerCase(Locale.getDefault());
            DriversInfo.clear();
            if (chartext.length() == 0) {
                DriversInfo.addAll(arrayList);
            } else {
                for (DriversInfo Driver : arrayList) {
                    if (Driver != null) {
                        if (Driver.getName().toLowerCase(Locale.getDefault()).contains(chartext)) {
                            DriversInfo.add(Driver);
                        }
                    }
                }
            }
            notifyDataSetChanged();

        }
    }

}
