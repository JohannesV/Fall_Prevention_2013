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
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
/**
 * Activity class which makes the content in CreateContact available
 * The main purpose of this class is to create a new contact.
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
