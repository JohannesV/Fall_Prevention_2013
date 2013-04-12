package com.example.falltimeseries;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CalibrationThread implements Runnable, SensorEventListener {

	private List<Float> mVectorLengths;
	private SensorManager mSensorManager;
	private Sensor mAccSensor;
	private CalibrationActivity context;
	private Long startTime;

	public CalibrationThread(CalibrationActivity context) {
		this.context = context;
		mVectorLengths = new ArrayList<Float>();
	}

	@Override
	public void run() {
		// Find and register sensor
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		for (Sensor sensor : mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER)) {
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				mAccSensor = sensor;
			}
		}

		mSensorManager.registerListener(this, mAccSensor,
				SensorManager.SENSOR_DELAY_GAME);
		// Find the start time of calibration calculation
		startTime = System.currentTimeMillis();
	}

	// We don't really care about this, it just has to be implemented because of
	// the SensorListener-interface
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		mVectorLengths.add(Methods.calculateVectorLength(event.values));
		// If calibration has run for a long enough time, stop gathering more data
		if (System.currentTimeMillis() - startTime > Values.CAL_TIME) {
			finish();
		}
	}

	private void finish() {
		// Stop listening to sensor input
		mSensorManager.unregisterListener(this);
		// Calculate mean and std
		List<Float> smoothedData = Methods.smooth(mVectorLengths, Values.SMOOTHING_WINDOW);
		List<Float> peakStrengths = Methods.calculatePeakStrengths(smoothedData);
		Double mean = Methods.calculateMean(peakStrengths);
		Double std = Methods.calculateStd(peakStrengths, mean);
		// Tell the calibration activity to play a sound and store values
		context.finishCalibration(mean, std);
	}
}
