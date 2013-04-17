package no.ntnu.stud.fallprevention.activity;

import java.util.List;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.R.id;
import no.ntnu.stud.fallprevention.R.layout;
import no.ntnu.stud.fallprevention.auxiliary.EditContactAdapter;
import no.ntnu.stud.fallprevention.connectivity.DatabaseHelper;
import no.ntnu.stud.fallprevention.datastructures.Contact;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
/**
 * Activity class which makes the content in CreateContact available
 * The main purpose of this class is to create a new contact.
 * @author Dot
 *
 */
public class CreateContact extends ListActivity {
	
	EditText editName, editPhone;
	List<String> alarms;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_person);
		editName = (EditText) findViewById(R.id.name);
		editPhone = (EditText) findViewById(R.id.phone_number);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		alarms = new DatabaseHelper(this).dbGetAlarmTypes();
		
		// Display the information
		listView = (ListView) findViewById(android.R.id.list);
		this.setListAdapter(new EditContactAdapter(this, alarms));
	}
	
	/**
	 * fireStore will Store the information from the textfields; editName and editPhone.
	 * And send you back to the related activity.
	 * @param view
	 */
	public void fireStore(View view) {
		// Store in the database
		Contact contact = new Contact();
		contact.setName(editName.getText().toString());
		contact.setPhoneNumber(editPhone.getText().toString());
		new DatabaseHelper(this).dbAddContact(contact);
		// Return to the contacts list
		Intent intent = new Intent(this, Related.class);
		startActivity(intent);
	}
	
}