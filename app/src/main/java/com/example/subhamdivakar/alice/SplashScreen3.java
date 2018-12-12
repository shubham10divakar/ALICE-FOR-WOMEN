package com.example.subhamdivakar.alice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class SplashScreen3 extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen3);

        ImageView img,img2;
        img=findViewById(R.id.gif1);
//        img2=findViewById(R.id.gif2);
        Glide.with(SplashScreen3.this).load("file:///android_asset/lady.gif").asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(img);
        //Glide.with(SplashScreen3.this).load("file:///android_asset/lady.gif").asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(img2);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen3.this,Help.class);
                startActivity(i);
//                startService(new Intent(getBaseContext(), MyService.class));

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
