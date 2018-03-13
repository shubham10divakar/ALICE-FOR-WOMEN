package com.example.subhamdivakar.alice;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.subhamdivakar.alice.UTILS.SqDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();

        findViewById(R.id.microphoneButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listen();
            }
        });
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
                speak("My name is ALICE.");
            speak("i have been created by subham divakar.");
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
        if(text.contains("swipe")||text.contains("hello"))
        {

            Intent obj=new Intent(this,Main2Activity.class);
            startActivity(obj);
        }
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
    }
    public void startService(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
        startService(new Intent(getBaseContext(), ShakeService.class));
    }
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

}
