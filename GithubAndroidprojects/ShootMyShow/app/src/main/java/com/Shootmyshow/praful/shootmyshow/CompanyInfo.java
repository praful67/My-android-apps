package com.Shootmyshow.praful.shootmyshow;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CompanyInfo extends AppCompatActivity {

    String companyid;
    TextView comId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getIntent() !=null){
            companyid =getIntent().getStringExtra("companyid");
        }
        comId = (TextView)findViewById(R.id.comid);
        comId.setText(companyid);
    }
}
