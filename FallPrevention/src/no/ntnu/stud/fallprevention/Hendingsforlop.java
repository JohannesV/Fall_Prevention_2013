package no.ntnu.stud.fallprevention;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class Hendingsforlop extends ListActivity {
	
	String[] strings = new String[] {"Du har gjort som du skulle!", "Hugs å gjera øvingane dine!", "Legen din er fornøgd :)", "Dette går bra!", "Skjerp deg!"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ListDrawAdapter(this, strings));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_hendingsforlop, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String selectedString = strings[position];
		Intent intent = new Intent(this, Hendingsdetalj.class);
		intent.putExtra("com.example.mockonthetable.MESSAGE", selectedString);
		startActivity(intent);
	}

}
