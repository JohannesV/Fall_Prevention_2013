package com.example.falltimeseries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
	private Sensor mOriSensor, mAccSensor;
	private TextView t_o_x, t_o_y, t_o_z;
	private TextView t_a_x, t_a_y, t_a_z;
	private List<Float> o_x, o_y, o_z;
	private List<Float> a_x, a_y, a_z;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get handlers for GUI text
		t_o_x = (TextView)findViewById(R.id.textView1);
		t_o_y = (TextView)findViewById(R.id.textView2);
		t_o_z = (TextView)findViewById(R.id.textView3);
		t_a_x = (TextView)findViewById(R.id.textView5);
		t_a_y = (TextView)findViewById(R.id.textView6);
		t_a_z = (TextView)findViewById(R.id.textView7);
		// Initialize lists
		o_x = new ArrayList<Float>();
		o_y = new ArrayList<Float>();
		o_z = new ArrayList<Float>();
		a_x = new ArrayList<Float>();
		a_y = new ArrayList<Float>();
		a_z = new ArrayList<Float>();
		
		// Register sensor for orientation events
		mSensorManager = (SensorManager)getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
		for (Sensor s : mSensorManager.getSensorList(Sensor.TYPE_ALL)) {
			if (s.getType() == Sensor.TYPE_ORIENTATION) {
				mOriSensor = s;
			}
			else if (s.getType() == Sensor.TYPE_ACCELEROMETER) {
				mAccSensor = s;
			}
		}
		
		Log.w("Ori", mOriSensor.toString());
		Log.w("Acc", mAccSensor.toString());
		// Check that we found a sensor object
		if (mOriSensor == null ||  mAccSensor == null) {
			System.out.println("Did not find all sensors!");
			cleanUp();
		}
		
		mSensorManager.registerListener(this, mOriSensor, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_UI);
	}

	private void cleanUp() {
		mSensorManager.unregisterListener(this);
		finish();
	}

	public void storeData(View view) {
		storeToSD("o_x.txt", o_x);
		storeToSD("o_y.txt", o_y);
		storeToSD("o_z.txt", o_z);
		storeToSD("a_x.txt", a_x);
		storeToSD("a_y.txt", a_y);
		storeToSD("a_z.txt", a_z);
		cleanUp();
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// We don't really care about this
		
	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		if (se.sensor.getType() == Sensor.TYPE_ORIENTATION) { 
			o_x.add(se.values[0]);
			o_y.add(se.values[1]);
			o_z.add(se.values[2]);
			t_o_x.setText(String.valueOf(se.values[0]));
			t_o_y.setText(String.valueOf(se.values[1]));
			t_o_z.setText(String.valueOf(se.values[2]));
		}
		else if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			a_x.add(se.values[0]);
			a_y.add(se.values[1]);
			a_z.add(se.values[2]);
			t_a_x.setText(String.valueOf(se.values[0]));
			t_a_y.setText(String.valueOf(se.values[1]));
			t_a_z.setText(String.valueOf(se.values[2]));
		}
	}
	
	private void storeToSD(String filename, List<Float> values) {
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		File file = new File(path, filename);
		try {
			OutputStream os = new FileOutputStream(file);
			for (Float f : values) {
				os.write((String.valueOf(f)+"\n").getBytes());
			}
		} catch(IOException e) {
			Log.w("ExternalStorage", "Error writing " + file, e);
		}
	}

}
