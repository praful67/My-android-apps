package com.example.praful.data;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    private ListView ListView;
    private ImageAdapter imageAdapter;
    private DatabaseReference databaseReference;
    private List<Upload> uploads;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        uploads = new ArrayList<>();
        ListView = (ListView) findViewById(R.id.ListView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait , loading Tip Posts...");
        progressDialog.show();


        databaseReference = FirebaseDatabase.getInstance().getReference("Tips");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Upload upload = snapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                imageAdapter= new ImageAdapter(ImagesActivity.this ,R.layout.image_item, uploads);
                ListView.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });
    }
}
