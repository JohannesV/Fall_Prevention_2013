package com.example.falltimeseries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	public static final int SMOOTHING_WINDOW = 5;
	public static final int WINDOW_SIZE = 10;
	public static final int DATA_STREAM_SIZE = 1000;
	public static final int COMMIT_DATA_THRESHOLD = (SMOOTHING_WINDOW * 2) + (WINDOW_SIZE * 2) + DATA_STREAM_SIZE;
	public static final float STD_THRESHOLD = 0.8f;
	
	private SensorManager mSensorManager;
	private Sensor mAccSensor;
	private TextView mTextViewAccelerationX, mTextViewAccelerationY, mTextViewAccelerationZ, mTextViewSteps;
	private List<String> mOutputCSV;
	private List<Float> mVectorLengths;
	private List<Long> mTimeStamps, mSteps;
	private double mMean, mStd;
	private boolean meanStdSet = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get handlers for GUI text
		mTextViewAccelerationX = (TextView)findViewById(R.id.textViewAccelerationX);
		mTextViewAccelerationY = (TextView)findViewById(R.id.textViewAccelerationY);
		mTextViewAccelerationZ = (TextView)findViewById(R.id.textViewAccelerationZ);
		mTextViewSteps = (TextView)findViewById(R.id.textViewSteps);
		// Initialize lists
		mOutputCSV = new ArrayList<String>();
		mVectorLengths = new ArrayList<Float>();
		mTimeStamps = new ArrayList<Long>();
		mSteps = new ArrayList<Long>();
		
		// Register sensor for orientation events
		mSensorManager = (SensorManager)getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
		for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)) {
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				mAccSensor = sensor;
			}
		}
		
		Log.w("Acc", mAccSensor.toString());
		
		// Check that we found a sensor object
		if (mAccSensor == null) {
			System.out.println("Did not find sensor!");
			cleanUp();
		}
		
		mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_GAME);
	}

	private void cleanUp() {
		mSensorManager.unregisterListener(this);
		finish();
	}

	public void storeData(View view) {
		storeToSD("csvdata.txt", mOutputCSV);
		cleanUp();
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// We don't really care about this
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			Long timeStamp = (new Date()).getTime();
			// GUI
			mTextViewAccelerationX.setText("X: " + String.valueOf(event.values[0]));
			mTextViewAccelerationY.setText("Y: " + String.valueOf(event.values[1]));
			mTextViewAccelerationZ.setText("Z: " + String.valueOf(event.values[2]));
			// Print CSV
//			DecimalFormat df = new DecimalFormat("#.##");
//			mOutputCSV.add(df.format(event.values[0]) + ";" + df.format(event.values[1]) + ";" + df.format(event.values[2]) + ";" + String.valueOf(timeStamp));
			// Calculate peaks
			mVectorLengths.add(calculateVectorLength(event.values));
			mTimeStamps.add(timeStamp);
			if (mVectorLengths.size() >= COMMIT_DATA_THRESHOLD) {
				// TODO: Move calculation to separate thread
				detectSteps();
				discardData();
				mTextViewSteps.setText("Steps: " + String.valueOf(mSteps.size()));
			}
		}
	}
	
	private void detectSteps() {
		List<Float> smoothedData = smooth(mVectorLengths, SMOOTHING_WINDOW);
		List<Float> peakStrengths = calculatePeakStrengths(smoothedData);
		// TODO: MOVE TO A DIFFERENT STAGE
		if (!meanStdSet) {
			calculateMeanAndStd(peakStrengths);
			meanStdSet = true;
		}
		List<Integer> peakIndices = findPossiblePeaks(peakStrengths);
		peakIndices = removeClosePeaks(peakIndices, peakStrengths);
		storeSteps(peakIndices);
	}
	
	private void storeSteps(List<Integer> peakIndices) {
		for (Integer peak : peakIndices) {
			mSteps.add(mTimeStamps.get(peak));
		}
	}

	private List<Integer> removeClosePeaks(List<Integer> peaks, List<Float> peakStrengths) {
		// Remove all peaks closer to each other than WINDOW
		int i = 0;
		while (i < peaks.size()-1) {
			// If too subsequent peaks are too close ...
			if (peaks.get(i+1) - peaks.get(i) <= WINDOW_SIZE) {
				// ... remove the least significant
				if (peakStrengths.get(peaks.get(i+1)) > peakStrengths.get(peaks.get(i))) {
					peaks.remove(i);
				}
				else {
					peaks.remove(i+1);
				}
			}
			// If peaks are distance, move the counter one step ahead
			else {
				i++;
			}
		}
		return peaks;
	}

	private void calculateMeanAndStd(List<Float> values) {
		// Calculate mean of all values over zero
		mMean = 0.0f;
		for (Float f : values) {
			if (f>0) mMean += f;
		}
		mMean = mMean / values.size();
		// Calculate standard deviation of all values over zero
		mStd = 0.0f;
		for (Float f : values) {
			if (f>0) mStd += (f-mMean)*(f-mMean);
		}
		mStd = mStd / values.size();
		mStd = Math.sqrt(mStd);
	}

	private List<Integer> findPossiblePeaks(List<Float> peakStrengths) {
		List<Integer> peakIndices = new ArrayList<Integer>();
		
		// Add all those values 
		for (int i = 0; i < peakStrengths.size(); i++) {
			if ((peakStrengths.get(i)-mMean) > (mStd*STD_THRESHOLD)) {
				peakIndices.add(i);
			}
		}
		
		return peakIndices;
	}

	private List<Float> calculatePeakStrengths(List<Float> data) {
		List<Float> peakStrengths = new ArrayList<Float>();

		for (int i = WINDOW_SIZE; i < (data.size() - WINDOW_SIZE); i++) {
			float peakPointValue = data.get(i);
			float prePeakStrength = 0.0f;
			float postPeakStrength = 0.0f;

			// Calculate pre- and post-peak strengths simultaneously
			for (int j = 1; j < (WINDOW_SIZE+1); j++) {
				prePeakStrength += (peakPointValue - data.get(i-j));
				postPeakStrength += (peakPointValue - data.get(i+j));
			}
			// Normalize to averages
			prePeakStrength = (prePeakStrength / WINDOW_SIZE);
			postPeakStrength = (postPeakStrength / WINDOW_SIZE);
			float peakStrength = (prePeakStrength+postPeakStrength)/2;

			peakStrengths.add(peakStrength);
		}

		return peakStrengths;
	}

	// TODO: Has room for efficiency improvement
	private List<Float> smooth(List<Float> data, int window) {
		List<Float> smoothed = new ArrayList<Float>();
		
		for (int i = window; i<(data.size()-window); i++) {
			float total = 0.0f;
			for (int j = -window; j < (window+1); j++) {
				 total += data.get(i+j);
			}
			float avg = (total / ((window*2)+1));
			smoothed.add(avg);
		}
		
		return smoothed;
	}

	private void discardData() {
		List<Long> newTimeStamps = new ArrayList<Long>();
		List<Float> newVectorLengths = new ArrayList<Float>();
		for (int i = mTimeStamps.size()-(WINDOW_SIZE*2); i < mTimeStamps.size(); i++) {
			newTimeStamps.add(mTimeStamps.get(i));
			newVectorLengths.add(mVectorLengths.get(i));
		}
		mTimeStamps = newTimeStamps;
		mVectorLengths = newVectorLengths;
	}

	private void storeToSD(String filename, List<String> values) {
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File file = new File(path, filename);
		try {
			OutputStream os = new FileOutputStream(file);
			for (String f : values) {
				os.write((f+"\n").getBytes());
			}
		} catch(IOException e) {
			Log.w("ExternalStorage", "Error writing " + file, e);
		}
	}

	
	
	private float calculateVectorLength(float[] vector) {
		float sum = ((vector[0] * vector[0]) + (vector[1] * vector[1]) + (vector[2] * vector[2]));
		return (float) Math.sqrt((double) sum);
	}
	
	
}
