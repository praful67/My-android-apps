package com.example.praful.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    Button bt1 ,bt2;
    TextView mytext, mytext2;
    ToggleButton tg;
    EditText edit;
    SeekBar seekBar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt1 = (Button) findViewById(R.id.button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MainActivity.this , SecondActivity.class);
                startActivity(myintent);
            }
        });

        tg =(ToggleButton) findViewById(R.id.toggleButton);
        tg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity.this,"Turned ON" ,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"Turned OFF" ,Toast.LENGTH_SHORT).show();

                }
            }
        });

        bt2 = (Button) findViewById(R.id.button4);
        mytext = (TextView) findViewById(R.id.textView3);
        mytext2 = (TextView) findViewById(R.id.textView4);
        edit = (EditText) findViewById(R.id.editText);

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mystring = edit.getText().toString();
                String mystring0 = "YOUR NAME IS ";
                mytext.setText(mystring);
                mytext2.setText(mystring0);

            }
        });

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        seekBar.setMax(100);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





    }
}
