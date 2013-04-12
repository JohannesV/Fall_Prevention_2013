package com.example.falltimeseries;

import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class CalibrationActivity extends Activity {

	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibration);
		mMediaPlayer = MediaPlayer.create(this, R.raw.beep);
	}

	public void startCalibration(View view) {
		// Wait before starting
		Timer timer = new Timer();
		CalibrationStartTask cst = new CalibrationStartTask(this);
		timer.schedule(cst, Values.CAL_WAIT);
	}

	public void finishCalibration(double mean, double std) {
		// Play sound
		playSound();
		// Show message
		Toast.makeText(this, "Calibration complete!", Toast.LENGTH_LONG).show();
		// Store information in phone using SharedPreferences
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat("mean", (float) mean);
		editor.putFloat("std", (float) std);
		editor.commit();
		// Go back to main screen
		Intent intent = new Intent(this, LaunchActivity.class);
		startActivity(intent);
	}
	
	public void playSound() {
		mMediaPlayer.start();
	}
}
