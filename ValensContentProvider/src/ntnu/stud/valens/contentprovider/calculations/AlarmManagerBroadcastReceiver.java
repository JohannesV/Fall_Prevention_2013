/*******************************************************************************
 * Licensed to UbiCollab.org under one or more contributor
 * license agreements.  See the NOTICE file distributed 
 * with this work for additional information regarding
 * copyright ownership. UbiCollab.org licenses this file
 * to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package ntnu.stud.valens.contentprovider.calculations;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.format.DateUtils;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	final public static String ONE_TIME = "onetime";
	final static String TAG = "AlarmBroadcastReciever";

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();

		// You can do the processing here.
		Bundle extras = intent.getExtras();
		StringBuilder msgStr = new StringBuilder();

		if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
			// Make sure this intent has been sent by the one-time timer button.
			msgStr.append("One time Timer : ");
		}
		Format formatter = new SimpleDateFormat("hh:mm:ss a");
		msgStr.append(formatter.format(new Date()));

		Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
		ManipulatorHelper mh = new ManipulatorHelper(context);
		(new Thread(mh)).start();

		// Release the lock
		wl.release();
	}

	public void setAlarm(Context context) {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 4);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		intent.putExtra(ONE_TIME, Boolean.FALSE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		// After after one day
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				DateUtils.DAY_IN_MILLIS, pi);
	}
}
