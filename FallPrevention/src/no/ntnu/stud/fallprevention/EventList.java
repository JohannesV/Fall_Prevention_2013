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

public class EventList extends ListActivity {

	List<String> strings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Redraw the list every time the activity is resumed, in order to 
		// ensure that the list is always up-to-date.
		strings = new ArrayList<String>();
		
		DatabaseHelper dbh = new DatabaseHelper(this);
		SQLiteDatabase db = dbh.getReadableDatabase();
		
		Cursor c = db.rawQuery("SELECT Headline, Icon FROM Event INNER JOIN EventType ON Event.TypeID=EventType.TypeID", null);
		c.moveToFirst();
		do {
			strings.add("Haha");
		} while(c.moveToNext());
		
		setListAdapter(new ListDrawAdapter(this, strings));
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
		intent.putExtra("no.ntnu.stud.fallprevention.ID", 0);
		startActivity(intent);
	}

}