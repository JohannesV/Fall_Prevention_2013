package ntnu.stud.valens.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * @author fiLLLip
 * @author Elias
 *
 */
public class ValensDataProvider extends ContentProvider {

	/**
	 * Constant defining the TAG for debugging
	 */
	private static final String TAG = "ValensDataProvider";
	
	// the underlying database
	private SQLiteDatabase db = null;

	/**
	 * Does nothing at the moment, since delete is not valid from external apps.
	 * Should handle requests to delete one or more rows given a selection.
	 * 
	 * @param uri The full URI to query, including a row ID (if a specific record is requested).
	 * @param selection An optional restriction to apply to rows when deleting.
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new IllegalArgumentException("Delete is not a valid operation!");
	}

	/**
	 * Handles requests for the MIME type of the data at the given URI. 
	 * The returned MIME type should start with vnd.android.cursor.item 
	 * for a single record, or vnd.android.cursor.dir/ for multiple items. 
	 * This method can be called from multiple threads
	 * 
	 * @param uri The URI to query.
	 */
	@Override
	public String getType(Uri uri) {
		switch (URI_MATCHER.match(uri)) {
		case STEPS:
			return Steps.CONTENT_TYPE;
		case RAW_STEPS:
			return RawSteps.CONTENT_TYPE;
		case GAIT:
			return Gait.CONTENT_TYPE;
		case TESTS:
			return Tests.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	/**
	 * Handles requests to insert a new row.
	 * @param uri The content:// URI of the insertion request.
	 * @param values A set of column_name/value pairs to add to the database.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Log.v(TAG,"Trying to insert!");
		long insertedInRow;
		switch (URI_MATCHER.match(uri)) {
		case RAW_STEPS:
			Log.v(TAG, "Inserting into raw_steps with content:"+values.toString());
			 insertedInRow = db.insert(DBSchema.RawSteps.TABLE_NAME, null,
					values);
			if (insertedInRow > 0) {
				Uri itemUri = ContentUris.withAppendedId(uri, insertedInRow);
				getContext().getContentResolver().notifyChange(itemUri, null);
				Log.v(TAG,"Inserted into row:"+itemUri.toString());
				return itemUri;
			}
			break;
			//throw new SQLException("Problem while inserting into "
				//	+ DBSchema.RawSteps.TABLE_NAME + ", uri: " + uri);
			
		case STEPS:
		    Log.v(TAG, "Inserting into steps with content:"+values.toString());
		    insertedInRow= db.insert(DBSchema.Steps.TABLE_NAME, null, values);
		    if(insertedInRow>0){
		        Uri itemUri=ContentUris.withAppendedId(uri, insertedInRow);
		        getContext().getContentResolver().notifyChange(itemUri, null);
		        Log.v(TAG, "Inserted into row:"+ itemUri.toString());
		        return itemUri;
		    }
		    break;
            
		    
		case GAIT:
		    Log.v(TAG, "Inserting into gait with content:"+values.toString());
		    insertedInRow=db.insert(DBSchema.Gaits.TABLE_NAME, null, values);
	          if(insertedInRow>0){
	                Uri itemUri=ContentUris.withAppendedId(uri, insertedInRow);
	                getContext().getContentResolver().notifyChange(itemUri, null);
	                Log.v(TAG, "Inserted into row:"+ itemUri.toString());
	                return itemUri;
	          }
	          break;
		
			
			
		default:
		
			throw new IllegalArgumentException(
					"Unsupported URI for insertion: " + uri); 
			
		}
		return null;
	}

	/**
	 *  Initializes the content provider on startup.
	 */
	@Override
	public boolean onCreate() {
		this.db = new CPValensDB(this.getContext()).getWritableDatabase();
		if (this.db == null) {
			return false;
		}
		if (this.db.isReadOnly()) {
			this.db.close();
			this.db = null;
			return false;
		}
		return true;
	}

	/**
	 * Handles query requests from clients.
	 * 
	 * @param uri The URI to query. This will be the full URI sent by the client; if the client is requesting a specific record, the URI will end in a record number that the implementation should parse and add to a WHERE or HAVING clause, specifying that _id value.
	 * @param projection	The list of columns to put into the cursor. If null all columns are included.
	 * @param selection	A selection criteria to apply when filtering rows. If null then all rows are included.
	 * @param selectionArgs	You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
	 * @param sortOrder	How the rows in the cursor should be sorted. If null then the provider is free to define the sort order.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;

		Log.v(TAG, String.valueOf(URI_MATCHER.match(uri)));
		switch (URI_MATCHER.match(uri)) {
		case TESTS:
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = Tests.SORT_ORDER_DEFAULT;
			}
			String queryTest = "select a."
					+ DBSchema.Tests.COLUMN_NAME_TIMESTAMP + ", a."
					+ DBSchema.Tests.COLUMN_NAME_SCORE + ", b."
					+ DBSchema.TestTypes.COLUMN_NAME_NAME + ", b."
					+ DBSchema.TestTypes.COLUMN_NAME_DESCRIPTION + ", b."
					+ DBSchema.TestTypes.COLUMN_NAME_SCORE_DESCRIPTION
					+ "from " + DBSchema.Tests.TABLE_NAME + " a, "
					+ DBSchema.TestTypes.TABLE_NAME + " b where a."
					+ DBSchema.Tests.COLUMN_NAME_TYPE_CODE_KEY + "=b."
					+ DBSchema.TestTypes.COLUMN_NAME_CODE_KEY + " order by "
					+ Tests.SORT_ORDER_DEFAULT;
			cursor = this.db.rawQuery(queryTest, selectionArgs);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			break;
		default:
			cursor = this.db.query(uri.getLastPathSegment(), projection,
					selection, selectionArgs, null, null, null);
			break;
		}
		return cursor;
	}

	/**
	 * Does nothing at the moment, since update is not valid from external apps.
	 * Should handle requests to delete one or more rows given a selection.
	 * 
	 * @param uri	The URI to query. This can potentially have a record ID if this is an update request for a specific record.
	 * @param values	A Bundle mapping from column names to new column values (NULL is a valid value).
	 * @param selection	An optional filter to match rows to update.
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new IllegalArgumentException("Update is not a valid operation!");
	}

	// public constants for client development
	public static final String AUTHORITY = "ntnu.stud.valens.contentprovider";
	public static final Uri STEPS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + Steps.CONTENT_PATH);
	public static final Uri GAIT_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + Gait.CONTENT_PATH);
	public static final Uri RAW_STEPS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + RawSteps.CONTENT_PATH);
	public static final Uri TESTS_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + Tests.CONTENT_PATH);

	/**
	 * Column and content type definitions for the Steps.
	 */
	public static interface Steps extends BaseColumns {
		public static final Uri CONTENT_URI = ValensDataProvider.STEPS_CONTENT_URI;
		public static final String TIMESTAMP = "timestamp";
		public static final String TIMESTAMP_START = "timestamp";
		public static final String TIMESTAMP_END = "timestamp";
		public static final String CONTENT_PATH = "steps";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd.valens.steps";
		public static final String[] PROJECTION_ALL = { TIMESTAMP_START,
				TIMESTAMP_END };
		public static final String SORT_ORDER_DEFAULT = TIMESTAMP + " DESC";
	}

	/**
	 * Column and content type definitions for the Steps.
	 */	
	public static interface RawSteps extends BaseColumns {
		public static final Uri CONTENT_URI = ValensDataProvider.RAW_STEPS_CONTENT_URI;
		public static final String TIMESTAMP = "timestamp";
		public static final String SOURCE = "source";
		public static final String CONTENT_PATH = "raw_steps";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd.valens.rawsteps";
		public static final String[] PROJECTION_ALL = { TIMESTAMP, SOURCE };
		public static final String SORT_ORDER_DEFAULT = TIMESTAMP + " DESC";
	}

	/**
	 * Column and content type definitions for the Steps.
	 */
	public static interface Gait extends BaseColumns {
		public static final Uri CONTENT_URI = ValensDataProvider.GAIT_CONTENT_URI;
		public static final String SPEED = "speed";
		public static final String VARIABILITY = "variability";
		public static final String START = "start";
		public static final String END = "end";
		public static final String CONTENT_PATH = "gaits";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd.valens.gait";
		public static final String[] PROJECTION_ALL = { SPEED,
				VARIABILITY, START, END };
		public static final String SORT_ORDER_DEFAULT = START
				+ " DESC";
	}

	/**
	 * Column and content type definitions for the Steps.
	 */
	public static interface Tests extends BaseColumns {
		public static final Uri CONTENT_URI = ValensDataProvider.TESTS_CONTENT_URI;
		public static final String TIMESTAMP = "timestamp";
		public static final String SCORE = "score";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String SCORE_DESCRIPTION = "score_description";
		public static final String CONTENT_PATH = "tests";
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd.valens.gait";
		public static final String[] PROJECTION_ALL = { TIMESTAMP, SCORE, NAME,
				DESCRIPTION, SCORE_DESCRIPTION };
		public static final String SORT_ORDER_DEFAULT = TIMESTAMP + " DESC";
	}

	// helper constants for use with the UriMatcher
	private static final int STEPS = 1;
	private static final int GAIT = 2;
	private static final int RAW_STEPS = 3;
	private static final int TESTS = 4;
	private static final UriMatcher URI_MATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);

	// prepare the UriMatcher
	static {
		URI_MATCHER.addURI(AUTHORITY, Steps.CONTENT_PATH, STEPS);
		URI_MATCHER.addURI(AUTHORITY, Gait.CONTENT_PATH, GAIT);
		URI_MATCHER.addURI(AUTHORITY, RawSteps.CONTENT_PATH, RAW_STEPS);
		URI_MATCHER.addURI(AUTHORITY, Tests.CONTENT_PATH, TESTS);
	}

}
