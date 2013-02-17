package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class EventDetail extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventdetail);

		Intent motherIntent = getIntent();
		String message = motherIntent
				.getStringExtra("no.ntnu.stud.fallprevention.MESSAGE");
		TextView textView = (TextView) findViewById(R.id.headlineTextView);
		textView.setText(message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_eventdetail, menu);
		return true;
	}
	
	public void fireDeleteButton(View view) {
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}
	
	public void fireKeepButton(View view) {
		Intent intent = new Intent(this, EventList.class);
		startActivity(intent);
	}
}
