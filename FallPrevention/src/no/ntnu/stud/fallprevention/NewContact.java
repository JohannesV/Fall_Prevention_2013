package no.ntnu.stud.fallprevention;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

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
	
	public void fireClick(int position) {
		final int _id = contacts.get(position).getId();
		AlertDialog.Builder alert_box=new AlertDialog.Builder(this);
		String message = getResources().getString(R.string.add_contact_confirm_dialogue_text_1) +
				" " + contacts.get(position).getName() + " " + 
				getResources().getString(R.string.add_contact_confirm_dialogue_text_2);
		alert_box.setMessage(message);
		alert_box.setPositiveButton(R.string.GENERAL_positive, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String contactName = new DatabaseHelper(getApplicationContext()).dbAddContact(Integer.toString(_id));
				String prefix = getResources().getString(R.string.contact_added);
				Toast.makeText(NewContact.this, prefix + " " + contactName, Toast.LENGTH_SHORT).show();
			}
		});
		alert_box.setNegativeButton(R.string.GENERAL_negative, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Pass
			}
		});
		alert_box.show();
	}
	
	public void populateContactList() {
        contacts = new ArrayList<Contact>();
		
        Cursor cursor = getContactsCursor();
        
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.entry_contact, cursor,
                fields, new int[] {R.id.contactName});
        
        for (int i = 0; i<cursor.getCount(); i++) {
        	cursor.moveToPosition(i);
        	String name = cursor.getString(1);
        	int id = cursor.getInt(0);
        	contacts.add(new Contact(name, id));
        }
        
       contactList.setAdapter(adapter);
	}
	
	public Cursor getContactsCursor() {
		 // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        // Only care about contacts with a phone number
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1";
        String[] selectionArgs = null;
        
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        
        return getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
	}

}