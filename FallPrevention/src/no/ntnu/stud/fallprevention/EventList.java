package no.ntnu.stud.fallprevention;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class EventList extends ListActivity {

	List<String> strings = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		strings.add("Du har gjort som du skulle!");
		strings.add("Hugs å gjera øvingane dine!");
		strings.add("Legen din er fornøgd :)");
		strings.add("Dette går bra!");
		strings.add("Skjerp deg!");
		strings.add("Du er ein latsabb :(");
		strings.add("Fortsett som det her framover");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Redraw the list every time the activity is resumed, in order to 
		// ensure that the list is always up-to-date.
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