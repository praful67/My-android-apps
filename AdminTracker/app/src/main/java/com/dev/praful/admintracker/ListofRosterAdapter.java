package com.dev.praful.admintracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ListofRosterAdapter extends BaseAdapter {

    List<Rostersetlistitem> rostersetlistitems;
    Activity activity;
    ArrayList<Rostersetlistitem> arrayList;

    public ListofRosterAdapter(List<Rostersetlistitem> rostersetlistitems, Activity activity) {
        this.rostersetlistitems = rostersetlistitems;
        this.activity = activity;
        this.arrayList = new ArrayList<Rostersetlistitem>();
        this.arrayList.addAll(rostersetlistitems);
    }


    @Override
    public int getCount() {
        return rostersetlistitems.size();
    }

    @Override
    public Object getItem(int i) {
        return rostersetlistitems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view1, ViewGroup viewGroup) {
        final LayoutInflater inflater = activity.getLayoutInflater();
        View view = view1;
        view = inflater.inflate(R.layout.rostersetitem, null);
        CardView card = (CardView) view.findViewById(R.id.card);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        ImageView edit = (ImageView) view.findViewById(R.id.edit);
        FirebaseDatabase.getInstance().getReference()
                .child("List of Rosters Sets")
                .child(rostersetlistitems.get(i).getId())
                .child("id")
                .setValue(rostersetlistitems.get(i).getId());
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Carslist.class);
                intent.putExtra("rostersetid", rostersetlistitems.get(i).getId());
                editor.putString("rostersetid", rostersetlistitems.get(i).getId());
                editor.apply();
                activity.startActivity(intent);

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getBaseContext(), SetRosterdetails.class);
                editor.putString("rostersetid", rostersetlistitems.get(i).getId());
                editor.apply();
                intent.putExtra("rostersetid", rostersetlistitems.get(i).getId());
                activity.startActivity(intent);

            }
        });
        final TextView dates = (TextView) view.findViewById(R.id.dates);

        if (rostersetlistitems.get(i).getDates() != null)
            dates.setText(rostersetlistitems.get(i).getDates());

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.scale_in_slow);
        if (sharedPreferences.getString("anim", "").equals("yes")) {

            view.startAnimation(animation);
            if (String.valueOf(getItemId(i)).equals(String.valueOf(rostersetlistitems.size() - 1))) {
                editor.putString("anim", "no");
                editor.apply();
            }

        }
        TextView setname = (TextView) view.findViewById(R.id.setname);
        setname.setText(rostersetlistitems.get(i).getName());


        return view;
    }

    public void filter(String chartext) {
        chartext = chartext.toLowerCase(Locale.getDefault());
        rostersetlistitems.clear();
        if (chartext.length() == 0) {
            rostersetlistitems.addAll(arrayList);
        } else {
            for (Rostersetlistitem rosteritem : arrayList) {
                if (rosteritem.getName().toLowerCase(Locale.getDefault()).contains(chartext)) {
                    rostersetlistitems.add(rosteritem);
                }
            }
        }
        notifyDataSetChanged();

    }
}
