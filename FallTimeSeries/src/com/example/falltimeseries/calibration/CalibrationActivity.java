package com.example.falltimeseries.calibration;

import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.falltimeseries.LaunchActivity;
import com.example.falltimeseries.R;
import com.example.falltimeseries.Values;

/**
 * Describes the calibration activity screen, and the functionality surrounding
 * it, i.e. what happens when the user presses the button.
 * 
 * @author Elias
 * 
 */
public class CalibrationActivity extends Activity {

	// Used to play the "beep"-sound
	private MediaPlayer mMediaPlayer;

	@Override
	// Standard onCreate
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibration);
		mMediaPlayer = MediaPlayer.create(this, R.raw.beep);
	}

	/**
	 * This is an onClick-event listener. Called by clicking the
	 * "Calibrate"-button. Starts the countdown before starting a
	 * CalibrationStartTask.
	 * 
	 * @param view
	 *            - Provided by the system
	 */
	public void startCalibration(View view) {
		// Wait before starting
		Timer timer = new Timer();
		CalibrationStartTask cst = new CalibrationStartTask(this);
		timer.schedule(cst, Values.CAL_WAIT);
	}

	/**
	 * The method is fired by the CalibrationThread when mean and std are fully
	 * calculated. It plays a sound to show the user that the calibration is
	 * finished, and stores the mean and std to file.
	 * 
	 * @param mean - the calculated mean
	 * @param std - the calculated std
	 */
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

	/**
	 * Plays the "beep" sound.
	 */
	public void playSound() {
		mMediaPlayer.start();
	}
}

