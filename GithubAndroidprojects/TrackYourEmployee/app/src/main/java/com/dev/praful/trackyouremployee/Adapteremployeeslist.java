package com.dev.praful.trackyouremployee;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Adapteremployeeslist extends BaseAdapter {

    List<Employeedetails> employeedetails;
    Activity activity;
    String result;
    String lat, lng;
    IGoogleAPI Services;
    String distance_textD, time_textD;
    JSONObject distance;
    JSONObject time;
    TextView distancefromdiver;

    public Adapteremployeeslist(List<Employeedetails> employeedetails, Activity activity) {
        this.employeedetails = employeedetails;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return employeedetails.size();
    }

    @Override
    public Object getItem(int position) {
        return employeedetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = convertView;
        view = inflater.inflate(R.layout.finalcaremployeeitem, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        final TextView distance = (TextView) view.findViewById(R.id.distance);
        final TextView rank = (TextView) view.findViewById(R.id.rank);
        LinearLayout details = (LinearLayout) view.findViewById(R.id.details);
        Services = CommonSwitch.getGoogleService();
        final TextView pickuptime = (TextView)view.findViewById(R.id.pickuptime);

        FirebaseDatabase.getInstance().getReference()
                .child("Employeespickuptimes")
                .child(employeedetails.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                pickuptime.setText(String.valueOf(map.get("time")));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final TextView timeoffE = (TextView) view.findViewById(R.id.timeoffE);
        final LinearLayout TF = (LinearLayout) view.findViewById(R.id.timeofflayout);

        FirebaseDatabase.getInstance().getReference()
                .child("StatusE")
                .child(employeedetails.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                if (!String.valueOf(map.get("status")).equals("Active")) {
                                    TF.setVisibility(View.VISIBLE);
                                    timeoffE.setText(String.valueOf(map.get("status")));
                                } else {
                                    TF.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        name.setText(employeedetails.get(position).getUsername());
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getBaseContext(), Employeedetailspage.class);
                intent.putExtra("employeeid", employeedetails.get(position).getId());
                activity.startActivity(intent);
            }
        });
        lat = employeedetails.get(position).getAddresslat();
        lng = employeedetails.get(position).getAddresslng();
        String id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

        distancefromdiver = (TextView) view.findViewById(R.id.distancefromdiver);
        FirebaseDatabase.getInstance().getReference()
                .child("Drivers Info")
                .child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DriversInfo driversInfo = dataSnapshot.getValue(DriversInfo.class);
                if (driversInfo != null) {
                    String lat0 = driversInfo.getLat();
                    String lng0 = driversInfo.getLng();

                    Double lat1 = Double.parseDouble(lat);
                    Double lat2 = Double.parseDouble(lat0);
                    Double lng2 = Double.parseDouble(lng0);
                    Double lng1 = Double.parseDouble(lng);
                    getDT(lat1, lng1, lat2, lng2);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ImageView imageView = (ImageView) view.findViewById(R.id.getdirections);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat != null && lng != null) {
                    if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
                        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + Double.parseDouble(lat) + " , " + Double.parseDouble(lng) + "&mode=d");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        activity.startActivity(mapIntent);
                    }
                }
            }
        });
        FirebaseDatabase.getInstance().getReference()
                .child("Final Rank of employee")
                .child(employeedetails.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            rank.setText(String.valueOf(map.get("Rank")));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("Final Ordered Employees")
                .child(employeedetails.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (map != null) {
                            distance.setText(String.valueOf(map.get("distance")) + " " + String.valueOf(map.get("time")) + " far from Office");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return view;
    }

    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String response = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    String distanceString = distance.getString("text");
                    JSONObject time = steps.getJSONObject("duration");
                    String timeString = time.getString("text");
                    result = distanceString + " " + timeString;


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getDT(final double lat1, final double lng1, final double lat2, final double lng2) {


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String requestUrl = null;
                try {
                    requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                            "transit_routing_preference=less_driving&" + "origin=" + lat1 + "," + lng1 + "&" +
                            "destination=" + lat2 + "," + lng2 + "&" +
                            "key=" + activity.getResources().getString(R.string.google_maps_key);
                    Log.e("LINK", requestUrl);
                    Services.getPath(requestUrl).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray routes = jsonObject.getJSONArray("routes");
                                JSONObject object = routes.getJSONObject(0);

                                JSONArray legs = object.getJSONArray("legs");
                                JSONObject legsobject = legs.getJSONObject(0);

                                distance = legsobject.getJSONObject("distance");
                                distance_textD = distance.getString("text");

                                time = legsobject.getJSONObject("duration");
                                time_textD = time.getString("text");

                                distancefromdiver.setText(distance_textD + " " + time_textD + " (Pick up address) from you");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Log.e("ERROR", t.getMessage());
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        }.start();
        return null;
    }


}
//    Integer time_value = Integer.parseInt(time_textD.replaceAll("\\D+", ""));
//    Integer time_value = Integer.parseInt(time_textD.replaceAll("\\D+", ""));
                               /* String start_address = legsobject.getString("start_address");
                                String end_address = legsobject.getString("end_address");

                              */  // Toast.makeText(Map.this, distance_textD + " " + time_textD, Toast.LENGTH_SHORT).show();
