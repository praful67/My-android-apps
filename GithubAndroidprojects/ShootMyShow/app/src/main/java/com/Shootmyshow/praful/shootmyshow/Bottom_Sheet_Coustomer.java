package com.Shootmyshow.praful.shootmyshow;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.Shootmyshow.praful.shootmyshow.Common.Common;
import com.Shootmyshow.praful.shootmyshow.Remote.IGoogleAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bottom_Sheet_Coustomer extends BottomSheetDialogFragment {

    String Locationlat, Locationlng, Destinationlng, Destinationlat;

    boolean isTaponMap;
    IGoogleAPI Services;
    String address1;
    Geocoder geocoder1;
    List<Address> addresses1;
    TextView txtCalculate, txtlocation, txtdestination;

    public static Bottom_Sheet_Coustomer newInstance(String locationlat, String locationlng, String destinationlat, String destinationlng, boolean isTaponMap) {
        Bottom_Sheet_Coustomer f = new Bottom_Sheet_Coustomer();
        Bundle args = new Bundle();
        args.putString("locationlat", locationlat);
        args.putString("locationlng", locationlng);
        args.putString("destinationlat", destinationlat);
        args.putString("destinationlng", destinationlng);
        args.putBoolean("isTaponMap", isTaponMap);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locationlat = getArguments().getString("locationlat");
        Locationlng = getArguments().getString("locationlng");
        Destinationlat = getArguments().getString("destinationlat");
        Destinationlng = getArguments().getString("destinationlng");
        isTaponMap = getArguments().getBoolean("isTaponMap");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_coustomer, container, false);
        txtlocation = (TextView) view.findViewById(R.id.txtLocation);
        txtdestination = (TextView) view.findViewById(R.id.txtDestination);
        txtCalculate = (TextView) view.findViewById(R.id.txtCalculate);
        Button tappedLBSF = (Button) view.findViewById(R.id.tappedLBSF);
        tappedLBSF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Switch.setChecked(true);
                dismiss();

            }
        });
        Location locationA = new Location("A");
        locationA.setLatitude(Double.parseDouble(Locationlat));
        locationA.setLongitude(Double.parseDouble(Locationlng));

        Location locationB = new Location("B");
        locationB.setLatitude(Double.parseDouble(Destinationlat));
        locationB.setLongitude(Double.parseDouble(Destinationlng));


        txtCalculate.setText(String.format("%.2f", locationA.distanceTo(locationB) / 1000) + " km");
        Services = Common.getGoogleService();
        geocoder1 = new Geocoder(getContext(), Locale.getDefault());

        try {
                addresses1 = geocoder1.getFromLocation(Double.parseDouble(Locationlat), Double.parseDouble(Locationlng), 1);
            if (addresses1 != null && addresses1.size() > 0) {

                txtlocation.setText(addresses1.get(0).getAddressLine(0));
            }else {
                txtlocation.setText("Sorry , Your location is not found");
                Toast.makeText(getContext(), "Sorry ,  we could not recognise your location", Toast.LENGTH_SHORT).show();


            }
                addresses1 = geocoder1.getFromLocation(Double.parseDouble(Destinationlat), Double.parseDouble(Destinationlng), 1);
            if (addresses1 != null && addresses1.size() > 0) {

                txtdestination.setText(addresses1.get(0).getAddressLine(0));
            }else {
                txtdestination.setText("Sorry , Your pick up address is not found");
                Toast.makeText(getContext(), "Sorry ,  we could not recognise your location", Toast.LENGTH_SHORT).show();


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
      // getDT(Location, Destination);
        if (!isTaponMap) {

            txtdestination.setText("NOT TAPPED");
            txtlocation.setText("NOT TAPPED");

        }

        return view;
    }

    private void getDT(String location, String destination) {

        String requestUrl = null;
        try {
            /* requestUrl = "https://maps.googleapis.com/maps/api/directions/json?"
                    +"mode=driving&"
                    +"transit_routing_preference=less_driving&"
                    +"origin="+Location+"&"+"destination+"+Destination+"&"
                    + "key="+getResources().getString(R.string.google_direction_api); */
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" + location + "&" +
                    "destination=" + destination + "&" +
                    "key=" + getResources().getString(R.string.google_direction_api);
            Log.e("LINK", requestUrl);
            Services.getPath(requestUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray routes = jsonObject.getJSONArray("routes");
                        JSONObject object = routes.getJSONObject(0);

                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject legsobject = legs.getJSONObject(0);

                        //GET DISTANCE
                        JSONObject distance = legsobject.getJSONObject("distance");
                        String distance_text = distance.getString("text");

                        Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));

                        JSONObject time = legsobject.getJSONObject("duration");
                        String time_text = time.getString("text");
                        Integer time_value = Integer.parseInt(time_text.replaceAll("\\D+", ""));

                        String finalCal = String.format("%s %s", distance_text, time_text);

                        txtCalculate.setText(finalCal);

                        if (isTaponMap) {
                            String start_address = legsobject.getString("start_address");
                            String end_address = legsobject.getString("end_address");

                            txtdestination.setText(end_address);
                            txtlocation.setText(start_address);
                        }

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
}
