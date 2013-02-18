package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
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
		
		// Fetch description and headline from database based on eventType
		DatabaseHelper dbh = new DatabaseHelper(this);
		SQLiteDatabase db = dbh.getReadableDatabase();
		
		Cursor c = db.rawQuery("SELECT " + DatabaseContract.EventType.COLUMN_NAME_DESCRIPTION + ", " +
				DatabaseContract.EventType.COLUMN_NAME_TITLE + " FROM " +
				DatabaseContract.Event.TABLE_NAME + " INNER JOIN " +
				DatabaseContract.EventType.TABLE_NAME + " ON " +
				DatabaseContract.Event.TABLE_NAME + "." + DatabaseContract.Event.COLUMN_NAME_TYPEID +
				"=" + DatabaseContract.EventType.TABLE_NAME + "." + DatabaseContract.EventType.COLUMN_NAME_ID +
				" WHERE ID=" + eventId, null);
		
		// Read from DB answer
		c.moveToFirst();
		String headline = c.getString(1);
		String description = c.getString(0);
		c.close();
		db.close();
		// Fill in information
		TextView textView = (TextView) findViewById(R.id.headlineTextView);
		textView.setText(headline);
		TextView textView2 = (TextView) findViewById(R.id.mainTextView);
		textView2.setText(description);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_eventdetail, menu);
		return true;
	}
	
	public void fireDeleteButton(View view) {
		// First, delete this event from the database
		DatabaseHelper dbh = new DatabaseHelper(this);
		SQLiteDatabase db = dbh.getWritableDatabase();
		db.delete(DatabaseContract.Event.TABLE_NAME, DatabaseContract.Event.COLUMN_NAME_ID + " = ?", new String[] {String.valueOf(eventId)});
		db.close();
		// Then go back to EventList screen
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}
	
	public void fireKeepButton(View view) {
		// Go back to EventList screen
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}
}
