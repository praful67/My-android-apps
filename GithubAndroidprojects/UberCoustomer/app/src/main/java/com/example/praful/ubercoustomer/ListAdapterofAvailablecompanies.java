package com.example.praful.ubercoustomer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapterofAvailablecompanies extends BaseAdapter {

    Activity activity;
    List<String> ids;
    LayoutInflater layoutInflater;

    public ListAdapterofAvailablecompanies(Activity activity, List<String> ids) {
        this.activity = activity;
        this.ids = ids;
    }

    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public Object getItem(int position) {
        return ids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.availablecompanyitem, null);
        final TextView txtcompanyname = (TextView) itemView.findViewById(R.id.companyname);
        final TextView txtcompanyphone = (TextView) itemView.findViewById(R.id.companyphone);
        final ImageView call_phone = (ImageView) itemView.findViewById(R.id.Tcall);
        final CircleImageView companyphoto = (CircleImageView) itemView.findViewById(R.id.companyphoto);
        call_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + txtcompanyphone.getText().toString()));
                if (ActivityCompat.checkSelfPermission(activity.getBaseContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                activity.getBaseContext().startActivity(intent);

            }
        });
        ImageView details = (ImageView) itemView.findViewById(R.id.detailsbtn);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Availablecompanydetails.class);
                intent.putExtra("companyId", ids.get(position).toString());

                activity.getBaseContext().startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table)
                .child(ids.get(position).toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (!user.getAvatarUrl().isEmpty()) {

                            Picasso.with(activity.getBaseContext()).load(user.getAvatarUrl())
                                    .into(companyphoto);
                        }

                        txtcompanyname.setText(user.getName());
                        txtcompanyphone.setText(user.getPhone());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        TextView id = (TextView) itemView.findViewById(R.id.id);
        id.setText(ids.get(position).toString());

        return itemView;

    }
}
