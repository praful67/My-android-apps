package com.praful.praful.uber;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.praful.praful.uber.Common.Common;
import com.praful.praful.uber.Model.Coustomers;
import com.praful.praful.uber.Model.Token;
import com.praful.praful.uber.Remote.IGoogleAPI;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    private GoogleMap mMap;

    LocationCallback locationCallback;
    String k;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference, imagefolder;

    private static final int MY_PERMISSION_REQUEST_CODE = 7000;

    private LocationRequest locationRequest;


    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    DatabaseReference drivers;
    GeoFire geoFire;

    Marker current;
    SupportMapFragment mapFragment;

    private List<LatLng> polyLineList;
    private Marker cmarker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endposition, currentposition;
    private int index, next;
    private String destination;
    private PolylineOptions polylineOptions, blackpolylineoptions;
    private Polyline blackpolyline, greypolyline;
    StickySwitch location_switch1;
    DatabaseReference onlineref, currentuserref;

    private IGoogleAPI services;
    Runnable drawPathRunnable = new Runnable() {
        @Override
        public void run() {
            if (index < polyLineList.size() - 1) {
                index++;
                next = index + 1;

            }
            if (index < polyLineList.size() - 1) {
                startPosition = polyLineList.get(index);
                endposition = polyLineList.get(next);
            }
            final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    v = valueAnimator.getAnimatedFraction();
                    lng = v * endposition.longitude + (1 - v) * startPosition.longitude;
                    lat = v * endposition.latitude + (1 - v) * startPosition.latitude;
                    LatLng newPos = new LatLng(lat, lng);
                    cmarker.setPosition(newPos);
                    cmarker.setAnchor(0.5f, 0.5f);
                    cmarker.setRotation(getBearing(startPosition, newPos));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(newPos).zoom(15.5f).build()));

                }
            });

            // valueAnimator.start();
            handler.postDelayed(this, 3000);

        }
    };

    private float getBearing(LatLng startPosition, LatLng endposition) {
        double lat = Math.abs(startPosition.latitude - endposition.latitude);
        double lng = Math.abs(startPosition.longitude - endposition.longitude);

        if (startPosition.latitude < endposition.latitude && startPosition.longitude < endposition.longitude)

            return (float) (Math.toDegrees(Math.atan(lng / lat)));

        else if (startPosition.latitude >= endposition.latitude && startPosition.longitude < endposition.longitude)

            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat)) + 90));

        else if (startPosition.latitude >= endposition.latitude && startPosition.longitude >= endposition.longitude)

            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (startPosition.latitude < endposition.latitude && startPosition.longitude >= endposition.longitude)

            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat)) + 270));


        return -1;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startService(new Intent(this, Myservices.class));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        CircleImageView companyphoto = (CircleImageView) findViewById(R.id.companyphoto);
        TextView companyName = (TextView) findViewById(R.id.CompanyName);
        TextView comapnyPhone = (TextView) findViewById(R.id.companyphone);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        location_switch1 = (StickySwitch) findViewById(R.id.location_switch1);

        if (getIntent() != null) {
            if (getIntent().getStringExtra("NFT") != null) {
                if (getIntent().getStringExtra("NFT").equals("NFT")) {
                    Toast.makeText(this, "Your Online", Toast.LENGTH_SHORT).show();
                    location_switch1.setDirection(StickySwitch.Direction.RIGHT);
                }
            }

            if ((getIntent().getStringExtra("BID") != null ) && (getIntent().getStringExtra("type") != null))
            {
                if (getIntent().getStringExtra("type").equals("cancel"))
                {
                    Intent intent = new Intent(CompanyHome.this , CustomercancelledtheBooking.class);
                    intent.putExtra("Id" , getIntent().getStringExtra("BID"));
                    startActivity(intent);
                }
            }
            if (getIntent().getStringExtra("type") != null && (getIntent().getStringExtra("type").equals("AdvanceBooking")))
            {
                Intent intent = new Intent(CompanyHome.this ,CustomerCallForAdvanceBooking.class );
                intent.putExtra("N" , "N");
                startActivity(intent);
            }
        }
        View navigationHeaderView = navigationView.getHeaderView(0);
        TextView txtName = (TextView) navigationHeaderView.findViewById(R.id.CompanyName);
        final TextView numberofsuccessfulbookings = (TextView) navigationHeaderView.findViewById(R.id.numberofsuccessfulbookings);
        final TextView txtStars = (TextView) navigationHeaderView.findViewById(R.id.txtStar);
        CircleImageView imageAvatar = (CircleImageView) navigationHeaderView.findViewById(R.id.image_avatar);
        txtName.setText(Common.currentUser.getName());
        /*

                Intent intent = new Intent();
                String packageName = getPackageName();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
 */

        databaseReference.child(Common.companiesInfo_table)
                .child(Common.userid).child("rates")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String txt = dataSnapshot.getValue(String.class);
                        txtStars.setText(txt);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        final FloatingActionButton currentLocation = (FloatingActionButton) findViewById(R.id.currentlocation);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUploation();

            }
        });

        numberofsuccessfulbookings.setText(Common.currentUser.getNumberofsuccessfulbookings());

        FloatingActionButton profile = (FloatingActionButton) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyHome.this, Profile.class);
                startActivity(intent);
            }
        });

        // txtStars.setText();

        if (Common.currentUser.getAvatarUrl() != null && !TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
            Picasso.with(this).load(Common.currentUser.getAvatarUrl()).into(imageAvatar);
        }
        if (Common.currentUser.getAvatarUrl() != null && !TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
            Picasso.with(this).load(Common.currentUser.getAvatarUrl()).into(companyphoto);
        }
        comapnyPhone.setText(Common.currentUser.getPhone());
        companyName.setText(Common.currentUser.getName());

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                onlineref = FirebaseDatabase.getInstance().getReference().child(".info/connected");

                currentuserref = FirebaseDatabase.getInstance().getReference(Common.companies_table)
                        .child(account.getId());
                // currentuserref.keepSynced(true);
                onlineref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //   currentuserref.onDisconnect().removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
        Button help = (Button) findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyHome.this, Help.class);
                startActivity(intent);
            }
        });


        location_switch1.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                FirebaseDatabase.getInstance().getReference(Common.companies_table).child(Common.userid).removeValue();

                if (direction == StickySwitch.Direction.RIGHT) {
                    FirebaseDatabase.getInstance().goOnline();

                    if (ActivityCompat.checkSelfPermission(CompanyHome.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CompanyHome.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    buildLocationreq();
                    buildLocationCallback();

                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    displaylocation();
                    Toast.makeText(CompanyHome.this, "Your Online", Toast.LENGTH_SHORT).show();

                } else if (direction == StickySwitch.Direction.LEFT) {
                    FirebaseDatabase.getInstance().goOffline();
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    if (current != null)
                        current.remove();

                    mMap.clear();
                    if (handler != null) {
                        handler.removeCallbacks(drawPathRunnable);
                    }
                    Toast.makeText(CompanyHome.this, "Your Offline", Toast.LENGTH_SHORT).show();


                }
            }
        });
        setUploation();

        updateFirebaseToken();

        drivers = FirebaseDatabase.getInstance().getReference(Common.companies_table);
        geoFire = new GeoFire(drivers);
        services = Common.getGoogleAPI();


    }


    private void updateFirebaseToken() {

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference tokens = db.getReference(Common.token_table);


                Token token = new Token(FirebaseInstanceId.getInstance().getToken());

                tokens.child(account.getId()).setValue(token);

            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
    }

    private void getDirection() {
        currentposition = new LatLng(Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude());
        String requestApi = null;
        try {

            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" + currentposition.latitude + "," + currentposition.longitude + "&" +
                    "destination=" + destination + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            Log.d("C A M E R A N", requestApi);
            services.getPath(requestApi).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject route = jsonArray.getJSONObject(i);
                            JSONObject poly = route.getJSONObject("overview_polyline");
                            String polyline = poly.getString("points");
                            polyLineList = decodePoly(polyline);
                        }
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng latLng : polyLineList)

                            builder.include(latLng);
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                        mMap.animateCamera(cameraUpdate);

                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.GRAY);
                        polylineOptions.width(5);
                        polylineOptions.startCap(new SquareCap());
                        polylineOptions.endCap(new SquareCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polyLineList);
                        greypolyline = mMap.addPolyline(polylineOptions);


                        blackpolylineoptions = new PolylineOptions();
                        blackpolylineoptions.color(Color.BLACK);
                        blackpolylineoptions.width(5);
                        blackpolylineoptions.startCap(new SquareCap());
                        blackpolylineoptions.endCap(new SquareCap());
                        blackpolylineoptions.jointType(JointType.ROUND);
                        blackpolyline = mMap.addPolyline(blackpolylineoptions);

                        mMap.addMarker(new MarkerOptions().position(polyLineList.get(polyLineList.size() - 1)).
                                title("Pickup Location"));

                        //Animation
                        ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0, 100);
                        polyLineAnimator.setDuration(2000);
                        polyLineAnimator.setInterpolator(new LinearInterpolator());
                        polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {

                                List<LatLng> points = greypolyline.getPoints();
                                int percentValue = (int) animation.getAnimatedValue();
                                int size = points.size();
                                int newpoints = (int) (size * (percentValue / 100.0f));
                                List<LatLng> p = points.subList(0, newpoints);
                                blackpolyline.setPoints(p);

                            }
                        });

                        //  polyLineAnimator.start();
                        cmarker = mMap.addMarker(new MarkerOptions().position(currentposition).flat(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera)));

                        handler = new Handler();
                        index = -1;
                        next = 1;
                        handler.postDelayed(drawPathRunnable, 3000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(CompanyHome.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void setUploation() {
        //  if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        //         && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        //         ) {

        //     ActivityCompat.requestPermissions(this, new String[]{

        //             android.Manifest.permission.ACCESS_COARSE_LOCATION,
        //           android.Manifest.permission.ACCESS_FINE_LOCATION


        //   }, MY_PERMISSION_REQUEST_CODE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{

                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE


            }, MY_PERMISSION_REQUEST_CODE);
        } else {

            buildLocationreq();
            buildLocationCallback();
            if (location_switch1.getDirection() == StickySwitch.Direction.RIGHT)
                displaylocation();


        }

    }

    private void buildLocationCallback() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("latitude", String.valueOf(location.getLatitude()));
                    editor.putString("longitude", String.valueOf(location.getLongitude()));
                    editor.apply();
                    Common.Lastlocation = location;
                }


                displaylocation();
            }
        };
    }

    private void buildLocationreq() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildLocationCallback();
                    buildLocationreq();
                    location_switch1.setDirection(StickySwitch.Direction.RIGHT);
                    displaylocation();


                }

        }
    }

    private void displaylocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        /*                 */


        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Common.Lastlocation = location;

                        if (Common.Lastlocation != null) {

                            if (location_switch1.getDirection() == StickySwitch.Direction.RIGHT) {

                                final double latitude = Common.Lastlocation.getLatitude();
                                final double logitude = Common.Lastlocation.getLongitude();


                                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                                    @Override
                                    public void onSuccess(Account account) {

                                        geoFire.setLocation(account.getId(), new GeoLocation(latitude, logitude), new GeoFire.CompletionListener() {

                                            @Override

                                            public void onComplete(String key, DatabaseError error) {

                                                if (current != null)
                                                    current.remove();
                                                current = mMap.addMarker(new MarkerOptions().
                                                        position(new LatLng(latitude, logitude))
                                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).title("You")
                                                );

                                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, logitude), 15.0f));
                                                // rotateMarker(current , -360 , mMap);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(AccountKitError accountKitError) {

                                    }
                                });
                            }
                        } else {
                            Log.d("ERROR", "CANNOT GET YOUR LOCATION");
                        }
                    }
                });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.company_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_AdvanceBooking) {

            Intent intent = new Intent(CompanyHome.this, AdvanceBookings.class);
            startActivity(intent);

        } else if (id == R.id.nav_signout) {
            signout();

        } else if (id == R.id.nav_CancelledAdvanceBooking) {
            Intent intent = new Intent(CompanyHome.this, CancelledBookings.class);
            startActivity(intent);

        } else if (id == R.id.nav_updateinfo) {

            showDialogupdateinfo();

        } else if (id == R.id.nav_CompletedAdvanceBooking) {
            Intent intent = new Intent(CompanyHome.this, CompletedBookings.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showDialogupdateinfo() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompanyHome.this);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_updateinfo, null);

        final MaterialEditText editname = (MaterialEditText) view.findViewById(R.id.editName);
        final MaterialEditText editphone = (MaterialEditText) view.findViewById(R.id.editphone);
        imageView = (ImageView) view.findViewById(R.id.imageupload);
        final MaterialEditText priceforonepic = (MaterialEditText) view.findViewById(R.id.priceforonepic);
        final MaterialEditText pricefor1hourvideo = (MaterialEditText) view.findViewById(R.id.pricefor1hourvideo);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                final SpotsDialog waiting = new SpotsDialog(CompanyHome.this);
                waiting.show();

                Map<String, Object> pricings = new HashMap<>();
                pricings.put("Price_for_one_photo", priceforonepic.getText().toString());
                pricings.put("Price_for_onehour_video", pricefor1hourvideo.getText().toString());
                databaseReference.child("pricingdetails").child(Common.userid).setValue(pricings);
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        String name = editname.getText().toString();
                        String phone = editphone.getText().toString();

                        Map<String, Object> updateInfo = new HashMap<>();
                        if (!TextUtils.isEmpty(name))
                            updateInfo.put("name", name);

                        if (!TextUtils.isEmpty(phone))
                            updateInfo.put("phone", phone);

                        DatabaseReference companyInformation = FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table);
                        companyInformation.child(account.getId())
                                .updateChildren(updateInfo)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                            Toast.makeText(CompanyHome.this, "Information Updated ! please signout and signin to see the change", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(CompanyHome.this, "Informatin Update failed!", Toast.LENGTH_SHORT).show();

                                        waiting.dismiss();
                                    }
                                });

                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {

                    }
                });
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void chooseimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture: "), Common.PICK_IMAGE_REQ);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null) {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Uploading");
                dialog.show();

                String imagename = UUID.randomUUID().toString();
                imagefolder = storageReference.child("ImagesCompanies/" + imagename);
                imagefolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(CompanyHome.this, "Uploaded your image", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                                    @Override
                                    public void onSuccess(Account account) {
                                        Map<String, Object> avatarUpdate = new HashMap<>();
                                        avatarUpdate.put("avatarUrl", uri.toString());
                                        DatabaseReference companyInformation = FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table);
                                        companyInformation.child(account.getId())
                                                .updateChildren(avatarUpdate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(CompanyHome.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                                                            Picasso.with(CompanyHome.this).load(uri).into(imageView);

                                                        } else
                                                            Toast.makeText(CompanyHome.this, "Upload error !", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                    }

                                    @Override
                                    public void onError(AccountKitError accountKitError) {

                                    }
                                });
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        dialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        buildLocationreq();
        buildLocationCallback();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void signout() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to sign out ?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AccountKit.logOut();
                        Intent intent = new Intent(CompanyHome.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();

    }
}
