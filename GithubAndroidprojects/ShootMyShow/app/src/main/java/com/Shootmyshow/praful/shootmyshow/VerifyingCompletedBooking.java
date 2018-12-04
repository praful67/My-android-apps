package com.Shootmyshow.praful.shootmyshow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow.Model.Bookings;
import com.Shootmyshow.praful.shootmyshow.Model.CompanyBookings;
import com.Shootmyshow.praful.shootmyshow.Model.DataMessage;
import com.Shootmyshow.praful.shootmyshow.Model.FCMResponse;
import com.Shootmyshow.praful.shootmyshow.Model.Token;
import com.Shootmyshow.praful.shootmyshow.Remote.FCMClient;
import com.Shootmyshow.praful.shootmyshow.Remote.IFCMSerives;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyingCompletedBooking extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String companyId, BookingIdC;
    TextView message, eventtype, address, date, time;
    String userid;
    String ID;
    IFCMSerives ifcmSerives;
    public static final String fcmURL = "https://fcm.googleapis.com";

    public static IFCMSerives ifcmSerives() {
        return FCMClient.getClient(fcmURL).create(IFCMSerives.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifying_completed_booking);
        Button yes, no;
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ifcmSerives = ifcmSerives();
        //   ifcmSerives = Common.getFCMService();
        FirebaseApp.initializeApp(this);
        message = (TextView) findViewById(R.id.message);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        userid = pref.getString("Id", "");
        eventtype = (TextView) findViewById(R.id.eventtype);
        address = (TextView) findViewById(R.id.addressevent);
        date = (TextView) findViewById(R.id.dateevent);
        time = (TextView) findViewById(R.id.timeevent);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        if (getIntent() != null) {
            BookingIdC = getIntent().getStringExtra("BookingIdC");
            //companyId = getIntent().getStringExtra("companyid");
            //   message.setText(getIntent().getStringExtra("message"));


        }
        databaseReference.child("CompletedcustomerBookings").child(pref.getString("Id", ""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals(BookingIdC)) {

                                finish();
                                Toast.makeText(VerifyingCompletedBooking.this, "You have responded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("verifyingBooking")
                .child(pref.getString("Id", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    finish();
                    Toast.makeText(VerifyingCompletedBooking.this, "You have responded", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final TextView bookingdate = (TextView) findViewById(R.id.bookingdate);
        final TextView bookingtime = (TextView) findViewById(R.id.bookingtime);


        FirebaseDatabase.getInstance().getReference("BookingsDateandTime")
                .child(BookingIdC).child("Date")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        bookingdate.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference("BookingsDateandTime")
                .child(BookingIdC).child("Time")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        bookingtime.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        databaseReference.child("coustomerBookings").child(userid).addValueEventListener(new ValueEventListener() {
            // databaseReference.child("coustomerBookings").child().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals(BookingIdC)) {
                        Bookings bookings = dataSnapshot1.getValue(Bookings.class);
                        date.setText(bookings.getDate());
                        time.setText(bookings.getTime());
                        address.setText(bookings.getAddress());
                        eventtype.setText(bookings.getEventType());
                        companyId = bookings.getCompanyId();

                        message.setText("Hello , Studio " + bookings.getCompanyName() + " says that this booking has completed ");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //    load();
        yes = (Button) findViewById(R.id.Yes);
        no = (Button) findViewById(R.id.No);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCompletenotification(companyId, ifcmSerives, VerifyingCompletedBooking.this, "YES");

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCompletenotification(companyId, ifcmSerives, VerifyingCompletedBooking.this, "NO");
                //finish();
            }
        });
    }


    private void load() {


        databaseReference.child("coustomerBookings").child(userid).addValueEventListener(new ValueEventListener() {
            // databaseReference.child("coustomerBookings").child().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals(BookingIdC)) {
                        Bookings bookings = dataSnapshot1.getValue(Bookings.class);
                        date.setText(bookings.getDate());
                        time.setText(bookings.getTime());
                        address.setText(bookings.getAddress());
                        eventtype.setText(bookings.getEventType());
                        companyId = bookings.getCompanyId();

                        message.setText("Comapany " + bookings.getCompanyName() + " says that this booking has completed ");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendCompletenotification(final String companyId, final IFCMSerives ifcmSerives, final Context context, final String response1) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.show();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        tokens.orderByKey().equalTo(companyId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Token token = dataSnapshot1.getValue(Token.class);
                            Map<String, String> content = new HashMap<>();
                            content.put("Type", "customerresponseoncompletedAdvanceBooking");
                            content.put("response", response1);
                            content.put("BookingIdC", BookingIdC);

                            DataMessage dataMessage = new DataMessage(token.getToken(), content);

                            ifcmSerives.sendMessage(dataMessage)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body().success == 1) {

                                                dialog.dismiss();
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child(response1)
                                                        .child(companyId)
                                                        .child("BookingId")
                                                        .removeValue();
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child(response1)
                                                        .child(companyId)
                                                        .child("BookingId")
                                                        .setValue(BookingIdC);

                                                if (response1.equals("YES")) {
                                                    databaseReference.child("coustomerBookings").child(userid)
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                        if (snapshot.getKey().equals(BookingIdC)) {
                                                                            Bookings bookings = snapshot.getValue(Bookings.class);
                                                                            databaseReference.child("CompletedcustomerBookings").child(userid).child(BookingIdC).setValue(bookings);
                                                                            databaseReference.child("coustomerBookings").child(userid).child(BookingIdC).removeValue();


                                                                        }

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                    databaseReference.child("Bookings").child(companyId)
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                        if (snapshot.getKey().equals(BookingIdC)) {
                                                                            CompanyBookings bookings = snapshot.getValue(CompanyBookings.class);
                                                                            databaseReference.child("CompletedBookings").child(companyId).child(BookingIdC).setValue(bookings);
                                                                            databaseReference.child("Bookings").child(companyId).child(BookingIdC).removeValue();


                                                                        }

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });


                                                    Intent intent = new Intent(VerifyingCompletedBooking.this, RateactivityforAdvanceBooking.class);
                                                    intent.putExtra("companyId", companyId);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (response1.equals("NO")) {
                                                    FirebaseDatabase.getInstance().getReference().child("verifyingBooking")
                                                            .child(pref.getString("Id", "")).removeValue();
                                                    finish();
                                                }


                                                Toast.makeText(context, "Your response has been sent", Toast.LENGTH_LONG).show();
                                                finish();
                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(context, "Failed to send your response!", Toast.LENGTH_LONG).show();
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

}
