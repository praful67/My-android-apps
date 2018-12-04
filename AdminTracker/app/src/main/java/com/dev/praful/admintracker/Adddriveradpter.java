package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adddriveradpter extends BaseAdapter {
    java.util.List<Credentialmail> credentialmails;
    Activity activity;
    ArrayList<Credentialmail> arrayList = new ArrayList<>();

    public Adddriveradpter(List<Credentialmail> credentialmails, Activity activity) {
        this.credentialmails = credentialmails;
        this.activity = activity;
        this.arrayList.addAll(credentialmails);
    }

    @Override
    public int getCount() {
        return credentialmails.size();
    }

    @Override
    public Object getItem(int position) {
        return credentialmails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = convertView;
        view = layoutInflater.inflate(R.layout.credentialitem, null);
        TextView credentialmail = (TextView) view.findViewById(R.id.credentialmail);
        credentialmail.setText(credentialmails.get(position).getEmail());
        Button remove = (Button) view.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    builder = new android.support.v7.app.AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                else
                    builder = new android.support.v7.app.AlertDialog.Builder(activity);

                builder.setMessage("Do you want remove this Credential ? ")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                String e1 = credentialmails.get(position).getEmail().replace(".", ",");
                                String e2 = e1.replace("@", ",");

                                FirebaseDatabase.getInstance().getReference()
                                        .child("CredentialsDrivers1")
                                        .child(e2)
                                        .removeValue();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("CredentialsDrivers")
                                        .child(e2)
                                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.dismiss();
                                        Toast.makeText(activity, "Removed !!", Toast.LENGTH_SHORT).show();
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
        credentialmails.clear();
        if (chartext.length() == 0) {
            credentialmails.addAll(arrayList);
        } else {
            for (Credentialmail Credentialmail : arrayList) {
                if (Credentialmail.getEmail().toLowerCase(Locale.getDefault()).contains(chartext)) {
                    credentialmails.add(Credentialmail);
                }
            }
        }
        notifyDataSetChanged();


    }
}