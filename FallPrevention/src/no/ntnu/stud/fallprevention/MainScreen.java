package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Intent;
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

		// Extract the information contained in the intent that created this
		// activity
		Intent motherIntent = getIntent();
		String name = motherIntent
				.getStringExtra("no.ntnu.stud.fallprevention.MESSAGE");
		name = getString(R.string.greeting) + ", " + name + "!";
		// Show name on screen
		TextView namnesyn = (TextView) findViewById(R.id.textView1);
		namnesyn.setText(name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mainscreen, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_statistics:
			//TODO: start the statistics activity
			break;
		case R.id.menu_settings:
			//TODO: start the setting activity, if we get one
			break;
		}
		return false;
	}

	public void fireEvent(View view) {
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}

}
