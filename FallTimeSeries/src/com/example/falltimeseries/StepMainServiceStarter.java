package com.example.falltimeseries;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The StepMainServiceStarter ensures is a on-boot broadcast receiver, so it is
 * started when the mobile boots. It's function is to start the step detector
 * service.
 * 
 * @author Elias
 * 
 */
public class StepMainServiceStarter extends BroadcastReceiver {

	public static final String TAG = "StepMainServiceStarter";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Make sure to only start if the broadcast was on-boot.
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			ComponentName comp = new ComponentName(context.getPackageName(),
					StepMainService.class.getName());
			ComponentName service = context.startService(new Intent()
					.setComponent(comp));
			if (null == service) {
				// This should really not happen. This is bad.
				Log.e(TAG, "Could not start service " + comp.toString());
			}
		} else {
			Log.e(TAG, "Received unexpected intent " + intent.toString());
		}
	}
}
