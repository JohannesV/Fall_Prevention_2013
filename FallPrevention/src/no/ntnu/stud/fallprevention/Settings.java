package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final Button button = (Button) findViewById(R.id.nameButton);
		
	}
	// testChange
	void selfDestruct(){
		Intent intent = new Intent(this, WriteName.class);
		startActivity(intent);
	}

	//NExtTestChange

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
*/
}
