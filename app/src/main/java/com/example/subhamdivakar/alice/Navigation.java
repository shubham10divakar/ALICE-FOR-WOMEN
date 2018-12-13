package com.example.subhamdivakar.alice;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.subhamdivakar.alice.Bean.ContactSaving;
import com.example.subhamdivakar.alice.UTILS.SqDB;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MyReceiver myReceiver;
    private static final int CREDENTIALS_RESULT = 4342; //just make sure it's unique within your activity.
    public boolean verify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        ImageView img;
        img=findViewById(R.id.women);
        Glide.with(Navigation.this).load("file:///android_asset/girl2.gif").asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(img);


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, filter);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);//prevent phone from being locked

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fm=Navigation.this.getSupportFragmentManager();
//        fm.beginTransaction().replace(R.id.container,new BlankFragmentUserDetails()).commit();
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if(!hasFocus) {
//            // Close every kind of system dialog
//            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            sendBroadcast(closeDialog);
//            Toast.makeText(Navigation.this, "Your LongPress Power Button", Toast.LENGTH_SHORT).show();
//            checkCredentials();
//        }
//    }


//    void checkCredentials() {
//        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//
//        Intent credentialsIntent = keyguardManager.createConfirmDeviceCredentialIntent("Password required", "please enter your pattern to receive your token");
//        if (credentialsIntent != null) {
//            startActivityForResult(credentialsIntent, CREDENTIALS_RESULT);
//        } else {
//            //no password needed
//            Toast.makeText(this, "Please create a password for device", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK && requestCode==CREDENTIALS_RESULT)
//        {
//            Toast.makeText(this, "Success: Verified user's identity", Toast.LENGTH_SHORT).show();
//            verify=true;
//
//        }
//        else if(resultCode == RESULT_CANCELED && requestCode == CREDENTIALS_RESULT)
//        {
////            finish();
//            checkCredentials();
//        }
//        else
//        {
//            Toast.makeText(this, "Failure: Unable to verify user's identity", Toast.LENGTH_SHORT).show();
//
//        }
//    }


    @Override
    protected void onDestroy()
    {
        if (myReceiver != null)
        {
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
//        if(verify)
//        {
//            finish();
//        }
//        else
//        {
//            checkCredentials();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (id == R.id.action_settings) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                return true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.motion) {
            // Handle the camera action
            Intent obj=new Intent(this,SpyCamera.class);
            startActivity(obj);
        } else if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "BUS APP");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        }
        else if(id==R.id.gps){
            Intent obj=new Intent(this,GPSTracking.class);
            startActivity(obj);
        }
        else if(id==R.id.sms){
            SqDB database = new SqDB(this);
            ContactSaving obj = database.getContact();
            if (obj != null) {
                Intent obj1 = new Intent(this, SMSsending.class);
                startActivity(obj1);
            } else {
                Toast.makeText(Navigation.this, "Contacts not found.Please add contacts", Toast.LENGTH_LONG).show();
                Intent obj1 = new Intent(this, Contacts.class);
                startActivity(obj1);
            }
        }
        else if(id==R.id.contact){
            SqDB database = new SqDB(this);
            ContactSaving obj = database.getContact();
            if (obj != null) {

                Toast.makeText(Navigation.this, "Contacts already stored", Toast.LENGTH_LONG).show();
            } else {
                Intent obj1 = new Intent(this, Contacts.class);
                startActivity(obj1);
            }
        }
        else if(id==R.id.police){
            Intent obj=new Intent(this,GPSTracking.class);
            startActivity(obj);
        }
        else if(id==R.id.theft){
            Intent obj=new Intent(this,GPSTracking.class);
            startActivity(obj);
        }
        else if(id==R.id.health){
            Intent obj=new Intent(this,pedometer.class);
            startActivity(obj);
        }
        else if(id==R.id.immergency){
            Intent obj=new Intent(this,GPSTracking.class);
            startActivity(obj);
        }
        else if(id==R.id.erase){
            Intent obj=new Intent(this,Contacts.class);
            startActivity(obj);
        }
        else if(id==R.id.note){
            //SqDB database = new SqDB(this);
            Intent obj=new Intent(this,DisplayToken.class);
            startActivity(obj);
        }
        else if(id==R.id.display){
            //SqDB database = new SqDB(this);
            Intent obj=new Intent(this,DisplaySavedContacts.class);
            startActivity(obj);
        }
        else if(id==R.id.email){
            //SqDB database = new SqDB(this);
            Intent obj=new Intent(this,Email.class);
            startActivity(obj);
        }
        else if(id==R.id.record){
            //SqDB database = new SqDB(this);
            Intent obj=new Intent(this,AudioRecording.class);
            startActivity(obj);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        SqDB obj = new SqDB(this);
        String p1 = String.valueOf(obj.getContact().phone1);
        smsIntent.putExtra("address", new String(p1));
        smsIntent.putExtra("sms_body", "Hello");
        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Navigation.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
