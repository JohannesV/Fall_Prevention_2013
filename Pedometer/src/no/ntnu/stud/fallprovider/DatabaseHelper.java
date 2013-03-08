package no.ntnu.stud.fallprovider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database helper is an object that builds and maintains the database. It also
 * works as an interface to the database.
 * 
 * @author Filip
 * 
 */

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "no.ntnu.stud.fallprovider.DatabaseHelper";

	public static final int DATABASE_VERSION = 3;
	public static final String DATABASE_NAME = "FallService.db";

	public static final String COMMA = ", ";
	public static final String START_PAR = " (";
	public static final String END_PAR = ") ";
	public static final String DOT = ".";
	public static final String EQUAL = "=";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Fill the database with some random entries, and of course build
		// thetables
		final String CREATE_TABLE_1 = "CREATE TABLE "
				+ DatabaseContract.Movement.TABLE_NAME + START_PAR
				+ DatabaseContract.Movement.COLUMN_NAME_ID
				+ " integer primary key autoincrement,"
				+ DatabaseContract.Movement.COLUMN_NAME_STEPS + " integer,"
				+ DatabaseContract.Movement.COLUMN_NAME_DISTANCE + " real,"
				+ DatabaseContract.Movement.COLUMN_NAME_TIMESTART + " date,"
				+ DatabaseContract.Movement.COLUMN_NAME_TIMEEND + " date,"
				+ END_PAR;
		db.execSQL(CREATE_TABLE_1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Clears the database on an upgrade, and reset it
		db.execSQL("DROP TABLE " + DatabaseContract.Movement.TABLE_NAME);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Does the same as if the database has been upgraded.
		onUpgrade(db, oldVersion, newVersion);
	}

	/**
	 * Fetches a list of (Event.ID, EventType.Description, EventType.Icon)
	 * tuples from the database.
	 * 
	 * @return
	 */
	public List<Movement> dbGetEventList() {
		List<Movement> movements = new ArrayList<Movement>();
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor c = db.rawQuery("SELECT " + 
				DatabaseContract.Movement.COLUMN_NAME_ID + COMMA +
				DatabaseContract.Movement.COLUMN_NAME_STEPS + COMMA +
				DatabaseContract.Movement.COLUMN_NAME_DISTANCE + COMMA +
				DatabaseContract.Movement.COLUMN_NAME_TIMESTART + COMMA +
				DatabaseContract.Movement.COLUMN_NAME_TIMEEND + 
				" FROM " + DatabaseContract.Movement.TABLE_NAME, null);

		// Iterate over the data fetched
		for (int i = 0; i < c.getCount(); i++) {
			c.moveToPosition(i);
			Movement m = new Movement(c.getInt(0), c.getInt(1), c.getDouble(2), Helper.parseDate(c.getString(3)), Helper.parseDate(c.getString(4)));
			movements.add(m);
		}
		
		// Close database connection
		c.close();
		db.close();
		
		return movements;
	}
	
	public void AddMovement (int steps, double distance, Date timeStart, Date timeEnd){
		
	}
}
