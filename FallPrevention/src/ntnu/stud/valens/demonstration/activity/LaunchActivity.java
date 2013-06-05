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
package ntnu.stud.valens.demonstration.activity;

import ntnu.stud.valens.demonstration.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Creates activity on launch. Checks if you have a username, If none was found
 * you will be sent to the WriteName screen. Will send you to the mainScreen
 * otherwise
 * 
 * @author Elias
 */
public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);

		// Open the SharedPrefrences-file, check whether a username is already
		// stored in the phone
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String name = sp.getString("name", "NONAME");

		// Either go the main screen, or go to write name screen
		if (name.equals("NONAME")) {
			Intent intent = new Intent(this, WriteName.class);
			startActivity(intent);
		} else {

			Intent intent = new Intent(this, MainScreen.class);
			startActivity(intent);
		}
		// Finish the current activity so that you cannot go back to it later.
		this.finish();
	}
}
