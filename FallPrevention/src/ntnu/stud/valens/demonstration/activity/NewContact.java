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

import java.util.ArrayList;
import java.util.List;
import ntnu.stud.valens.demonstration.R;
import ntnu.stud.valens.demonstration.connectivity.DatabaseHelper;
import ntnu.stud.valens.demonstration.datastructures.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Handles the screen for getting contacts from the phone contact list. 
 * 
 * @author Elias
 */
public class NewContact extends Activity {

	ListView contactList;
	List<Contact> contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_contact);

		// Get handles for UI objects
		contactList = (ListView) findViewById(android.R.id.list);

		// Add event listeners
		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				fireClick(position);
			}
		});

		populateContactList();
	}

	/**
	 * Handles click events
	 * 
	 * @param position
	 *            position for detection the position to display
	 */
	public void fireClick(int position) {
		final int _id = contacts.get(position).getId();
		AlertDialog.Builder alert_box = new AlertDialog.Builder(this);
		String message = getResources().getString(
				R.string.add_contact_confirm_dialogue_text_1)
				+ " "
				+ contacts.get(position).getName()
				+ " "
				+ getResources().getString(
						R.string.add_contact_confirm_dialogue_text_2);
		alert_box.setMessage(message);
		alert_box.setPositiveButton(R.string.GENERAL_positive,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String contactName = new DatabaseHelper(
								getApplicationContext()).dbAddContact(Integer
								.toString(_id));
						String prefix = getResources().getString(
								R.string.contact_added);
						Toast.makeText(NewContact.this,
								prefix + " " + contactName, Toast.LENGTH_SHORT)
								.show();
					}
				});
		alert_box.setNegativeButton(R.string.GENERAL_negative,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Pass
					}
				});
		alert_box.show();
	}

	/**
	 * Makes a structured contact list
	 */
	public void populateContactList() {
		getContacts();

		ArrayList<String> contactNames = new ArrayList<String>();

		for (Contact c : contacts) {
			contactNames.add(c.getName());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, contactNames);
		contactList.setAdapter(adapter);
	}

	/**
	 * Fetches the contacts from the phone database.
	 */
	public void getContacts() {
		// Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };
		// Only care about contacts with a phone number
		String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1";
		String[] selectionArgs = null;

		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		Cursor cursor = getContentResolver().query(uri, projection, selection,
				selectionArgs, sortOrder);
		// Iterate over cursor to extract the contacts
		contacts = new ArrayList<Contact>();

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			String name = cursor.getString(1);
			int id = cursor.getInt(0);
			contacts.add(new Contact(name, id));
		}
	}

	/**
	 * its a method for a button that sends you to the CreateContact screen
	 * 
	 * @param view
	 */
	public void createContact(View view) {
		Intent intent = new Intent(this, CreateContact.class);
		startActivity(intent);
	}
}
