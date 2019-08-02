package com.map4d.vbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TestDistance extends AppCompatActivity {
    private Button submit;
    private TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_distance);

        distance = (TextView) findViewById(R.id.ketqua);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                double la1 = 16.0936668;
                double lo1 = 108.225361;
                double la2 = 15.9721906;
                double lo2 = 108.247422;

                double R = 6371e3;
                double dLat = (la2 - la1) * (Math.PI / 180);
                double dLon = (lo2 - lo1) * (Math.PI / 180);
                double la1ToRad = la1 * (Math.PI / 180);
                double la2ToRad = la2 * (Math.PI / 180);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(la1ToRad)
                        * Math.cos(la2ToRad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double kq = R * c;
                distance.setText("khoảng cách là: "+ kq);
            }
        });

//        HaversineInKM(16.095100,108.229880,16.094317,108.227273);

    }
//    public static double distanceBetween2Points(double la1, double lo1, double la2, double lo2) {
//        double dLat = (la2 - la1) * (Math.PI / 180);
//        double dLon = (lo2 - lo1) * (Math.PI / 180);
//        double la1ToRad = la1 * (Math.PI / 180);
//        double la2ToRad = la2 * (Math.PI / 180);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(la1ToRad)
//                * Math.cos(la2ToRad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double d = R * c;
//        return d;
//    }




}
