package com.Shootmyshow.praful.shootmyshow;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.Shootmyshow.praful.shootmyshow.Model.AboutMe;
import com.Shootmyshow.praful.shootmyshow.Model.Rate;
import com.Shootmyshow.praful.shootmyshow.Model.User;
import com.Shootmyshow.praful.shootmyshow.Remote.IFCMSerives;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallCompany extends AppCompatActivity {

    CircleImageView avatar_image;
    TextView txt_Companyname, txt_Compnayphone, txt_companyrates, Numberofsuccessfulbookings, Price_for_one_photo, Price_for_onehour_video;
    Button btn_call_phone, btnAdvanceBookthisCompany;

    String companyId;
    AboutMe aboutMe;
    List<Rate> rates;
    Location mlastLocation;
    FirebaseListAdapter<Rate> adapter;
    ScrollView mScrollView;
    ListView commentsList;


    IFCMSerives ifcmSerives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_company);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdView adView1 = (AdView) findViewById(R.id.ad_banner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView.loadAd(adRequest1);
        adView1.loadAd(adRequest);
        if (getIntent() != null) {

            companyId = getIntent().getStringExtra("companyId");
           /* double lat = getIntent().getDoubleExtra("lat", -1.0);
            double lng = getIntent().getDoubleExtra("lng", -1.0);

            mlastLocation = new Location("");
            mlastLocation.setLatitude(lat);
            mlastLocation.setLongitude(lng);
*/

            loadCompanyInfo(companyId);
            loadcomments(companyId);
        }
        final TextView aboutcompany = (TextView) findViewById(R.id.aboutcompany);
        final TextView link = (TextView) findViewById(R.id.link);
        final Button visit = (Button) findViewById(R.id.visit);
        link.setText("No link updated");
        visit.setEnabled(false);
        aboutcompany.setText("Not Updated");


        FirebaseDatabase.getInstance().getReference().child("AboutCompany").child(companyId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            aboutMe = snapshot.getValue(AboutMe.class);
                        }

                        if (aboutMe != null) {

                            if (aboutMe.getLink() != null && !TextUtils.isEmpty(aboutMe.getLink().toString())) {
                                link.setText(aboutMe.getLink());
                                visit.setEnabled(true);
                            }
                            if (aboutMe.getAboutcompany() != null && !TextUtils.isEmpty(aboutMe.getAboutcompany()))
                            {
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

        ifcmSerives = Common.getFCMService();


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
                Intent intent = new Intent(CallCompany.this, CallCompanyforAdvanceBooking.class);
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CallCompany.this, android.R.layout.simple_list_item_1, comments);
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
