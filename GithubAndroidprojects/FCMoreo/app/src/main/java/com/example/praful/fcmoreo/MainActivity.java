package com.example.praful.fcmoreo;

import android.app.Notification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    NotificationHelper helper;
    Button send;
    EditText title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = (Button)findViewById(R.id.send);
        title = (EditText) findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.Builder builder = helper.getchannelNoti(title.getText().toString() , content.getText().toString());
                helper.getManager().notify(new Random().nextInt(),builder.build());
            }
        });
    }
}
