package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		// Open SharedPrefrences-fila, og finn ut om namn allereie er lagra i mobilen
		SharedPreferences sp = getSharedPreferences("com.example.mockonthetable.PREFILE", Context.MODE_PRIVATE);
		String namn = sp.getString("namn", "NONAME");
		
		// Anten gå rett til hovudmeny, eller be brukar skrive inn namn
		if (namn.equals("NONAME")) {
			Intent intent = new Intent(this, SkrivNamn.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, Hovudskjerm.class);
			intent.putExtra("com.example.mockonthetable.MESSAGE", namn);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_launch, menu);
		return true;
	}

}
