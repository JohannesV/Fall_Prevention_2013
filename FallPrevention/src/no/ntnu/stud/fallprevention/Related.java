package no.ntnu.stud.fallprevention;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Related extends Activity {
	
	List<Contact> contacts = new ArrayList<Contact>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_related);
		//set inn getContactsfrom Database
		contacts = new DatabaseHelper(this).dbGetContactList();
		final ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
			
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						String c = (String) listView.getItemAtPosition(position);
						fireEvent(c);
					}  
			      }); 
		ArrayList<String> listItems = new ArrayList<String>();
		for (Contact c : contacts) {
			listItems.add(c.getName());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, listItems);
		listView.setAdapter( adapter );
		
		
		
	}
	protected void fireEvent(String c) {
		Intent intent = new Intent(this, ContactPerson.class);
		
		intent.putExtra("Contact", c);
		
		startActivity(intent);
	}
	public void fireNewPerson(View view){
		Intent intent = new Intent(this, NewContact.class);
		startActivity(intent);
	}


}
