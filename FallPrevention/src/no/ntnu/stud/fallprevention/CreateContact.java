package no.ntnu.stud.fallprevention;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

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