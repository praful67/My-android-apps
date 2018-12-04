package com.Shootmyshow.praful.shootmyshow_company.Service;

import android.content.Intent;

import com.Shootmyshow.praful.shootmyshow_company.Common.Common;
import com.Shootmyshow.praful.shootmyshow_company.CustomerCallForAdvanceBooking;
import com.Shootmyshow.praful.shootmyshow_company.CustomercancelledtheBooking;
import com.Shootmyshow.praful.shootmyshow_company.ResponseNO;
import com.Shootmyshow.praful.shootmyshow_company.ResponseYES;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    public MyFirebaseMessaging() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {
            //  Log.d("PRAFUL" , remoteMessage.getNotification().getBody() );
            Map<String, String> data = remoteMessage.getData();

            String customer = data.get("customer");
            String customerName = data.get("CustomerName");
            String customerPhone = data.get("CustomerPhone");
            String Date = data.get("Date");
            String Time = data.get("Time");
            String EventType = data.get("EventType");
            String Address = data.get("Address");
            String Type = data.get("Type");
            String BookingIdC = data.get("BookingIdC");
            String lat = data.get("lat");
            String Id = data.get("Id");
            String lng = data.get("lng");
            String lat1 = data.get("lat1");
            String lng1 = data.get("lng1");
            String response = data.get("response");
            String customerId = data.get("customerId");
            String DateCB = data.get("DateCB");
            String TimeCB = data.get("TimeCB");
            String messageCB = data.get("messageCB");
            String AddressCB = data.get("AddressCB");
            String CustomerNamethatcancelledthebooking = data.get("CustomerNamethatcancelledthebooking");

            //  LatLng customer_location = new Gson().fromJson(remoteMessage.getNotification().getBody(), LatLng.class);

          /*  if (Type.equals("InstantBooking")) {
                Intent intent = new Intent(getBaseContext(), CustomerCall.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("customer", customer);

                startActivity(intent);
            } else */
            if (Type != null && Type.equals("AdvanceBooking")) {
                Intent intent = new Intent(getBaseContext(), CustomerCallForAdvanceBooking.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("customer", customer);
                intent.putExtra("Date", Date);
                intent.putExtra("Time", Time);
                intent.putExtra("lat1", lat1);
                intent.putExtra("EventType", EventType);
                intent.putExtra("lng1", lng1);
                intent.putExtra("Address", Address);
                intent.putExtra("customerName", customerName);
                intent.putExtra("customerPhone", customerPhone);
                intent.putExtra("customerId", customerId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             //   startActivity(intent);


            } else if (Type != null && Type.equals("cancelAdvanceBooking")) {

                Intent intent = new Intent(getBaseContext(), CustomercancelledtheBooking.class);
                intent.putExtra("DateCB", DateCB);
                intent.putExtra("TimeCB", TimeCB);
                intent.putExtra("messageCB", messageCB);
                intent.putExtra("AddressCB", AddressCB);
                intent.putExtra("Id", Id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CustomerNamethatcancelledthebooking", CustomerNamethatcancelledthebooking);
              //  startActivity(intent);

            } else if (Type != null && Type.equals("customerresponseoncompletedAdvanceBooking")) {

                if (response.equals("YES")) {

                    double number = Double.parseDouble(Common.currentUser.getNumberofsuccessfulbookings());
                    number = number + 1;
                    FirebaseDatabase.getInstance().getReference(Common.companiesInfo_table)
                            .child(Common.userid).child("numberofsuccessfulbookings").setValue(String.valueOf((int) number));
                    Intent intent = new Intent(MyFirebaseMessaging.this, ResponseYES.class);
                    intent.putExtra("BookingIdC", BookingIdC);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   // startActivity(intent);


                } else if (response.equals("NO")) {

                    Intent intent = new Intent(MyFirebaseMessaging.this, ResponseNO.class);
                    intent.putExtra("BookingIdC", BookingIdC);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   // startActivity(intent);

                }

            }


        }

    }


}
