package com.example.falltimeseries;

import java.util.ArrayList;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CalibrationActivity extends Activity {

	private static final int WAIT_TIME = 10000;
	private static final int CAL_TIME = 60000;

	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibration);
		mMediaPlayer = MediaPlayer.create(this, R.raw.beep);
	}

	public void startCalibration(View view) {
		try {
			// Wait before starting
			Thread.sleep(WAIT_TIME);
			// Play sound
			mMediaPlayer.start();
			// Start getting input to calibrate
			CalibrationThread ct = new CalibrationThread(this);
			ct.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void finishCalibration(double mean, double std) {
		// Play sound
		mMediaPlayer.start();
		// Show message
		Toast.makeText(this, "Calibration complete!", Toast.LENGTH_LONG).show();
		// Store information in phone using SharedPreferences
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat("mean", (float)mean);
		editor.putFloat("std", (float)std);
		editor.commit();
		// Go back to main screen
		Intent intent = new Intent(this, LaunchActivity.class);
		startActivity(intent);
	}
}
