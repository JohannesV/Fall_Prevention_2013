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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
	    alarm.setAlarm(context);
	}
}
