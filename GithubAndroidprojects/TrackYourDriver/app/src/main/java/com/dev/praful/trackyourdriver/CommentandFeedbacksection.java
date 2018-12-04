package com.dev.praful.trackyourdriver;

import android.app.ProgressDialog;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.google.android.gms.internal.zzbfq.NULL;

public class CommentandFeedbacksection extends AppCompatActivity {
    String id;
    String name;
    double ratingstars = 0.0;
    MaterialRatingBar ratingBar;

    android.support.v7.widget.Toolbar mToolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
int num = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentsection);
        final EditText comment = (EditText) findViewById(R.id.comment);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Button submit = (Button) findViewById(R.id.submit);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        String monthh = null;

        switch (month)

        {
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

        ratingBar = (MaterialRatingBar) findViewById(R.id.ratingbar);

        ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                ratingstars = rating;
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Employee'sComments1")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null)
                        {
                            Map<String , Object>map1 = (HashMap<String, Object>)dataSnapshot.getValue();
                            if (map1 != null){
                                String number = String.valueOf(map1.get("number"));
                                num = Integer.parseInt(number);
                                num++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




        final String dateofsubmit = monthh + " " + day_of_month + " , " + year;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        FirebaseDatabase.getInstance().getReference()
                .child("SignedEmployees")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Employeedetails employeedetails = dataSnapshot.getValue(Employeedetails.class);
                            if (employeedetails != null) {
                                name = employeedetails.getUsername();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        String current_time = (currentDateTimeString);

        submit.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment.getText().toString());
                map.put("stars", String.valueOf(ratingstars));
                map.put("dateofsubmit", dateofsubmit + " " + current_time);
                map.put("name", name);
                map.put("id", id);
                FirebaseDatabase.getInstance().
                        getReference().child("Employee'sComments1").child("number").setValue(String.valueOf(num));

                FirebaseDatabase.getInstance().getReference()
                        .child("Employee'sComments1")
                        .child(id)
                        .setValue(map);
                FirebaseDatabase.getInstance().getReference()
                        .child("EComment")
                        .child(id)
                        .child("Updated")
                        .setValue("yes");
                FirebaseDatabase.getInstance().getReference()
                        .child("Employee'sComments")
                        .child(id)
                        .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(CommentandFeedbacksection.this, "Sent", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        final TextView previoustimeoff = (TextView) findViewById(R.id.previouscomment);

        FirebaseDatabase.getInstance().getReference().child("Employee'sComments")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                String dateofsubmit = String.valueOf(map.get("dateofsubmit"));
                                String message = String.valueOf(map.get("comment"));
                                String stars = NULL;
                                if (map.get("stars") != null)
                                    stars = String.valueOf(map.get("stars"));
                                else
                                    stars = "Not submited";

                                previoustimeoff.setText("Date of submit : " + dateofsubmit + "\n\nRating : " + stars + " / 5 "+"\n\nComment : " + message);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


}
