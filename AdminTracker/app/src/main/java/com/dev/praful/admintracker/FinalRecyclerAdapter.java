package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalRecyclerAdapter extends RecyclerView.Adapter<FinalRecyclerAdapter.RecyclerViewHolder> {

    List<Finalcaremployees> finalcaremployees;
    Activity activity;
    String time;
    String carid;

    public FinalRecyclerAdapter(List<Finalcaremployees> finalcaremployees, Activity activity, String carid) {
        this.finalcaremployees = finalcaremployees;
        this.activity = activity;
        this.carid = carid;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.finalcaremployeeitem, null);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        holder.detailslayout.setOnClickListener(new View.OnClickListener() {
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
                                holder.pickuptime.setText(String.valueOf(map.get("time")));
                                holder.pickuptime.setTextColor(Color.BLUE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
                                    holder.TF.setVisibility(View.VISIBLE);
                                    holder.timeoffE.setText(String.valueOf(map.get("status")));
                                } else {
                                    holder.TF.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("Employee's Rank").child(carid).child(finalcaremployees.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            holder.editedrank.setText(String.valueOf(map.get("rank")));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("employeeselection")
                .child(finalcaremployees.get(position).getId())
                .removeValue();

        FirebaseDatabase.getInstance().getReference()
                .child("Final Rank of employee").child(finalcaremployees.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            holder.rank.setText(String.valueOf(map.get("Rank")));
                            holder.rank.setTextColor(Color.GREEN);
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
                                holder.distance.setText(orderedemployeedetails.getDistance().toString() + " " + String.valueOf(map.get("time")) + " far from Office ");
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
                    if (Driverdetails != null)
                        holder.name.setText(Driverdetails.getUsername());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return finalcaremployees.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name, distance, rank, editedrank, pickuptime;
        CardView detailslayout;
        TextView timeoffE;
        LinearLayout TF;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            timeoffE = (TextView) itemView.findViewById(R.id.timeoffE);
            TF = (LinearLayout) itemView.findViewById(R.id.timeofflayout);

            distance = (TextView) itemView.findViewById(R.id.distance);
            rank = (TextView) itemView.findViewById(R.id.rank);
            editedrank = (TextView) itemView.findViewById(R.id.editedrank);
            pickuptime = (TextView) itemView.findViewById(R.id.pickuptime);
            detailslayout = (CardView) itemView.findViewById(R.id.detailslayout);

        }
    }


}
