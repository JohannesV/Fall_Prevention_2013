package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WriteName extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_name);
	}
	
	/** Method is fired by button click. Takes the user to the main screen **/
	public void fireName(View view) {
		Intent intent = new Intent(this, MainScreen.class);
		// Extract information from text field
		EditText eText = (EditText) findViewById(R.id.editText1);
		String name = eText.getText().toString();
		// Add the name as extra information to the intent 
		intent.putExtra("no.ntnu.stud.fallprevention.MESSAGE", name);
		// Store the name in the phone, using the shared prefrences file
		SharedPreferences sp = getSharedPreferences("no.ntnu.stud.fallprevention.PREFILE", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("name",name);
		editor.commit();
		// Start next activity
		startActivity(intent);
		//Makes sure this activity goes away and cannot be accessed with back-button
		this.finish();
		
	}

}
