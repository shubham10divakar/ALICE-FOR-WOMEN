package com.example.subhamdivakar.alice;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GPSTracking  extends Activity {

    Button btnShowLocation;

    // GPSTracker class
    GPSTracker gps;
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpstracking);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(GPSTracking.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();



                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });
    }
    public void Start(View view) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            Handler handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    //Toast.makeText(GPSTracking.this, "Message from Service", Toast.LENGTH_LONG).show();
                    gps = new GPSTracker(GPSTracking.this);

                    // check if GPS enabled
                    if(gps.canGetLocation()){

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        Toast.makeText(getApplicationContext(), "GPS or Network is not enabled", Toast.LENGTH_LONG).show();
                        gps.showSettingsAlert();
                    }
                }
            };
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, 10000);
    }
}