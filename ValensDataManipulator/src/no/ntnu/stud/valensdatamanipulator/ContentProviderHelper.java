package no.ntnu.stud.valensdatamanipulator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

/**
 * Class that provides an interface to the content provider.
 * 
 * @author Elias Aamot
 * 
 */
@SuppressLint("NewApi")
public class ContentProviderHelper {

	private Context context;

	public ContentProviderHelper(Context context) {
		this.context = context;
	}
	
	/**
	 * Fetches the timestamps of all raw steps that occurred between the two timestamps start and stop. 
	 * Sorts them by source and returns lists of lists of timestamps where each list correlate to a source.
	 * 
	 * @param start
	 * 		- Get steps that occurred after this timestamp.
	 * @param stop
	 * 		- Get steps that occurred before this timestamp.
	 * @return A list of list of step timestamps.
	 */
	public List<List<Long>> getRawSteps(Timestamp start, Timestamp stop) {
		List<Long> rawSteps = new ArrayList<Long>();
		List<List<Long>> sortedSteps = new ArrayList<List<Long>>();
		
		// Find the content provider using a unique resource identifier (URI)
		Uri uri = Uri.parse("content://ntnu.stud.valens.contentprovider");
		ContentProviderClient stepsProvider = context.getContentResolver()
				.acquireContentProviderClient(uri);

		// Setting variables for the query
		uri = Uri.parse("content://ntnu.stud.valens.contentprovider/raw_steps");
		// sets the projection part of the query
		String[] projection = new String[] { "timestamp", "source" };
		// sets the selection part of the query
		String selection =  "timestamp > " + start.getTime() +
							 " AND timestamp < " + stop.getTime();
								 
		// not used, therefore null
		String[] selectionArgs = null;
		// no need for sorting
		String sortOrder = "source DESC";
	
		// Try do ask the content provider for information
		Cursor cursor;
		try {
			cursor = stepsProvider.query(uri, projection, selection,
					selectionArgs, sortOrder);
			String prevSource = "";
			cursor.moveToPosition(0);
			prevSource = cursor.getString(1);
			
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				if(!prevSource.equals(cursor.getString(1))){
					sortedSteps.add(rawSteps);
					prevSource = cursor.getString(1);
					rawSteps.clear();
				}
				rawSteps.add(cursor.getLong(0));
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sortedSteps;
	}
	
	/**
	 * Returns timestamp for number of hours counting backwards
	 * 
	 * @param hours
	 * 		- 0 means current, 24 means 24 hours back, etc
	 * @return
	 */
	public static Timestamp getHoursBack(int hours) {

		return new Timestamp(System.currentTimeMillis()
				- TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS));
	}

}