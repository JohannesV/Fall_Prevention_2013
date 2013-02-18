package no.ntnu.stud.fallprevention;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class EventList extends ListActivity {

	List<Event> events;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Redraw the list every time the activity is resumed, in order to 
		// ensure that the list is always up-to-date.
		events = new ArrayList<Event>();
		
		DatabaseHelper dbh = new DatabaseHelper(this);
		SQLiteDatabase db = dbh.getReadableDatabase();
		
		Cursor c = db.rawQuery("SELECT " + 
				DatabaseContract.EventType.COLUMN_NAME_TITLE + ", " +
				DatabaseContract.Event.COLUMN_NAME_ID + ", " +
				DatabaseContract.EventType.COLUMN_NAME_ICON + 
				" FROM " + DatabaseContract.Event.TABLE_NAME + " INNER JOIN " +
				DatabaseContract.EventType.TABLE_NAME + " ON " + 
				DatabaseContract.Event.TABLE_NAME + "." + DatabaseContract.Event.COLUMN_NAME_TYPEID + "=" + 
				DatabaseContract.EventType.TABLE_NAME + "." + DatabaseContract.EventType.COLUMN_NAME_ID, null);

		// Iterate over the data fetched
		for (int i = 0; i < c.getCount(); i++) {
			c.moveToPosition(i);
			Event e = new Event( c.getString(0), Integer.parseInt(c.getString(1)), c.getString(2) );
			events.add(e);
		}
		
		// Close database connection
		c.close();
		db.close();
		
		// Display the information
		setListAdapter(new ListDrawAdapter(this, events));
		
		// Display a message if there are no events in the queue
		if (events.isEmpty()) {
			Toast.makeText(this, getString(R.string.no_events), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_eventlist, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, EventDetail.class);
		intent.putExtra("no.ntnu.stud.fallprevention.ID", events.get(position).getId());
		startActivity(intent);
	}

}