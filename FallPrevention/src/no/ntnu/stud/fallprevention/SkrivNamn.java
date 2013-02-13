package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SkrivNamn extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skriv_namn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_skriv_namn, menu);
		return true;
	}
	
	/** Blir fyrt av knappen. Skift til hovudskjerm **/
	public void fyrNamn(View view) {
		Intent intent = new Intent(this, Hovudskjerm.class);
		// Hent ut info frå teksten
		EditText eText = (EditText) findViewById(R.id.editText1);
		String namn = eText.getText().toString();
		// Legg inn teksten i intenten
		intent.putExtra("com.example.mockonthetable.MESSAGE", namn);
		// Lagre namn i prefrences-fila
		SharedPreferences sp = getSharedPreferences("com.example.mockonthetable.PREFILE", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("namn",namn);
		editor.commit();
		// Fyr neste aktivitet
		startActivity(intent);
		
	}

}
