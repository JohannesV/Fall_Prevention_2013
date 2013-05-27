package ntnu.stud.valens.stepdetector.calibration;

import java.util.ArrayList;
import java.util.List;

import ntnu.stud.valens.stepdetector.Methods;
import ntnu.stud.valens.stepdetector.Values;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * This thread handles calibration. When it is launched, it accesses the
 * accelerometer sensor and stores data for a period of time. At the end of the
 * period, it makes a sound, and then calculates mean and std for the data. This
 * is done by performing the ordinary first steps for step detection: Finding
 * vector lengths, smoothing, calculating peak strength. Finally it calculates
 * the mean and std for the calculated data.
 * 
 * @author Elias
 * 
 */
public class CalibrationThread implements Runnable, SensorEventListener {

	private List<Float> mVectorLengths;
	private SensorManager mSensorManager;
	private Sensor mAccSensor;
	private CalibrationActivity context;
	private Long startTime;

	/**
	 * Constructor.
	 * 
	 * @param c
	 *            - The CalibrationActivity that the Timer belongs to. This is
	 *            need to use the playSound() function in the
	 *            CalibrationActivity during run().
	 */
	public CalibrationThread(CalibrationActivity context) {
		this.context = context;
		mVectorLengths = new ArrayList<Float>();
	}

	/**
	 * Started at launch of thread. Starts gathering data.
	 */
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

	/**
	 * Fired by a SensorChange-event. Parameter is provided by the system.
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		mVectorLengths.add(Methods.calculateVectorLength(event.values));
		// If calibration has run for a long enough time, stop gathering
		// data
		if (System.currentTimeMillis() - startTime > Values.CAL_TIME) {
			finish();
		}
	}

	/**
	 * Called when enough data has been gathered. It performs the ordinary steps
	 * for step detection, and calculates mean and std. Finally, it fires
	 * finishCalibration() in the CalibrationActivity.
	 */
	private void finish() {
		// Stop listening to sensor input
		mSensorManager.unregisterListener(this);
		// Calculate mean and std
		List<Float> smoothedData = Methods.smooth(mVectorLengths,
				Values.SMOOTHING_WINDOW);
		List<Float> peakStrengths = Methods
				.calculatePeakStrengths(smoothedData);
		Double mean = Methods.calculateMean(peakStrengths);
		Double std = Methods.calculateStd(peakStrengths, mean);
		// Tell the calibration activity to play a sound and store values
		context.finishCalibration(mean, std);
	}
}
