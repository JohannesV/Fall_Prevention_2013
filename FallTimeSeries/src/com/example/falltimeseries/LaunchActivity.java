package com.example.falltimeseries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * The Launch activity for the service app. Started when the app is run, 
 * provides a basic interface for starting and stopping the service. 
 * 
 * @author Elias Aamot
 *
 */
public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
	}

	/**
	 * Called when the "start"-button is pressed. Makes an intent, 
	 * and starts the service wit parameter "stop" = false, so that
	 * the service knows that it should start, not stop.
	 * 
	 * @param View - Provided by the code that calls onClick-events
	 */
	public void startMain(View view) {
		Intent intent = new Intent(this, StepMainService.class);
		intent.putExtra("stop", false);
		startService(intent);
	}
	
	/**
	 * Called when the "stop"-button is pressed. Builds an intent
	 * to send the message "stop" = true to the service. The intent
	 * then calls the onStartCommand()-method of the StepMainService
	 * 
	 * @param View - Provided by the code that calls onClick-events
	 */
	public void stopMain(View view) {
		Intent intent = new Intent(this, StepMainService.class);
		intent.putExtra("stop", true);
		startService(intent);
	}
	
}
