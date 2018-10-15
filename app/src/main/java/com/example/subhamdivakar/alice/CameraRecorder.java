
package com.example.subhamdivakar.alice;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class CameraRecorder extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = CameraRecorder.class.getSimpleName();

	public static SurfaceView mSurfaceView;
	public static SurfaceHolder mSurfaceHolder;
	public static Camera mCamera;
	public static boolean mPreviewRunning;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincamera);
		onWindowFocusChanged(false);
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		wakelock= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
//		wakelock.acquire();
		Button btnStart = (Button) findViewById(R.id.StartService);
		btnStart.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
//				Intent intent = new Intent(CameraRecorder.this, RecorderService.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startService(intent);
//				Intent intent1 = new Intent(CameraRecorder.this, PowerButtonService.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startService(intent1);
				finish();
			}
		});

		Button btnStop = (Button) findViewById(R.id.StopService);
		btnStop.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//stopService(new Intent(CameraRecorder.this, RecorderService.class));
			}
		});
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
