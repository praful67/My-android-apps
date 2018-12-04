package com.Shootmyshow.praful.shootmyshow_company;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Shootmyshow.praful.shootmyshow_company.Common.Common;
import com.Shootmyshow.praful.shootmyshow_company.Model.Bookings;
import com.Shootmyshow.praful.shootmyshow_company.Model.Coustomers;
import com.Shootmyshow.praful.shootmyshow_company.Remote.IFCMSerives;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CancelledBookingListAdapter extends BaseAdapter {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Activity activity;
    IFCMSerives ifcmSerives;


    List<com.Shootmyshow.praful.shootmyshow_company.Model.Bookings> Bookings;

    public CancelledBookingListAdapter(Activity activity, List<Bookings> bookings) {
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


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        ifcmSerives = Common.getFCMService();
        inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = convertView;
        itemView = inflater.inflate(R.layout.cancelledbookingitem, null);

        final TextView txtcustomername = (TextView) itemView.findViewById(R.id.customerName);
        final TextView txtcustomerphone = (TextView) itemView.findViewById(R.id.customerphone);
        TextView eventtype= (TextView)itemView.findViewById(R.id.eventtype);
        eventtype.setText(Bookings.get(position).getEventType());

        final TextView txtcustomerId = (TextView) itemView.findViewById(R.id.customerId);
        txtcustomerId.setText(Bookings.get(position).getCustomerId());
        txtcustomername.setText(Bookings.get(position).getCustomerName());
        txtcustomerphone.setText(Bookings.get(position).getCustomerPhone());
        final CircleImageView customerphoto = (CircleImageView) itemView.findViewById(R.id.customerPhoto);
        ImageView details = (ImageView) itemView.findViewById(R.id.detailsbtn);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), AdvanceBookingsDetails.class);
                intent.putExtra("Date", Bookings.get(position).getDate());
                intent.putExtra("Time", Bookings.get(position).getTime());
                intent.putExtra("Address", Bookings.get(position).getAddress());
                intent.putExtra("customername", Bookings.get(position).getCustomerName());
                intent.putExtra("customerphone", Bookings.get(position).getCustomerPhone());
                intent.putExtra("customerId", Bookings.get(position).getCustomerId());
                intent.putExtra("Eventtype" , Bookings.get(position).getEventType());
                intent.putExtra("customeraddress" , Bookings.get(position).getCustomerAddress());

                activity.getBaseContext().startActivity(intent);
            }
        });

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
        FirebaseDatabase.getInstance().getReference(Common.coustomers_table)
                .child(Bookings.get(position).getCustomerId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Coustomers user = dataSnapshot.getValue(Coustomers.class);

                        if (!user.getAvatarUrl().isEmpty()) {

                            Picasso.with(activity.getBaseContext()).load(user.getAvatarUrl())
                                    .into(customerphoto);
                        }

                        txtcustomername.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        ImageView removebtn = (ImageView) itemView.findViewById(R.id.cancelAdvancebooking);


        removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("cancelledBookings").child(Common.userid).child(Bookings.get(position).getId()).removeValue();

            }
        });


        return itemView;

    }
}
