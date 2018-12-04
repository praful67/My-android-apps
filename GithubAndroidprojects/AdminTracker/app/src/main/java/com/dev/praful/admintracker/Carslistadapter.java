package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Carslistadapter extends BaseAdapter {

    List<CarsInfo> carsInfos;
    Activity activity;
    ArrayList<CarsInfo> arrayList;

    String rank, employeename, pickuptime;
    String logindriverid, logoutdriverid;

    public Carslistadapter(List<CarsInfo> carsInfos, Activity activity) {
        this.carsInfos = carsInfos;
        this.activity = activity;
        this.arrayList = new ArrayList<CarsInfo>();
        this.arrayList.addAll(carsInfos);
    }

    @Override
    public int getCount() {
        return carsInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return carsInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = activity.getLayoutInflater();
        View view = convertView;
        view = inflater.inflate(R.layout.caritem, null);
        TextView carname = (TextView) view.findViewById(R.id.carname);
        final ExpandableTextView text = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        carname.setText(carsInfos.get(position).getName());
        LinearLayout car = (LinearLayout) view.findViewById(R.id.car);

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), CurrentCardetails.class);
                intent.putExtra("carId", carsInfos.get(position).getId());

                activity.startActivity(intent);

            }
        });
        final TextView dates = (TextView) view.findViewById(R.id.dates);
        final String listid = sharedPreferences.getString("rostersetid", "");
        final String carId = carsInfos.get(position).getId();
        final TextView CSlogin = (TextView) view.findViewById(R.id.currentstatuslogin);
        final TextView CSlogout = (TextView) view.findViewById(R.id.currentstatuslogout);

        FirebaseDatabase.getInstance().getReference()
                .child("Cars Info")
                .child(carsInfos.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            CarsInfo carsInfo1 = dataSnapshot.getValue(CarsInfo.class);
                            if (carsInfo1 != null && carsInfo1.getListid() != null) {
                                if (carsInfo1.getListid().equals(listid)) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("List of Rosters Sets")
                                            .child(listid)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                        if (map != null) {
                                                            dates.setText(String.valueOf(map.get("dates")));
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


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("Driver's Car")
                .child("logout")
                .child(carsInfos.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                            if (map != null) {
                                if (carId.equals(String.valueOf(map.get("car id")))) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("SignedDrivers")
                                            .child(dataSnapshot1.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                        if (Driverdetails != null) {

                                                            logoutdriverid = Driverdetails.getId();
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("RidestartandendD")
                                                                    .child(logoutdriverid)
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                            if (map != null) {
                                                                                if (map.get("carid") != null) {
                                                                                    if (String.valueOf(map.get("carid")).equals(carsInfos.get(position).getId())) {

                                                                                        if (String.valueOf(map.get("status")).equals("RideStarted")) {
                                                                                            SpannableStringBuilder builder = new SpannableStringBuilder();

                                                                                            String BLACK = "Logout Driver Current status is :";
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

                                                                                            CSlogout.setText(builder, TextView.BufferType.SPANNABLE);

                                                                                        } else {
                                                                                            SpannableStringBuilder builder = new SpannableStringBuilder();

                                                                                            String BLACK = "Logout Driver Current status is :";
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

                                                                                            CSlogout.setText(builder, TextView.BufferType.SPANNABLE);


                                                                                        }
                                                                                    }else {
                                                                                        CSlogout.setVisibility(View.GONE);
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
                .child(carsInfos.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                            if (map != null) {
                                if (carId.equals(String.valueOf(map.get("car id")))) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("SignedDrivers")
                                            .child(dataSnapshot1.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        Driverdetails Driverdetails = dataSnapshot.getValue(Driverdetails.class);
                                                        if (Driverdetails != null) {
                                                            logindriverid = Driverdetails.getId();
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("RidestartandendD")
                                                                    .child(logindriverid)
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            java.util.Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                            if (map != null) {
                                                                                if (map.get("carid") != null) {
                                                                                    if (String.valueOf(map.get("carid")).equals(carsInfos.get(position).getId())) {


                                                                                        if (String.valueOf(map.get("status")).equals("RideStarted")) {
                                                                                            SpannableStringBuilder builder = new SpannableStringBuilder();

                                                                                            String BLACK = "Login Current status is :";
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

                                                                                            CSlogin.setText(builder, TextView.BufferType.SPANNABLE);

                                                                                        } else {
                                                                                            SpannableStringBuilder builder = new SpannableStringBuilder();

                                                                                            String BLACK = "Login Current status is :";
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

                                                                                            CSlogin.setText(builder, TextView.BufferType.SPANNABLE);


                                                                                        }
                                                                                    }else {
                                                                                        CSlogin.setVisibility(View.GONE);
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
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.scale_in_slow);
        if (sharedPreferences.getString("anim", "").equals("yes")) {

            view.startAnimation(animation);
            if (String.valueOf(getItemId(position)).equals(String.valueOf(carsInfos.size() - 1))) {
                editor.putString("anim", "no");
                editor.apply();
            }

        }
        FirebaseDatabase.getInstance().getReference()
                .child("Atonesight")
                .child(carsInfos.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {

                                text.setText(String.valueOf(map.get("employees")));
                                carsInfos.get(position).setText(text.getText().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return view;
    }

    public void filter(String chartext) {
        chartext = chartext.toLowerCase(Locale.getDefault());
        carsInfos.clear();
        if (chartext.length() == 0) {
            carsInfos.addAll(arrayList);
        } else {
            for (CarsInfo carsInfos1 : arrayList) {
                if (carsInfos1.getName().toLowerCase(Locale.getDefault()).contains(chartext) || carsInfos1.getText().toLowerCase(Locale.getDefault()).contains(chartext)) {
                    carsInfos.add(carsInfos1);
                }
            }
        }
        notifyDataSetChanged();

    }

}
/*FirebaseDatabase.getInstance().getReference()
                .child("Employee's Car")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                if (map != null) {
                                    if (String.valueOf(map.get("car id")).equals(carsInfos.get(position).getId())) {
                                        if (String.valueOf(map.get("checkselection")).equals("selected")) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Employeespickuptimes")
                                                    .child(dataSnapshot1.getKey())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot != null) {
                                                                Map<String, Object> map2 = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                if (map2 != null) {
                                                                    pickuptime = String.valueOf(map2.get("time"));

                                                                    //   Rosteritem rosteritem = new Rosteritem(rank, employeename, pickuptime);
                                                                    //  list.add(rosteritem);
                                                                }

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("SignedEmployees")
                                                    .child(dataSnapshot1.getKey())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                                                            if (employeedetails != null) {
                                                                employeename = employeedetails.getUsername();

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Final Rank of employee")
                                                    .child(dataSnapshot1.getKey())
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot != null) {
                                                                Map<String, Object> map1 = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                if (map1 != null) {
                                                                    rank = String.valueOf(map1.get("Rank"));
                                                                    text.append("\n"+rank +" "+ employeename + " "+pickuptime+"\n");

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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
      */