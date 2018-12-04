package com.dev.praful.trackyouremployee;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        carname.setText(carsInfos.get(position).getName());
        LinearLayout car = (LinearLayout) view.findViewById(R.id.car);

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), RideDetails.class);
                intent.putExtra("carId", carsInfos.get(position).getId());
                activity.startActivity(intent);

            }
        });
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