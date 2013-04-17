package no.ntnu.stud.fallprevention.activity;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.R.xml;
import android.os.Bundle;
import android.preference.PreferenceActivity;
/**
 * Calls the methods onCreate to prepare the screen for the program
 * and sets the activity for setting.
 * @author Tayfun
 *
 */
public class Settings extends PreferenceActivity {
    // Called when the activity is first created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
    }
}