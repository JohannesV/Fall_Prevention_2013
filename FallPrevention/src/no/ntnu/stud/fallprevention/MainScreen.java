package no.ntnu.stud.fallprevention;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainscreen);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
			// This hides the title and icon from the action-bar (menu-bar)
			//ActionBar ab = getActionBar();
			//ab.setDisplayShowTitleEnabled(false);
			//ab.setDisplayShowHomeEnabled(false);
		}

		updateVisible();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mainscreen, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateVisible();
	}

	private void updateVisible() {
		// Extract the information contained in the intent that created this
		// activity
		Intent motherIntent = getIntent();
		String name = motherIntent
				.getStringExtra("no.ntnu.stud.fallprevention.MESSAGE");
		name = getString(R.string.greeting) + ", " + name + "!";
		// Show name on screen
		TextView txtGreetingName = (TextView) findViewById(R.id.textView1);
		txtGreetingName.setText(name);
		TextView txtSubGreeting = (TextView) findViewById(R.id.mainScreenSubText);
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		if (dbHelper.dbHaveEvents()) {
			String message = getString(R.string.main_got_new_events);
			txtSubGreeting.setText(message);
		} else {
			txtSubGreeting.setVisibility(View.GONE);
		}
	}

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
		case R.id.menu_related:
			intent = new Intent(this, Related.class);
			startActivity(intent);
			break;
		}
		return false;
	}

	public void fireEvent(View view) {
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}

}
