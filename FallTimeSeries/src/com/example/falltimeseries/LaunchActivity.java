package com.example.falltimeseries;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falltimeseries.calibration.CalibrationActivity;

/**
 * The Launch activity for the service app. Started when the app is run,
 * provides a basic interface for starting and stopping the service.
 * 
 * @author Elias
 * 
 */
public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// Enables only the correct button		
		Button startButton = (Button)findViewById(R.id.btnStoreData);
		Button stopButton = (Button)findViewById(R.id.btnStopData);
		
		if (isMyServiceRunning()) {
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
		} else {
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
		}
		
	}
	
	/**
	 * Called when the "start"-button is pressed. Makes an intent, and starts
	 * the service wit parameter "stop" = false, so that the service knows that
	 * it should start, not stop.
	 * 
	 * @param View
	 *            - Provided by the code that calls onClick-events
	 */
	public void startMain(View view) {
		Intent intent = new Intent(this, StepMainService.class);
		intent.putExtra("stop", false);
		startService(intent);
		
		Button startButton = (Button)findViewById(R.id.btnStoreData);
		Button stopButton = (Button)findViewById(R.id.btnStopData);
		
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
	}

	/**
	 * Called when the "stop"-button is pressed. Builds an intent to send the
	 * message "stop" = true to the service. The intent then calls the
	 * onStartCommand()-method of the StepMainService
	 * 
	 * @param View
	 *            - Provided by the code that calls onClick-events
	 */
	public void stopMain(View view) {
		Intent intent = new Intent(this, StepMainService.class);
		intent.putExtra("stop", true);
		startService(intent);
		
		Button startButton = (Button)findViewById(R.id.btnStoreData);
		Button stopButton = (Button)findViewById(R.id.btnStopData);
		
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
	}

	/**
	 * Called when the "calibrate"-button is pressed. Starts the calibrate activity.
	 * 
	 * @param View
	 *            - Provided by the code that calls onClick-events
	 */
	public void calibrate(View view) {
		Intent intent = new Intent(this, CalibrationActivity.class);
		startActivity(intent);
	}
	
	/**
	 * A method to see if a Valens Step Detector Service is running or not.
	 * 
	 * @return A boolean that is true if there is a service running, false otherwise.
	 */
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (StepMainService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}
