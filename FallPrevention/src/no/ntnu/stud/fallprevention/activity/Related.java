package no.ntnu.stud.fallprevention.activity;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.connectivity.DatabaseHelper;
import no.ntnu.stud.fallprevention.datastructures.Contact;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Handles the screen that displays the list of related people.
 * 
 * @author Elias
 */
public class Related extends Activity {

	List<Contact> contacts = new ArrayList<Contact>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_related);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// set inn getContactsfrom Database
		contacts = new DatabaseHelper(this).dbGetContactList();
		// shows the contacts in a list
		final ListView listView = (ListView) findViewById(R.id.listView1);
		// views the list and allows to fireEvent on the contacts position
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				fireEvent(position);
			}
		});
		ArrayList<String> listItems = new ArrayList<String>();
		for (Contact c : contacts) {
			listItems.add(c.getName());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);
		listView.setAdapter(adapter);
	}

	/**
	 * Fired by clicks on the related people list. Takes you to the
	 * ContactPerson activity, with information about the ID of the contact, so
	 * that the activity can fetch contact details from the db
	 * 
	 * @param position
	 *            - Index of the clicked contact in the list. Filled in by the
	 *            OS.
	 */
	protected void fireEvent(int position) {
		Intent intent = new Intent(this, ContactPerson.class);
		intent.putExtra("no.ntnu.stud.fallprevention.CONTACT_ID_MESSAGE",
				contacts.get(position).getId());
		startActivity(intent);
	}

	/**
	 * Fired by clicks on the "new contect" button. Takes you to the proper activity.
	 * 
	 * @param view - Provided by the OS.
	 */
	public void fireNewPerson(View view) {
		Intent intent = new Intent(this, NewContact.class);
		startActivity(intent);
	}

}
