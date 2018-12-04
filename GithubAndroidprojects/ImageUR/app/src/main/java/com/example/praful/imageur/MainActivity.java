package com.example.praful.imageur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    Button bt;
    StorageReference storageReference;
    static final int GALLERY_code =2;
    ProgressDialog progressDialog;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt =  (Button)findViewById(R.id.button);
        imageView = (ImageView)findViewById(R.id.img);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent , GALLERY_code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_code && resultCode == RESULT_OK){

            progressDialog.setMessage("Uploading image...");
            progressDialog.show();
            Uri uri = data.getData();
            StorageReference storageReference1 = storageReference.child("Images").child(uri.getLastPathSegment());
            storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                     Uri img =taskSnapshot.getDownloadUrl();
                     Picasso.get().load(img).fit().centerCrop().into(imageView);


                    Toast.makeText(MainActivity.this, "Uploded" ,Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
