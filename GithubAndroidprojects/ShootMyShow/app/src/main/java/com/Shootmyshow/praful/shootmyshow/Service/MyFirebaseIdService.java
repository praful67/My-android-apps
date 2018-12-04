package com.Shootmyshow.praful.shootmyshow.Service;

import android.content.Intent;

import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.Shootmyshow.praful.shootmyshow.Model.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService  {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateTokenToServer(refreshedToken);

    }

    private void updateTokenToServer(String refreshedToken) {

        FirebaseDatabase db =FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference(Common.token_table);

        Token token = new Token(refreshedToken);
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            tokens.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction("com.example.praful.ubercoustomer");
        intent.putExtra("key" , "F");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        sendBroadcast(intent);

    }
}
