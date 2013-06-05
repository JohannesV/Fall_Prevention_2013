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
