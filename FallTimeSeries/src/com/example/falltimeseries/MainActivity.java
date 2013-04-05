package com.example.falltimeseries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends IntentService implements SensorEventListener {

	public MainActivity(String name) {
		super(name);
	}

	public static final int SMOOTHING_WINDOW = 5;
	public static final int WINDOW_SIZE = 10;
	public static final int DATA_STREAM_SIZE = 100;
	public static final int COMMIT_DATA_THRESHOLD = (SMOOTHING_WINDOW * 2)
			+ (WINDOW_SIZE * 2) + DATA_STREAM_SIZE;

	private SensorManager mSensorManager;
	private Sensor mAccSensor;
	private TextView mTextViewAccelerationX, mTextViewAccelerationY,
			mTextViewAccelerationZ, mTextViewSteps;
	private List<Float> mVectorLengths;
	public List<Long> mTimeStamps, mSteps;
	public double mMean, mStd;
	public boolean meanStdSet = false;

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
			mTextViewAccelerationX.setText("X: "
					+ String.valueOf(event.values[0]));
			mTextViewAccelerationY.setText("Y: "
					+ String.valueOf(event.values[1]));
			mTextViewAccelerationZ.setText("Z: "
					+ String.valueOf(event.values[2]));
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

	public void update() {
		mTextViewSteps.setText("Steps: " + String.valueOf(mSteps.size()));
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
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
			System.out.println("Did not find sensor!");
			cleanUp(null);
		}

		mSensorManager.registerListener(this, mAccSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

}
