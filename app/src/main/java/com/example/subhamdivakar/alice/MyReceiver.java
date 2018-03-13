package com.example.subhamdivakar.alice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.util.Log;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                ctr++;
                if (ctr >= 3) {
                    Intent obj=new Intent(context,DataService.class);
                    context.startService(obj);
                    ctr = 0;
                }
                Log.e("In on receive", "In Method:  ACTION_SCREEN_OFF");

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                ctr++;
                if (ctr >= 3) {
                    Intent obj=new Intent(context,DataService.class);
                    context.startService(obj);
//                    Intent obj1=new Intent(context,ShakeService.class);
//                    context.startService(obj1);
                    ctr = 0;
                }
                Log.e("In on receive", "In Method:  ACTION_SCREEN_ON");
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");
                String phoneNo = "6200331655";
                String sms = "Someone has unlocked your phone";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, sms, null, null);

            }
        }
    }

