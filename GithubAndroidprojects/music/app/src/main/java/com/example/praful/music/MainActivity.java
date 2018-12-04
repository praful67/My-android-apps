package com.example.praful.music;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ToggleButton bt = (ToggleButton) findViewById(R.id.toggleButton);

        final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this , R.raw.music);

      bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(bt.isChecked()){
                  mediaPlayer.start();
              }else
              {
                  mediaPlayer.pause();
              }
          }
      });


    }
}
