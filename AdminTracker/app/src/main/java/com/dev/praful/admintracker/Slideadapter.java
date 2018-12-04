package com.dev.praful.admintracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Slideadapter extends PagerAdapter {

    List<Slideitem> slideitems;
    Context context;
    LayoutInflater layoutInflater;

    public Slideadapter(List<Slideitem> integerList, Context context) {
        this.slideitems = integerList;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return slideitems.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = layoutInflater.inflate(R.layout.carditem, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(slideitems.get(position).getInteger());
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("CAB ROSTING");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        text.setText(slideitems.get(position).getText());

        if (slideitems.get(position).getText().toString().equals("CAB ROSTING")) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Carslist.class);
                    editor.putString("anim", "yes");
                    editor.apply();
                    context.startActivity(intent);
                }
            });
        } else if (slideitems.get(position).getText().toString().equals("TIME OFF")) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("anim", "yes");
                    editor.apply();
                    Intent intent = new Intent(context, EmployeesTimeoff.class);
                    context.startActivity(intent);
                }
            });
        } else if (slideitems.get(position).getText().toString().equals("COMMENTS")) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("anim", "yes");
                    editor.apply();
                    Intent intent = new Intent(context, Employeescomment.class);
                    context.startActivity(intent);
                }
            });
        }


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
