package no.ntnu.stud.fallprevention.activity;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.R.id;
import no.ntnu.stud.fallprevention.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

/**
 * Class for editing the user name and displaying it.
 * Usually used when entering the program for the first time,
 */
public class WriteName extends Activity {

	@Override
	/**
	 * Creates the viewable contents on the screen 
	 */
	 protected void onCreate(Bundle savedInstanceState) {
	   	  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_write_name);
  	 }
	

	 /**
	  * Method is fired by button click. Takes the user to the main screen. Updates the name
	  * @param view 
	  */
	  public void fireName(View view) {
	      // Declerates new instance of the class MainScreen
		  // which shows the smiley-feedbacks
		  Intent intent = new Intent(this, MainScreen.class);
		  // Extract information from text field
		  EditText eText = (EditText) findViewById(R.id.editText1);
		  String name = eText.getText().toString();
		  // Store the name in the phone, using the shared preferences file
		  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		  SharedPreferences.Editor editor = sp.edit();
		  // Displays the edited name
		  editor.putString("name",name);
		  editor.commit();
		  // Start next activity
		  startActivity(intent);
		  //Makes sure this activity goes away and cannot be accessed with back-button
		  this.finish();
		
      } 

}
