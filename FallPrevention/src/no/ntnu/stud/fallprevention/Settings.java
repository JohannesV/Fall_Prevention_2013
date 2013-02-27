package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final Button button = (Button) findViewById(R.id.nameButton);
		
	}
	// testChang55
	public void selfDestruct(View v){
		Intent intent = new Intent(this, WriteName.class);
		startActivity(intent);
	}

	

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
*/
}
