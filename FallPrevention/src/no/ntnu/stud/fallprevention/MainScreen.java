package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainscreen);

		// Hent ut infoen som kom med intenten
		Intent motherIntent = getIntent();
		String name = motherIntent
				.getStringExtra("no.ntnu.stud.fallprevention.MESSAGE");
		name = getString(R.string.greeting) + ", " + name + "!";
		// Vis namnet på skjermen
		TextView namnesyn = (TextView) findViewById(R.id.textView1);
		namnesyn.setText(name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_hovudskjerm, menu);
		return true;
	}

	public void fireEvent(View view) {
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}

}
