

package com.example.subhamdivakar.alice;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackCameraRecorderService extends Service {
	private static final String TAG = "RecorderService";
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	public static Camera mCamera;
	private static Camera mServiceCamera;
	private boolean mRecordingStatus;
	private MediaRecorder mMediaRecorder;
	Uri filepath;
	String path="/video.mp4";

	@Override
	public void onCreate() {
		mRecordingStatus = false;
		mServiceCamera = mCamera;
//		mSurfaceView = CameraRecorder.mSurfaceView;
//		mSurfaceHolder = CameraRecorder.mSurfaceHolder;
		//onWindowFocusChanged(false);
//		Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//		sendBroadcast(closeDialog);
		super.onCreate();
	}


//	public void onWindowFocusChanged(boolean hasFocus) {
//		//super.onWindowFocusChanged(hasFocus);
//		if(!hasFocus) {
//			Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//			sendBroadcast(closeDialog);
//		}
//	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		if (mRecordingStatus == false)
			startRecording();

		//startRecording();
//		java.util.Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new TimerTask() {
//
//			Handler handler = new Handler() {
//				@Override
//				public void dispatchMessage(Message msg) {
//					super.dispatchMessage(msg);
//					//Toast.makeText(GPSTracking.this, "Message from Service", Toast.LENGTH_LONG).show();
//					//startRecording();
//					stopRecording();
//					path="/video1.mp4";
//					startRecording();
//				}
//			};
//			@Override
//			public void run() {
//				handler.sendEmptyMessage(0);
//			}
//		}, 10000, 1000000000);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		stopRecording();
		mRecordingStatus = false;
		super.onDestroy();
	}   

	public boolean startRecording(){
		try {
			Toast.makeText(getBaseContext(), "Recording Started", Toast.LENGTH_SHORT).show();
			
			mServiceCamera = Camera.open();
			Camera.Parameters params = mServiceCamera.getParameters();
			mServiceCamera.setParameters(params);
			Camera.Parameters p = mServiceCamera.getParameters();
			
			final List<Size> listPreviewSize = p.getSupportedPreviewSizes();
			for (Size size : listPreviewSize) {
                Log.i(TAG, String.format("Supported Preview Size (%d, %d)", size.width, size.height));
            }

            Size previewSize = listPreviewSize.get(0);
			p.setPreviewSize(previewSize.width, previewSize.height);
			mServiceCamera.setParameters(p);

			try {
				mServiceCamera.setPreviewDisplay(mSurfaceHolder);
				mServiceCamera.startPreview();
			}
			catch (IOException e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
			
			mServiceCamera.unlock();

			mMediaRecorder = new MediaRecorder();
			mMediaRecorder.setCamera(mServiceCamera);
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
			//path="/video.mp4";
			mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + path);
			//mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
			filepath= Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/video.mp4");
			mMediaRecorder.prepare();
			mMediaRecorder.start();
//"/storage/emulated/0/video.mp4"
			mRecordingStatus = true;
			
			return true;

		} catch (IllegalStateException e) {
//			Log.d(TAG, e.getMessage());
//			e.printStackTrace();
			return false;

		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}

		// your code here
	}

	public void stopRecording() {
		Toast.makeText(getBaseContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
		Intent serviceIntent = new Intent(getBaseContext(),FirebaseBackgroundService.class);
		serviceIntent.putExtra("UserID", filepath.toString());
		getBaseContext().startService(serviceIntent);
		try {
			mServiceCamera.reconnect();

		} catch (IOException e) {
			e.printStackTrace();
		}

		mMediaRecorder.stop();
		mMediaRecorder.reset();
		
		mServiceCamera.stopPreview();
		mMediaRecorder.release();
		
		mServiceCamera.release();
		mServiceCamera = null;
//		Intent serviceIntent = new Intent(getBaseContext(),FirebaseBackgroundService.class);
//		serviceIntent.putExtra("UserID", filepath.toString());
//		getBaseContext().startService(serviceIntent);
	}

}
