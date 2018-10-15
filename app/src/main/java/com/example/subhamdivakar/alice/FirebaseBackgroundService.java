package com.example.subhamdivakar.alice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseBackgroundService extends Service {

    StorageReference storageReference;
    private Uri filePath;
    private MediaPlayer player;
    public void OnCreate(){
        super.onCreate();
        Log.w("TAG", "ScreenListenerService---OnCreate ");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // String userID = intent.getStringExtra("UserID");
        filePath= Uri.parse("/storage/emulated/0/video.mp4");
        Uri.fromFile(new File("/storage/emulated/0/video.mp4"));
        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(true);

        //staring the player
        player.start();
        Log.w("TAG", "ScreenListenerService---onStart ");
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            //final ProgressDialog progressDialog = new ProgressDialog(this);
            //progressDialog.setTitle("Uploading");
            //progressDialog.show();

            // Create a storage reference from our app
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storageReference= storage.getReference();

            StorageReference riversRef = storageReference.child("Video/video.mp4");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            //progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            stopService(new Intent(getBaseContext(),FirebaseBackgroundService.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            //progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            stopService(new Intent(getBaseContext(),FirebaseBackgroundService.class));
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            //progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }

        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
