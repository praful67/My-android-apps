package com.example.praful.home;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.bounce);
        layout.setAnimation(animation);
        Button bt = (Button) findViewById(R.id.button);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , Cards.class);
                startActivity(intent);
            }
        });
    }
}
