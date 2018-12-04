package com.dev.praful.admintracker;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class Editdriveraddress extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String id1;
    String address1;
    java.util.List<Address> addresses1 = null;
    PlaceAutocompleteFragment place_location;
    double lat1, lng1;
    Geocoder geocoder1;
    String address;
    Marker current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdriveraddress);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        place_location = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_location);

        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (current != null)
                    current.remove();


               /* Placelocation = place.getAddress().toString();
                lat1 = place.getLatLng().latitude;
                lng1 = place.getLatLng().longitude;

*/
               /* current = mMap.addMarker(new MarkerOptions().position(place.getLatLng()
                ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Pick up here"));
              */  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

            }

            @Override
            public void onError(Status status) {

            }
        });

        if (getIntent().getStringExtra("driverid") != null) {
            id1 = getIntent().getStringExtra("driverid");
            showtaponmapdialog();

        }
    }

    private void showtaponmapdialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.showtaponmapdialog, null);
        Button done = (Button) view1.findViewById(R.id.done);

        builder.setView(view1);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);

        current = mMap.addMarker(new MarkerOptions().
                position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Main Office")
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                lat1 = latLng.latitude;
                lng1 = latLng.longitude;
                Double lat1 = latLng.latitude;
                Double lng1 = latLng.longitude;
                geocoder1 = new Geocoder(Editdriveraddress.this, Locale.getDefault());

                try {
                    addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                    if (addresses1 != null && addresses1.size() > 0) {

                        address1 = addresses1.get(0).getAddressLine(0);

                        Toast.makeText(Editdriveraddress.this, "TAPPED LOCATION : " + address1, Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(Editdriveraddress.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses1 != null && addresses1.size() > 0) {

                    if (current != null) {
                        current.remove();
                    }

                    current = mMap.addMarker(new MarkerOptions()
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(latLng).title("Tapped location").snippet(address1.toString()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                    current.showInfoWindow();
                    //showlocationdailog();
                }

            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                showlocationdailog();
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng markerLocation = marker.getPosition();
                lat1 = markerLocation.latitude;
                lng1 = markerLocation.longitude;
                final Double lat1 = markerLocation.latitude;
                final Double lng1 = markerLocation.longitude;
                geocoder1 = new Geocoder(Editdriveraddress.this, Locale.getDefault());

                try {
                    addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                    if (addresses1 != null && addresses1.size() > 0) {

                        address1 = addresses1.get(0).getAddressLine(0);

                        Toast.makeText(Editdriveraddress.this, "DRAGGED LOCATION : " + address1, Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(Editdriveraddress.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses1 != null && addresses1.size() > 0) {

                    if (current != null) {
                        current.remove();
                    }

                    current = mMap.addMarker(new MarkerOptions()
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(markerLocation).title("Dragged location").snippet(address1.toString()));

                    current.showInfoWindow();
                    //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 15.0f));

                }
            }
        });

    }

    private void showlocationdailog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.showlocation, null);
        Button done = (Button) view1.findViewById(R.id.done);
        final Button cancel = (Button) view1.findViewById(R.id.cancel);


        final TextView addresstap = (TextView) view1.findViewById(R.id.address);
        addresstap.setText(address1);
        builder.setView(view1);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Map<String, Object> map = new HashMap<>();
                map.put("address", addresstap.getText().toString());
                map.put("addresslat", String.valueOf(lat1));
                map.put("addresslng", String.valueOf(lng1));
                FirebaseDatabase.getInstance().getReference()
                        .child("SignedDrivers")
                        .child(id1)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(Editdriveraddress.this, "Done !", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("ProfileEditedbyadminD")
                                .child(id1)
                                .child("changed")
                                .setValue("yes");
                        finish();

                    }
                });
            }
        });
    }

}
