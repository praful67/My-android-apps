package com.Shootmyshow.praful.shootmyshow;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.Shootmyshow.praful.shootmyshow.Model.Coustomers;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.wearable.MessageApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    Button btnContinue;
    RelativeLayout rootlayout;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    ProgressDialog progressDialog;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_main);
        Paper.init(this);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.coustomers_table);

        btnContinue = (Button) findViewById(R.id.btnContinue);
        rootlayout = (RelativeLayout) findViewById(R.id.rootlayout);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinwithphone();
            }
        });

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        final SharedPreferences.Editor editor = pref.edit();

        if (AccountKit.getCurrentAccessToken() != null) {
            final SpotsDialog waitingDailog = new SpotsDialog(this);

                waitingDailog.show();
                waitingDailog.setMessage("Please wait");
                waitingDailog.setCancelable(false);


            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {
                    Toast.makeText(MainActivity.this, account.getId(), Toast.LENGTH_SHORT).show();
                    editor.putString("Id", account.getId());
                    editor.apply();
                    Common.userid = account.getId();
                    users.child(account.getId())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                   /* FirebaseDatabase.getInstance().getReference()
                                            .child("trackingNotification")
                                            .child(pref.getString("Id" , "")).setValue("");
*/
                                    Common.currentUser = dataSnapshot.getValue(Coustomers.class);
                                    Intent homeintent = new Intent(MainActivity.this, Home.class);
                                    Common.check = "DSVN";
                                    startActivity(homeintent);
                                    startService(new Intent(getBaseContext(), MyService.class));
                                    waitingDailog.dismiss();
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                }

                @Override
                public void onError(AccountKitError accountKitError) {
                        waitingDailog.dismiss();
                        Toast.makeText(MainActivity.this, "Please verify your mobile number , Tap continue", Toast.LENGTH_LONG).show();
                    }
            });
        }

    }

    private void signinwithphone() {

        Intent intent = new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        final SharedPreferences.Editor editor = pref.edit();

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "CANCEL LOGIN", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (result.getAccessToken() != null) {
                    final SpotsDialog waitingDailog = new SpotsDialog(this);

                        waitingDailog.show();
                        waitingDailog.setMessage("Please wait...");
                        waitingDailog.setCancelable(false);


                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            editor.putString("Id", account.getId());
                            editor.putString("F", "F");

                            editor.apply();

                            Common.userid = account.getId();

                            final String userid = account.getId();
                            users.orderByKey().equalTo(account.getId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (!dataSnapshot.child(userid).exists()) {

                                                Coustomers user = new Coustomers();
                                                user.setPhone(account.getPhoneNumber().toString());
                                                user.setName("Name not updated");
                                                user.setAvatarUrl("");

                                                users.child(account.getId())
                                                        .setValue(user)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                users.child(account.getId())
                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                                                Common.currentUser = dataSnapshot.getValue(Coustomers.class);
                                                                                Intent homeintent = new Intent(MainActivity.this, Intro.class);
                                                                                startService(new Intent(getBaseContext(), MyService.class));

                                                                                startActivity(homeintent);

                                                                                waitingDailog.dismiss();
                                                                                finish();
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

                                                                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {

                                                users.child(account.getId())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                                Common.currentUser = dataSnapshot.getValue(Coustomers.class);
                                                                Intent homeintent = new Intent(MainActivity.this, Home.class);
                                                                startActivity(homeintent);
                                                                startService(new Intent(getBaseContext(), MyService.class));

                                                                waitingDailog.dismiss();
                                                                finish();
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {

                            Toast.makeText(MainActivity.this, "" + accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }
    }

}
