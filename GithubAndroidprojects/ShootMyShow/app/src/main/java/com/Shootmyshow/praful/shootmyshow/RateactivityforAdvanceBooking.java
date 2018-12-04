package com.Shootmyshow.praful.shootmyshow;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow.Model.Rate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RateactivityforAdvanceBooking extends AppCompatActivity {


    Button btnsubmit;
    MaterialRatingBar ratingBar;
    MaterialEditText comments;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference rateDetailref;
    DatabaseReference companyInfo;

    double ratingstars = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateactivityfor_advance_booking);
        firebaseDatabase = FirebaseDatabase.getInstance();
        rateDetailref = firebaseDatabase.getReference("RateDetails");
        companyInfo = firebaseDatabase.getReference("CompaniesInfo");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        comments = (MaterialEditText) findViewById(R.id.edtcomment);
        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingbar);

        ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                ratingstars = rating;
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRatings(getIntent().getStringExtra("companyId"));

            }
        });


    }

    private void submitRatings(final String companyId) {

        final SpotsDialog alertDialog = new SpotsDialog(this);
        alertDialog.show();

        final Rate rate = new Rate();
        rate.setRates(String.valueOf(ratingstars));
        rate.setComments(comments.getText().toString());

        rateDetailref.child(companyId)
                .push()
                .setValue(rate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                rateDetailref.child(companyId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                double averageStars = 0.0;
                                int count = 0;
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Rate rate = dataSnapshot1.getValue(Rate.class);
                                    averageStars += Double.parseDouble(rate.getRates());
                                    count++;

                                }
                                double finalAvg = averageStars / count;
                                DecimalFormat df = new DecimalFormat("#.#");

                                String valueupdate = df.format(finalAvg);

                                Map<String, Object> companyUpateRate = new HashMap<>();

                                companyUpateRate.put("rates", valueupdate);

                                companyInfo.child(companyId).updateChildren(companyUpateRate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                alertDialog.dismiss();

                                                Toast.makeText(RateactivityforAdvanceBooking.this, "Thank you for submit", Toast.LENGTH_SHORT).show();
                                                finish();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                alertDialog.dismiss();
                                                Toast.makeText(RateactivityforAdvanceBooking.this, "Rate updated but can't show to Driver Info.", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        alertDialog.dismiss();
                        Toast.makeText(RateactivityforAdvanceBooking.this, "Rating Failed !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
