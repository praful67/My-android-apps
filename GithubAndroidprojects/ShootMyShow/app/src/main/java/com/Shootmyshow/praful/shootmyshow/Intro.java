package com.Shootmyshow.praful.shootmyshow;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tomer.fadingtextview.FadingTextView;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().getAttributes().windowAnimations = R.style.Dialogslide1;
        final FadingTextView fadingTextView = (FadingTextView)findViewById(R.id.fadingTextview);
        final Button skip = (Button)findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro.this , Home.class);
                intent.putExtra("first" , "first");
                startActivity(intent);
                finish();
            }
        });
        String[] texts = {"HELLO" , "Soon having some event ? " , "Wanna book a camera ? " };
        final String [] texts1 = {"","Set a Pick up address" , "Set Time" , "Set Date" ,"And Enjoy your shoot"};
        fadingTextView.setTexts(texts);
        fadingTextView.setTimeout(1500 , TimeUnit.MILLISECONDS);
       new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                fadingTextView.setTexts(texts1);
                fadingTextView.setTimeout(500 , TimeUnit.MILLISECONDS);

            }
        }.start();

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                fadingTextView.stop();

                skip.setText("NEXT");
            }
        }.start();

    }
}
