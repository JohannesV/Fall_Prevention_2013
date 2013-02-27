package no.ntnu.stud.fallprevention;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Related extends Activity{
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_related);
		//set inn getContactsfrom Database
		contacts.add(new Contact("Tore", "Hansen", 42341515));
		ListView listView = (ListView) findViewById(R.id.listView1);
		
		ArrayList<String> listItems = new ArrayList<String>();
		for (Contact c : contacts) {
			listItems.add(c.toString());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, listItems);
		listView.setAdapter( adapter );
		listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  
			   public void onItemSelected(AdapterView parentView, View childView, int position, long id) {  
			            fireEventEditContact();  
			        }
			        public void onNothingSelected(AdapterView parentView) {  

			        }  
			      });  
	}
	
	public void fireEventEditContact(){
		Toast t = new Toast(this);
		t.setDuration(Toast.LENGTH_LONG);
		t.setText("askiiiii");
		t.show();
	}
	
	



}
