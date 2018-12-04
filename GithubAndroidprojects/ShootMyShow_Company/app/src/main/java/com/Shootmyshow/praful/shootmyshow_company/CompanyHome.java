package com.Shootmyshow.praful.shootmyshow_company;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow_company.Common.Common;
import com.Shootmyshow.praful.shootmyshow_company.Model.AboutMe;
import com.Shootmyshow.praful.shootmyshow_company.Model.Token;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IGoogleAPI;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;

public class CompanyHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    private GoogleMap mMap;
    String aboutcompany;
    String linktosamplephotos;
    TextView comapnyPhone, companyName;

    LocationCallback locationCallback;
    TextView txtName;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView imageView;
    Switch switch3;
    FirebaseDatabase firebaseDatabase;
    LocationManager locationManager;

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
    CircleImageView imageAvatar, companyphoto;
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
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

     /*   if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.locationdialog, null);
        dialog.setView(view);
        final AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog.setCancelable(false);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        switch3 = (Switch) findViewById(R.id.switch3);
        switch3.setChecked(true);
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shownotidialog();
                if (switch3.isChecked()) {
                    startService(new Intent(CompanyHome.this, Myservices.class));
                    FirebaseDatabase.getInstance().getReference()
                            .child("Studioforegroundserivces")
                            .child(pref.getString("Id", ""))
                            .child("want").setValue("yes");
                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Studioforegroundserivces")
                            .child(pref.getString("Id", ""))
                            .child("want").setValue("no");

                }
            }
        });

        Button cancel = (Button)view.findViewById(R.id.cancel);
        Button go = (Button)view.findViewById(R.id.go);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);

            }
        });
       // alertDialog.show();



        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        if(!gps_enabled && !network_enabled) {
            if (!(CompanyHome.this.isFinishing())) {
                alertDialog.show();
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        companyphoto = (CircleImageView) findViewById(R.id.companyphoto);
        companyName = (TextView) findViewById(R.id.CompanyName);
        comapnyPhone = (TextView) findViewById(R.id.companyphone);
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
        }
        View navigationHeaderView = navigationView.getHeaderView(0);
        txtName = (TextView) navigationHeaderView.findViewById(R.id.CompanyName);
        final TextView numberofsuccessfulbookings = (TextView) navigationHeaderView.findViewById(R.id.numberofsuccessfulbookings);
        final TextView txtStars = (TextView) navigationHeaderView.findViewById(R.id.txtStar);
        imageAvatar = (CircleImageView) navigationHeaderView.findViewById(R.id.image_avatar);
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

        companyphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyHome.this, Profile.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CompanyHome.this, companyphoto, ViewCompat.getTransitionName(companyphoto));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, options.toBundle());

            }
        });

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

                    if (ActivityCompat.checkSelfPermission(CompanyHome.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CompanyHome.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void shownotidialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CompanyHome.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.shownoti, null);
        TextView notificationmessage = (TextView) view1.findViewById(R.id.notificationmessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications from the Customers that you have booked to , even when the app is working in background or it is killed . Like if they have cancelled your booking or it can be a Booking Request !, you will be notified ." +
                    "\nAnd for those whose Android version is lesser to 8, no need to switch ON.<font color=\"#42f471\"><bold>"
                    + "Yours is more or equal to 8 "
                    + "</bold></font>" + ", So please switch it ON . ";


            notificationmessage.setText(Html.fromHtml(text));

        } else {
            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications from the Camera Studio that you have booked to , even when the app is working in background or it is killed . Like if they have cancelled your booking or it can be a Booking Request ! , you will be notified .   " +
                    "And for those whose Android version is lesser to 8, no need to switch ON.  <font color=\"#42f471\"><bold>" + "Yours is lesser than 8" + "</bold></font>" + " , So there is no need to Switch it On";
            notificationmessage.setText(Html.fromHtml(text));
        }
        Button ok = (Button) view1.findViewById(R.id.ok);
        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

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
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{

                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CALL_PHONE


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

                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            showDialogupdateinfo();
                            Toast.makeText(CompanyHome.this, "Please update your Info.", Toast.LENGTH_LONG).show();


                        }
                    }.start();


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

        } else if (id == R.id.nav_TC) {
            Intent intent = new Intent(CompanyHome.this, Tc.class);
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
        final MaterialEditText link = (MaterialEditText) view.findViewById(R.id.linktosamplephotos);
        final MaterialEditText about = (MaterialEditText) view.findViewById(R.id.aboutme);
        final MaterialEditText editphone = (MaterialEditText) view.findViewById(R.id.editphone);
        imageView = (ImageView) view.findViewById(R.id.imageupload);
        final MaterialEditText priceforonepic = (MaterialEditText) view.findViewById(R.id.priceforonepic);
        final MaterialEditText pricefor1hourvideo = (MaterialEditText) view.findViewById(R.id.pricefor1hourvideo);

        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button update = (Button) view.findViewById(R.id.update);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        final AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
                final SpotsDialog waiting = new SpotsDialog(CompanyHome.this);
                waiting.show();
                aboutcompany = about.getText().toString();
                linktosamplephotos = link.getText().toString();

                AboutMe aboutMe = new AboutMe(aboutcompany, linktosamplephotos);
                if (!TextUtils.isEmpty(aboutcompany)) {
                    FirebaseDatabase.getInstance().getReference().child("AboutCompany")
                            .child(Common.userid)
                            .child("AboutMe")
                            .child("aboutcompany").setValue(aboutcompany);

                }
                if (!TextUtils.isEmpty(linktosamplephotos)) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("AboutCompany")
                            .child(Common.userid)
                            .child("AboutMe")
                            .child("link").setValue(linktosamplephotos);
                }


                Map<String, Object> pricings = new HashMap<>();
                pricings.put("Price_for_one_photo", priceforonepic.getText().toString());
                pricings.put("Price_for_onehour_video", pricefor1hourvideo.getText().toString());

                if (!TextUtils.isEmpty(pricefor1hourvideo.getText().toString())) {
                    databaseReference.child("pricingdetails").child(Common.userid).child("Price_for_onehour_video")
                            .setValue(pricefor1hourvideo.getText().toString());
                }
                if (!TextUtils.isEmpty(priceforonepic.getText().toString())) {
                    databaseReference.child("pricingdetails").child(Common.userid).child("Price_for_one_photo")
                            .setValue(priceforonepic.getText().toString());
                }
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        final String name = editname.getText().toString();
                        final String phone = editphone.getText().toString();

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
                                        if (task.isSuccessful()) {
                                            if (!TextUtils.isEmpty(name)) {
                                                companyName.setText(name);
                                                txtName.setText(name);
                                            }
                                            if (!TextUtils.isEmpty(phone))
                                                comapnyPhone.setText(phone);


                                            Toast.makeText(CompanyHome.this, "Information Updated ! ", Toast.LENGTH_LONG).show();
                                        } else
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
                dialog.setCancelable(false);
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
                                                            Toast.makeText(CompanyHome.this, "Image updated !", Toast.LENGTH_SHORT).show();
                                                            Picasso.with(CompanyHome.this).load(uri).into(imageView);
                                                            Picasso.with(CompanyHome.this).load(uri).into(companyphoto);
                                                            Picasso.with(CompanyHome.this).load(uri).into(imageAvatar);

                                                            Common.currentUser.setAvatarUrl(uri.toString());
                                                        } else
                                                            Toast.makeText(CompanyHome.this, "Image update error !", Toast.LENGTH_SHORT).show();

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
       mMap.setPadding(100, 10, 10, 10);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        if (mapFragment.getView() != null &&
                mapFragment.getView().findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        buildLocationreq();
        buildLocationCallback();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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


           /* LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                       return;
            }
            lm.addGpsStatusListener(new android.location.GpsStatus.Listener() {
                public void onGpsStatusChanged(int event) {
                    switch (event) {
                        case GPS_EVENT_STARTED:
                            Toast.makeText(CompanyHome.this, "started", Toast.LENGTH_SHORT).show();
                            break;
                        case GPS_EVENT_STOPPED:
                            Toast.makeText(CompanyHome.this, "ended", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });*/
           /* boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}
*/
           /* if(!gps_enabled && !network_enabled) {
                // notify user
            *//*    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Please turn ON GPS");
                dialog.setPositiveButton("Open GPS settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        //get gps
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();*//*

            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
*/
          /*  if ((getIntent().getStringExtra("BID") != null) && (getIntent().getStringExtra("type") != null)) {
                if (getIntent().getStringExtra("type").equals("cancel")) {
                    Intent intent = new Intent(CompanyHome.this, CustomercancelledtheBooking.class);
                    intent.putExtra("Id", getIntent().getStringExtra("BID"));
                    startActivity(intent);
                }
            }
            if (getIntent().getStringExtra("type") != null && (getIntent().getStringExtra("type").equals("AdvanceBooking"))) {
                Intent intent = new Intent(CompanyHome.this, CustomerCallForAdvanceBooking.class);
                intent.putExtra("N", "N");
                startActivity(intent);
            }*/
