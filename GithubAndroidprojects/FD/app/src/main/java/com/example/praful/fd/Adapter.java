package com.example.praful.fd;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Adapter extends BaseAdapter {

    Activity activity;
    LayoutInflater inflater;
    List<Users> listUsers;

    public Adapter(Activity activity, List<Users> listUsers) {
        this.activity = activity;
        this.listUsers = listUsers;
    }

    @Override
    public int getCount() {
        return listUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return listUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listitem, null);

        TextView name = (TextView) itemView.findViewById(R.id.txtname);
        TextView email = (TextView) itemView.findViewById(R.id.txtemail);

        name.setText(listUsers.get(position).getName());
        email.setText(listUsers.get(position).getEmail());

        return itemView;

    }
}
