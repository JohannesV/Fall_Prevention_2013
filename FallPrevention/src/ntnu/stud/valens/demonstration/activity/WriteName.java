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
import android.view.View;
import android.widget.EditText;

/**
 * Handles the write name screen. Called the first time when the program is
 * launched.
 * 
 * @author Elias
 */
public class WriteName extends Activity {

	@Override
	/**
	 * Creates the viewable contents on the screen 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_name);
	}

	/**
	 * Method is fired by button click. Takes the user to the main screen.
	 * Stores the name in a file.
	 * 
	 * @param view
	 *            - Provided on all onClick events by the OS.
	 */
	public void fireName(View view) {
		// Declerates new instance of the class MainScreen
		// which shows the smiley-feedbacks
		Intent intent = new Intent(this, MainScreen.class);
		// Extract information from text field
		EditText eText = (EditText) findViewById(R.id.editText1);
		String name = eText.getText().toString();
		// Store the name in the phone, using the shared preferences file
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sp.edit();
		// Displays the edited name
		editor.putString("name", name);
		editor.commit();
		// Start next activity
		startActivity(intent);
		// Makes sure this activity goes away and cannot be accessed with
		// back-button
		this.finish();

	}

}
