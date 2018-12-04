package com.dev.praful.admintracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.loopingviewpager.LoopingPagerAdapter;

import java.util.ArrayList;

public class LoopInfiniteAdapter extends LoopingPagerAdapter<Integer> {

    private static final int VIEW_TYPE_NORMAL = 100;
    private static final int VIEW_TYPE_SPECIAL = 101;

    public LoopInfiniteAdapter(Context context, ArrayList<Integer> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected int getItemViewType(int listPosition) {
        //  if (itemList.get(listPosition) == 0) return VIEW_TYPE_SPECIAL;
        return VIEW_TYPE_NORMAL;
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        //   if (viewType == VIEW_TYPE_SPECIAL) return LayoutInflater.from(context).inflate(R.layout.item_special, container, false);
        return LayoutInflater.from(context).inflate(R.layout.item_pager, container, false);
    }

    @Override
    protected void bindView(View convertView, final int listPosition, int viewType) {
        convertView.findViewById(R.id.image).setBackgroundColor(context.getResources().getColor(getBackgroundColor(listPosition)));
        TextView description = convertView.findViewById(R.id.description);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        ImageView imageView = convertView.findViewById(R.id.imageview);
        if (listPosition == 0) {
            imageView.setImageResource(R.drawable.car1);
            description.setText("CAB ROSTING");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ListofRostersets.class);
                    editor.putString("anim", "yes");
                    editor.apply();
                    context.startActivity(intent);
                }
            });

        } else if (listPosition == 1) {
            imageView.setImageResource(R.drawable.time);
            description.setText("TIME OFFS");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("anim", "yes");
                    editor.apply();
                    Intent intent = new Intent(context, EmployeesTimeoff.class);
                    context.startActivity(intent);
                }
            });

        } else if (listPosition == 2) {
            description.setText("COMMENTS");
            imageView.setImageResource(R.drawable.comments);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("anim", "yes");
                    editor.apply();
                    Intent intent = new Intent(context, Employeescomment.class);
                    context.startActivity(intent);
                }
            });
        }

    }


    private int getBackgroundColor(int number) {
        switch (number) {
            case 0:
                return android.R.color.holo_blue_light;
            case 1:
                return android.R.color.holo_orange_light;
            case 2:
                return android.R.color.holo_red_light;
           /* case 3:
                return android.R.color.holo_blue_light;
            case 4:
                return android.R.color.holo_purple;
            case 5:
                return android.R.color.black;
           */
            default:
                return android.R.color.black;
        }
    }
}
