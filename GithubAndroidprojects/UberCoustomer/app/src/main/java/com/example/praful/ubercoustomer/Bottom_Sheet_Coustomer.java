package com.example.praful.ubercoustomer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.praful.ubercoustomer.Common.Common;
import com.example.praful.ubercoustomer.Remote.IGoogleAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bottom_Sheet_Coustomer extends BottomSheetDialogFragment {

    String  Location , Destination;

    boolean isTaponMap;
    IGoogleAPI Services;
    TextView txtCalculate , txtlocation , txtdestination;

    public static Bottom_Sheet_Coustomer newInstance(String location , String destination,    boolean isTaponMap)
    {
        Bottom_Sheet_Coustomer f = new Bottom_Sheet_Coustomer();
        Bundle args = new Bundle();
        args.putString("location" , location);
        args.putString("destination" , destination);
        args.putBoolean("isTaponMap" , isTaponMap);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Location = getArguments().getString("location");
        Destination = getArguments().getString("destination");
        isTaponMap = getArguments().getBoolean("isTaponMap");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_coustomer, container,false);
        txtlocation = (TextView)view.findViewById(R.id.txtLocation);
        txtdestination = (TextView)view.findViewById(R.id.txtDestination);
        txtCalculate = (TextView)view.findViewById(R.id.txtCalculate);

        Services = Common.getGoogleService();

        getDT(Location , Destination);
        if (!isTaponMap){

            txtdestination.setText(Destination);
            txtlocation.setText(Location);

        }

        return view;
    }

    private void getDT(String location, String destination) {

        String requestUrl = null;
        try{
            /* requestUrl = "https://maps.googleapis.com/maps/api/directions/json?"
                    +"mode=driving&"
                    +"transit_routing_preference=less_driving&"
                    +"origin="+Location+"&"+"destination+"+Destination+"&"
                    + "key="+getResources().getString(R.string.google_direction_api); */
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?"+"mode=driving&"+
                    "transit_routing_preference=less_driving&"+"origin="+location+"&"+
                    "destination="+destination+"&"+
                    "key="+getResources().getString(R.string.google_direction_api);
            Log.e("LINK", requestUrl);
            Services.getPath(requestUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray routes = jsonObject.getJSONArray("routes");
                        JSONObject object = routes.getJSONObject(0);

                        JSONArray legs = object.getJSONArray("legs");
                        JSONObject legsobject = legs.getJSONObject(0);

                        //GET DISTANCE
                        JSONObject distance = legsobject.getJSONObject("distance");
                        String distance_text = distance.getString("text");

                        Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+",""));


                        JSONObject time = legsobject.getJSONObject("duration");
                        String time_text = time.getString("text");
                        Integer  time_value = Integer.parseInt(time_text.replaceAll("\\D+" ,""));

                        String finalCal = String.format("%s %s",distance_text ,time_text);

                        txtCalculate.setText(finalCal);

                        if (isTaponMap){
                            String start_address = legsobject.getString("start_address");
                            String end_address = legsobject.getString("end_address");

                            txtdestination.setText(end_address);
                            txtlocation.setText(start_address);
                        }

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Log.e("ERROR" , t.getMessage());
                }
            });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
