package com.dev.praful.trackyourdriver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapteremployeeslist extends BaseAdapter {

    List<Employeedetails> employeedetails;
    Activity activity;
    String lat, lng;

    String result;

    public Adapteremployeeslist(List<Employeedetails> employeedetails, Activity activity) {
        this.employeedetails = employeedetails;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return employeedetails.size();
    }

    @Override
    public Object getItem(int position) {
        return employeedetails.get(position);
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
        TextView name = (TextView) view.findViewById(R.id.name);
        final TextView distance = (TextView) view.findViewById(R.id.distance);
        final TextView rank = (TextView) view.findViewById(R.id.rank);
        CardView details = (CardView) view.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Employeedetailspage.class);
                intent.putExtra("employeeid", employeedetails.get(position).getId());
                activity.startActivity(intent);
            }
        });
        final TextView pickuptime = (TextView) view.findViewById(R.id.pickuptime);
        FirebaseDatabase.getInstance().getReference()
                .child("Employeespickuptimes")
                .child(employeedetails.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                pickuptime.setText(String.valueOf(map.get("time")));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        lat = employeedetails.get(position).getAddresslat();
        lng = employeedetails.get(position).getAddresslng();

        name.setText(employeedetails.get(position).getUsername());
        FirebaseDatabase.getInstance().getReference()
                .child("Final Rank of employee")
                .child(employeedetails.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            rank.setText(String.valueOf(map.get("Rank")));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        final TextView timeoffE;
        final LinearLayout TF;
        timeoffE = (TextView) view.findViewById(R.id.timeoffE);
        TF = (LinearLayout) view.findViewById(R.id.timeofflayout);

        FirebaseDatabase.getInstance().getReference()
                .child("StatusE")
                .child(employeedetails.get(position).getId())
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


        FirebaseDatabase.getInstance().getReference()
                .child("Final Ordered Employees")
                .child(employeedetails.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            distance.setText(String.valueOf(map.get("distance")) + " " + String.valueOf(map.get("time")) + " far from Office");

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        ImageView imageView = (ImageView) view.findViewById(R.id.getdirections);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat != null && lng != null) {
                    if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
                        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + Double.parseDouble(lat) + " , " + Double.parseDouble(lng) + "&mode=d");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        activity.startActivity(mapIntent);
                    }
                }
            }
        });
        return view;
    }

}
