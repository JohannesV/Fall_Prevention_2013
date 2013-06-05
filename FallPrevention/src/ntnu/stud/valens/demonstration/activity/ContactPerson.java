/*******************************************************************************
 * Licensed to UbiCollab.org under one or more contributor
 * license agreements.  See the NOTICE file distributed 
 * with this work for additional information regarding
 * copyright ownership. UbiCollab.org licenses this file
 * to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package ntnu.stud.valens.demonstration.activity;

import java.util.List;

import ntnu.stud.valens.demonstration.R;
import ntnu.stud.valens.demonstration.connectivity.DatabaseHelper;
import ntnu.stud.valens.demonstration.datastructures.Contact;
import ntnu.stud.valens.demonstration.listadapters.EditContactAdapter;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Activity class which makes the content in ContactPerson available
 * The main purpose of this class is to edit a contact.
 * 
 */

public class ContactPerson extends ListActivity {
	
	Contact contact;
	List<String> alarms;
	ListView listView;
	EditText editName, editNumber;
	
	
	/**
	 * Adds information when the ContactPerson Activity is created
	 * Requires ContactID to get the information. 
	 * @param: savedInstanceState
	 */
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
	/**
	 * Adds more information when updated
	 * Will update when you click on the content
	 */
	@Override
	protected void onResume() {
		super.onResume();
		alarms = new DatabaseHelper(this).dbGetAlarmTypes();
		
		// Display the information
		listView = (ListView) findViewById(android.R.id.list);
		this.setListAdapter(new EditContactAdapter(this, alarms));
	}
	/**
	 * Add related information to the Fields with the given ID
	 */
	public void fillFields() {
		editName = (EditText) findViewById(R.id.name);
		editNumber = (EditText) findViewById(R.id.phone_number);
		
		editName.setText(contact.getName());
		editNumber.setText(contact.getPhoneNumber());
	}
	/**
	 * Will fire an event which deletes an Contact form the list in ContactPerson
	 * @param view
	 */
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
	/**
	 * Fire an event to Store data(Name,PhoneNumber) about the Contact we are currently viewing.
	 * @param view
	 */
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
