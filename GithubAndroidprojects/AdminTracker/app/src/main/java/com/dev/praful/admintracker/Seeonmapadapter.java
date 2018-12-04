package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Seeonmapadapter implements GoogleMap.InfoWindowAdapter {

    Activity activity;
    View view;
    String address1;
    String DDT;

    public Seeonmapadapter(Activity activity) {
        this.activity = activity;
    }


    @Override
    public View getInfoWindow(final Marker marker) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.custominfowindow, null);

        final TextView name = (TextView) view1.findViewById(R.id.name);
        final TextView addressandDT = (TextView) view1.findViewById(R.id.addressandDT);
        addressandDT.setVisibility(View.GONE);

        if (marker.getSnippet() != null) {
            name.setText(marker.getTitle());

        } else {
            name.setText("Main office or a Searched address");
            Toast.makeText(activity, "Main office or a Searched address", Toast.LENGTH_SHORT).show();
        }

        return view1;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }
}
