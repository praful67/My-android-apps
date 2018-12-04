package com.dev2.tracker.TrackYourFriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    public static final int PICK_IMAGE_REQ = 9999;
    StorageReference storageReference, imagefolder;
    ImageView addicon;
    CircleImageView face;
    String myid;
    Uri uri1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final EditText username = (EditText) findViewById(R.id.username);
        Button submit = (Button) findViewById(R.id.submit);
        addicon = (ImageView) findViewById(R.id.addicon);
        face = (CircleImageView) findViewById(R.id.face);
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        storageReference = FirebaseStorage.getInstance() .getReference();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = pref.edit();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(username.getText().toString())) {
                    progressDialog.show();
                    editor.putString("id", randString(7));
                    editor.apply();
                    myid = pref.getString("id", "");
                    if (uri1 != null) {
                        imagefolder = storageReference.child("Faces").child(myid + "/" + "Face");
                        imagefolder.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(SignUp.this, "Uploaded your image", Toast.LENGTH_SHORT).show();
                                imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {
                                        Userprofile userprofile = new Userprofile(username.getText().toString(), uri.toString(), myid, "");
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Signedusers")
                                                .child(myid)
                                                .setValue(userprofile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SignUp.this, "Signed Up", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUp.this, Welcome.class);
                                                progressDialog.dismiss();
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUp.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        pref.getAll().clear();
                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
                    } else {
                        Userprofile userprofile = new Userprofile(username.getText().toString(), "", myid, "");
                        FirebaseDatabase.getInstance().getReference()
                                .child("Signedusers")
                                .child(myid)
                                .setValue(userprofile).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUp.this, "Signed Up", Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUp.this, "Please update your image later", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(SignUp.this, Welcome.class);
                                startActivity(intent);
                                finish();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                pref.getAll().clear();
                            }
                        });
                    }

                } else {
                    Toast.makeText(SignUp.this, "Please type your username", Toast.LENGTH_SHORT).show();
                }


            }
        });

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();
            }
        });
    }

    public static String randString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, Math.min(length, 32)) + (length > 32 ? randString(length - 32) : "");
    }

    private void chooseimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture: "), PICK_IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQ && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final Uri uri = data.getData();
            if (uri != null) {
                Picasso.with(SignUp.this).load(uri).into(face);
                uri1 = uri;
                addicon.setVisibility(View.GONE);
            }
        }
    }

}
