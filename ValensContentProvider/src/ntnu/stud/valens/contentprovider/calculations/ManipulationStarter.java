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
		// Only start if this method is caled by BOOT_COMPLETED
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
<<<<<<< HEAD
		// Set a daily "alarm" at 04:00 am
	    Log.v(TAG, "Starting alarm manager");
	    alarm = new AlarmManagerBroadcastReceiver();
	    alarm.SetAlarm(context);
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.HOUR_OF_DAY, 11);
//		calendar.set(Calendar.MINUTE, 37);
//		calendar.set(Calendar.SECOND, 0);
//		PendingIntent pi = PendingIntent.getService(context, 0, new Intent(
//				context, ManipulatorHelper.class),
//				PendingIntent.FLAG_UPDATE_CURRENT);
//		AlarmManager am = (AlarmManager) context
//				.getSystemService(Context.ALARM_SERVICE);
//		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//				AlarmManager.INTERVAL_DAY, pi);

=======
		Log.v(TAG, "StartMani");
		new ManipulatorHelper().startAlarm(context);
		Log.v(TAG, "AlarmStart finished");
>>>>>>> 0690d416383a5c478cd4c68addb0bfe347b830a4
		// Check whether we should do a calculation immediately
		/* Get Last Update Time from Preferences */
		SharedPreferences prefs = context.getSharedPreferences(TAG,
				Context.MODE_PRIVATE);
		long lastUpdateTime = prefs.getLong("lastUpdateTime", 0);

		Log.v(TAG, "LastUpdate: " + new Date(lastUpdateTime).toString());
		Log.v(TAG,
				"Current: " + new Date(System.currentTimeMillis()).toString());
		Log.v(TAG, "The time is: " + Calendar.HOUR_OF_DAY);

		/* Should Activity Check for Updates Now? */
		if ((lastUpdateTime + (24 * 60 * 60 * 1000)) < System
				.currentTimeMillis() && Calendar.HOUR_OF_DAY >= 4) {
			Log.v(TAG, "It's the right time!");
			/* Save current timestamp for next Check */
			lastUpdateTime = System.currentTimeMillis();
			SharedPreferences.Editor editor = prefs.edit();
			editor.putLong("lastUpdateTime", lastUpdateTime);
			editor.commit();

			/* Start Update */

			new ManipulatorHelper().calculate(context);
		}
	}
}
