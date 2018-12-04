package com.example.praful.ubercoustomer.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.praful.ubercoustomer.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    View mview;

    public CustomInfoWindow(Context context){

        mview = LayoutInflater.from(context)
                .inflate(R.layout.custom_coustomer_info,null);
    }
    @Override
    public View getInfoWindow(Marker marker) {

        TextView textPickuptitle = (TextView)mview.findViewById(R.id.txtpickupinfo);
        textPickuptitle.setText(marker.getTitle());


        TextView textPickupsnippet = (TextView)mview.findViewById(R.id.txtpickupsnippet);
        textPickupsnippet.setText(marker.getSnippet());

        return mview;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
