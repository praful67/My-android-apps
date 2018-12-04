package com.example.praful.ubercoustomer;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Model.Rate;
import com.example.praful.ubercoustomer.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class RateActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_rate);

        firebaseDatabase = FirebaseDatabase.getInstance();
        rateDetailref = firebaseDatabase.getReference(Common.rate_company_tbl);
        companyInfo = firebaseDatabase.getReference(Common.companiesInfo_table);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnsubmit = (Button)findViewById(R.id.btnsubmit);
        ratingBar = (MaterialRatingBar)findViewById(R.id.ratingbar);
        comments = (MaterialEditText)findViewById(R.id.edtcomment);

        ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                ratingstars = rating;
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRatings(Common.companyId);
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
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                {
                                    Rate rate = dataSnapshot1.getValue(Rate.class);
                                    averageStars+=Double.parseDouble(rate.getRates());
                                    count++;

                                }
                                double finalAvg = averageStars/count;
                                DecimalFormat df =new DecimalFormat("#.#");

                                String valueupdate = df.format(finalAvg);

                                Map<String , Object> companyUpateRate = new HashMap<>();

                                companyUpateRate.put("rates" ,valueupdate);

                              companyInfo.child(Common.companyId).updateChildren(companyUpateRate)
                             //  companyInfo.child(Common.companyId).child("stars").setValue(valueupdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                alertDialog.dismiss();

                                                Toast.makeText(RateActivity.this , "Thank you for submit" ,Toast.LENGTH_SHORT).show();
                                                finish();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                alertDialog.dismiss();
                                                Toast.makeText(RateActivity.this, "Rate updated but can't show to Driver Info.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RateActivity.this  , "Rating Failed !" ,Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
