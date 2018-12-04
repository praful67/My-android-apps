package com.dev2.tracker.TrackYourFriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dev2.tracker.TrackYourFriends.SignUp.PICK_IMAGE_REQ;
import static com.dev2.tracker.TrackYourFriends.SignUp.randString;

public class EditProfile extends AppCompatActivity {

    String id, myid;
    StorageReference storageReference, imagefolder;
    Uri uri1;
    CircleImageView face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        face = (CircleImageView) findViewById(R.id.face);
        ImageView addicon = (ImageView)findViewById(R.id.addicon);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText about = (EditText) findViewById(R.id.about);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        id = pref.getString("id", "");
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        final InterstitialAd interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial));
        interstitialAd.loadAd(adRequest);
        interstitialAd.show();

        interstitialAd.setAdListener(new AdListener()

        {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                interstitialAd.show();
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
        myid = id;
        if (id != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Signedusers")
                    .child(id)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Userprofile userprofile = dataSnapshot.getValue(Userprofile.class);
                                if (userprofile != null) {
                                    if (!userprofile.getFace().toString().equals("")) {
                                        Uri uri = Uri.parse(userprofile.getFace());
                                        Picasso.with(EditProfile.this).load(uri).into(face);
                                    }
                                    if (userprofile.getAbout().toString().equals("")) {
                                        about.setHint("Please write about yourself something.");
                                    } else {
                                        about.setHint(userprofile.getAbout());
                                    }
                                    username.setHint(userprofile.getUsername());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if (uri1 != null) {
                    imagefolder = storageReference.child("Faces").child(myid + "/" + "Face");
                    imagefolder.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditProfile.this, "Uploaded your image", Toast.LENGTH_SHORT).show();
                            imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    Map<String, Object> map = new HashMap<>();
                                    if (!TextUtils.isEmpty(username.getText().toString()))
                                        map.put("username", username.getText().toString());
                                    if (!TextUtils.isEmpty(about.getText().toString()))
                                        map.put("about", about.getText().toString());
                                    map.put("face", uri.toString());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Signedusers")
                                            .child(myid)
                                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(EditProfile.this, "Updated your profile", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            finish();

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfile.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Map<String, Object> map = new HashMap<>();
                    if (!TextUtils.isEmpty(username.getText().toString()))
                        map.put("username", username.getText().toString());
                    if (!TextUtils.isEmpty(about.getText().toString()))
                        map.put("about", about.getText().toString());

                    FirebaseDatabase.getInstance().getReference()
                            .child("Signedusers")
                            .child(myid)
                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditProfile.this, "Updated your profile", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }


        });
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();
            }
        });
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
                Picasso.with(EditProfile.this).load(uri).into(face);
                uri1 = uri;
            }
        }
    }

}
