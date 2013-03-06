package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		
		// Open the SharedPrefrences-file, check whether a username is already stored in the phone
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
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
