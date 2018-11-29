package com.example.subhamdivakar.alice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.subhamdivakar.alice.Bean.ContactSaving;
import com.example.subhamdivakar.alice.UTILS.SqDB;

import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p1;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p2;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p3;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p4;
import static com.example.subhamdivakar.alice.UTILS.SqDB.contact_INFO_TABLE_COLUMN_p5;

/**
 * Created by Subham Divakar on 3/2/2018.
 */
public class MyReceiver extends BroadcastReceiver {
    private MediaPlayer player;
    private MyReceiver myReceiver;
    private static int countPowerOff = 0;
    static int ctr = 0;

    Context cntx;
    Vibrator vibe;
    long a, seconds_screenoff, OLD_TIME, seconds_screenon, actual_diff;
    boolean OFF_SCREEN, ON_SCREEN, sent_msg;


    @Override
    public void onReceive(final Context context, final Intent intent) {
        cntx = context;
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.v("onReceive", "Power button is pressed.");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            a = System.currentTimeMillis();
            seconds_screenoff = a;
            OLD_TIME = seconds_screenoff;
            OFF_SCREEN = true;

            new CountDownTimer(500, 200) {

                public void onTick(long millisUntilFinished) {


                    if (ON_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {

                            actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 2000) {
                                sent_msg = true;
                                if (sent_msg)
                                {
                                    Toast.makeText(cntx, " POWER BUTTON CLICKED 2 TIMES ", Toast.LENGTH_LONG).show();
                                    vibe.vibrate(100);
                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;
                                    sent_msg = false;
                                }
                            }
                            else
                            {
                                seconds_screenon = 0L;
                                seconds_screenoff = 0L;

                            }
                        }
                    }
                }

                public void onFinish() {

                    seconds_screenoff = 0L;
                }
            }.start();


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            a = System.currentTimeMillis();
            seconds_screenon = a;
            OLD_TIME = seconds_screenoff;

            new CountDownTimer(500, 200) {

                public void onTick(long millisUntilFinished) {
                    if (OFF_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {
                            actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 4000) {
                                sent_msg = true;
                                if (sent_msg) {

                                    Toast.makeText(cntx, "POWER BUTTON CLICKED 2 TIMES", Toast.LENGTH_LONG).show();

                                    Intent obj=new Intent(context,DataService.class);
                                    context.startService(obj);
                                    Intent obj1=new Intent(context,ShakeService.class);
                                    context.startService(obj1);

                                    vibe.vibrate(100);
                                    seconds_screenon = 0L;
                                    seconds_screenoff = 0L;
                                    sent_msg = false;


                                }
                            } else {
                                seconds_screenon = 0L;
                                seconds_screenoff = 0L;

                            }
                        }
                    }

                }

                public void onFinish() {

                    seconds_screenon = 0L;
                }
            }.start();


        }else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT))
            {
                Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");
                String phoneNo = "6200331655";
                String sms = "Someone has unlocked your phone";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, sms, null, null);

            }
    }

    private long cal_diff(long seconds_screenon2, long seconds_screenoff2) {
        long diffrence;
        if (seconds_screenon2 >= seconds_screenoff2) {
            diffrence = (seconds_screenon2) - (seconds_screenoff2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        } else {
            diffrence = (seconds_screenoff2) - (seconds_screenon2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        }

        return diffrence;
    }


//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//                ctr++;
//                if (ctr >= 3) {
//                    Intent obj=new Intent(context,DataService.class);
//                    context.startService(obj);
//                    ctr = 0;
//                }
//                Log.e("In on receive", "In Method:  ACTION_SCREEN_OFF");
//
//            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//                ctr++;
//                if (ctr >= 3) {
//                    Intent obj=new Intent(context,DataService.class);
//                    context.startService(obj);
////                    Intent obj1=new Intent(context,ShakeService.class);
////                    context.startService(obj1);
//                    ctr = 0;
//                }
//                Log.e("In on receive", "In Method:  ACTION_SCREEN_ON");
//            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT))
//            {
//                Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");
//                String phoneNo = "6200331655";
//                String sms = "Someone has unlocked your phone";
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(phoneNo, null, sms, null, null);
//
//            }
//        }
    }

