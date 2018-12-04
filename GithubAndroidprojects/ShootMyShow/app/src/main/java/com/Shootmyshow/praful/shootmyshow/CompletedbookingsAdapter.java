package com.Shootmyshow.praful.shootmyshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.Shootmyshow.praful.shootmyshow.Model.Bookings;
import com.Shootmyshow.praful.shootmyshow.Model.User;
import com.Shootmyshow.praful.shootmyshow.Remote.IFCMSerives;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompletedbookingsAdapter extends BaseAdapter {
    IFCMSerives ifcmSerives;

    Activity activity;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<Bookings> Bookings;

    public CompletedbookingsAdapter(Activity activity, List<Bookings> bookings) {
        this.activity = activity;
        this.Bookings = bookings;
    }

    LayoutInflater inflater;


    @Override
    public int getCount() {
        return Bookings.size();
    }

    @Override
    public Object getItem(int position) {
        return Bookings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = convertView;
        itemView  = inflater.inflate(R.layout.completedbookingitem, null);

      //  ifcmSerives = Common.getFCMService();
        FirebaseApp.initializeApp(activity.getBaseContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        final TextView eventtype = (TextView) itemView.findViewById(R.id.eventtype);


        final TextView txtcompanyname = (TextView) itemView.findViewById(R.id.companyname);

        final TextView txtcompanyphone = (TextView) itemView.findViewById(R.id.companyphone);
        final CircleImageView companyphoto = (CircleImageView)itemView.findViewById(R.id.companyphoto);
        final TextView companyId = (TextView)itemView.findViewById(R.id.companyId);
        companyId.setText(Bookings.get(position).getCompanyId());
        ImageView details = (ImageView) itemView.findViewById(R.id.detailsbtn);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), AdvanceBookingsDetails.class);
                intent.putExtra("Date", Bookings.get(position).getDate());
                intent.putExtra("Time", Bookings.get(position).getTime());
                intent.putExtra("Address", Bookings.get(position).getAddress());
                intent.putExtra("companyId", Bookings.get(position).getCompanyId());
                intent.putExtra("EventType", Bookings.get(position).getEventType());

                activity.getBaseContext().startActivity(intent);
            }
        });
        eventtype.setText(Bookings.get(position).getEventType());



        final TextView bookingdate = (TextView)itemView.findViewById(R.id.bookingdate);
        final TextView bookingtime = (TextView)itemView.findViewById(R.id.bookingtime);


        FirebaseDatabase.getInstance().getReference("BookingsDateandTime")
                .child(Bookings.get(position).getId()).child("Date")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null)
                            bookingdate.setText(dataSnapshot.getValue().toString());
                        else
                            bookingdate.setText("Not Updated");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference("BookingsDateandTime")
                .child(Bookings.get(position).getId()).child("Time")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null)
                            bookingtime.setText(dataSnapshot.getValue().toString());
                        else
                            bookingtime.setText("Not Updated");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        companyphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), CallCompany.class);
                intent.putExtra("companyId" ,Bookings.get(position).getCompanyId());
                activity.startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table)
                .child(Bookings.get(position).getCompanyId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (!user.getAvatarUrl().isEmpty()) {

                            Picasso.with(activity.getBaseContext()).load(user.getAvatarUrl())
                                    .into(companyphoto);
                        }

                        txtcompanyname.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        txtcompanyname.setText(Bookings.get(position).getCompanyName());
        txtcompanyphone.setText(Bookings.get(position).getCompanyPhone());
        final ImageView removebtn = (ImageView) itemView.findViewById(R.id.removecompletedbooking);


        removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("CompletedcustomerBookings").child(Common.userid).child(Bookings.get(position).getId()).removeValue();

            }
        });


        return itemView;

    }

}
