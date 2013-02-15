package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		
		// Open the SharedPrefrences-file, check whether a username is already stored in the phone
		SharedPreferences sp = getSharedPreferences("no.ntnu.stud.fallprevention.PREFILE", Context.MODE_PRIVATE);
		String name = sp.getString("name", "NONAME");
		
		// Either go the main screen, or go to write name screen
		if (name.equals("NONAME")) {
			Intent intent = new Intent(this, WriteName.class);
			//setContentView(R.layout.activity_write_name);
			startActivity(intent);
		} else {
			
			Intent intent = new Intent(this, MainScreen.class);
			intent.putExtra("no.ntnu.stud.fallprevention.MESSAGE", name);
			//setContentView(R.layout.activity_mainscreen);
			//TextView namnesyn = (TextView) findViewById(R.id.textView1);
			// namnesyn.setText(getString(R.string.greeting)+", "+namn);
			startActivity(intent);
		}
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_launch, menu);
		return true;
	}

}
