package no.ntnu.stud.fallprevention;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;

public class Related extends Activity {
	List<Contact> contactPersons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_related);
		//set inn getContactsfrom Database
		contactPersons.add(new Contact("Tore", "Hansen", 42341515));
	}



}
