package com.example.praful.presencesystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class LstViewAdapter extends BaseAdapter {

    Activity activity;
    List<User> listusers;
    String lat , lng;

    public LstViewAdapter(Activity activity, List<User> listusers) {
        this.activity = activity;
        this.listusers = listusers;
    }

    LayoutInflater inflater;


    @Override
    public int getCount() {
        return listusers.size();
    }

    @Override
    public Object getItem(int position) {
        return listusers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View itemView = inflater.inflate(R.layout.listview_layout, null);
        final String name = listusers.get(position).getName();


        TextView txtuser = (TextView) itemView.findViewById(R.id.txtName);
        Button track = (Button) itemView.findViewById(R.id.track);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity.getBaseContext(), MapTracking.class);
                intent.putExtra("name" , listusers.get(position).getName().toString());
                activity.startActivity(intent);

            }
        });

        txtuser.setText(listusers.get(position).getName());

        return itemView;
    }
}
