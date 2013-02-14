package no.ntnu.stud.fallprevention;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class EventDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventdetail);

		Intent motherIntent = getIntent();
		String message = motherIntent
				.getStringExtra("no.ntnu.stud.fallprevention.MESSAGE");
		TextView btw = (TextView) findViewById(R.id.textView1);
		btw.setText(message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_hendingsdetalj, menu);
		return true;
	}

}
