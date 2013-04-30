package no.ntnu.stud.fallprevention.activity;

import java.sql.Timestamp;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.connectivity.ContentProviderHelper;
import no.ntnu.stud.fallprevention.connectivity.DatabaseHelper;
import no.ntnu.stud.fallprevention.datastructures.RiskStatus;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Handles the main screen interactions.
 * 
 * @author Elias
 */
public class MainScreen extends Activity {
	RiskStatus status;
	Thread notificationThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainscreen);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
		}

		updateVisible();

		// Change image depending on information from the database
		Drawable drawable;
		if (status == null) {
			status = RiskStatus.OK_JOB;
		}

		status = new ContentProviderHelper(getApplicationContext())
				.cpGetStatus(status);
		if (status == RiskStatus.BAD_JOB) {
			drawable = getResources().getDrawable(R.drawable.bad_job);
		} else if (status == RiskStatus.NOT_SO_OK_JOB) {
			drawable = getResources().getDrawable(R.drawable.not_so_ok_job);
		} else if (status == RiskStatus.OK_JOB) {
			drawable = getResources().getDrawable(R.drawable.ok_job);
		} else if (status == RiskStatus.GOOD_JOB) {
			drawable = getResources().getDrawable(R.drawable.good_job);
		} else if (status == RiskStatus.VERY_GOOD_JOB) {
			drawable = getResources().getDrawable(R.drawable.very_good_job);
		} else {
			// Problem
			drawable = null;
		}
		shouldPush();

		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		Drawable d = new BitmapDrawable(getResources(),
				Bitmap.createScaledBitmap(bitmap, 200, 200, true));
		ImageButton imageButton = (ImageButton) findViewById(R.id.mainScreenSmileyImage);
		imageButton.setBackgroundDrawable(d);
	}

	/**
	 * This is just an OnCreate method for displaying the Menu when you choose
	 * to activate it
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mainscreen, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		shouldPush();
		updateVisible();
	}

	/**
	 * Updates the mainscreen when a new notification is available, will also
	 * tell when you dont have any new notifications. Gets the predefined name
	 * if the user if there were any.
	 */
	private void updateVisible() {
		// Find name from shared prefences file
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String name = sp.getString("name", "");
		String displayString = getString(R.string.greeting) + ", " + name + "!";
		TextView txtGreetingName = (TextView) findViewById(R.id.textView1);
		txtGreetingName.setText(displayString);

		// Display a message if there are new messages
		// TODO: DOes not update the screen when we removed all the
		// notifications, not tested further so need more testing to verify the
		// source of the problem
		TextView txtSubGreeting = (TextView) findViewById(R.id.mainScreenSubText);
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		if (dbHelper.dbHaveEvents()) {
			String message = getString(R.string.main_got_new_events);
			txtSubGreeting.setText(message);
		} else {
			txtSubGreeting.setVisibility(View.GONE);
		}
	}

	/**
	 * this is basically a listener for the menu buttons
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_statistics:
			intent = new Intent(this, Statistics.class);
			startActivity(intent);
			break;
		case R.id.menu_settings:
			intent = new Intent(this, Settings.class);
			startActivity(intent);
			break;
		// case R.id.menu_related:
		// intent = new Intent(this, Related.class);
		// startActivity(intent);
		// break;
		}
		return false;
	}

	/**
	 * Checks if it is a good idea to push in new notifications to the database.
	 */
	@SuppressLint("NewApi")
	private void shouldPush() {
		long current = System.currentTimeMillis();
		Timestamp now = new Timestamp(current);
		Log.v("Main Screen", "Checking for pushing");
		Timestamp last;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		last = new Timestamp(sp.getLong("lastPushed", 0l));
		Log.v("Main Screen", String.valueOf(last.getTime()));
		if (DateUtils.HOUR_IN_MILLIS < (now.getTime() - last.getTime())) {
			Log.v("Main Screen", "Minute passed, notification pushed: "
					+ (status == null));
			new ContentProviderHelper(this).pushNotification(status.getCode());
			SharedPreferences.Editor editor = sp.edit();
			// Displays the edited name
			editor.putLong("lastPushed", current);
			editor.commit();
		} else if (last.getTime() == 0l) {
			Log.v("Main Screen", "Time not smaller");
			SharedPreferences.Editor editor = sp.edit();
			// Displays the edited name
			editor.putLong("lastPushed", current);
			editor.commit();
		}

	}

	/**
	 * A listener for a button that sends you to the Eventlist screen
	 * 
	 * @param view
	 */
	public void fireEvent(View view) {
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}

}
