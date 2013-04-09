package com.example.falltimeseries;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class StepMainService extends Service implements SensorEventListener {

	public final static int myID = 13377331;
	public static final String tag = "no.ntnu.stud.valens.stepcounter";

	public static final int SMOOTHING_WINDOW = 5;
	public static final int WINDOW_SIZE = 10;
	public static final int DATA_STREAM_SIZE = 100;
	public static final int COMMIT_DATA_THRESHOLD = (SMOOTHING_WINDOW * 2)
			+ (WINDOW_SIZE * 2) + DATA_STREAM_SIZE;

	private SensorManager mSensorManager;
	private Sensor mAccSensor;
//	private TextView mTextViewAccelerationX, mTextViewAccelerationY,
//			mTextViewAccelerationZ, mTextViewSteps;
	private List<Float> mVectorLengths;
	public List<Long> mTimeStamps, mSteps;
	public double mMean, mStd;
	public boolean meanStdSet = false;

	
	public void onCreate() {
		Log.v("Service", "OnCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("Service","StartCommand");
		
		if (!intent.getBooleanExtra("stop", false)) {
			// Initialize the service
			//The intent to launch when the user clicks the expanded notification
			Intent intento = new Intent(this, LaunchActivity.class);
			intento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intento, 0);
	
			//This constructor is deprecated. Use Notification.Builder instead
			Notification notice = new Notification(R.drawable.ic_launcher, "Ticker text", System.currentTimeMillis());
	
			//This method is deprecated. Use Notification.Builder instead.
			notice.setLatestEventInfo(this, "Title text", "Content text", pendIntent);
	
			notice.flags |= Notification.FLAG_NO_CLEAR;
			startForeground(myID, notice);
			
			// Initialize detection part of the service
			// Initialize lists
			mVectorLengths = new ArrayList<Float>();
			mTimeStamps = new ArrayList<Long>();
			mSteps = new ArrayList<Long>();

			// Register sensor for orientation events
			mSensorManager = (SensorManager) getApplicationContext()
					.getSystemService(Context.SENSOR_SERVICE);
			for (Sensor sensor : mSensorManager
					.getSensorList(Sensor.TYPE_ACCELEROMETER)) {
				if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					mAccSensor = sensor;
				}
			}

			// Check that we found a sensor object
			if (mAccSensor == null) {
				Log.v("Service","Did not find sensor!");
				cleanUp(null);
			}

			mSensorManager.registerListener(this, mAccSensor,
					SensorManager.SENSOR_DELAY_GAME);
			
			return Service.START_NOT_STICKY;
		} else {
			stopForeground(true);
			cleanUp(null);
			return Service.START_NOT_STICKY;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void cleanUp(View view) {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// We don't really care about this
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			Long timeStamp = System.currentTimeMillis();
			// GUI
//			mTextViewAccelerationX.setText("X: "
//					+ String.valueOf(event.values[0]));
//			mTextViewAccelerationY.setText("Y: "
//					+ String.valueOf(event.values[1]));
//			mTextViewAccelerationZ.setText("Z: "
//					+ String.valueOf(event.values[2]));
			// Print CSV
			mVectorLengths.add(calculateVectorLength(event.values));
			mTimeStamps.add(timeStamp);
			if (mVectorLengths.size() >= COMMIT_DATA_THRESHOLD) {
				DetectStepsThread ds = new DetectStepsThread(
						new ArrayList<Float>(mVectorLengths),
						new ArrayList<Long>(mTimeStamps), this);
				ds.run();
				discardData();
			}
		}
	}

	private void discardData() {
		List<Long> newTimeStamps = new ArrayList<Long>();
		List<Float> newVectorLengths = new ArrayList<Float>();
		for (int i = mTimeStamps.size() - (WINDOW_SIZE * 2); i < mTimeStamps
				.size(); i++) {
			newTimeStamps.add(mTimeStamps.get(i));
			newVectorLengths.add(mVectorLengths.get(i));
		}
		mTimeStamps = newTimeStamps;
		mVectorLengths = newVectorLengths;
	}

	private float calculateVectorLength(float[] vector) {
		float sum = ((vector[0] * vector[0]) + (vector[1] * vector[1]) + (vector[2] * vector[2]));
		return (float) Math.sqrt((double) sum);
	}

//	public void update() {
//		mTextViewSteps.setText("Steps: " + String.valueOf(mSteps.size()));
//	}
}
