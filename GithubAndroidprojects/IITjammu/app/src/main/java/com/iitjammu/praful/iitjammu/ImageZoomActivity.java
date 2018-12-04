package com.iitjammu.praful.iitjammu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Picasso;

public class ImageZoomActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar mToolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView imageView = (ImageView) findViewById(R.id.img);
        imageView.setOnTouchListener(new ImageMatrixTouchHandler(this));

        String imageurl = getIntent().getStringExtra("imageurl");
        if (imageurl != null) {
            Picasso.with(this).load(imageurl).into(imageView);

        }

    }
}
