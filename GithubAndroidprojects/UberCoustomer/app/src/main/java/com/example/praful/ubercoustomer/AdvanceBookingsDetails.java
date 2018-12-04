package com.example.praful.ubercoustomer;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdvanceBookingsDetails extends AppCompatActivity {
    TextView txt_Companyname, txt_Compnayphone, txt_companyrates, Numberofsuccessfulbookings, Price_for_one_photo, Price_for_onehour_video;
    CircleImageView avatar_image;
    ScrollView mScrollView;
    Button btn_call_phone, btnAdvanceBookthisCompany;

    ListView commentsList;

    String Address, Time, Date, companyPhone, companyName, companyId, eventtype;
    TextView time, date, address, companyid, Eventtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getAttributes().windowAnimations = R.style.Dialogslide1;

        setContentView(R.layout.activity_advance_bookings_details);
        time = (TextView) findViewById(R.id.timeevent);
        date = (TextView) findViewById(R.id.dateevent);
        address = (TextView) findViewById(R.id.addressevent);
        Eventtype = (TextView) findViewById(R.id.eventtype);
        companyid = (TextView) findViewById(R.id.companyId);


        if (getIntent() != null) {
            Time = getIntent().getStringExtra("Time");
            Date = getIntent().getStringExtra("Date");
            eventtype = getIntent().getStringExtra("EventType");
            Address = getIntent().getStringExtra("Address");
            companyId = getIntent().getStringExtra("companyId");
        }
        companyid.setText(companyId);
        Eventtype.setText(eventtype);
        time.setText(Time);
        date.setText(Date);
        address.setText(Address);
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commentsList = (ListView) findViewById(R.id.comments);
        avatar_image = (CircleImageView) findViewById(R.id.avatar_image);
        txt_Companyname = (TextView) findViewById(R.id.txt_companyName);
        txt_companyrates = (TextView) findViewById(R.id.txt_companyRate);
        txt_Compnayphone = (TextView) findViewById(R.id.txt_companyPhone);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        Numberofsuccessfulbookings = (TextView) findViewById(R.id.Numberofsuccessfulbookings);
        Price_for_onehour_video = (TextView) findViewById(R.id.Price_for_onehour_video);
        Price_for_one_photo = (TextView) findViewById(R.id.Price_for_one_photo);

        FirebaseDatabase.getInstance().getReference("pricingdetails").child(companyId).child("Price_for_one_photo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
                .addListenerForSingleValueEvent(new ValueEventListener() {
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


        btn_call_phone = (Button) findViewById(R.id.btnCallcompanybyphone);
        btnAdvanceBookthisCompany = (Button) findViewById(R.id.btnAdvanceBookthisCompany);
        btnAdvanceBookthisCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvanceBookingsDetails.this, CallCompanyforAdvanceBooking.class);
                intent.putExtra("companyId", companyId);
                startActivity(intent);
            }
        });


        btn_call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + txt_Compnayphone.getText().toString()));
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);

            }
        });
        loadCompanyInfo(companyId);
        loadcomments(companyId);

    }

    private void loadCompanyInfo(final String companyId) {

        FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table)
                .child(companyId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (!user.getAvatarUrl().isEmpty()) {

                            Picasso.with(getBaseContext()).load(user.getAvatarUrl())
                                    .into(avatar_image);
                        }
                        Numberofsuccessfulbookings.setText(user.getNumberofsuccessfulbookings());
                        txt_Companyname.setText(user.getName());
                        txt_companyrates.setText(user.getRates());
                        txt_Compnayphone.setText(user.getPhone());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadcomments(final String companyId) {
        final TextView empty = (TextView) findViewById(R.id.empty);
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AdvanceBookingsDetails.this, android.R.layout.simple_list_item_1, comments);
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

}
