package com.iitjammu.praful.iitjammu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        if (listPosition == 0) {
            description.setText("1");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        } else if (listPosition == 1) {
            description.setText("2");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        } else if (listPosition == 2) {
            description.setText("3");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

    }


    private int getBackgroundColor(int number) {
        switch (number) {
            case 0:
                return android.R.color.holo_purple;
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
