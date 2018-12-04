package com.Shootmyshow.praful.shootmyshow_company;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.Shootmyshow.praful.shootmyshow_company.Common.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class WorkDetails extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView textdate ,txtDistance,txtFee,txtfrom ,txtto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textdate = (TextView)findViewById(R.id.textDate);
        txtDistance =(TextView)findViewById(R.id.textDistance);
        txtfrom =(TextView)findViewById(R.id.textFrom);
        txtto =(TextView)findViewById(R.id.textto);
        txtFee =(TextView)findViewById(R.id.textFee);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       settingInfo();

    }

    private void settingInfo() {

        if(getIntent() != null)
        {
            Calendar calendar =  Calendar.getInstance();
            String date = String.format("%s , %d/%d/%d" ,converttoDayofWeek(calendar.get(Calendar.DAY_OF_WEEK)),calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.YEAR));
        textdate.setText(date);
        txtfrom.setText(getIntent().getStringExtra("start_address"));
        txtto.setText(getIntent().getStringExtra("end_address"));

        LatLng dropoff = new LatLng(Common.Lastlocation.getLatitude() , Common.Lastlocation.getLongitude());

        mMap.addMarker(new MarkerOptions().position(dropoff).title("COMPLETED THE WORK HERE").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropoff , 12.0f));




        }

    }

    private String converttoDayofWeek(int day) {
        switch (day)
        {
            case Calendar.SUNDAY:
                return  "SUNDAY";
            case Calendar.MONDAY:
                return  "MONDAY";
            case Calendar.SATURDAY:
            return  "SATURDAY";
            case Calendar.WEDNESDAY:
                return  "WEDNESDAY";
            case Calendar.TUESDAY:
                return  "TUESDAY";
            case Calendar.FRIDAY:
                return  "FRIDAY";
            case Calendar.THURSDAY:
                return  "THURSDAY";


                default:
                    return "UNK";
        }


    }
}
