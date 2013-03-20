package no.ntnu.stud.fallprevention;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Manages(storing, creating) related people
 * @author Tayfun
 *
 */
public class Related extends Activity {
	
	List<Contact> contacts = new ArrayList<Contact>();
	
	@Override
	/**
	 * Prepares the screen for the program
	 * and sets the activity for related.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_related);
	}
	
	@Override
	/**
	 * Continues the contact management
	 */
	protected void onResume() {
		super.onResume();
		//set inn getContactsfrom Database
		contacts = new DatabaseHelper(this).dbGetContactList();
		// shows the contacts in a list
		final ListView listView = (ListView) findViewById(R.id.listView1);
		//views the list and allows to fireEvent on the contacts position
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
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, listItems);
		listView.setAdapter( adapter );	
	}
	/**
	 * Starts event on the contacts position and Id 
	 * @param position for detection the position to display
	 */
	protected void fireEvent(int position) {
		Intent intent = new Intent(this, ContactPerson.class);
		intent.putExtra("no.ntnu.stud.fallprevention.CONTACT_ID_MESSAGE", contacts.get(position).getId());
		startActivity(intent);
	}
	/**
	 * Creates new person
	 * @param view no function yet
	 */
	public void fireNewPerson(View view){
		Intent intent = new Intent(this, NewContact.class);
		startActivity(intent);
	}


}
