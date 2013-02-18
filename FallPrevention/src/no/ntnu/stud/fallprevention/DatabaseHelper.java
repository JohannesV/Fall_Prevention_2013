package no.ntnu.stud.fallprevention;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper is an object that builds and maintains the database. Or
 * something like that. I think.
 * 
 * @author elias
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 4;
	public static final String DATABASE_NAME = "FallPrevention.db";
	
	private static final String COMMA_SEP = ", ";
	private static final String START_PAR = " (";
	private static final String END_PAR = ") ";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		final String CREATE_TABLE_1 = 
				"CREATE TABLE " + DatabaseContract.EventType.TABLE_NAME + START_PAR +
				DatabaseContract.EventType.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
				DatabaseContract.EventType.COLUMN_NAME_TITLE + COMMA_SEP +
				DatabaseContract.EventType.COLUMN_NAME_DESCRIPTION + COMMA_SEP +
				DatabaseContract.EventType.COLUMN_NAME_ICON + END_PAR;
		final String CREATE_TABLE_2 = 
				"CREATE TABLE " + DatabaseContract.Event.TABLE_NAME + START_PAR +
				DatabaseContract.Event.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
				DatabaseContract.Event.COLUMN_NAME_TYPEID + END_PAR;
		final String FILL_INFO_1 = 
				"INSERT INTO EventType (TypeID, Description, Headline, Icon) VALUES (0, \'You should really keep working out and not be a lazy bastard!\', \'You are lazy!\', \'halo\')";
		final String FILL_INFO_2 = 
				"INSERT INTO EventType (TypeID, Description, Headline, Icon) VALUES (1, \'You are really cool, beacause you spend a lot of time clicking through our program!\', \'You are awesome!\', \'sleep\')";
		final String FILL_INFO_3 = 
				"INSERT INTO Event (ID, TypeID) VALUES (0, 0)";
		final String FILL_INFO_4 = 
				"INSERT INTO Event (ID, TypeID) VALUES (1, 1)";
		final String FILL_INFO_5 = 
				"INSERT INTO Event (ID, TypeID) VALUES (2, 1)";
		
		db.execSQL(CREATE_TABLE_1);
		db.execSQL(CREATE_TABLE_2);
		db.execSQL(FILL_INFO_1);
		db.execSQL(FILL_INFO_2);
		db.execSQL(FILL_INFO_3);
		db.execSQL(FILL_INFO_4);
		db.execSQL(FILL_INFO_5);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Clears the database on an upgrade, and reset it
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Event.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.EventType.TABLE_NAME);
		onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: May be implemented
	}

}
