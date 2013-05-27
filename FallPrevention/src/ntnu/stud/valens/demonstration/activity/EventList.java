package ntnu.stud.valens.demonstration.activity;

import java.util.List;

import ntnu.stud.valens.demonstration.R;
import ntnu.stud.valens.demonstration.connectivity.DatabaseHelper;
import ntnu.stud.valens.demonstration.datastructures.Event;
import ntnu.stud.valens.demonstration.listadapters.EventListAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Eventlist provides viewable Content for the Eventlist screen to the App. The contents is items such as Notifications/events
 *
 */

public class EventList extends ListActivity {

	List<Event> events;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/**
	 * Shows a text on the screen if you don't have any Events on the list.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		events = new DatabaseHelper(this).dbGetEventList();
		
		// Display the information
		setListAdapter(new EventListAdapter(this, events));
		
		// Display a message if there are no events in the queue
		if (events.isEmpty()) {
			Toast.makeText(this, getString(R.string.no_events), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Fires an Event and opens the EventDetail activity with the given ID
	 * @param ListView l
	 * @param View v
	 * @param int position
	 * @param long id
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, EventDetail.class);
		intent.putExtra("no.ntnu.stud.fallprevention.ID", events.get(position).getId());
		startActivity(intent);
	}

}