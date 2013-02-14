package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class WriteName extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_skriv_namn, menu);
		return true;
	}
	
	/** Blir fyrt av knappen. Skift til hovudskjerm **/
	public void fireName(View view) {
		Intent intent = new Intent(this, MainScreen.class);
		// Hent ut info frå teksten
		EditText eText = (EditText) findViewById(R.id.editText1);
		String name = eText.getText().toString();
		// Legg inn teksten i intenten
		intent.putExtra("no.ntnu.stud.fallprevention.MESSAGE", name);
		// Lagre namn i prefrences-fila
		SharedPreferences sp = getSharedPreferences("no.ntnu.stud.fallprevention.PREFILE", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("name",name);
		editor.commit();
		// Fyr neste aktivitet
		startActivity(intent);
		//Makes sure this activity goes away and cannot be accessed with back-button
		this.finish();
		
	}

}
