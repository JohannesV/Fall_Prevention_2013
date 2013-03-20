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

	private SensorManager mSensorManager;
	private Sensor mAccSensor;
	private TextView mTextViewAccelerationX, mTextViewAccelerationY, mTextViewAccelerationZ;
//	private List<Object> mAccelerationX, mAccelerationY, mAccelerationZ;
	private List<String> mOutputCSV;
	private Date mPeriodStart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get handlers for GUI text
		mTextViewAccelerationX = (TextView)findViewById(R.id.textViewAccelerationX);
		mTextViewAccelerationY = (TextView)findViewById(R.id.textViewAccelerationY);
		mTextViewAccelerationZ = (TextView)findViewById(R.id.textViewAccelerationZ);
		// Initialize lists
//		mAccelerationX = new ArrayList<Object>();
//		mAccelerationY = new ArrayList<Object>();
//		mAccelerationZ = new ArrayList<Object>();
		mOutputCSV = new ArrayList<String>();
		
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
//		storeToSD("a_x.txt", mAccelerationX);
//		storeToSD("a_y.txt", mAccelerationY);
//		storeToSD("a_z.txt", mAccelerationZ);
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
//			if(mPeriodStart == null){
//				mPeriodStart = new Date();
//				mOutputCSV.add("#TIMESTAMP#" + (mPeriodStart.getTime()));
//			}
//			else if(((new Date()).getTime() - mPeriodStart.getTime()) > 1000){
//				mPeriodStart = new Date();
//				mOutputCSV.add("#TIMESTAMP#" + (mPeriodStart.getTime()));
//			}
//			mAccelerationX.add(event.values[0]);
//			mAccelerationY.add(event.values[1]);
//			mAccelerationZ.add(event.values[2]);
			mTextViewAccelerationX.setText("X: " + String.valueOf(event.values[0]));
			mTextViewAccelerationY.setText("Y: " + String.valueOf(event.values[1]));
			mTextViewAccelerationZ.setText("Z: " + String.valueOf(event.values[2]));
			DecimalFormat df = new DecimalFormat("#.##");
			mOutputCSV.add(df.format(event.values[0]) + ";" + df.format(event.values[1]) + ";" + df.format(event.values[2]) + ";" + String.valueOf((new Date()).getTime()));
		}
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

}
