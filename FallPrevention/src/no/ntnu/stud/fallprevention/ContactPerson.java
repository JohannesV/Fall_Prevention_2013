package no.ntnu.stud.fallprevention;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
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
	
	public void fireDelete(View view){
		// Ask for confirmation
		AlertDialog.Builder alert_box = new AlertDialog.Builder(this);
		String message = getResources().getString(
				R.string.contact_person_delete_dialogue_1)
				+ " "
				+ editName.getText().toString()
				+ " "
				+ getResources().getString(
						R.string.contact_person_delete_dialogue_2);
		alert_box.setMessage(message);
		alert_box.setPositiveButton(R.string.GENERAL_positive,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Delete contatct from database
						new DatabaseHelper(getApplicationContext()).dbDeleteContact(contact);
						// Return to the contact list screen
						Intent intent = new Intent(getApplicationContext(), Related.class);
						startActivity(intent);
					}
				});
		alert_box.setNegativeButton(R.string.GENERAL_negative,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// To nothing
					}
				});
		alert_box.show();
	}

	public void fireStore(View view) {
		// Update the database
		contact.setName(editName.getText().toString());
		contact.setPhoneNumber(editNumber.getText().toString());
		new DatabaseHelper(this).dbUpdateContact(contact);
		// Return to the contact list screen
		Intent intent = new Intent(this, Related.class);
		startActivity(intent);
	}
}
