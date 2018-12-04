package com.example.praful.firebasedatabase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {


    Activity activity;

    List<Users> listusers;
    LayoutInflater inflater;

    public ListAdapter(Activity activity, List<Users> listusers) {
        this.activity = activity;
        this.listusers = listusers;
    }

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
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview_item ,null);

        TextView txtname = (TextView)itemView.findViewById(R.id.txtname);
        TextView txtemail = (TextView)itemView.findViewById(R.id.txtemail);

        txtname.setText(listusers.get(position).getName());
        txtemail.setText(listusers.get(position).getEmail());

        return itemView;

    }
}
