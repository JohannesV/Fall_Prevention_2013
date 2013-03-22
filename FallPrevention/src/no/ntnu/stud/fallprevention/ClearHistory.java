package no.ntnu.stud.fallprevention;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * This class will Clear your personal data from the app.
 * This includes Name, Settings and Data stored by the program.
 * @author Dot
 *
 */
public class ClearHistory extends Preference {

	Context context;
	/**
	 * This method creates an Object of ClearHistory 
	 * @param context
	 */
	public ClearHistory(Context context) {
		super(context);
		this.context = context;
	}
	/**
	 * This method creates an Object of ClearHistory 
	 * @param context
	 * @param attr
	 */
	public ClearHistory(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context;
	}
	/**
	 * This method creates an object of ClearHistory
	 * @param context
	 * @param attr
	 * @param defStyle
	 */
	public ClearHistory(Context context, AttributeSet attr,
			int defStyle) {
		super(context, attr, defStyle);
		this.context = context;
	}
	/**
	 * Deletes all local data from the database
	 * TODO: Delete name and settings
	 */
	@Override
	protected void onClick() {
		AlertDialog.Builder alert_box = new AlertDialog.Builder(context);
		String message = context.getResources().getString(R.string.preferences_clear_history_warning);
		alert_box.setMessage(message);
		alert_box.setPositiveButton(R.string.GENERAL_positive, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear database data
				new DatabaseHelper(context).dbClearAllData();
				// Restart the program and show a message
				Toast.makeText(context, context.getResources().getString(R.string.preferences_data_cleared), Toast.LENGTH_SHORT).show();
			}
		});
		alert_box.setNegativeButton(R.string.GENERAL_negative, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Pass
			}
		});
		alert_box.show();
	}

}
