package no.ntnu.stud.fallprevention.activity;

import no.ntnu.stud.fallprevention.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
/**
 * Creates the viewable contents for the settings screen
 * 
 *  @author Elias
 */
public class Settings extends PreferenceActivity {
	
    // Called when the activity is first created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}