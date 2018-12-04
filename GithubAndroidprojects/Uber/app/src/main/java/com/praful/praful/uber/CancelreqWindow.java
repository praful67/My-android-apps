package com.praful.praful.uber;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class CancelreqWindow extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelreq_window);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        button = (Button)findViewById(R.id.btnDone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();
            }
        });

    }
}
