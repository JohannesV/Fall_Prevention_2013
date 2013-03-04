package no.ntnu.stud.fallprevention;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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
		contactList.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				fireLongClick(position);
				return false;
			}
		});
		
		populateContactList();
	}

	public void fireLongClick(int position) {
		String _id = contacts.get(position).getId();
		String contactName = new DatabaseHelper(this).dbAddContact(_id);
		String prefix = getResources().getString(R.string.contact_added);
		Toast.makeText(NewContact.this, prefix + " " + contactName, Toast.LENGTH_SHORT).show();
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
        	String id = cursor.getString(0);
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