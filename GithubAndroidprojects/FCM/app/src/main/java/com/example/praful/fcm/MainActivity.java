package com.example.praful.fcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.praful.fcm.Model.Notification;
import com.example.praful.fcm.Model.Response;
import com.example.praful.fcm.Model.Sender;
import com.example.praful.fcm.Remote.APIService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    Button send;
    EditText  title , content;

    APIService apiService;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = Common.getFCMClient();
        send = (Button)findViewById(R.id.send);
        title = (EditText) findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);
        Common.currentToken = FirebaseInstanceId.getInstance().getToken();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification =new Notification(title.getText().toString() ,content.getText().toString());

                Sender sender = new Sender(Common.currentToken, notification);
                apiService.sendNotification(sender)
                        .enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                if (response.body().success == 1){
                                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {

                            }
                        });
            }
        });
    }
}
