package com.praful.praful.uber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.praful.praful.uber.Common.Common;
import com.praful.praful.uber.Model.Bookings;
import com.praful.praful.uber.Model.User;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        AccountKit.initialize(this);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        final SharedPreferences.Editor editor = pref.edit();


        printkeyHash();

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.companiesInfo_table);

        btnContinue = (Button) findViewById(R.id.btnContinue);

        rootlayout = (RelativeLayout) findViewById(R.id.rootlayout);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinwithphone();
            }
        });


        if (AccountKit.getCurrentAccessToken() != null) {

            final SpotsDialog waitingDailog = new SpotsDialog(this);
            waitingDailog.show();
            waitingDailog.setMessage("Please wait");
            waitingDailog.setCancelable(false);
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    editor.putString("Id", account.getId());
                    editor.apply();
                    Common.userid = account.getId();
                    users.child(account.getId())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    Common.currentUser = dataSnapshot.getValue(User.class);
                                    Intent homeintent = new Intent(MainActivity.this, CompanyHome.class);
                                    homeintent.putExtra("NFT" , "NFT");
                                    startActivity(homeintent);

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
                            editor.apply();
                            Common.userid = account.getId();
                            users.orderByKey().equalTo(account.getId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (!dataSnapshot.child(Common.userid).exists()) {

                                                User user = new User(account.getPhoneNumber().toString(),
                                                        account.getPhoneNumber().toString(), "0.0", "", "0");
                                         /*  user.setPhone(account.getPhoneNumber().toString());
                                            user.setName(account.getPhoneNumber().toString());
                                            user.setAvatarUrl("");
                                            user.setRates("0.0");
                                            user.setNumberofsuccessfulbookings(0);*/

                                                users.child(account.getId())
                                                        .setValue(user)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                users.child(account.getId())
                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                Common.currentUser = dataSnapshot.getValue(User.class);
                                                                                Intent homeintent = new Intent(MainActivity.this, CompanyHome.class);
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


                                                                Common.currentUser = dataSnapshot.getValue(User.class);
                                                                Intent homeintent = new Intent(MainActivity.this, CompanyHome.class);
                                                                startActivity(homeintent);

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

    private void printkeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.praful.uber", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {


                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

}
