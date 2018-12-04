package com.example.praful.blog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseref;
    private List<ImageUpload> imgList;
    private ListView lv;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewImage);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait , loading Tip posts...");
        progressDialog.show();


        mDatabaseref = FirebaseDatabase.getInstance().getReference(MainActivity.FB_DATABASE_PATH);

        mDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }

                adapter = new ImageListAdapter(ImageListActivity.this , R.layout.image_item , imgList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();



            }
        });


    }
}
