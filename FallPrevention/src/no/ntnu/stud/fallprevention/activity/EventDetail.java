package no.ntnu.stud.fallprevention.activity;

import java.util.Map;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.connectivity.DatabaseContract;
import no.ntnu.stud.fallprevention.connectivity.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EventDetail extends Activity {
	
	private int eventId; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventdetail);

		// Get the event type and ID from the intent
		Intent motherIntent = getIntent();
		eventId = motherIntent
				.getIntExtra("no.ntnu.stud.fallprevention.ID", -1);
		
		// Fetch description and headline from database
		Map<String, String> eventInformation = new DatabaseHelper(this).dbGetEventInfo(eventId);
		
		// Fill in information
		TextView textView = (TextView) findViewById(R.id.headlineTextView);
		textView.setText(eventInformation.get(DatabaseContract.EventType.COLUMN_NAME_TITLE));
		TextView textView2 = (TextView) findViewById(R.id.mainTextView);
		textView2.setText(eventInformation.get(DatabaseContract.EventType.COLUMN_NAME_DESCRIPTION));
	}
	/**
	 * Fires an event to delete the event you are currently viewing
	 * Sends you back to Eventlist afterwards.
	 * 
	 * @param view
	 */
	public void fireDeleteButton(View view) {
		// Delete event from database
		new DatabaseHelper(this).dbDeleteEvent(eventId);
		// Then go back to EventList screen
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}
	/**
	 * Doesn't do anything to the Event
	 * Sends you back to the EventList
	 * @param view
	 */
	public void fireKeepButton(View view) {
		// Go back to EventList screen
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}
}
