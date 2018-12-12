package com.example.subhamdivakar.alice;

/**
 * Created by Subham Divakar on 3/8/2018.
 */
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Random;


//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    public static final String FCM_PARAM = "picture";
//    private static final String CHANNEL_NAME = "FCM";
//    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
//    private int numMessages = 0;
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        Map<String, String> data = remoteMessage.getData();
//        Log.d("FROM", remoteMessage.getFrom());
//        sendNotification(notification, data);
//    }
//
//    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
//        Bundle bundle = new Bundle();
//        bundle.putString(FCM_PARAM, data.get(FCM_PARAM));
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtras(bundle);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
////            JSONObject data1 = json.getJSONObject("data");
////            //parsing json data
////            String title = data1.getString("title");
////            String message = data1.getString("message");
////            String imageUrl = data1.getString("image");
//
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
//                .setContentTitle(notification.getTitle())
//                .setContentText(notification.getBody())
//                .setAutoCancel(true)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
//                .setContentIntent(pendingIntent)
//                .setContentInfo("Hello")
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                .setColor(getColor(R.color.colorAccent))
//                .setLights(Color.RED, 1000, 300)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setNumber(++numMessages)
//                .setSmallIcon(R.drawable.ic_launcher_background);
//
//        try {
//            String picture = data.get(FCM_PARAM);
//            if (picture != null && !"".equals(picture)) {
//                URL url = new URL(picture);
//                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                notificationBuilder.setStyle(
//                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
//                );
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
//            );
//            channel.setDescription(CHANNEL_DESC);
//            channel.setShowBadge(true);
//            channel.canShowBadge();
//            channel.enableLights(true);
//            channel.setLightColor(Color.RED);
//            channel.enableVibration(true);
//            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
//
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        assert notificationManager != null;
//        notificationManager.notify(0, notificationBuilder.build());
//    }
//}




public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationUtils mNotificationUtils;
    private String CHANNEL_ID="1234";
    private  String CHANNEL_NAME="alice";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(message);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

            NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,CHANNEL_ID);
            notificationBuilder.setAutoCancel(false)
                    .setContentTitle(title)
                    .setContentInfo("Info")
                    .setContentText(message);


            notificationManager.notify(new Random().nextInt(),notificationBuilder.build());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            //if there is no image
//            if(imageUrl.equals("null")){
//                //displaying small notification
//                mNotificationManager.showSmallNotification(title, message, intent);
//
//
//                Notification.Builder nb = mNotificationUtils.
//                        getAndroidChannelNotification(title, "By " + "Good");
//
//                mNotificationUtils.getManager().notify(101, nb.build());
//
//
//            }else{
//                //if there is an image
//                //displaying a big notification
//                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
//
//                Notification.Builder nb = mNotificationUtils.
//                        getAndroidChannelNotification(title, "By " + "Good");
//
//                mNotificationUtils.getManager().notify(101, nb.build());
//
//            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}