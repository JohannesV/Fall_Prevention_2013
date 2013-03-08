package no.ntnu.stud.fallprovider;

import android.provider.BaseColumns;

/**
 * Database Contract specifies the details of the application's database.
 * Using a database contract to store all the table and column names is a
 * common way to ensure consistent name usage throughout the application.
 * 
 * @author Filip
 *
 */

public abstract class DatabaseContract {
	
	public static abstract class Movement implements BaseColumns {
		public static final String TABLE_NAME = "Movement";
		public static final String COLUMN_NAME_ID = "ID"; 
		public static final String COLUMN_NAME_STEPS = "Steps";
		public static final String COLUMN_NAME_DISTANCE = "Distance";
		public static final String COLUMN_NAME_TIMESTART = "TimeStart";
		public static final String COLUMN_NAME_TIMEEND = "TimeEnd"; 
	}
}
