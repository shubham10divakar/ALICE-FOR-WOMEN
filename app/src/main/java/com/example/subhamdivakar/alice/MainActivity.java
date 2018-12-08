package com.example.subhamdivakar.alice;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.subhamdivakar.alice.Bean.ContactSaving;
import com.example.subhamdivakar.alice.UTILS.SqDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p1;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p2;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p3;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p4;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p5;

import com.example.subhamdivakar.alice.mylocation.MainGeoActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{


    String a,b,c,d,e;
    int a1,b1,c1,d1,e1;
    int ctr=0;
    int open=0;
    private TextToSpeech tts;
    private ArrayList<String> questions;
    private String name, surname, age, asName;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String NEW = "new";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String AS_NAME = "as_name";
    public static float shake =0;

    private static final String TAG = CameraRecorder.class.getSimpleName();

    public static SurfaceView mSurfaceView;
    public static SurfaceHolder mSurfaceHolder;
    public static Camera mCamera;
    public static boolean mPreviewRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onWindowFocusChanged(false);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();

        requestStoragePermission();
        requestCameraPermission();

        findViewById(R.id.microphoneButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listen();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "id_product";
            // The user-visible name of the channel.
            CharSequence name = "Product";
            // The user-visible description of the channel.
            String description = "Notifications regarding our products";
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);
        }


        loadQuestions();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak("Hello.I am ALICE.");
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

    }

    private void loadQuestions(){
        questions = new ArrayList<>();
        questions.clear();
        questions.add("Hello, what is your name?");
        questions.add("What is your surname?");
        questions.add("How old are you?");
        questions.add("That's all I had, thank you ");
    }

    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);

            }
        }
    }

    private void recognition(String text){
        Log.e("Speech",""+text);
        String[] speech = text.split(" ");
        if(text.contains("say hello")){
            speak(questions.get(0));
        }
        //
        if(text.contains("my name is")){
            name = speech[speech.length-1];
            Log.e("THIS", "" + name);
            editor.putString(NAME,name).apply();
            speak(questions.get(2));
        }
        //This must be the age
        if(text.contains("years") && text.contains("old")){
            String age = speech[speech.length-3];
            Log.e("THIS", "" + age);
            editor.putString(AGE, age).apply();
        }

        if(text.contains("what time is it")){
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
            Date now = new Date();
            String[] strDate = sdfDate.format(now).split(":");
            if(strDate[1].contains("00"))
                strDate[1] = "o'clock";
            speak("The time is " + sdfDate.format(now));

        }

        if(text.contains("wake me up at")){
            speak(speech[speech.length-1]);
            String[] time = speech[speech.length-1].split(":");
            String hour = time[0];
            String minutes = time[1];
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_HOUR, Integer.valueOf(hour));
            i.putExtra(AlarmClock.EXTRA_MINUTES, Integer.valueOf(minutes));
            startActivity(i);
            speak("Setting alarm to ring at " + hour + ":" + minutes);
        }

        if(text.contains("thank you")){
            //speak("Thank you too " + preferences.getString(NAME, null));
//            try {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_SUBJECT, "BUS APP");
//                String sAux = "\nLet me recommend you this application\n\n";
//                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
//                i.putExtra(Intent.EXTRA_TEXT, sAux);
//                startActivity(Intent.createChooser(i, "Choose one"));
//            } catch(Exception e) {
//                //e.toString();
//            }
            speak("Thank you too " + preferences.getString(NAME, null));
        }

        if(text.contains("how old am I")){
            speak("You are "+preferences.getString(AGE,null)+" years old.");
        }

        if(text.contains("what is your name")||text.contains("your name")){
            String as_name = preferences.getString(AS_NAME,"");
            if(as_name.equals(""))
                speak("How do you want to call me?");
            else
                speak("");
            speak("My name is ALICE ");
        }

        if(text.contains("call you")){
            String name = speech[speech.length-1];
            editor.putString(AS_NAME,name).apply();
            speak("I like it, thank you "+preferences.getString(NAME,null));
        }

        if(text.contains("what is my name")){
            speak("Your name is "+preferences.getString(NAME,null));
        }
        if(text.contains("share"))
        {
            speak("Choose from the options to share");
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "CLUEDO");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose one"));
            } catch(Exception e) {
                //e.toString();

            }
        }
        if(text.contains("Create new code")||text.contains("add new code")||text.contains("new code")||text.contains("write new code"))
        {
            speak("Now you need to speak the body inside the main part of your code one line at a time. at last we will show you the code.");
            Intent obj=new Intent(this,MainSpeechToTextActivity.class);
            startActivity(obj);
        }
        if(text.contains("Camera")||text.contains("photo")||text.contains("picture")||text.contains("snapshot"))
        {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        }
        if(text.contains("facebook")||text.contains("book")||text.contains("facebook app"))
        {
            Intent intent = new Intent("android.intent.category.LAUNCHER");
            String facebookPackageName = "com.facebook.katana";
            String facebookClassName = "com.facebook.katana.LoginActivity";
            intent.setClassName(facebookPackageName, facebookClassName);
            startActivity(intent);
            speak("Sir your facebook account is opened");
        }
        if(text.contains("whatsapp")||text.contains("app"))
        {
            Intent i = new Intent(Intent.ACTION_MAIN);
            PackageManager managerclock = getPackageManager();
            i = managerclock.getLaunchIntentForPackage("com.whatsapp");
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        }
        if(text.contains("call mummy"))
        {
            String call="tel:8864044703";
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            //callIntent.setData(Uri.parse("tel:0377778888"));
            callIntent.setData(Uri.parse(call));
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
        if(text.contains("contacts"))
        {
//            Intent obj=new Intent(this,ContactPicker.class);
//            startActivity(obj);
        }
        if(text.contains("call papa"))
        {
            String call="tel:9430113524";
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            //callIntent.setData(Uri.parse("tel:0377778888"));
            callIntent.setData(Uri.parse(call));
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
//        if(text.contains("swipe")||text.contains("hello"))
//        {
//
//            Intent obj=new Intent(this,Main2Activity.class);
//            startActivity(obj);
//        }
        if(text.contains("step")||text.contains("count"))
        {

            Intent obj=new Intent(this,pedometer.class);
            startActivity(obj);
        }
//        if(text.contains("help"))
//        {
//            Intent obj=new Intent(this,GPSTracking.class);
//            startActivity(obj);
//        }
        if(text.contains("help"))
        {
            speak("Hi,I am alice.I will guide you to set up some basic things and will tell you some features of this app.");
            Intent obj=new Intent(this,SplashScreen3.class);
            startActivity(obj);
        }
        if(text.contains("introduce")||text.contains("tell about yourself"))
        {
            speak("Good morning everyone. I am ALICE and i am a chatbot developed for the safety of women.   I will alert the contacts stored by you to alert them   and the police that you are in danger. I will also alert the users of the application within a radius of 5 kilometres."
            );
        }
        if(text.contains("activate")||text.contains("shield on"))
        {
            if(isMyServiceRunning(MyService.class)||isMyServiceRunning(ShakeService.class)||isMyServiceRunning(BackCameraRecorderService.class)) {
                speak(" Shield are already activated ");
            }
            else
            {
                speak(" Shields are switched on ");
                startService(new Intent(getBaseContext(), MyService.class));
                startService(new Intent(getBaseContext(), ShakeService.class));
            }
        }
        if(text.contains("stop"))
        {
            if(isMyServiceRunning(MyService.class)||isMyServiceRunning(ShakeService.class)||isMyServiceRunning(BackCameraRecorderService.class)) {
                speak("Shield are switched off");
                stopService(new Intent(getBaseContext(), MyService.class));
                stopService(new Intent(getBaseContext(), ShakeService.class));
                //stopService(new Intent(getBaseContext(), Timer.class));
            }
            else
            {
                speak(" Shields are not activated. Switch them on first");
            }
        }
        if(text.contains("geofence")) {
            Intent obj=new Intent(this,MainGeoActivity.class);
            startActivity(obj);
        }
    }
    public void startService(View view) {

        if(isMyServiceRunning(MyService.class)||isMyServiceRunning(ShakeService.class)||isMyServiceRunning(BackCameraRecorderService.class)) {
            Toast.makeText(MainActivity.this, "Shield is already on", Toast.LENGTH_SHORT).show();
        }
        else {
            SqDB obj=new SqDB(this);
            ContactSaving obj1=obj.getContact();
            if(obj1!=null) {
                Cursor res = obj.getAllData();
                if (res.moveToNext()) {
                    String ph1 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p1)));
                    String ph2 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p2)));
                    String ph3 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p3)));
                    String ph4 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p4)));
                    String ph5 = String.valueOf(res.getString(res.getColumnIndex(contact_INFO_TABLE_COLUMN_p5)));
                    if (ph1 == null) {
                        Toast.makeText(this, "Please store contacts first", Toast.LENGTH_SHORT).show();
                        Intent obj23 = new Intent(MainActivity.this, Contacts.class);
                        startActivity(obj23);
                    }
                }
                startService(new Intent(getBaseContext(), MyService.class));
                startService(new Intent(getBaseContext(), ShakeService.class));
                startService(new Intent(getBaseContext(), BackCameraRecorderService.class));
                //startService(new Intent(getBaseContext(), Timer.class));
            }
            else{
                speak("Please store contacts first");
                Toast.makeText(this, "Please store contacts first", Toast.LENGTH_SHORT).show();
                Intent obj23 = new Intent(MainActivity.this, Contacts.class);
                startActivity(obj23);
            }
        }
    }
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
        stopService(new Intent(getBaseContext(), ShakeService.class));
        stopService(new Intent(getBaseContext(),BackCameraRecorderService.class));
//       Uri filepath= Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/video.mp4");
//        Intent serviceIntent = new Intent(getBaseContext(),FirebaseBackgroundService.class);
//        serviceIntent.putExtra("UserID", filepath.toString());
//        getBaseContext().startService(serviceIntent);
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


    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.SYSTEM_ALERT_WINDOW)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Requesting camera permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        //openCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void saveContacts(View view)
    {
        Intent obj=new Intent(MainActivity.this,Contacts.class);
        startActivity(obj);
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

}
