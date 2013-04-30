package ntnu.stud.valens.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is a database helper that is an object that builds and maintains the database. 
 * It also works as an interface to the database.
 * 
 * @author fiLLLip
 *
 */
public class CPValensDB extends SQLiteOpenHelper{
	/**
	 * These constants defines the name of the database being used
	 * by the content provider and the version. If the database
	 * is modified, the database version should be updated to the
	 * next whole number.
	 */
	private static final String DATABASE_NAME="valens.db";
	private static final int DATABASE_VERSION=2;

	/**
	 * Constants being used in creating database scheme to easily modify
	 * large amount of queries if needed.
	 */
	public static final String COMMA = ", ";
	public static final String START_PAR = " (";
	public static final String END_PAR = ") ";
	public static final String DOT = ".";
	public static final String EQUAL = "=";
	
	/**
	 * Constructor of the class.
	 * @param context The context of the application
	 */
	public CPValensDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Called when the database is created for the first time.
	 * @param db The database being used
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		final String CREATE_TABLE_RAW_STEPS = "CREATE TABLE "
				+ DBSchema.RawSteps.TABLE_NAME + START_PAR
				+ DBSchema.RawSteps.COLUMN_NAME_TIMESTAMP
				+ " INTEGER PRIMARY KEY" + COMMA
				+ DBSchema.RawSteps.COLUMN_NAME_SOURCE + END_PAR;
		final String CREATE_TABLE_TESTS = "CREATE TABLE "
				+ DBSchema.Tests.TABLE_NAME + START_PAR
				+ DBSchema.Tests.COLUMN_NAME_TIMESTAMP
				+ " INTEGER PRIMARY KEY" + COMMA
				+ DBSchema.Tests.COLUMN_NAME_SCORE + COMMA
				+ DBSchema.Tests.COLUMN_NAME_TYPE_CODE_KEY +END_PAR;
		final String CREATE_TABLE_TEST_TYPES = "CREATE TABLE "
				+ DBSchema.TestTypes.TABLE_NAME + START_PAR
				+ DBSchema.TestTypes.COLUMN_NAME_CODE_KEY
				+ " INTEGER PRIMARY KEY" + COMMA
				+ DBSchema.TestTypes.COLUMN_NAME_DESCRIPTION + COMMA
				+ DBSchema.TestTypes.COLUMN_NAME_NAME + COMMA 
				+ DBSchema.TestTypes.COLUMN_NAME_SCORE_DESCRIPTION + END_PAR;
		final String CREATE_TABLE_STEPS = "CREATE TABLE "
				+ DBSchema.Steps.TABLE_NAME + START_PAR
				+ DBSchema.Steps.COLUMN_NAME_TIMESTAMP
				+ " INTEGER PRIMARY KEY " + END_PAR;
		final String CREATE_TABLE_GAITS = "CREATE TABLE "
				+ DBSchema.Gaits.TABLE_NAME + START_PAR
				+ DBSchema.Gaits.COLUMN_NAME_SPEED + COMMA
				+ DBSchema.Gaits.COLUMN_NAME_VARIABILITY + COMMA
				+ DBSchema.Gaits.COLUMN_NAME_START + COMMA 
				+ DBSchema.Gaits.COLUMN_NAME_STOP + END_PAR;
		db.execSQL(CREATE_TABLE_RAW_STEPS);
		db.execSQL(CREATE_TABLE_TEST_TYPES);
		db.execSQL(CREATE_TABLE_TESTS);
		db.execSQL(CREATE_TABLE_STEPS);
		db.execSQL(CREATE_TABLE_GAITS);
	}

	/**
	 * Called when the database needs to be upgraded.
	 * @param db The database that needs to be upgraded
	 * @param oldVersion The old version of the database
	 * @param newVersion The new version of the database
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		reset(db);
	}
	
	/**
	 * Resets the database to it's new condition. Drops all data and reinitializes it.
	 * @param db The database being reset
	 */
	public void reset(SQLiteDatabase db) {
		try {
			db.execSQL("DROP TABLE " + DBSchema.RawSteps.TABLE_NAME);
		} catch (SQLiteException e) {
			// Do nothing...
		}
		try {
			db.execSQL("DROP TABLE " + DBSchema.Tests.TABLE_NAME);
		} catch (SQLiteException e) {
			// Do nothing...
		}
		try {
			db.execSQL("DROP TABLE " + DBSchema.TestTypes.TABLE_NAME);
		} catch (SQLiteException e) {
			// Do nothing...
		}
		try {
			db.execSQL("DROP TABLE " + DBSchema.Gaits.TABLE_NAME);
		} catch (SQLiteException e) {
			// Do nothing...
		}
		try {
			db.execSQL("DROP TABLE " + DBSchema.Steps.TABLE_NAME);
		} catch (SQLiteException e) {
			// Do nothing...
		}
		onCreate(db);
	}

}
