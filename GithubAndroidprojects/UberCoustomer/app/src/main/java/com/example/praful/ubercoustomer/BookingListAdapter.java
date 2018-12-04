package com.example.praful.ubercoustomer;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.transition.Fade;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Model.Bookings;
import com.example.praful.ubercoustomer.Model.CompanyBookings;
import com.example.praful.ubercoustomer.Model.DataMessage;
import com.example.praful.ubercoustomer.Model.FCMResponse;
import com.example.praful.ubercoustomer.Model.Token;
import com.example.praful.ubercoustomer.Model.User;
import com.example.praful.ubercoustomer.Remote.IFCMSerives;
import com.facebook.accountkit.AccountKit;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListAdapter extends BaseAdapter {

    IFCMSerives ifcmSerives;

    Activity activity;
    FirebaseDatabase firebaseDatabase;
    String Id;
    DatabaseReference databaseReference;

    List<com.example.praful.ubercoustomer.Model.Bookings> Bookings;

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


        inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.bookingitem, null);

        ifcmSerives = Common.getFCMService();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

      /*  Fade fade = new Fade();
        View decor = activity.getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container) , true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground , true);

        activity.getWindow().setEnterTransition(fade);
        */final TextView txtcompanyname = (TextView) itemView.findViewById(R.id.companyname);
        final TextView eventtype = (TextView) itemView.findViewById(R.id.eventtype);
        final TextView companyId = (TextView) itemView.findViewById(R.id.companyId);
        final TextView txtcompanyphone = (TextView) itemView.findViewById(R.id.companyphone);
        final CircleImageView companyphoto = (CircleImageView) itemView.findViewById(R.id.companyphoto);


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
        //      txtcompanyname.setText(Bookings.get(position).getCompanyName());
        txtcompanyphone.setText(Bookings.get(position).getCompanyPhone());
        companyId.setText(Bookings.get(position).getCompanyId());
        eventtype.setText(Bookings.get(position).getEventType());
        final ImageView call_phone = (ImageView) itemView.findViewById(R.id.Tcall);
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

               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

              /*  ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity ,companyphoto , ViewCompat.getTransitionName(companyphoto));
                activity.getBaseContext().startActivity(intent , options.toBundle());
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, companyphoto, "image");
                activity.getBaseContext().startActivity(intent , transitionActivityOptions.toBundle());
               */ activity.getBaseContext().startActivity(intent);
            }
        });

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
        Id = Bookings.get(position).getId();

        ImageView cancelbooking = (ImageView) itemView.findViewById(R.id.cancelAdvancebooking);

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

                                databaseReference.child("coustomerBookings").child(Common.userid).child(Bookings.get(position).getId()).removeValue();
                                databaseReference.child("Bookings").child(Bookings.get(position).getCompanyId()).child(Bookings.get(position).getId()).removeValue();
                                CompanyBookings companycancelledBooking = new CompanyBookings(Bookings.get(position).getId(), Common.userid,
                                        Common.currentUser.getName(),
                                        Common.currentUser.getPhone()
                                        , String.valueOf(Common.Lastlocation),
                                        Bookings.get(position).getAddress(),
                                        Bookings.get(position).getDate(),
                                        Bookings.get(position).getTime(), String.valueOf(Common.Lastlocation), String.valueOf(Common.Lastlocation),
                                        Bookings.get(position).getEventType()
                                );
                                databaseReference.child("cancelledBookings").child(Bookings.get(position).getCompanyId()).child(Bookings.get(position).getId()).setValue(companycancelledBooking);

                                Bookings cancelledBooking = new Bookings(Bookings.get(position).getId(), Bookings.get(position).getCompanyId(), Bookings.get(position).getCompanyName(), Bookings.get(position)
                                        .getCompanyPhone(), Bookings.get(position).getAddress(), Bookings.get(position).getDate(), Bookings.get(position).getTime(), Bookings.get(position).getEventType()
                                );
                                databaseReference.child("cancelledcustomerBookings").child(Common.userid).child(Bookings.get(position).getId()).setValue(cancelledBooking);
                                sendCancelbookingNotificationTocompany(Bookings.get(position).getCompanyId(), ifcmSerives, activity.getBaseContext(),
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


        return itemView;

    }

    public void sendCancelbookingNotificationTocompany(final String companyId, final IFCMSerives ifcmSerives, final Context context, final String Date, final String Time
            , final String Address, final String customerName, final String EventType) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference(Common.token_table);

        tokens.orderByKey().equalTo(companyId)
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
                            content.put("Type", "cancelAdvanceBooking");
                            content.put("DateCB", Date);
                            content.put("TimeCB", Time);
                            content.put("messageCB", customerName + " has cancelled your Advance Booking unfortunately");
                            content.put("AddressCB", Address);
                            content.put("EventType", EventType);
                            content.put("Id", Id);
                            content.put("CustomerNamethatcancelledthebooking", customerName);

                            //  content.put("customer", companytoken);
                            DataMessage dataMessage = new DataMessage(token.getToken(), content);

                            ifcmSerives.sendMessage(dataMessage)
                                    .enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body().success == 1) {
                                                databaseReference.child("cuscancelbooking")
                                                        .child(companyId)
                                                        .child(Id)
                                                        .removeValue();

                                                databaseReference.child("cuscancelbooking")
                                                        .child(companyId)
                                                        .child(Id)
                                                        .setValue(Id);

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


}

