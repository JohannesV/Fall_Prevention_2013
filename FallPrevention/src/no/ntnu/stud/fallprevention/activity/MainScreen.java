package no.ntnu.stud.fallprevention.activity;

import java.sql.Timestamp;
import java.util.Calendar;

import no.ntnu.stud.fallprevention.Constants;
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
				.getRiskValue();
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
		checkForPush();

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
		checkForPush();
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
	 * Checks if it is time to push the daily notification to the database.
	 */
	@SuppressLint("NewApi")
	private void checkForPush() {
		Log.v("Main Screen", "Checking for push time");
		// Fetch current time and time stored in file
		Calendar current = Calendar.getInstance();
		Calendar last = Calendar.getInstance();

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		Long lastTimestamp = sp.getLong("lastPushed", 0l);
		last.setTimeInMillis(lastTimestamp);

		if (DateUtils.DAY_IN_MILLIS < (current.getTimeInMillis() - last
				.getTimeInMillis())) {
			// More than a day has passed since the last message, so we'll make
			// a new one. To do so, we need to know the step count yesterday and
			// the day before that. So we first need to establish the
			// time stamps to find step count between..
			Calendar thisMorningC = Calendar.getInstance();
			thisMorningC.set(Calendar.HOUR, 5);
			thisMorningC.set(Calendar.SECOND, 0);
			thisMorningC.set(Calendar.MINUTE, 0);
			long thisMorning = thisMorningC.getTimeInMillis();
			long yesterday = thisMorning - DateUtils.DAY_IN_MILLIS;
			long dayBeforeYesterday = yesterday - DateUtils.DAY_IN_MILLIS;
			Timestamp yesterdayTs = new Timestamp(yesterday);
			Timestamp todayTs = new Timestamp(thisMorning);
			Timestamp dayBeforeTs = new Timestamp(dayBeforeYesterday);
			// Get step counts 
			ContentProviderHelper cph = new ContentProviderHelper(getApplicationContext());
			int yesterdaySteps = cph.getStepCount(yesterdayTs, todayTs);
			int dayBeforeSteps = cph.getStepCount(dayBeforeTs, todayTs);
			// Finally, generate a message to be stored in the local DB
			DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
			if ((double)yesterdaySteps / (double)dayBeforeSteps < Constants.BAD_STEP_CHANGE) {
				dbh.dbAddEvent(1, yesterdaySteps, dayBeforeSteps);
			} else if ((double)yesterdaySteps / (double)dayBeforeSteps > Constants.GOOD_STEP_CHANGE) {
				dbh.dbAddEvent(0, yesterdaySteps, dayBeforeSteps);
			} else {
				dbh.dbAddEvent(2, yesterdaySteps, dayBeforeSteps);
			}
			// Record that the message that was created corresponds to the last
			// 24 hours before 5 this morning.
			SharedPreferences.Editor editor = sp.edit();
			editor.putLong("lastPushed", thisMorning);
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
