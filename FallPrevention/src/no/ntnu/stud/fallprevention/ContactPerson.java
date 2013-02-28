package no.ntnu.stud.fallprevention;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

public class ContactPerson extends ListActivity {
	int phoneNumbers;
	String firstName, surname; 
	List<Event> events;
	ScrollView scrollView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_person);
		Intent i = getIntent();
		Contact contact = (Contact) i.getParcelableExtra("Contact");
	}
	@Override
	protected void onResume() {
		super.onResume();
		events = new DatabaseHelper(this).dbGetEventList();
		
		// Display the information
		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		this.setListAdapter(new EditContactAdapter(this, events));
		
		// Display a message if there are no events in the queue
		if (events.isEmpty()) {
			Toast.makeText(this, getString(R.string.no_events), Toast.LENGTH_LONG).show();
		}
	}
	public void ContactPerson(Contact contact) {
		EditText eText1 = (EditText) findViewById(R.id.firstName);
		EditText eText2 = (EditText) findViewById(R.id.surName);
		EditText eText3 = (EditText) findViewById(R.id.phoneNumber);
		this.firstName = contact.getFirstName();
		
		this.surname = contact.getSurName();
		
		this.phoneNumbers = contact.getPhoneNumber();
		
		eText1.setText(firstName);
		eText2.setText(surname);
		eText3.setText(phoneNumbers);
	}

}
