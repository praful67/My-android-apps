package com.praful.praful.uber;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.praful.praful.uber.Common.Common;
import com.praful.praful.uber.Model.Bookings;
import com.praful.praful.uber.Model.Coustomers;
import com.praful.praful.uber.Model.CustomerBookings;
import com.praful.praful.uber.Model.DataMessage;
import com.praful.praful.uber.Model.FCMResponse;
import com.praful.praful.uber.Model.Token;
import com.praful.praful.uber.Remote.IFCMSerives;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListAdapter extends BaseAdapter {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Activity activity;
    IFCMSerives ifcmSerives;

    String Id;
    List<com.praful.praful.uber.Model.Bookings> Bookings;

    public BookingListAdapter(Activity activity, List<Bookings> bookings) {
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
        View itemView = inflater.inflate(R.layout.bookingitem, null);

        final TextView txtcustomername = (TextView) itemView.findViewById(R.id.customerName);
        final TextView txtcustomerphone = (TextView) itemView.findViewById(R.id.customerphone);
        TextView eventtype = (TextView) itemView.findViewById(R.id.eventtype);
        eventtype.setText(Bookings.get(position).getEventType());

        final TextView txtcustomerId = (TextView) itemView.findViewById(R.id.customerId);
        txtcustomerId.setText(Bookings.get(position).getCustomerId());
        txtcustomername.setText(Bookings.get(position).getCustomerName());
        txtcustomerphone.setText(Bookings.get(position).getCustomerPhone());
        ImageView getdirection = (ImageView) itemView.findViewById(R.id.getdirections);
        ImageView complete = (ImageView) itemView.findViewById(R.id.completeBooking);

        final CircleImageView customerphoto = (CircleImageView) itemView.findViewById(R.id.customerPhoto);

        final TextView bookingdate = (TextView) itemView.findViewById(R.id.bookingdate);
        final TextView bookingtime = (TextView) itemView.findViewById(R.id.bookingtime);


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
        getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Companytrackingforadvancebooking.class);
                intent.putExtra("lat1", Bookings.get(position).getLat1());
                intent.putExtra("lng1", Bookings.get(position).getLng1());
                intent.putExtra("BookingIdT", Bookings.get(position).getId());
                intent.putExtra("customerId", Bookings.get(position).getCustomerId());
                activity.getBaseContext().startActivity(intent);
            }
        });
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
                intent.putExtra("Eventtype", Bookings.get(position).getEventType());

                activity.getBaseContext().startActivity(intent);
            }
        });
        Id = Bookings.get(position).getId();

        ImageView call_phone = (ImageView) itemView.findViewById(R.id.Tcall);


        final ImageView cancelbooking = (ImageView) itemView.findViewById(R.id.cancelAdvancebooking);

        cancelbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                else
                    builder = new AlertDialog.Builder(activity);

                builder.setMessage("Do you want to cancel this booking ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                databaseReference.child("cancelBooking").child(Bookings.get(position).getCustomerId())
                                        .child("bookingid")
                                        .setValue(Bookings.get(position).getId());

                                databaseReference.child("Bookings").child(Common.userid).child(Bookings.get(position).getId()).removeValue();
                                databaseReference.child("coustomerBookings").child(Bookings.get(position).getCustomerId()).child(Bookings.get(position).getId()).removeValue();

                                CustomerBookings cancelledcustomerBookings = new CustomerBookings(Bookings.get(position).getId(), Common.userid, Common.currentUser.getName(),
                                        Common.currentUser.getPhone(), Bookings.get(position).getAddress(), Bookings.get(position).getDate(), Bookings.get(position).getTime(), Bookings.get(position).getEventType()
                                );
                                databaseReference.child("cancelledcustomerBookings").child(Bookings.get(position).getCustomerId()).child(Bookings.get(position).getId()).setValue(cancelledcustomerBookings);

                                Bookings cancelledBooking = new Bookings(Bookings.get(position).getId(), Bookings.get(position).getCustomerId(), Bookings.get(position).getCustomerName(), Bookings.get(position)
                                        .getCustomerPhone()
                                        , Bookings.get(position).getCustomerAddress(), Bookings.get(position).getAddress(),
                                        Bookings.get(position).getDate(),
                                        Bookings.get(position).getTime(), Bookings.get(position).getLat1(), Bookings.get(position)
                                        .getLng1(), Bookings.get(position).getEventType()
                                );
                                databaseReference.child("cancelledBookings").child(Common.userid).child(Bookings.get(position).getId()).setValue(cancelledBooking);
                                sendCancelbookingNotificationTocustomer(Bookings.get(position).getCustomerId(), ifcmSerives, activity.getBaseContext(),
                                        Bookings.get(position).getDate(), Bookings.get(position).getTime(), Bookings.get(position).getAddress(),
                                        Common.currentUser.getName(), Bookings.get(position).getEventType()
                                );


                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });


        call_phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + txtcustomerphone.getText().toString()));

                if (ActivityCompat.checkSelfPermission(activity.getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                activity.getBaseContext().startActivity(intent);
            }
        });


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sendCompletenotification(txtcustomerId.getText().toString(), ifcmSerives, activity.getBaseContext(),
                        Bookings.get(position).getId());


                databaseReference.child("verifyingBooking").child(txtcustomerId.getText().toString())
                        .child("bookingId").removeValue();



            }
        });

        return itemView;

    }

    private void sendCompletenotification(final String customerId, final IFCMSerives ifcmSerives, final Context context
            , final String BookingIdC) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Common.token_table);

        tokens.orderByKey().equalTo(customerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Token token = dataSnapshot1.getValue(Token.class);
                            Map<String, String> content = new HashMap<>();
                            content.put("title", "completedAdvancebooking");
                            String message = "Comapany " + Common.currentUser.getName() + " says that this booking has completed";
                            content.put("message", message);
                            content.put("companyIdC", Common.userid);
                            content.put("BookingIdC", BookingIdC);

                            DataMessage dataMessage = new DataMessage(token.getToken(), content);

                            ifcmSerives.sendMessage(dataMessage)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body().success == 1) {

                                                new CountDownTimer(2000, 1000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {

                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        databaseReference.child("verifyingBooking").child(customerId)
                                                                .child("bookingId").setValue(BookingIdC);
                                                    }
                                                }.start();
                                                Intent intent = new Intent(activity.getBaseContext(), Reqsent.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                activity.getBaseContext().startActivity(intent);


                                                Toast.makeText(context, "Notification sent!", Toast.LENGTH_SHORT).show();
                                            } else
                                                Toast.makeText(context, "Failed to send the Request!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {

                                            Log.e("ERROR", t.getMessage());

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void sendCancelbookingNotificationTocustomer(String customerId, final IFCMSerives ifcmSerives, final Context context, final String Date, final String Time
            , final String Address, final String companyName, final String EventType) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Common.token_table);

        tokens.orderByKey().equalTo(customerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot post : dataSnapshot.getChildren()) {
                            Token token = post.getValue(Token.class);

                            // String json_lat_lng = new Gson().toJson(new LatLng(location.getLatitude(), location.getLongitude()));

                            //   String companytoken = FirebaseInstanceId.getInstance().getToken();
                            // Notification data = new Notification( FirebaseInstanceId.getInstance().getToken(), json_lat_lng);
                            // Sender content = new Sender(token.getToken() , data);

                            Map<String, String> content = new HashMap<>();
                            content.put("title", "cancelAdvanceBooking");
                            content.put("DateCB", Date);
                            content.put("TimeCB", Time);
                            content.put("messageCB", companyName + " has cancelled your Advance Booking unfortunately");
                            content.put("AddressCB", Address);
                            content.put("EventType", EventType);
                            content.put("Id", Id);
                            content.put("CompanyNamethatcancelledthebooking", companyName);

                            //  content.put("customer", companytoken);
                            DataMessage dataMessage = new DataMessage(token.getToken(), content);

                            ifcmSerives.sendMessage(dataMessage)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body().success == 1)
                                                Toast.makeText(context, "Notification sent!", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(context, "Failed to send the Request!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {

                                            Log.e("ERROR", t.getMessage());

                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


}
