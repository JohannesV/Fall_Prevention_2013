package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetLevel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_level);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_write_level, menu);
		return true;
	}*/
	
	/** Method is fired by button click. Takes the user to the main screen **/
	public void fireLevel(View view) {
		Intent intent = new Intent(this, MainScreen.class);
		// Extract information from text field
		EditText eText = (EditText) findViewById(R.id.editText1);
		String level = eText.getText().toString();
		// Add the level as extra information to the intent 
		intent.putExtra("no.ntnu.stud.fallprevention.MESSAGE", level);
		// Store the level in the phone, using the shared prefrences file
		SharedPreferences sp = getSharedPreferences("no.ntnu.stud.fallprevention.PREFILE", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("Level",level);
		editor.commit();
		// Start next activity
		startActivity(intent);
		//Makes sure this activity goes away and cannot be accessed with back-button
		this.finish();
		
	}

}
