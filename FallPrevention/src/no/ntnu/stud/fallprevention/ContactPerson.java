package no.ntnu.stud.fallprevention;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ContactPerson extends ListActivity {
	
	Contact contact;
	List<String> alarms;
	ListView listView;
	EditText editName, editNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_person);
		// Fetch detailed info about the contact
		Intent i = getIntent();
		int id = i.getIntExtra("no.ntnu.stud.fallprevention.CONTACT_ID_MESSAGE", -1);
		contact = new DatabaseHelper(this).dbGetContact(id);
		fillFields();
	}

	@Override
	protected void onResume() {
		super.onResume();
		alarms = new DatabaseHelper(this).dbGetAlarmTypes();
		
		// Display the information
		listView = (ListView) findViewById(android.R.id.list);
		this.setListAdapter(new EditContactAdapter(this, alarms));
	}
	
	public void fillFields() {
		editName = (EditText) findViewById(R.id.name);
		editNumber = (EditText) findViewById(R.id.phone_number);
		
		editName.setText(contact.getName());
		editNumber.setText(contact.getPhoneNumber());
	}
	
	public void fireStore (View view){
		contact.setName(editName.getText().toString());
		contact.setPhoneNumber(editNumber.getText().toString());
		new DatabaseHelper(this).dbUpdateContact(contact);
		Intent intent = new Intent(this, Related.class);
		startActivity(intent);
	}

}
