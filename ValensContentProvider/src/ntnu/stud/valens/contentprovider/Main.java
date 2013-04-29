package ntnu.stud.valens.contentprovider;

import ntnu.stud.valens.contentprovider.calculations.ManipulatorHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					SQLiteDatabase db = new CPValensDB(getApplicationContext())
							.getWritableDatabase();
					new CPValensDB(getApplicationContext()).reset(db);
					updateDebugFields();
					Toast.makeText(getApplicationContext(), "Deleted data",
							Toast.LENGTH_SHORT).show();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
			}
		};
		final AlertDialog.Builder alertDeleteYesNo = new AlertDialog.Builder(
				this).setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener);

		DialogInterface.OnClickListener dialogClickListenerCalculations = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
		            new ManipulatorHelper().calculate(getApplicationContext());
					updateDebugFields();
					Toast.makeText(getApplicationContext(), "Did calculations",
							Toast.LENGTH_SHORT).show();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No-button clicked, don't do anything.
					break;
				}
			}
		};
		final AlertDialog.Builder alertDeleteYesNoCalculations = new AlertDialog.Builder(
				this).setPositiveButton("Yes", dialogClickListenerCalculations)
				.setNegativeButton("No", dialogClickListenerCalculations);

		final Button buttonDelete = (Button) findViewById(R.id.btnDeleteDB);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDeleteYesNo.setMessage("Are you sure?").show();
			}
		});

		final Button buttonCalculations = (Button) findViewById(R.id.btnDoCalculations);
		buttonCalculations.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDeleteYesNoCalculations.setMessage("Are you sure?").show();
			}
		});
		updateDebugFields();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateDebugFields();
	}

	private void updateDebugFields() {
		TextView rawSteps = (TextView) findViewById(R.id.txtRawSteps);
		TextView steps = (TextView) findViewById(R.id.txtSteps);
		TextView gaits = (TextView) findViewById(R.id.txtGaits);
		TextView tests = (TextView) findViewById(R.id.txtTests);
		TextView testTypes = (TextView) findViewById(R.id.txtTestTypes);

		SQLiteDatabase db = new CPValensDB(getApplicationContext())
				.getReadableDatabase();

		Cursor mCount = db.rawQuery("select count(*) from "
				+ DBSchema.RawSteps.TABLE_NAME, null);
		mCount.moveToFirst();
		rawSteps.setText(Integer.toString(mCount.getInt(0)));
		mCount.close();

		mCount = db.rawQuery("select count(*) from "
				+ DBSchema.Steps.TABLE_NAME, null);
		mCount.moveToFirst();
		steps.setText(Integer.toString(mCount.getInt(0)));
		mCount.close();

		mCount = db.rawQuery("select count(*) from "
				+ DBSchema.Gaits.TABLE_NAME, null);
		mCount.moveToFirst();
		gaits.setText(Integer.toString(mCount.getInt(0)));
		mCount.close();

		mCount = db.rawQuery("select count(*) from "
				+ DBSchema.Tests.TABLE_NAME, null);
		mCount.moveToFirst();
		tests.setText(Integer.toString(mCount.getInt(0)));
		mCount.close();

		mCount = db.rawQuery("select count(*) from "
				+ DBSchema.TestTypes.TABLE_NAME, null);
		mCount.moveToFirst();
		testTypes.setText(Integer.toString(mCount.getInt(0)));
		mCount.close();
	}
}
