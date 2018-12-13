package com.example.subhamdivakar.alice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class SpyCamera extends AppCompatActivity implements SurfaceHolder.Callback {


    public static SurfaceView mSurfaceView;
    public static SurfaceHolder mSurfaceHolder;
    public static Camera mCamera;
    public static boolean mPreviewRunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spy_camera);

        onWindowFocusChanged(false);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView2);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(SpyCamera.this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        ImageView img;
        img=findViewById(R.id.main);
        Glide.with(SpyCamera.this).load("file:///android_asset/spy3.gif").asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(img);


    }
    public void begin(View view)
    {
        if(isMyServiceRunning(BackCameraRecorderService2.class)){
            Toast.makeText(this, "Recording already in progress", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
            startService(new Intent(getBaseContext(), BackCameraRecorderService2.class));
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {//function for closing the power dialog box
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void stopRecording(View view)
    {
        if(isMyServiceRunning(BackCameraRecorderService2.class))
        {
            stopService(new Intent(getBaseContext(),BackCameraRecorderService2.class));
        }
        else
        {
            Toast.makeText(this, "Start recording first", Toast.LENGTH_SHORT).show();
        }

    }
}
