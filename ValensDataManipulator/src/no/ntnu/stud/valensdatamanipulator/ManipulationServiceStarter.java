package no.ntnu.stud.valensdatamanipulator;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ManipulationServiceStarter extends BroadcastReceiver {

	public static final String TAG = "LocationLoggerServiceManager";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Check that the right type of intent started this
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			ComponentName comp = new ComponentName(context.getPackageName(),
					ManipulationService.class.getName());
			ComponentName service = context.startService(new Intent()
					.setComponent(comp));
			if (null == service) {
				// Something horrible wrong happened
				Log.e(TAG, "Could not start service " + comp.toString());
			}
		} else {
			Log.e(TAG, "Received unexpected intent " + intent.toString());
		}
	}
}
