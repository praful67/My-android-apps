package com.iitjammu.praful.iitjammu;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class Campus extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);
        ImageView imageView = (ImageView) findViewById(R.id.image);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        }


        Eventitem eventitem1 = new Eventitem("https://www.hindustantimes.com/rf/image_size_640x362/HT/p2/2016/06/11/Pictures/chandigarh-wednesday-college-hindustan-students-college-others_1eced6c6-2f45-11e6-85eb-521f5a9851b5.JPG", "Event Yoga Day");

        Picasso.with(this)
                .load(eventitem1.getImageurl())
                .into(imageView);



    }


}
