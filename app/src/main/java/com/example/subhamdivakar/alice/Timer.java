package com.example.subhamdivakar.alice;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

public class Timer extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        new java.util.Timer().schedule(
//                new java.util.TimerTask() {
//                    @Override
//                    public void run() {
//                        // your code here
//
//                    }
//                },
//                5000
//        );
        startService(new Intent(getBaseContext(), BackCameraRecorderService.class));
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            Handler handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    Toast.makeText(Timer.this, "Message from Service", Toast.LENGTH_LONG).show();
                    stopService(new Intent(getBaseContext(), BackCameraRecorderService.class));
                    //startRecording();
                    startService(new Intent(getBaseContext(), BackCameraRecorderService.class));
                }
            };
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 15000, 20000);
        //stopService(new Intent(getBaseContext(), BackCameraRecorderService.class));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
