package com.Shootmyshow.praful.shootmyshow;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.Shootmyshow.praful.shootmyshow.MyReceiver;
import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.Shootmyshow.praful.shootmyshow.Helper.CustomInfoWindow;
import com.Shootmyshow.praful.shootmyshow.Model.AboutMe;
import com.Shootmyshow.praful.shootmyshow.Model.Coustomers;
import com.Shootmyshow.praful.shootmyshow.Model.DataMessage;
import com.Shootmyshow.praful.shootmyshow.Model.FCMResponse;
import com.Shootmyshow.praful.shootmyshow.Model.Presentbooking;
import com.Shootmyshow.praful.shootmyshow.Model.Token;
import com.Shootmyshow.praful.shootmyshow.Model.User;
import com.Shootmyshow.praful.shootmyshow.Remote.IFCMSerives;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.felipecsl.gifimageview.library.GifImageView;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    SupportMapFragment mapFragment;
    ImageView imageView;
    AboutMe aboutMe;
    private GoogleMap mMap;
    TextView txtphone, txtName;

    String address1;
    ScrollView mScrollView;
    ListView commentsList;
    String time;
    CircleImageView imageAvatar;

    String move = "yes";
    String datetap = "nottapped", timetap = "nottapped";
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference, imagefolder;

    ProgressDialog progressDialog;
    double lat1, lng1;

    TextView txtDate, txtTime, txtAddress, txtevent;
    private static final int MY_PERMISSION_REQUEST_CODE = 7000;

    private LocationRequest locationRequest;
    Switch switch3;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    Geocoder geocoder, geocoder1;

    List<Address> addresses = null;
    List<Address> addresses1 = null;
    CircleImageView photo;
    Marker CoustMarker, markerdestination;

    Bottom_Sheet_Coustomer bottom_sheet_coustomer;

    Button btnbooklater;

    int radius = 1;
    int distance = 1;
    private static final int LIMIT = 25;

    IFCMSerives ifcmSerives;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    DatabaseReference companiesAvailable;

    PlaceAutocompleteFragment place_location;

    String Placelocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button Whentouse = (Button) findViewById(R.id.Whentouse);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       /* BroadcastReceiver broadcastReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.praful.ubercoustomer");
        registerReceiver(broadcastReceiver, intentFilter);*/
        getWindow().getAttributes().windowAnimations = R.style.Dialogslide1;
        if (getIntent().getStringExtra("type") != null && getIntent().getStringExtra("type").equals("response")) {
            Toast.makeText(getBaseContext(), getIntent().getStringExtra("response"), Toast.LENGTH_LONG).show();
        }
        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.locationdialog, null);
        dialog.setView(view);
        final AlertDialog alertDialog = dialog.create();

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button go = (Button) view.findViewById(R.id.go);
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
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        switch3 = (Switch) findViewById(R.id.switch3);
        switch3.setChecked(true);
        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shownotidialog();
                if (switch3.isChecked()) {
                    startService(new Intent(Home.this, MyService.class));
                    FirebaseDatabase.getInstance().getReference()
                            .child("foregroundserivces")
                            .child(pref.getString("Id", ""))
                            .child("want").setValue("yes");
                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("foregroundserivces")
                            .child(pref.getString("Id", ""))
                            .child("want").setValue("no");

                }
            }
        });

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            if (!(Home.this.isFinishing())) {
                alertDialog.show();
            }
        }
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.addresspickup, null);
        txtAddress = (TextView) view1.findViewById(R.id.txtaddress);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }.start();


        /*startService(new Intent(getBaseContext(), Myservice.class));*/
       /* Intent intent = new Intent();
        String packageName = getPackageName();
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
*/
        Common.Switch = (Switch) findViewById(R.id.switch1);
        Common.Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Common.Switch.isChecked()) {
                    txtAddress.setText(address1);
                    move = "yes";
                    showdatetimepickdialog();
                    Common.Switch.setChecked(false);
                }
            }
        });


        if (getIntent().getStringExtra("type") != null && getIntent().getStringExtra("type").equals("response")) {
            Toast.makeText(getBaseContext(), getIntent().getStringExtra("response"), Toast.LENGTH_LONG).show();
        }
        photo = (CircleImageView) findViewById(R.id.photo);
        if (Common.currentUser.getAvatarUrl() != null && !TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
            Picasso.with(this).load(Common.currentUser.getAvatarUrl()).into(photo);
        }


        FloatingActionButton currentLocation = (FloatingActionButton) findViewById(R.id.currentlocation);
        FloatingActionButton refresh = (FloatingActionButton) findViewById(R.id.refresh);
        FloatingActionButton idavailablecompany = (FloatingActionButton) findViewById(R.id.idavailablecompany);
        idavailablecompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Listofavailablecompanies.class);
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile.class);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Home.this, photo, ViewCompat.getTransitionName(photo));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent, options.toBundle());
            }
        });
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        ifcmSerives = Common.getFCMService();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navigationHeaderView = navigationView.getHeaderView(0);
        txtphone = (TextView) navigationHeaderView.findViewById(R.id.Customerphone);
        txtphone.setText(Common.currentUser.getPhone());

        txtName = (TextView) navigationHeaderView.findViewById(R.id.CustomerName);

        imageAvatar = (CircleImageView) navigationHeaderView.findViewById(R.id.image_avatar);
        txtName.setText(Common.currentUser.getName());
        FirebaseDatabase.getInstance().goOnline();


        if (Common.currentUser.getAvatarUrl() != null && !TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
            Picasso.with(this).load(Common.currentUser.getAvatarUrl()).into(imageAvatar);
        }


        navigationView.setNavigationItemSelectedListener(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //   imexp = (ImageView) findViewById(R.id.imgExp);

        // btnpickupreq = (Button) findViewById(R.id.pickupreq);
        btnbooklater = (Button) findViewById(R.id.booklater);

        btnbooklater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // showpickupaddressdailog();
                showeventpickerdialog();

            }
        });

        Whentouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Whentouse.class);
                startActivity(intent);
                move = "yes";
            }
        });


        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.isCompanyFound = false;
                Common.companyId = "";

                move = "yes";
                setUploation();

            }
        });
//        place_destination = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_destination);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.isCompanyFound = false;
                Common.companyId = "";
                move = "yes";

                loadAllAvailablecompanies(new LatLng(Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));
                final ProgressDialog dialog1 = new ProgressDialog(Home.this);

                dialog1.setMessage("Getting all available companies for you , around 25km.");
                dialog1.show();

                new CountDownTimer(2000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub

                        dialog1.dismiss();
                    }
                }.start();

            }
        });
        setUploation();
        updateFirebaseToken();
        place_location = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_location);

        place_location.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.clear();

                Placelocation = place.getAddress().toString();
                lat1 = place.getLatLng().latitude;
                lng1 = place.getLatLng().longitude;
                move = "not";

                CoustMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()
                ).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).title("Pick up here"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));

                Toast.makeText(Home.this, String.format("Address for pick up in Advance Booking is %s", Placelocation), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });


    }

    private void shownotidialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.shownoti, null);
        TextView notificationmessage = (TextView) view1.findViewById(R.id.notificationmessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications from the Camera Studio that you have booked to , even when the app is working in background or it is killed . Like if they have cancelled your booking or to verify for completeness of a booking , you will be notified ." +
                    "\nAnd for those whose Android version is lesser to 8, no need to switch ON.<font color=\"#42f471\"><bold>"
                    + "Yours is more or equal to 8 "
                    + "</bold></font>" + ", So please switch it ON,  atleast till you get the response from the Camera Studio that you have booked and later you can switch it OFF";


            notificationmessage.setText(Html.fromHtml(text));

        } else {
            String text = "For those whose Android version is  <font color=\"#42f471\"><bold>* more or equal to 8 (Oreo)</bold></font> , by switching ON , you will be receiving notifications from the Camera Studio that you have booked to , even when the app is working in background or it is killed . Like if they have cancelled your booking or to verify for completeness of a booking , you will be notified .   " +
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

    private void showpickupaddressdailog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.addresspickup, null);
        txtAddress = (TextView) view1.findViewById(R.id.txtaddress);
        Button next = (Button) view1.findViewById(R.id.next);
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        txtAddress.setText(Placelocation);


        RadioButton radiobuttoncurrentlocation;
        RadioButton tappedlocation;

        radiobuttoncurrentlocation = (RadioButton) view1.findViewById(R.id.currentlocation);
        tappedlocation = (RadioButton) view1.findViewById(R.id.tappedlocation);

        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogslide1;
        alertDialog.show();

        radiobuttoncurrentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat1 = Common.Lastlocation.getLatitude();
                lng1 = Common.Lastlocation.getLongitude();
                //txtAddress.setText(String.format("%f %f", Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));

                Double lat = Common.Lastlocation.getLatitude();
                Double lng = Common.Lastlocation.getLongitude();
                geocoder = new Geocoder(Home.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1);

                    if (addresses != null && addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0);
                        txtAddress.setText(address);
                    } else {
                        Toast.makeText(Home.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        showwritepickupaddressdailog();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        tappedlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(address1)) {
                    txtAddress.setText(address1);
                } else {
                    Toast.makeText(Home.this, "Hey ,you did not tap on map yet !", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    move = "not";
                    showtaponmapdialog();
                }
            }
        });

        AdView adView = (AdView) view1.findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        Rect displayRectangle = new Rect();
        Window window = this.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                1.0f), (int) (displayRectangle.height() * 0.77f));
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
                if (!TextUtils.isEmpty(txtAddress.getText().toString())) {
                    alertDialog.dismiss();
                    showdatetimepickdialog();
                } else {
                    Toast.makeText(Home.this, "Please select the pick up address", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void showwritepickupaddressdailog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.showwritepickupaddressdailog, null);
        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        Button next = (Button) view1.findViewById(R.id.next);
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        final MaterialEditText editaddress = (MaterialEditText) view1.findViewById(R.id.editAddress);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editaddress.getText().toString())) {
                    txtAddress.setText(editaddress.getText().toString());
                    showdatetimepickdialog();
                    alertDialog.dismiss();

                } else {
                    Toast.makeText(Home.this, "Please type address", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog.show();


    }

    private void showtaponmapdialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.showtaponmapdialog, null);
        Button done = (Button) view1.findViewById(R.id.done);

        builder.setView(view1);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

    }

    private void showdatetimepickdialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.dateandtime, null);
        Button next = (Button) view1.findViewById(R.id.next);
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        txtDate = (TextView) view1.findViewById(R.id.txtDate);
        txtTime = (TextView) view1.findViewById(R.id.txtTime);
        AdView adView = (AdView) view1.findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog dialog = new DatePickerDialog(
                        Home.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth, onDateSetListener
                        , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                datetap = "tapped";
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
                mTimePicker = new RangeTimePickerDialog(Home.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timetap = "tapped";
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
                        Home.this, android.R.style.Theme_Holo_Dialog_MinWidth, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
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
                timetap = "tapped";
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
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogslide2;
        alertDialog.show();

        Rect displayRectangle = new Rect();
        Window window = this.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                1.0f), (int) (displayRectangle.height() * 0.7f));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datetap.equals("tapped") && timetap.equals("tapped")) {
                    datetap = "nottapped";
                    timetap = "nottapped";
                    alertDialog.dismiss();
                    orderdetails();
                } else {
                    Toast.makeText(Home.this, "Hey , select time and date first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datetap = "nottapped";
                timetap = "nottapped";
                alertDialog.dismiss();
            }
        });

    }

    private void showeventpickerdialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.selectevent, null);
        Button Continue = (Button) view1.findViewById(R.id.Continue);
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        final LinearLayout layoutfun = (LinearLayout) view1.findViewById(R.id.functioneventlayout);
        final LinearLayout layoutparty = (LinearLayout) view1.findViewById(R.id.partyeventlayout);
        final LinearLayout layoutphoto = (LinearLayout) view1.findViewById(R.id.photoshooteventlayout);
        AdView adView = (AdView) view1.findViewById(R.id.ad_banner);
        AdView adView1 = (AdView) view1.findViewById(R.id.ad_banner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView1.loadAd(adRequest);

        final KenBurnsView F1 = (KenBurnsView) view1.findViewById(R.id.F1);
        final KenBurnsView F2 = (KenBurnsView) view1.findViewById(R.id.F2);
        final KenBurnsView F3 = (KenBurnsView) view1.findViewById(R.id.F3);
        final KenBurnsView F4 = (KenBurnsView) view1.findViewById(R.id.F4);
        final KenBurnsView P1 = (KenBurnsView) view1.findViewById(R.id.p1);
        final KenBurnsView P2 = (KenBurnsView) view1.findViewById(R.id.p2);
        final KenBurnsView P1h = (KenBurnsView) view1.findViewById(R.id.PH1);
        final KenBurnsView P2h = (KenBurnsView) view1.findViewById(R.id.PH2);
        final KenBurnsView P3h = (KenBurnsView) view1.findViewById(R.id.PH3);
        final KenBurnsView P4h = (KenBurnsView) view1.findViewById(R.id.PH4);
        final GabrielleViewFlipper F = (GabrielleViewFlipper) view1.findViewById(R.id.VFF);
        final GabrielleViewFlipper P = (GabrielleViewFlipper) view1.findViewById(R.id.VFP);
        final GabrielleViewFlipper PH = (GabrielleViewFlipper) view1.findViewById(R.id.VFPH);

        final GabrielleViewFlipper viewFlipper = (GabrielleViewFlipper) view1.findViewById(R.id.VF);
        Button nextVF = (Button) view1.findViewById(R.id.nextVF);
        txtevent = (TextView) view1.findViewById(R.id.myevent);
        Button previous = (Button) view1.findViewById(R.id.previousVF);
        txtevent.setText("FUNCTION");

        nextVF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtevent.getText().equals("FUNCTION"))
                    txtevent.setText("PARTY EVENT");

                else if (txtevent.getText().equals("PARTY EVENT"))
                    txtevent.setText("PHOTOSHOOTING");

                else if (txtevent.getText().equals("PHOTOSHOOTING"))
                    txtevent.setText("FUNCTION");

                viewFlipper.setInAnimation(getBaseContext(), R.anim.scale_in_slow);
                viewFlipper.setOutAnimation(getBaseContext(), R.anim.scale_out_slow);
                viewFlipper.showNext();

            }
        });
        F.setFlipInterval(5000);
        F.setInAnimation(getBaseContext(), R.anim.slide_left_to_right);
        F.setOutAnimation(getBaseContext(), R.anim.slide_right_to_left);
        F.startFlipping();

        P.setFlipInterval(5000);
        P.setInAnimation(getBaseContext(), R.anim.slide_left_to_right);
        P.setOutAnimation(getBaseContext(), R.anim.slide_right_to_left);
        P.startFlipping();

        PH.setFlipInterval(5000);
        PH.setInAnimation(getBaseContext(), R.anim.slide_left_to_right);
        PH.setOutAnimation(getBaseContext(), R.anim.slide_right_to_left);
        PH.startFlipping();

        AccelerateDecelerateInterpolator a = new AccelerateDecelerateInterpolator();
        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000, a);

        F1.setTransitionGenerator(generator);
        F2.setTransitionGenerator(generator);
        F3.setTransitionGenerator(generator);
        F4.setTransitionGenerator(generator);
        P1.setTransitionGenerator(generator);
        P2.setTransitionGenerator(generator);
        P1h.setTransitionGenerator(generator);
        P2h.setTransitionGenerator(generator);
        P3h.setTransitionGenerator(generator);
        P4h.setTransitionGenerator(generator);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtevent.getText().equals("FUNCTION"))
                    txtevent.setText("PHOTOSHOOTING");

                else if (txtevent.getText().equals("PARTY EVENT"))
                    txtevent.setText("FUNCTION");

                else if (txtevent.getText().equals("PHOTOSHOOTING"))
                    txtevent.setText("PARTY EVENT");

                viewFlipper.setInAnimation(getBaseContext(), R.anim.scale_in_slow);
                viewFlipper.setOutAnimation(getBaseContext(), R.anim.scale_out_slow);
                viewFlipper.showPrevious();
            }
        });


        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog.show();
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                1.0f), (int) (displayRectangle.height() * 1.f));
        alertDialog.setCancelable(false);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //orderdetails();
                showpickupaddressdailog();


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
        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        final View view1 = layoutInflater1.inflate(R.layout.orderdetails, null);
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
        AdView adView = (AdView) view1.findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog.show();
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                1.0f), (int) (displayRectangle.height() * 0.8f));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (!Common.isCompanyFound) {

                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(Account account) {
                            requestAdvanceBooking(account.getId());

                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {

                        }
                    });
                } else {

                    showFoundDailog(Common.companyId);

                    // Common.sendrequesttocompany(Common.companyId, ifcmSerives, getBaseContext(), Common.Lastlocation);

                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });


    }

    private void requestAdvanceBooking(String id) {

        DatabaseReference dbRq = FirebaseDatabase.getInstance().getReference(Common.AdvanceBooking_table);
        GeoFire geoFire = new GeoFire(dbRq);
        geoFire.setLocation(id, new GeoLocation(Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()));

        if (CoustMarker.isVisible())
            CoustMarker.remove();

        CoustMarker = mMap.addMarker(new MarkerOptions().title("Pickup here").snippet("").position(new LatLng(Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        CoustMarker.showInfoWindow();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting your COMPANY...");
        progressDialog.show();

        //  btnpickupreq.setText("Getting your COMPANY...");

        findCompanyForAdvanceBooking();
    }

    private void findCompanyForAdvanceBooking() {
        DatabaseReference companies = FirebaseDatabase.getInstance().getReference(Common.companies_table);


        GeoFire gfcompanies = new GeoFire(companies);

        final GeoQuery geoQuery = gfcompanies.queryAtLocation(new GeoLocation(Common.Lastlocation.getLatitude(), Common.Lastlocation.getLongitude()), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if (!Common.isCompanyFound)

                {

                    progressDialog.dismiss();
                    Common.isCompanyFound = true;
                    Common.companyId = key;

                    // btnpickupreq.setText("CALL COMPANY");
                    Toast.makeText(Home.this, "Found " + key, Toast.LENGTH_SHORT).show();
                    showFoundDailog(Common.companyId);
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!Common.isCompanyFound && radius < LIMIT) {
                    radius++;
                    findCompanyForAdvanceBooking();
                } else {

                    if (!Common.isCompanyFound) {

                        Common.isCompanyFound = false;
                        Toast.makeText(Home.this, "No available companies near you", Toast.LENGTH_LONG).show();
                        //  btnpickupreq.setText("REQUEST PICKUP");
                        showNocompanyDailog();
                        progressDialog.dismiss();
                        geoQuery.removeAllListeners();
                    }
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void showNocompanyDailog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.nocompanyfound, null);


        builder.setView(view1);
        final AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alert.show();


    }

    private void showFoundDailog(final String companyId) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        View view1 = layoutInflater1.inflate(R.layout.companyinfo, null);

        final AlertDialog alertDialog = builder.create();

        final ImageView companyImage = (ImageView) view1.findViewById(R.id.avatar_image_of_company);
        final TextView companyname = (TextView) view1.findViewById(R.id.companyName);
        final TextView companyPhone = (TextView) view1.findViewById(R.id.companyPhone);
        final TextView compantrates = (TextView) view1.findViewById(R.id.companyRate);
        final TextView Numberofsuccessfulbookings = (TextView) view1.findViewById(R.id.Numberofsuccessfulbookings);
        commentsList = (ListView) view1.findViewById(R.id.comments);
        mScrollView = (ScrollView) view1.findViewById(R.id.mScrollView);
        final TextView companyIdo = (TextView) view1.findViewById(R.id.companyId);
        final TextView Price_for_onehour_video = (TextView) view1.findViewById(R.id.Price_for_onehour_video);
        final TextView Price_for_one_photo = (TextView) view1.findViewById(R.id.Price_for_one_photo);

        final TextView aboutcompany = (TextView) view1.findViewById(R.id.aboutcompany);
        final TextView link = (TextView) view1.findViewById(R.id.link);
        final Button visit = (Button) view1.findViewById(R.id.visit);
        link.setText("No link updated");
        visit.setEnabled(false);
        aboutcompany.setText("Not Updated");

        FirebaseDatabase.getInstance().getReference("AboutCompany").child(companyId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            aboutMe = snapshot.getValue(AboutMe.class);

                        if (aboutMe != null) {

                            if (aboutMe.getLink() != null && !TextUtils.isEmpty(aboutMe.getLink().toString())) {
                                link.setText(aboutMe.getLink());
                                visit.setEnabled(true);
                            }
                            if (aboutMe.getAboutcompany() != null && !TextUtils.isEmpty(aboutMe.getAboutcompany())) {
                                aboutcompany.setText(aboutMe.getAboutcompany());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(link.getText().toString()));
                startActivity(openURL);
            }
        });


        Button confirm = (Button) view1.findViewById(R.id.confirm);
        Button cancel = (Button) view1.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.isCompanyFound = false;
                Common.companyId = "";

                setUploation();
                alertDialog.dismiss();
            }
        });
        FirebaseDatabase.getInstance().getReference("pricingdetails").child(companyId).child("Price_for_one_photo")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null)
                            Price_for_one_photo.setText(dataSnapshot.getValue().toString());
                        else
                            Price_for_one_photo.setText("Not Updated");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("pricingdetails").child(companyId).child("Price_for_onehour_video")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null)
                            Price_for_onehour_video.setText(dataSnapshot.getValue().toString());
                        else
                            Price_for_onehour_video.setText("Not Updated");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final TextView empty = (TextView) view1.findViewById(R.id.empty);

        FirebaseDatabase.getInstance().getReference("RateDetails")
                .child(companyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null)
                    collectcomments((Map<String, Object>) dataSnapshot.getValue());
                else
                    commentsList.setEmptyView(empty);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table)
                .child(companyId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (!user.getAvatarUrl().isEmpty()) {

                            Picasso.with(getBaseContext()).load(user.getAvatarUrl())
                                    .into(companyImage);
                        }

                        Numberofsuccessfulbookings.setText(user.getNumberofsuccessfulbookings());
                        companyIdo.setText(companyId);
                        companyname.setText(user.getName());
                        compantrates.setText(user.getRates());
                        companyPhone.setText(user.getPhone());


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // loadcomments(companyId);

        alertDialog.setView(view1);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.Dialogslide2;
        alertDialog.show();

        alertDialog.setCancelable(false);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String customername = Common.currentUser.getName();
                String customerPhone = Common.currentUser.getPhone();
                String txtaddress = txtAddress.getText().toString();
                String txtdate = txtDate.getText().toString();
                String txtime = txtTime.getText().toString();

                String EventType = txtevent.getText().toString();


                sendrequesttocompanyForAdvanceBooking(Common.companyId, ifcmSerives, getBaseContext(), Common.Lastlocation, customername, customerPhone, txtdate, txtime, txtaddress, EventType);

            }
        });


    }


    private void collectcomments(Map<String, Object> value) {
        ArrayList<String> comments = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : value.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            comments.add((String) singleUser.get("comments"));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, comments);
        commentsList.setAdapter(arrayAdapter);
        //  commentsList.setEmptyView();
        commentsList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        //   setListViewHeightBasedOnChildren(commentsList);
    }

    public void sendrequesttocompanyForAdvanceBooking(final String companyId,
                                                      final IFCMSerives ifcmSerives, final Context context, final Location currentlocation,
                                                      final String Name, final String Phone,
                                                      final String Date, final String Time,
                                                      final String Address, final String EventType) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Common.token_table);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.show();

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
                                                dialog.dismiss();
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


                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(context, "Failed to send the Request!", Toast.LENGTH_SHORT).show();
                                            }
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

    private void showsentreqDailog() {

        final AlertDialog.Builder sentreqDailog = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater1 = this.getLayoutInflater();
        View view1 = layoutInflater1.inflate(R.layout.reqsent, null);
        sentreqDailog.setView(view1);

        Button done = (Button) view1.findViewById(R.id.done);

        Common.isCompanyFound = false;
        Common.companyId = "";

        setUploation();

        final AlertDialog alert = sentreqDailog.create();
        alert.setCancelable(false);
        alert.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alert.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                final AdRequest adRequest = new AdRequest.Builder().build();
                final InterstitialAd interstitialAd = new InterstitialAd(getApplicationContext());
                interstitialAd.setAdUnitId(getString(R.string.ad_interstitial));
                interstitialAd.loadAd(adRequest);
                interstitialAd.show();

                interstitialAd.setAdListener(new AdListener()

                {

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        interstitialAd.show();
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildLocationCallBack();
                    createlocationreq();
                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            showDialogupdateinfo();
                            Toast.makeText(Home.this, "Please update your profile.", Toast.LENGTH_LONG).show();


                        }
                    }.start();
                    displaylocation();


                }
                break;
        }
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //  if (id == R.id.action_settings) {
        //       return true;
        //   }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signout) {

            signout();
        } else if (id == R.id.nav_updateinfo) {

            showDialogupdateinfo();
        } else if (id == R.id.nav_AdvanceBooking) {

            Intent intent = new Intent(Home.this, AdvanceBookings.class);
            startActivity(intent);

        } else if (id == R.id.nav_CanceledBookings) {
            Intent intent = new Intent(Home.this, CancelledBookings.class);
            startActivity(intent);
        } else if (id == R.id.nav_CompletedBookings) {
            Intent intent = new Intent(Home.this, CompletedBookings.class);
            startActivity(intent);
        } else if (id == R.id.nav_howtouse) {
            Intent intent = new Intent(Home.this, Howtouse.class);
            startActivity(intent);
        } else if (id == R.id.nav_TC) {
            Intent intent = new Intent(Home.this, Tc.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogupdateinfo() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_updateinfo, null);

        final MaterialEditText editname = (MaterialEditText) view.findViewById(R.id.editName);
        final MaterialEditText editphone = (MaterialEditText) view.findViewById(R.id.editphone);
        imageView = (ImageView) view.findViewById(R.id.imageupload);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        alertDialog.setView(view);
       /* if (!TextUtils.isEmpty(Common.currentUser.getAvatarUrl()))
            Picasso.with(Home.this).load(Common.currentUser.getAvatarUrl()).into(imageView);
*/

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                dialog.dismiss();

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        final SpotsDialog waiting = new SpotsDialog(Home.this);
                        waiting.show();

                        final String name = editname.getText().toString();
                        final String phone = editphone.getText().toString();

                        Map<String, Object> updateInfo = new HashMap<>();
                        if (!TextUtils.isEmpty(name)) {

                            updateInfo.put("name", name);
                        }
                        if (!TextUtils.isEmpty(phone)) {
                            updateInfo.put("phone", phone);
                        }
                        DatabaseReference companyInformation = FirebaseDatabase.getInstance().getReference(Common.coustomers_table);
                        companyInformation.child(account.getId())
                                .updateChildren(updateInfo)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            if (!TextUtils.isEmpty(name)) {

                                                txtName.setText(name);
                                                Common.currentUser.setName(name);
                                            }
                                            if (!TextUtils.isEmpty(phone)) {
                                                txtphone.setText(phone);
                                                Common.currentUser.setPhone(phone);
                                            }
                                            Toast.makeText(Home.this, "Information Updated ! ", Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(Home.this, "Information Update failed!", Toast.LENGTH_SHORT).show();

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


        // alertDialog.show();
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.getWindow().getAttributes().windowAnimations = R.style.Dialogscale;
        alertDialog1.show();
    }

    private void chooseimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture: "), Common.PICK_IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null) {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Please wait..");
                dialog.setCancelable(false);
                dialog.show();

                String imagename = UUID.randomUUID().toString();
                imagefolder = storageReference.child("ImagesCustomers/" + imagename);
                imagefolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(Home.this, "Uploaded your image", Toast.LENGTH_SHORT).show();
                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                                    @Override
                                    public void onSuccess(Account account) {
                                        Map<String, Object> avatarUpdate = new HashMap<>();
                                        avatarUpdate.put("avatarUrl", uri.toString());
                                        DatabaseReference companyInformation = FirebaseDatabase.getInstance().getReference(Common.coustomers_table);
                                        companyInformation.child(account.getId())
                                                .updateChildren(avatarUpdate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Home.this, "Image Updated !", Toast.LENGTH_SHORT).show();
                                                            Picasso.with(Home.this).load(uri).into(imageView);
                                                            Picasso.with(Home.this).load(uri).into(imageAvatar);
                                                            Picasso.with(Home.this).load(uri).into(photo);
                                                            Common.currentUser.setAvatarUrl(uri.toString());

                                                        } else
                                                            Toast.makeText(Home.this, "Image Update error !", Toast.LENGTH_SHORT).show();

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
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindow(this));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                lat1 = latLng.latitude;
                lng1 = latLng.longitude;
                Double lat1 = latLng.latitude;
                Double lng1 = latLng.longitude;
                geocoder1 = new Geocoder(Home.this, Locale.getDefault());

                try {
                    addresses1 = geocoder1.getFromLocation(lat1, lng1, 1);

                    if (addresses1 != null && addresses1.size() > 0) {

                        address1 = addresses1.get(0).getAddressLine(0);

                        Toast.makeText(Home.this, "TAPPED LOCATION : " + address1, Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(Home.this, "Sorry ,  we could not recognise your location , please type your address", Toast.LENGTH_SHORT).show();

                        showwritepickupaddressdailog();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (markerdestination != null) {
                    markerdestination.remove();
                }
                if (addresses1 != null && addresses1.size() > 0) {

                    markerdestination = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker)).position(latLng).title("Tapped location").snippet(address1.toString()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                    Bottom_Sheet_Coustomer bottom_sheet_coustomer = Bottom_Sheet_Coustomer.newInstance(String.valueOf(Common.Lastlocation.getLatitude()),
                            String.valueOf(Common.Lastlocation.getLongitude()), String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), true);
                    bottom_sheet_coustomer.show(getSupportFragmentManager(), bottom_sheet_coustomer.getTag());
                }

            }
        });
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

        mMap.setOnInfoWindowClickListener(this);
        if (ActivityCompat.checkSelfPermission(Home.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                        Intent intent = new Intent(Home.this, MainActivity.class);
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


                    Log.d("SHOW MY SHOW", String.format("Your location has changed : %f / %f", latitude, logitude));
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
        if (move.equals("yes")) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));

        }

        DatabaseReference companylocation = FirebaseDatabase.getInstance().getReference(Common.companies_table);

        GeoFire gf = new GeoFire(companylocation);

        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(location.latitude, location.longitude), distance);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {

                FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table)
                        .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Coustomers coustomers = dataSnapshot.getValue(Coustomers.class);
                        if (coustomers != null) {
                            FirebaseDatabase.getInstance().getReference().child("Availablecompanies").
                                    child(Common.userid).child(key).setValue(key);
                            mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                                    .flat(true).title(coustomers.getName()).snippet("Company ID : " + dataSnapshot.getKey())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera)));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                FirebaseDatabase.getInstance().getReference().child("Availablecompanies").
                        child(Common.userid).child(key).removeValue();

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


    @Override
    public void onInfoWindowClick(Marker marker) {

        if (!marker.getTitle().equals("You") && !marker.getTitle().equals("Pickup here") && !marker.getTitle().equals("Tapped location")) {
            Intent intent = new Intent(Home.this, CallCompany.class);
            intent.putExtra("companyId", marker.getSnippet().replaceAll("\\D+", ""));
            intent.putExtra("lat", Common.Lastlocation.getLatitude());
            intent.putExtra("lng", Common.Lastlocation.getLongitude());

            startActivity(intent);
        }

    }
}

/* Button tappedLBSF = (Button) view.findViewById(R.id.tappedLBSF);
        tappedLBSF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Switch.setChecked(true);
                dismiss();

            }
        });*/

/* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        }*/