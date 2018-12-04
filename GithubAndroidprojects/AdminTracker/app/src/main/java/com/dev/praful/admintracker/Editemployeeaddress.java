package com.dev.praful.admintracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Editemployeeaddress extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String id1;
    String address1;
    PlaceAutocompleteFragment place_location;
    java.util.List<Address> addresses1 = null;
    double lat1, lng1;
    GeoFire geoFireaddress;
    Geocoder geocoder1;
    String address;
    java.util.Map<String, Object> map1;

    String time_txt = "";
    String parsedDistance = "";
    Marker current;
    IGoogleAPI Services;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editemployeeaddress);
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
        Services = CommonSwitch.getGoogleService();
        geoFireaddress = new GeoFire(FirebaseDatabase.getInstance().getReference().child("Employee's addresses"));

        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (current != null)
                    current.remove();


               /* Placelocation = place.getAddress().toString();
                lat1 = place.getLatLng().latitude;
                lng1 = place.getLatLng().longitude;

*/
              /*  current = mMap.addMarker(new MarkerOptions().position(place.getLatLng()
                ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Pick up here"));
              */
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

            }

            @Override
            public void onError(Status status) {

            }
        });

        if (getIntent().getStringExtra("employeeid") != null) {
            id1 = getIntent().getStringExtra("employeeid");
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
        final LatLng latLng1 = new LatLng(17.434810272796604, 78.38469553738832);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  java.util.Map<String, Object> map = new HashMap<>();
                map.put("pick_up_address", addresstap.getText().toString());
                map.put("addresslat", String.valueOf(lat1));
                map.put("addresslng", String.valueOf(lng1));
                FirebaseDatabase.getInstance().getReference()
                        .child("SignedEmployees")
                        .child(id1)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(Editemployeeaddress.this, "Done !", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });*/
                progressDialog.setMessage("please wait..");
                progressDialog.setCancelable(false);
                if (!Editemployeeaddress.this.isFinishing())
                    progressDialog.show();


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        String requestUrl = null;
                        try {
                            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                                    "transit_routing_preference=less_driving&" + "origin=" + latLng1.latitude + "," + latLng1.longitude + "&" +
                                    "destination=" + lat1 + "," + lng1 + "&" +
                                    "key=" + getResources().getString(R.string.google_maps_key);
                            Log.e("LINK", requestUrl);
                            Services.getPath(requestUrl).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().toString());
                                        JSONArray routes = jsonObject.getJSONArray("routes");
                                        JSONObject object = routes.getJSONObject(0);

                                        JSONArray legs = object.getJSONArray("legs");
                                        JSONObject legsobject = legs.getJSONObject(0);

                                        //GET DISTANCE
                                        JSONObject distance = legsobject.getJSONObject("distance");
                                        final String distance_text = distance.getString("text");

                                        final Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));

                                        parsedDistance = distance_text;
                                        JSONObject time = legsobject.getJSONObject("duration");
                                        final String time_text1 = time.getString("text");
                                        Integer time_value = Integer.parseInt(time_text1.replaceAll("\\D+", ""));

                                        time_txt = time_text1;
                                        String start_address = legsobject.getString("start_address");
                                        String end_address = legsobject.getString("end_address");

                                        Toast.makeText(Editemployeeaddress.this, distance_text + " " + time_text1, Toast.LENGTH_SHORT).show();
                                        new CountDownTimer(3000, 1000) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                if (!parsedDistance.equals("")) {
                                                    if (!time_txt.equals("")) {
                                                        FirebaseDatabase.getInstance()
                                                                .getReference().child("SignedEmployees")
                                                                .child(id1)
                                                                .addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        map1 = (HashMap<String, Object>) dataSnapshot.getValue();


                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                        new CountDownTimer(2000, 1000) {
                                                            @Override
                                                            public void onTick(long millisUntilFinished) {

                                                            }

                                                            @Override
                                                            public void onFinish() {
                                                                final Orderedemployeedetails orderedemployeedetails = new
                                                                        Orderedemployeedetails(String.valueOf(map1.get("username")),
                                                                        distance_text
                                                                        , id1);

                                                                //  Scanner sc = new Scanner(parsedDistance);

                                                                //   .setValue(String.valueOf(sc.nextDouble()))
                                                                FirebaseDatabase.getInstance().getReference()
                                                                        .child("Final Ordered Employees")
                                                                        .child(id1)
                                                                        .setValue(orderedemployeedetails)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                final java.util.Map<String, Object> map = new HashMap<>();
                                                                                map.put("pick_up_address", addresstap.getText().toString());
                                                                                map.put("addresslat", String.valueOf(lat1));
                                                                                map.put("addresslng", String.valueOf(lng1));
                                                                                FirebaseDatabase.getInstance().getReference()
                                                                                        .child("SignedEmployees")
                                                                                        .child(id1)
                                                                                        .updateChildren(map);
                                                                                FirebaseDatabase.getInstance().getReference()
                                                                                        .child("Final Ordered Employees")
                                                                                        .child(id1)
                                                                                        .child("distance1")
                                                                                        .setValue(distance_value);

                                                                                FirebaseDatabase.getInstance().getReference()
                                                                                        .child("Final Ordered Employees")
                                                                                        .child(id1)
                                                                                        .child("time")
                                                                                        .setValue(time_text1);
                                                                                new CountDownTimer(1000, 1000) {
                                                                                    @Override
                                                                                    public void onTick(long millisUntilFinished) {

                                                                                    }

                                                                                    @Override
                                                                                    public void onFinish() {
                                                                                        progressDialog.dismiss();
                                                                                        geoFireaddress.setLocation(id1, new GeoLocation(lat1, lng1));
                                                                                        dialog.dismiss();
                                                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                                            @Override
                                                                                            public void run() {
                                                                                                Toast.makeText(Editemployeeaddress.this, "Done !", Toast.LENGTH_SHORT).show();
                                                                                                FirebaseDatabase.getInstance()
                                                                                                        .getReference()
                                                                                                        .child("ProfileEditedbyadmin")
                                                                                                        .child(id1)
                                                                                                        .child("changed")
                                                                                                        .setValue("yes");


                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }.start();


                                                                            }
                                                                        })
                                                                ;
                                                            }
                                                        }.start();
                                                        time_txt = "";
                                                        parsedDistance = "";

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFinish() {

                                            }
                                        }.start();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

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
                geocoder1 = new Geocoder(Editemployeeaddress.this, Locale.getDefault());

                try {
                    addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                    if (addresses1 != null && addresses1.size() > 0) {

                        address1 = addresses1.get(0).getAddressLine(0);
                        Toast.makeText(Editemployeeaddress.this, "TAPPED LOCATION : " + address1, Toast.LENGTH_LONG).show();

                        if (current != null) {
                            current.remove();
                        }

                        current = mMap.addMarker(new MarkerOptions().draggable(true)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(latLng).title("Tapped location").snippet(address1.toString()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                        current.showInfoWindow();
                        //showlocationdailog();
                    } else {

                        Toast.makeText(Editemployeeaddress.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
                geocoder1 = new Geocoder(Editemployeeaddress.this, Locale.getDefault());

                try {
                    addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                    if (addresses1 != null && addresses1.size() > 0) {

                        address1 = addresses1.get(0).getAddressLine(0);

                        Toast.makeText(Editemployeeaddress.this, "DRAGGED LOCATION : " + address1, Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(Editemployeeaddress.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();


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
}
