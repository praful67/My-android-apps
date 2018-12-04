package com.dev2.tracker.TrackYourFriends;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SaveAdapter extends BaseAdapter {

    List<FriendId> FriendId;
    Activity activity;
    ArrayList<FriendId> arrayList;

    public SaveAdapter(List<FriendId> FriendId, Activity activity) {
        this.FriendId = FriendId;
        this.activity = activity;
        this.arrayList = new ArrayList<FriendId>();
        this.arrayList.addAll(FriendId);
    }

    @Override
    public int getCount() {
        return FriendId.size();
    }

    @Override
    public Object getItem(int position) {
        return FriendId.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        view = layoutInflater.inflate(R.layout.frienditem, null);
        final TextView info = (TextView) view.findViewById(R.id.info);
        Button remove = (Button) view.findViewById(R.id.remove);
        Button livetrack = (Button) view.findViewById(R.id.LiveTrack);

        livetrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, LiveTrack.class);
                intent.putExtra("userid", FriendId.get(position).getId());
                activity.startActivity(intent);

            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("Signedusers")
                .child(FriendId.get(position).getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                info.setText(String.valueOf(map.get("username")) + "\nID : " + FriendId.get(position).getId());
                                FriendId.get(position).setName(String.valueOf(map.get("username")));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FriendProfile.class);
                intent.putExtra("userid", FriendId.get(position).getId());
                activity.startActivity(intent);

            }
        });
        final android.support.v7.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new android.support.v7.app.AlertDialog.Builder(activity);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do you want remove this friend ? ")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Savedusers")
                                        .child(pref.getString("id", ""))
                                        .child(FriendId.get(position).getId())
                                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(activity, "Removed !", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Failed ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        return view;
    }
    public void filter(String chartext) {

        chartext = chartext.toLowerCase(Locale.getDefault());
        FriendId.clear();
        if (chartext.length() == 0) {
            FriendId.addAll(arrayList);
        } else {
            for (FriendId friendId : arrayList) {
                if (friendId.getName().toLowerCase(Locale.getDefault()).contains(chartext)) {
                    FriendId.add(friendId);
                }
            }
        }
        notifyDataSetChanged();

    }
}
