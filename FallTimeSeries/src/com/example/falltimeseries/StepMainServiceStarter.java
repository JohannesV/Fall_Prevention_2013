package com.example.falltimeseries;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class StepMainServiceStarter extends BroadcastReceiver {

	public static final String TAG = "StepMainServiceStarter";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Make sure that the right intet starts the service
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			ComponentName comp = new ComponentName(context.getPackageName(),
					StepMainService.class.getName());
			ComponentName service = context.startService(new Intent()
					.setComponent(comp));
			if (null == service) {
				// something really wrong here
				Log.e(TAG, "Could not start service " + comp.toString());
			}
		} else {
			Log.e(TAG, "Received unexpected intent " + intent.toString());
		}
	}
}
