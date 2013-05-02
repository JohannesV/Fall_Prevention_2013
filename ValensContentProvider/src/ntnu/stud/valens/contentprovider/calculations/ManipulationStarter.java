package ntnu.stud.valens.contentprovider.calculations;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * This receiver class starts on phone start-up. It schedules the AlarmManager
 * to start data manipulation at 04:00 every day.
 * 
 * @author Elias
 * 
 */
public class ManipulationStarter extends BroadcastReceiver {

	public static final String TAG = "ntnu.stud.valens.contentprovider";
	private AlarmManagerBroadcastReceiver alarm ;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Only start if this method is called by BOOT_COMPLETED
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Log.v(TAG, "OnRecieve");
			startManipulation(context);
		}
	}

	/**
	 * Starts manipulation of data in the content provider. Initializes the
	 * calculations and sets a time it should be called every day.
	 * 
	 * @param context
	 *            The context of the application
	 */
	public void startManipulation(Context context) {

		// Set a daily "alarm" at 04:00 am
	    Log.v(TAG, "Starting alarm manager");
	    alarm = new AlarmManagerBroadcastReceiver();
	    alarm.SetAlarm(context);



	

		// Check whether we should do a calculation immediately
		/* Get Last Update Time from Preferences */
//		SharedPreferences prefs = context.getSharedPreferences(TAG,
//				Context.MODE_PRIVATE);
//		long lastUpdateTime = prefs.getLong("lastUpdateTime", 0);
//
//		Log.v(TAG, "LastUpdate: " + new Date(lastUpdateTime).toString());
//		Log.v(TAG,
//				"Current: " + new Date(System.currentTimeMillis()).toString());
//		Log.v(TAG, "The time is: " + Calendar.HOUR_OF_DAY);
//
//		/* Should Activity Check for Updates Now? */
//		if ((lastUpdateTime + (24 * 60 * 60 * 1000)) < System
//				.currentTimeMillis() && Calendar.HOUR_OF_DAY >= 4) {
//			Log.v(TAG, "It's the right time!");
//			/* Save current timestamp for next Check */
//			lastUpdateTime = System.currentTimeMillis();
//			SharedPreferences.Editor editor = prefs.edit();
//			editor.putLong("lastUpdateTime", lastUpdateTime);
//			editor.commit();

			/* Start Update */

			//new ManipulatorHelper().calculate(context);
		
	}
}
