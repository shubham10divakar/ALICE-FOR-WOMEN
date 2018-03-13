package com.example.subhamdivakar.alice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class ShakeService extends Service {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float accval;
    private float acclast;
    public float Sensitivity= MainActivity.shake;
    private static float shake;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(sensorListener, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        accval=SensorManager.GRAVITY_EARTH;
        acclast=SensorManager.GRAVITY_EARTH;
        shake=0.00f;
        Toast.makeText(getApplicationContext(),"shake service started",Toast.LENGTH_LONG).show();

        return super.onStartCommand(intent, flags, startId);
    }
    private final SensorEventListener sensorListener= new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            if(Sensitivity<=13) Sensitivity=14;
            acclast=accval;
            accval=(float) Math.sqrt((double) (x*x+y*y+z*z));
            float delta=accval-acclast;
            shake=shake*.9f + delta;
            if(shake>Sensitivity)
            {
                String s = String.valueOf(shake);
                String s1 = String.valueOf(delta);
                String s2 = String.valueOf(Sensitivity);
                Toast.makeText(getApplicationContext(),"value of shake is "+s+" and delta is "+s1+" with sensitivity "+s2,Toast.LENGTH_LONG).show();
                startService(new Intent(getApplication(),DataService.class));
                shake=0;
                stopSelf();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(),"shake service stops",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
