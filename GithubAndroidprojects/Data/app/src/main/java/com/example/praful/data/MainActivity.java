package com.example.praful.data;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int Image_Req = 1;

    private Button chooseimg;
    private Button upload;
    private Button show;
    private EditText tip;
    private EditText title;
    private EditText des;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri uri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageReference = FirebaseStorage.getInstance().getReference("Images");

        databaseReference = FirebaseDatabase.getInstance().getReference("Tips");

        chooseimg = (Button)findViewById(R.id.browse);
        upload = (Button)findViewById(R.id.upload);
        show = (Button)findViewById(R.id.show);

        tip = (EditText)findViewById(R.id.tip);
        title = (EditText)findViewById(R.id.title);
        des = (EditText)findViewById(R.id.des);

        imageView = (ImageView)findViewById(R.id.imageView);

        progressBar = (ProgressBar)findViewById(R.id.progress);

        chooseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();

            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this , ImagesActivity.class);
                 startActivity(intent);

            }
        });
    }

    private void openFileChooser(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , Image_Req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Image_Req && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri =data.getData();
            Picasso.with(this).load(uri).into(imageView);
        }
    }
    private  String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime  = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(){
        if(uri != null){
            currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
            StorageReference storageReference1 =storageReference.child(currentDate + "."+
            getFileExtension(uri));
            storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    } , 500);
                    Toast.makeText(MainActivity.this , "Posted the tip", Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload(tip.getText().toString(), title.getText().toString(), des.getText().toString(),
                            taskSnapshot.getDownloadUrl().toString());

                    databaseReference.child(currentDate).setValue(upload);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MainActivity.this , e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress =(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);

                        }
                    });


        }else {
            Toast.makeText(MainActivity.this , "Please Select Image" ,Toast.LENGTH_SHORT).show();
        }

    }
}
