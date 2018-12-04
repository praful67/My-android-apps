package com.example.praful.ubercoustomer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Helper.CustomInfoWindow;
import com.example.praful.ubercoustomer.Model.Coustomers;
import com.example.praful.ubercoustomer.Model.DataMessage;
import com.example.praful.ubercoustomer.Model.FCMResponse;
import com.example.praful.ubercoustomer.Model.Presentbooking;
import com.example.praful.ubercoustomer.Model.Token;
import com.example.praful.ubercoustomer.Remote.IFCMSerives;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.felipecsl.gifimageview.library.GifImageView;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallCompanyforAdvanceBooking extends FragmentActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    String address1, time;
    TextView txtDate, txtTime, txtAddress, txtevent;
    private GifImageView functiongif, functiongif1, partygif, partygif1, photoshootgif, photoshootgif1, currentlocationgif, tappedlocationgif;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    double lat1, lng1;
    IFCMSerives ifcmSerives;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private LocationRequest locationRequest;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    Geocoder geocoder, geocoder1;

    List<Address> addresses, addresses1;

    Marker CoustMarker, markerdestination;

    Bottom_Sheet_Coustomer bottom_sheet_coustomer;
    int radius = 1;
    int distance = 1;
    private static final int LIMIT = 3;
    DatabaseReference companiesAvailable;
    PlaceAutocompleteFragment place_location, place_destination;

    String Placelocation;

    private GoogleMap mMap;
    String companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_companyfor_advance_booking);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent() != null) {
            companyId = getIntent().getStringExtra("companyId");
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Button btnCancel = (Button) findViewById(R.id.btncancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showbooklaterDailog();
                showpickupaddressdailog();
            }
        });

        ifcmSerives = Common.getFCMService();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SELECT PICK UP ADDRESS");
        builder.setMessage("Please select your pick up address either by searching or tapping on Map." + "\n" + "If you want your current location as pick up address , just click CONTINUE");

        builder.show();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(CallCompanyforAdvanceBooking.this, CallCompany.class);
                startActivity(intent);
                finish();

            }
        });
        FloatingActionButton currentLocation = (FloatingActionButton) findViewById(R.id.currentlocation);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUploation();

            }
        });
        setUploation();
        place_location = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_location);

        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.clear();

                Placelocation = place.getAddress().toString();
                lat1 = place.getLatLng().latitude;
                lng1 = place.getLatLng().longitude;


                CoustMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()
                ).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).title("Pick up here"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

                Toast.makeText(CallCompanyforAdvanceBooking.this, String.format("Address for pick up in Advance Booking is %s", Placelocation), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildLocationCallBack();
                    createlocationreq();
                    displaylocation();


                }
                break;
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindow(this));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerdestination != null) {
                    markerdestination.remove();
                }
                markerdestination = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker)).position(latLng).title("Destination"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));


                Bottom_Sheet_Coustomer bottom_sheet_coustomer = Bottom_Sheet_Coustomer.newInstance(String.format("%f %f", Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()
                ), String.format("%f %f", latLng.latitude, latLng.longitude), true);
                bottom_sheet_coustomer.show(getSupportFragmentManager(), bottom_sheet_coustomer.getTag());

                lat1 = latLng.latitude;
                lng1 = latLng.longitude;
                Double lat1 = latLng.latitude;
                Double lng1 = latLng.longitude;
                geocoder1 = new Geocoder(CallCompanyforAdvanceBooking.this, Locale.getDefault());

                try {
                    addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                    address1 = addresses1.get(0).getAddressLine(0);

                    Toast.makeText(CallCompanyforAdvanceBooking.this, "TAPPED LOCATION : " + address1, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        // mMap.setOnInfoWindowClickListener(this);
        if (ActivityCompat.checkSelfPermission(CallCompanyforAdvanceBooking.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CallCompanyforAdvanceBooking.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void setUploation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{

                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CALL_PHONE


            }, MY_PERMISSION_REQUEST_CODE);
        } else {

            buildLocationCallBack();
            createlocationreq();
            displaylocation();


        }
    }

    private void buildLocationCallBack() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                Common.Lastlocation = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                displaylocation();
            }
        };
    }


    private void createlocationreq() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void displaylocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                Common.Lastlocation = location;
                if (Common.Lastlocation != null) {

                    companiesAvailable = FirebaseDatabase.getInstance().getReference(Common.companies_table);
                    companiesAvailable.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            loadAllAvailablecompanies(new LatLng(Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    final double latitude = Common.Lastlocation.getLatitude();
                    final double logitude = Common.Lastlocation.getLongitude();


                    // rotateMarker(current , -360 , mMap);
                    loadAllAvailablecompanies(new LatLng(Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));


                    Log.d("C A M E R A N", String.format("Your location has changed : %f / %f", latitude, logitude));
                } else {
                    Log.d("ERROR", "CANNOT GET YOUR LOCATION");
                }
            }
        });
    }

    private void loadAllAvailablecompanies(final LatLng location) {

        mMap.clear();
        CoustMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).
                position(location).title("You")
        );


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));


        DatabaseReference companylocation = FirebaseDatabase.getInstance().getReference(Common.companies_table);

        GeoFire gf = new GeoFire(companylocation);

        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(location.latitude, location.longitude), distance);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, final GeoLocation location) {

                FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table)
                        .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Coustomers coustomers = dataSnapshot.getValue(Coustomers.class);

                        mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                                .flat(true).title(coustomers.getName()).snippet("Company ID : " + dataSnapshot.getKey())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera)));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (distance <= LIMIT) {

                    distance++;
                    loadAllAvailablecompanies(location);
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    private void showsentreqDailog() {

        final AlertDialog.Builder sentreqDailog = new AlertDialog.Builder(CallCompanyforAdvanceBooking.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        View view1 = layoutInflater1.inflate(R.layout.reqsent, null);
        sentreqDailog.setView(view1);

        //  sentreqDailog.show();

        final AlertDialog alert = sentreqDailog.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alert.show();
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                alert.dismiss();
                finish();
            }
        }.start();

       /* sentreqDailog.setNeutralButton("Ok", new DialogInterface.OnClickListener() { // define the 'Cancel' button
            public void onClick(DialogInterface dialog, int which) {
                //Either of the following two lines should work.
                dialog.cancel();
                //dialog.dismiss();
            }
        }); */
    }


    private void showpickupaddressdailog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CallCompanyforAdvanceBooking.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.addresspickup, null);
        txtAddress = (TextView) view1.findViewById(R.id.txtaddress);
        Button next = (Button) view1.findViewById(R.id.next);
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        currentlocationgif = (GifImageView) view1.findViewById(R.id.currentlocationgif);
        final TextView tappedlocation = (TextView) view1.findViewById(R.id.tappedlocation);

        try {
            InputStream inputStream = getAssets().open("curentlocation.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            currentlocationgif.setBytes(bytes);
            currentlocationgif.startAnimation();
        } catch (IOException e) {

        }

        tappedlocationgif = (GifImageView) view1.findViewById(R.id.tappedlocationgif);

        try {
            InputStream inputStream = getAssets().open("tapping.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            tappedlocationgif.setBytes(bytes);
            tappedlocationgif.startAnimation();
        } catch (IOException e) {

        }
        txtAddress.setText(Placelocation);

        final TextView radiobuttoncurrentlocation = (TextView) view1.findViewById(R.id.currentlocation);

        radiobuttoncurrentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat1 = Common.Lastlocation.getLatitude();
                lng1 = Common.Lastlocation.getLongitude();
                //txtAddress.setText(String.format("%f %f", Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));

                Double lat = Common.Lastlocation.getLatitude();
                Double lng = Common.Lastlocation.getLongitude();
                radiobuttoncurrentlocation.setBackgroundColor(getResources().getColor(R.color.dividerColor));
                tappedlocation.setBackgroundColor(getResources().getColor(R.color.unselect));

                geocoder = new Geocoder(CallCompanyforAdvanceBooking.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1);

                    String address = addresses.get(0).getAddressLine(0);
                    txtAddress.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tappedlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAddress.setText(address1);
                radiobuttoncurrentlocation.setBackgroundColor(getResources().getColor(R.color.unselect));
                tappedlocation.setBackgroundColor(getResources().getColor(R.color.dividerColor));

            }
        });
        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogslide1;
        alertDialog.show();
        alertDialog.setCancelable(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showdatetimepickdialog();
            }
        });


    }

    private void showdatetimepickdialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CallCompanyforAdvanceBooking.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.dateandtime, null);
        Button next = (Button) view1.findViewById(R.id.next);
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        txtDate = (TextView) view1.findViewById(R.id.txtDate);
        txtTime = (TextView) view1.findViewById(R.id.txtTime);


        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog dialog = new DatePickerDialog(
                        CallCompanyforAdvanceBooking.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListener
                        , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Calendar calendar = Calendar.getInstance();
                if (dayOfMonth == calendar.get(Calendar.DAY_OF_MONTH)) {
                    time = "today";
                } else {

                    time = "nottoday";
                }
                String monthh = null;

                switch (month) {
                    case 1:
                        monthh = "January";
                        break;
                    case 2:
                        monthh = "February";
                        break;
                    case 3:
                        monthh = "March";
                        break;
                    case 4:
                        monthh = "April";
                        break;
                    case 5:
                        monthh = "May";
                        break;
                    case 6:
                        monthh = "June";
                        break;
                    case 7:
                        monthh = "July";
                        break;
                    case 8:
                        monthh = "August";
                        break;
                    case 9:
                        monthh = "September";
                        break;
                    case 10:
                        monthh = "October";
                        break;
                    case 11:
                        monthh = "November";
                        break;
                    case 12:
                        monthh = "December";

                        break;


                }
                String date = monthh + " " + dayOfMonth + " , " + year;

                txtDate.setText(date);

            }
        };

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();

                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                final RangeTimePickerDialog mTimePicker;
                mTimePicker = new RangeTimePickerDialog(CallCompanyforAdvanceBooking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM;
                        if (selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        if (selectedHour > 12)
                            selectedHour = selectedHour - 12;


                        String time = selectedHour + " : " + selectedMinute + " " + AM_PM;
                        txtTime.setText(time);

                    }
                }, hour, minute, false);//true = 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.setMin(hour, minute);
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog dialog = new TimePickerDialog(
                        CallCompanyforAdvanceBooking.this, android.R.style.Theme_Holo_Dialog_MinWidth, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
                );
                if (time == "today") {
                    mTimePicker.show();
                } else {
                    dialog.show();
                }

            }
        });

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
                if (hourOfDay > 12)
                    hourOfDay = hourOfDay - 12;


                String time = hourOfDay + " : " + minute + " " + AM_PM;
                txtTime.setText(time);


            }
        };
        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogslide2;

        alertDialog.show();
        alertDialog.setCancelable(false);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showeventpickerdialog();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void showeventpickerdialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CallCompanyforAdvanceBooking.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.selectevent, null);
        Button Continue = (Button) view1.findViewById(R.id.Continue);
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        final ViewFlipper viewFlipper = (ViewFlipper) view1.findViewById(R.id.VF);
        Button nextVF = (Button) view1.findViewById(R.id.nextVF);
        Button previous = (Button) view1.findViewById(R.id.previousVF);

        nextVF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setInAnimation(getBaseContext(), R.anim.scale_in_slow);
                viewFlipper.setOutAnimation(getBaseContext(), R.anim.scale_out_slow);
                viewFlipper.showNext();

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setInAnimation(getBaseContext(), R.anim.scale_in_slow);
                viewFlipper.setOutAnimation(getBaseContext(), R.anim.scale_out_slow);
                viewFlipper.showPrevious();
            }
        });
        functiongif = (GifImageView) view1.findViewById(R.id.functioneventgif);

        try {
            InputStream inputStream = getAssets().open("function gif.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            functiongif.setBytes(bytes);
            functiongif.startAnimation();
        } catch (IOException e) {

        }

        functiongif1 = (GifImageView) view1.findViewById(R.id.functioneventgif1);

        try {
            InputStream inputStream = getAssets().open("function gif 2.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            functiongif1.setBytes(bytes);
            functiongif1.startAnimation();
        } catch (IOException e) {

        }


        partygif = (GifImageView) view1.findViewById(R.id.partyeventgif);

        try {
            InputStream inputStream = getAssets().open("party-gif-animated-2.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            partygif.setBytes(bytes);
            partygif.startAnimation();
        } catch (IOException e) {

        }
        partygif1 = (GifImageView) view1.findViewById(R.id.partyeventgif1);

        try {
            InputStream inputStream = getAssets().open("party-gif-animated-11.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            partygif1.setBytes(bytes);
            partygif1.startAnimation();
        } catch (IOException e) {

        }
        photoshootgif = (GifImageView) view1.findViewById(R.id.photoshooteventgif);

        try {
            InputStream inputStream = getAssets().open("photoshoot.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            photoshootgif.setBytes(bytes);
            photoshootgif.startAnimation();
        } catch (IOException e) {

        }
        photoshootgif1 = (GifImageView) view1.findViewById(R.id.photoshooteventgif1);

        try {
            InputStream inputStream = getAssets().open("photoshoot1.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            photoshootgif1.setBytes(bytes);
            photoshootgif1.startAnimation();
        } catch (IOException e) {

        }
        final LinearLayout layoutfun = (LinearLayout) view1.findViewById(R.id.functioneventlayout);
        final LinearLayout layoutparty = (LinearLayout) view1.findViewById(R.id.partyeventlayout);
        final LinearLayout layoutphoto = (LinearLayout) view1.findViewById(R.id.photoshooteventlayout);

        txtevent = (TextView) view1.findViewById(R.id.myevent);

        layoutphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtevent.setText("PHOTOSHOOTING");
                layoutphoto.setBackgroundColor(getResources().getColor(R.color.dividerColor));
                layoutparty.setBackgroundColor(getResources().getColor(R.color.unselect));
                layoutfun.setBackgroundColor(getResources().getColor(R.color.unselect));

            }

        });

        layoutfun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtevent.setText("FUNCTION");
                layoutfun.setBackgroundColor(getResources().getColor(R.color.dividerColor));
                layoutparty.setBackgroundColor(getResources().getColor(R.color.unselect));
                layoutphoto.setBackgroundColor(getResources().getColor(R.color.unselect));

            }
        });

        layoutparty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtevent.setText("PARTY EVENT");
                layoutparty.setBackgroundColor(getResources().getColor(R.color.dividerColor));
                layoutfun.setBackgroundColor(getResources().getColor(R.color.unselect));
                layoutphoto.setBackgroundColor(getResources().getColor(R.color.unselect));

            }
        });

        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;

        alertDialog.show();
        alertDialog.setCancelable(false);


        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                orderdetails();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void orderdetails() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CallCompanyforAdvanceBooking.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.orderdetailsmanual, null);
        Button submit = (Button) view1.findViewById(R.id.submit);
        Button cancel = (Button) view1.findViewById(R.id.btncancel);
        TextView eventtype = (TextView) view1.findViewById(R.id.eventtype);
        TextView address = (TextView) view1.findViewById(R.id.addressevent);
        TextView date = (TextView) view1.findViewById(R.id.dateevent);
        TextView time = (TextView) view1.findViewById(R.id.timeevent);
        eventtype.setText(txtevent.getText().toString());
        address.setText(txtAddress.getText().toString());
        date.setText(txtDate.getText().toString());
        time.setText(txtTime.getText().toString());

        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;

        alertDialog.show();
        alertDialog.setCancelable(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String customername = Common.currentUser.getName();
                String customerPhone = Common.currentUser.getPhone();
                String txtaddress = txtAddress.getText().toString();
                String txtdate = txtDate.getText().toString();
                String txtime = txtTime.getText().toString();


                String EventType = txtevent.getText().toString();

                sendrequesttocompanyForAdvanceBooking(getIntent().getStringExtra("companyId"), ifcmSerives, getBaseContext(), Common.Lastlocation, customername, customerPhone, txtdate, txtime, txtaddress, EventType);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });


    }

    public void sendrequesttocompanyForAdvanceBooking(final String companyId,
                                                      final IFCMSerives ifcmSerives, final Context context, final Location currentlocation,
                                                      final String Name, final String Phone,
                                                      final String Date, final String Time,
                                                      final String Address, final String EventType) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Common.token_table);

        tokens.orderByKey().equalTo(companyId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot post : dataSnapshot.getChildren()) {
                            Token token = post.getValue(Token.class);

                            // String json_lat_lng = new Gson().toJson(new LatLng(location.getLatitude(), location.getLongitude()));

                            final String customertoken = FirebaseInstanceId.getInstance().getToken();
                            // Notification data = new Notification( FirebaseInstanceId.getInstance().getToken(), json_lat_lng);
                            // Sender content = new Sender(token.getToken() , data);

                            Map<String, String> content = new HashMap<>();
                            content.put("Type", "AdvanceBooking");
                            content.put("Date", Date);
                            content.put("Time", Time);
                            content.put("Address", Address);
                            content.put("CustomerName", Name);
                            content.put("CustomerPhone", Phone);
                            content.put("customerId", Common.userid);
                            content.put("customer", customertoken);
                            content.put("lat1", String.valueOf(lat1));
                            content.put("lng1", String.valueOf(lng1));
                            content.put("lat", String.valueOf(currentlocation.getLatitude()));
                            content.put("lng", String.valueOf(currentlocation.getLongitude()));
                            content.put("EventType", EventType);
                            DataMessage dataMessage = new DataMessage(token.getToken(), content);

                            ifcmSerives.sendMessage(dataMessage)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body().success == 1) {
                                                Toast.makeText(context, "Request sent!", Toast.LENGTH_SHORT).show();

                                                Presentbooking presentbooking = new Presentbooking();
                                                presentbooking.setAddress(Address);
                                                presentbooking.setCustomerId(Common.userid);
                                                presentbooking.setCustomerName(Name);
                                                presentbooking.setCustomerPhone(Phone);
                                                presentbooking.setCustomertoken(customertoken);
                                                presentbooking.setDate(Date);
                                                presentbooking.setTime(Time);
                                                presentbooking.setLat(String.valueOf(currentlocation.getLatitude()));
                                                presentbooking.setLng(String.valueOf(currentlocation.getLongitude()));
                                                presentbooking.setLat1(String.valueOf(lat1));
                                                presentbooking.setLng1(String.valueOf(lng1));
                                                presentbooking.setEventType(EventType);


                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("PresentBookings")
                                                        .child(companyId)
                                                        .child("MyPresentBooking")
                                                        .setValue(presentbooking);

                                                showsentreqDailog();


                                            } else
                                                Toast.makeText(context, "Failed to send the Request!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {

                                            Log.e("ERROR", t.getMessage());

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

/*dialog.dismiss();
                String customername = Common.currentUser.getName();
                String customerPhone = Common.currentUser.getPhone();
                String txtaddress = txtAddress.getText().toString();
                String txtdate = txtDate.getText().toString();
                String txtime = txtTime.getText().toString();


                sendrequesttocompanyForAdvanceBooking(companyId, ifcmSerives, getBaseContext(), Common.Lastlocation, customername, customerPhone, txtdate, txtime, txtaddress);
 */