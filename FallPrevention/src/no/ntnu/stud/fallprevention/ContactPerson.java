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
	int phoneNumbers;
	String name; 
	List<String> alarms;
	ListView listView;

	
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
		alarms = new DatabaseHelper(this).dbGetAlarmTypes();
		
		// Display the information
		listView = (ListView) findViewById(android.R.id.list);
		this.setListAdapter(new EditContactAdapter(this, alarms));
	}
	public void ContactPerson(Contact contact) {
		EditText eText1 = (EditText) findViewById(R.id.name);
		EditText eText2 = (EditText) findViewById(R.id.phoneNumber);
		
		this.name = contact.getName();
		
		this.phoneNumbers = contact.getPhoneNumber();
		
		eText1.setText(name);
		eText2.setText(phoneNumbers);
	}
	public void FireBack (View view){
		Intent intent = new Intent(this, Related.class);
		startActivity(intent);
		
	}

}
