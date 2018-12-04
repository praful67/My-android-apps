package com.praful.praful.uber;

import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.praful.praful.uber.Common.Common;
import com.praful.praful.uber.Model.User;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class Profile extends AppCompatActivity {

    ScrollView mScrollView;
    ListView commentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final ImageView companyImage = (ImageView) findViewById(R.id.avatar_image_of_company);
        final TextView companyname = (TextView) findViewById(R.id.companyName);
        final TextView companyPhone = (TextView) findViewById(R.id.companyPhone);
        final TextView compantrates = (TextView) findViewById(R.id.companyRate);
        final TextView Numberofsuccessfulbookings = (TextView) findViewById(R.id.Numberofsuccessfulbookings);
        commentsList = (ListView) findViewById(R.id.comments);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        final TextView companyIdo = (TextView) findViewById(R.id.companyId);
        final TextView Price_for_onehour_video = (TextView) findViewById(R.id.Price_for_onehour_video);
        final TextView Price_for_one_photo = (TextView) findViewById(R.id.Price_for_one_photo);

        FirebaseDatabase.getInstance().getReference("pricingdetails").child(Common.userid).child("Price_for_one_photo")
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

        FirebaseDatabase.getInstance().getReference("pricingdetails").child(Common.userid).child("Price_for_onehour_video")
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

        final TextView empty = (TextView) findViewById(R.id.empty);

        FirebaseDatabase.getInstance().getReference("RateDetails")
                .child(Common.userid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                .child(Common.userid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (!user.getAvatarUrl().isEmpty()) {

                            Picasso.with(getBaseContext()).load(user.getAvatarUrl())
                                    .into(companyImage);
                        }

                        Numberofsuccessfulbookings.setText(user.getNumberofsuccessfulbookings());
                        companyIdo.setText(Common.userid);
                        companyname.setText(user.getName());
                        compantrates.setText(user.getRates());
                        companyPhone.setText(user.getPhone());


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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Profile.this, android.R.layout.simple_list_item_1, comments);
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
