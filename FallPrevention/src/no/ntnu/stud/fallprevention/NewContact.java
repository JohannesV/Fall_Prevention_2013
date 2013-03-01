package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class NewContact extends Activity {

	ListView contactList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_contact);
		
		// Get handles for UI objects
		contactList = (ListView) findViewById(android.R.id.list);
		
		// TODO: Add event listeners
		contactList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Toast.makeText(NewContact.this, "Click: " + position, Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
		populateContactList();
	}

	public void populateContactList() {
        // Build adapter with contact entries
        Cursor cursor = getContacts();
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.entry_contact, cursor,
                fields, new int[] {R.id.contactName});
       contactList.setAdapter(adapter);
	}
	
	public Cursor getContacts() {
		 // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
	}

}