package com.dev.praful.admintracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Seeonmap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String carId;
    Marker current;
    String snippet;
    String selected = "no";
    String longpressed = "no";
    ArrayList<String> ids = new ArrayList<String>();
    int X = 0;
    PlaceAutocompleteFragment place_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeonmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getWindow().getAttributes().windowAnimations = R.style.Style;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        carId = getIntent().getStringExtra("carId");

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (carId != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Car employees")
                            .child(carId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                ids.add(String.valueOf(map.get("id")));
                                X++;

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }.start();

        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            final Employeedetails employeedetails = dataSnapshot1.getValue(Employeedetails.class);
                            if (employeedetails != null) {
                                if (employeedetails.getAddresslat().length() != 0 && employeedetails.getAddresslat() != null) {
                                    final double lat = Double.parseDouble(employeedetails.getAddresslat());
                                    final double lng = Double.parseDouble(employeedetails.getAddresslng());

                                    final String address = employeedetails.pick_up_address;
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Employee's Car")
                                            .child(dataSnapshot1.getKey())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                    if (map != null) {
                                                        if (carId.equals(String.valueOf(map.get("car id"))) && map.get("car id") != null) {

                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("Final Ordered Employees")
                                                                    .child(dataSnapshot1.getKey())
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            String DT;
                                                                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                            DT = String.valueOf(map.get("distance")) + " " + String.valueOf(map.get("time")) + " far from Office";
                                                                            snippet = employeedetails.getUsername() + "\n" + "\n" + address + "\n" + "\n" + DT + "\n" + "\n" + "Selected by this car";
                                                                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                                                    .title(snippet)
                                                                                    .position(new LatLng(lat, lng))
                                                                                    .snippet(employeedetails.getId())

                                                                            );

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                        } else if (String.valueOf(map.get("checkselection")).equals("selected")) {
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("Final Ordered Employees")
                                                                    .child(dataSnapshot1.getKey())
                                                                    .addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            String DT;
                                                                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                            DT = String.valueOf(map.get("distance")) + " " + String.valueOf(map.get("time")) + " far from Office";
                                                                            snippet = employeedetails.getUsername() + "\n" + "\n" + address + "\n" + "\n" + DT + "\n" + "\n" + "Selected by a other car";
                                                                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                                                                    .title(snippet)
                                                                                    .position(new LatLng(lat, lng))
                                                                                    .snippet(employeedetails.getId())

                                                                            );
                                                                            selected = "no";
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                        }
                                                    } else {
                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("Final Ordered Employees")
                                                                .child(dataSnapshot1.getKey())
                                                                .addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String DT;
                                                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                                                        DT = String.valueOf(map.get("distance")) + " " + String.valueOf(map.get("time")) + " far from Office";
                                                                        snippet = employeedetails.getUsername() + "\n" + "\n" + address + "\n" + "\n" + DT + "\n" + "\n" + "Not selected by any car";
                                                                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                                                                .title(snippet)
                                                                                .position(new LatLng(lat, lng))
                                                                                .snippet(employeedetails.getId())

                                                                        );
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

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
        place_location = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_location);

        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (current != null)
                    current.remove();

                current = mMap.addMarker(new MarkerOptions().position(place.getLatLng()
                ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("SEARCHED"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

                //  Toast.makeText(Map.this, String.format("Address for pick up is %s", Placelocation), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FloatingActionButton currentl = (FloatingActionButton) findViewById(R.id.currentlocation);
        currentl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(17.434810272796604, 78.38469553738832);

                if (current != null)
                    current.remove();

                current = mMap.addMarker(new MarkerOptions().
                        position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Main Office")
                );

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(this);


        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait");
                progressDialog.show();

                Toast.makeText(Seeonmap.this, "Done !", Toast.LENGTH_SHORT).show();

                for (int i = 0; i < X; i++) {

                    final int finalI = i;
                    FirebaseDatabase.getInstance().getReference()
                            .child("Car employees")
                            .child(carId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(ids.get(finalI)).exists()) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Employee's Car")
                                                .child(ids.get(finalI))
                                                .child("car id")
                                                .setValue(carId);
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Updates")
                                                .child(ids.get(finalI))
                                                .child("whattodo")
                                                .setValue("update").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Seeonmap.this, "Notification sent !", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("DriverUpdates")
                                                .child(ids.get(finalI))
                                                .child("whattodo")
                                                .setValue("update");

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Employee's Car")
                                                .child(ids.get(finalI))
                                                .child("checkselection")
                                                .setValue("selected");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    FirebaseDatabase.getInstance().getReference()
                            .child("employeeselection")
                            .child(ids.get(i))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        String check = dataSnapshot.getValue(String.class);
                                        if (check != null) {
                                            if (check.equals("unselected")) {
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("Employee's Car")
                                                        .child(ids.get(finalI))
                                                        .removeValue();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                }


                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        finish();
                    }
                }.start();
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
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        current = mMap.addMarker(new MarkerOptions().
                position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Main Office")
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        mMap.setInfoWindowAdapter(new Seeonmapadapter(Seeonmap.this));

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(final Marker marker) {
                if (marker.getSnippet() != null) {
                    if (longpressed.equals("no")) {
                        longpressed = "yes";
                        FirebaseDatabase.getInstance().getReference()
                                .child("Employee's Car")
                                .child(marker.getSnippet())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                        if (map != null) {
                                            if (carId.equals(String.valueOf(map.get("car id"))) && map.get("car id") != null) {

                                            } else if (String.valueOf(map.get("checkselection")).equals("selected")) {
                                            }
                                        } else {
                                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                            Map<String, Object> map1 = new HashMap<>();
                                            map1.put("name", "");
                                            map1.put("id", marker.getSnippet());
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Car employees")
                                                    .child(carId)
                                                    .child(marker.getSnippet())
                                                    .setValue(map1);
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("employeeselection")
                                                    .child(marker.getSnippet())
                                                    .setValue("Selected");

                                            Toast.makeText(getBaseContext(), "Selected", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    } else if (longpressed.equals("yes")) {
                        longpressed = "no";


                        FirebaseDatabase.getInstance().getReference()
                                .child("Employee's Car")
                                .child(marker.getSnippet())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                        if (map != null) {
                                            if (carId.equals(String.valueOf(map.get("car id"))) && map.get("car id") != null) {

                                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                            } else if (String.valueOf(map.get("checkselection")).equals("selected")) {
                                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                            }
                                        } else {
                                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Car employees")
                                                    .child(carId)
                                                    .child(marker.getSnippet())
                                                    .removeValue();
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("employeeselection")
                                                    .child(marker.getSnippet())
                                                    .setValue("unselected");
                                            Toast.makeText(getBaseContext(), "Unselected", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                    }


                }
            }


        });


    }


}
