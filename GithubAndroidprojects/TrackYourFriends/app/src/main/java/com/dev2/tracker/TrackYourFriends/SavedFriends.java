package com.dev2.tracker.TrackYourFriends;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedFriends extends AppCompatActivity {

    List<FriendId> Friends = new ArrayList<>();
    ListView Friendlist;
    String id;
    SaveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_friends);
        Friendlist = (ListView) findViewById(R.id.listfriends);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
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
        id = pref.getString("id", "");
        FirebaseDatabase.getInstance().getReference()
                .child("Savedusers")
                .child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1 != null) {
                                    Map<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
                                    if (map != null) {

                                        FriendId friendId = new FriendId(dataSnapshot1.getKey(), "");
                                        Friends.add(friendId);

                                    }
                                }
                            }
                            adapter = new SaveAdapter( Friends, SavedFriends.this);

                            TextView t = (TextView) findViewById(R.id.t);

                            Friendlist.setEmptyView(t);

                            Friendlist.setAdapter(adapter);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null && newText != null) {
                    if (TextUtils.isEmpty(newText)) {
                        adapter.filter("");
                        Friendlist.clearTextFilter();
                    } else {
                        adapter.filter(newText);
                    }
                }
                return true;
            }
        });
    }
}
