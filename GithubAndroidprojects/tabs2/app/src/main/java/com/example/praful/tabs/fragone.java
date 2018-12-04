package com.example.praful.tabs;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class fragone extends Fragment {

    @Nullable
    Button bt;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view1 = inflater.inflate(R.layout.fragone ,container ,false);

        Button bt = (Button) view1.findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("dialog box")
                        .setPositiveButton("Positive Button", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "You have clicked Postive option" , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Negative Button" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "You have clicked Negative option" , Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });



        return view1;

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }



}
