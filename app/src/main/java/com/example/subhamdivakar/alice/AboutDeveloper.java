package com.example.subhamdivakar.alice;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class AboutDeveloper extends AppCompatActivity {

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        img=findViewById(R.id.backdrop);
        EditText txt12,q,w,e,r,t;
        txt12=findViewById(R.id.reg12);
        txt12.setEnabled(false);
        q=findViewById(R.id.reg21);
        q.setEnabled(false);
        w=findViewById(R.id.reg32);
        w.setEnabled(false);
        e=findViewById(R.id.reg42);
        e.setEnabled(false);
        r=findViewById(R.id.reg52);
        r.setEnabled(false);
        initCollapsingToolbar();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Developer");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the txtPostTitle when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Women Security App");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

        // loading toolbar header image
        Glide.with(getApplicationContext()).load("https://api.androidhive.info/webview/nougat.jpg")
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
