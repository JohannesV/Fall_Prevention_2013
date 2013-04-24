package no.ntnu.stud.fallprevention.connectivity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import no.ntnu.stud.fallprevention.Constants;
import no.ntnu.stud.fallprevention.datastructures.RiskStatus;
import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

/**
 * Retrives data from the contentprovider, interprets them, and passes them on
 * in a form useful to the gui
 * 
 * @author Johannes, Dat-Danny
 * 
 */
@SuppressLint("NewApi")
public class ContentProviderHelper {

	Context context;
	private final static String TAG = "ContentProviderHelper";

	public ContentProviderHelper(Context context) {
		this.context = context;

	}

	/**
	 * Returns number of steps done the in the period between start and stop
	 * 
	 * @return
	 */
	public int getStepCount(Timestamp start, Timestamp stop) {

		Log.v(TAG, "Getting step count");
		double mStepCount = 0;
		// Setting variables for the query
		// sets the unique resource identifier for the data
		Uri uri = Uri.parse("content://ntnu.stud.valens.contentprovider");
		ContentProviderClient stepsProvider = context.getContentResolver()
				.acquireContentProviderClient(uri);

		uri = Uri.parse("content://ntnu.stud.valens.contentprovider/raw_steps");
		// sets the projection part of the query
		String[] projection = new String[] { "count(timestamp) as count" };
		// sets the selection part of the query
		String selection =  "timestamp > " + start.getTime() +
							 " AND timestamp < " + stop.getTime();
								 
		// not used, therefore null
		String[] selectionArgs = null;// {String.valueOf(start.getTime()),String.valueOf(stop.getTime())};
		// no need for sorting
		String sortOrder = null;

		// uses variables to construct query
		Log.v(TAG, "Attempting query");
		try {
			// Everything in order
			Cursor cursor = stepsProvider.query(uri, projection, selection,
					selectionArgs, sortOrder);
			cursor.moveToFirst();
			Log.v(TAG, "Steps counted: " + String.valueOf(cursor.getString(0)));
			mStepCount = cursor.getDouble(0);
			Log.v(TAG, String.valueOf(mStepCount));
			Log.v(TAG, "Query done without errors!");

		} catch (SQLException e) {
			// SQL problems
			Log.v(TAG, e.toString());
			e.printStackTrace();
		} catch (RemoteException e) {
			// Remote binding problems

			e.printStackTrace();
		} catch (NullPointerException e) {
			// Nullpointer problems

			Log.v(TAG, e.toString());
			e.printStackTrace();
		}
		// Toast.makeText(context, String.valueOf(mStepCount),
		// Toast.LENGTH_LONG).show();
		return (int) mStepCount;
	}

	/**
	 * Returns timestamp for number of hours counting backwards
	 * 
	 * @param hours
	 *            : 0 means current, 24 means 24 hours back, etc
	 * @return
	 */

	public Timestamp getHoursBack(int hours) {

		return new Timestamp(System.currentTimeMillis()
				- TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS));
	}

	/**
	 * returns gait speed, for a particular time period
	 * 
	 * @param start
	 * @param stop
	 * @return
	 */
	public int getGaitSpeed(Timestamp start, Timestamp stop) {
		return (Integer) null;
	}


	/**
	 * returns a Riskstatus depending on amounts of steps taken
	 * 
	 * @param prevStatus
	 * @return
	 */
	public RiskStatus cpGetStatus(RiskStatus prevStatus) {
		Log.v(TAG, "Getting status");
		RiskStatus returner = RiskStatus.OK_JOB;

		// Toast.makeText(context,
		// String.valueOf(dayOne) + "\n" + String.valueOf(dayTwo),
		// Toast.LENGTH_LONG).show();

		returner = returner.getStatus(getRiskStepValue());

		return returner;
	}
	public double getGaitVariability(Timestamp start, Timestamp stop){
		double returner=-1;
		// Setting variables for the query
				// sets the unique resource identifier for the data
				Uri uri = Uri.parse("content://ntnu.stud.valens.contentprovider");
				ContentProviderClient stepsProvider = context.getContentResolver()
						.acquireContentProviderClient(uri);

				uri = Uri.parse("content://ntnu.stud.valens.contentprovider/gaits");
				// sets the projection part of the query
				String[] projection = new String[] { "gaitVariability" };
				// sets the selection part of the query
				//TODO fix query according to specifications
				String selection =  "start > " + start.getTime() +
									 " AND stop < " + stop.getTime();
										 
				// not used, therefore null
				String[] selectionArgs = null;// {String.valueOf(start.getTime()),String.valueOf(stop.getTime())};
				// no need for sorting
				String sortOrder = null;

				// uses variables to construct query
				Log.v(TAG, "Attempting query");
				try {
					// Everything in order
					Cursor cursor = stepsProvider.query(uri, projection, selection,
							selectionArgs, sortOrder);
					cursor.moveToFirst();
					Log.v(TAG, "Variability: " + String.valueOf(cursor.getString(0)));
					returner = cursor.getDouble(0);
					Log.v(TAG, String.valueOf(returner));
					Log.v(TAG, "Query done without errors!");

				} catch (SQLException e) {
					// SQL problems
					Log.v(TAG, e.toString());
					e.printStackTrace();
				} catch (RemoteException e) {
					// Remote binding problems

					e.printStackTrace();
				} catch (NullPointerException e) {
					// Nullpointer problems

					Log.v(TAG, e.toString());
					e.printStackTrace();
				}
		
		return returner;
	}

	/**
	 * returns a code for the appropriate riskstatus, given steps registered in
	 * the last days
	 * 
	 * @return
	 */
	public int getRiskStepValue() {
		Log.v(TAG, "Getting value");
		double dayOne = getStepCount(getHoursBack(24), getHoursBack(0));
		double dayTwo = getStepCount(getHoursBack(48), getHoursBack(24));
		int returner = 3;
		
		if (dayOne > Constants.GOOD_STEPS_NUMBER) {
			returner = RiskStatus.VERY_GOOD_JOB.getCode();
		} else {
			if (dayOne < dayTwo) {
				if (dayOne * 100 / dayTwo > Constants.SMALL_CHANGE_THRESHOLD) {
					
					returner = RiskStatus.OK_JOB.getCode();
				} else if (dayOne * 100 / dayTwo > Constants.LARGE_CHANGE_THRESHOLD) {
					returner = RiskStatus.NOT_SO_OK_JOB.getCode();
				} else {
					returner = RiskStatus.BAD_JOB.getCode();
				}

			} else if (dayOne > dayTwo) {
				if (dayTwo * 100 / dayOne > Constants.SMALL_CHANGE_THRESHOLD) {
					returner = RiskStatus.OK_JOB.getCode();
				} else if (dayTwo * 100 / dayOne > Constants.LARGE_CHANGE_THRESHOLD) {
					returner = RiskStatus.GOOD_JOB.getCode();
				} else {
					returner = RiskStatus.VERY_GOOD_JOB.getCode();
				}

			}
		}
		pushNotification(returner);
		return returner;

	}

	public synchronized void pushNotification(int returner) {
		DatabaseHelper dbh= new DatabaseHelper(context);
		if(returner==RiskStatus.OK_JOB.getCode()){
			dbh.dbAddEvent(2);
		}else if(returner==RiskStatus.BAD_JOB.getCode()){
			dbh.dbAddEvent(1);
		}else if(returner==RiskStatus.VERY_GOOD_JOB.getCode()){
			dbh.dbAddEvent(0);
		}
	}

	public void cpGetEventList() {
		// TODO: Rename and write to database in database helper
	}

	/**
	 * returns a list containing information for the statistics class to display
	 * Information is gotten from the content provider
	 * 
	 * @param length
	 * @return
	 */
	public List<Double> cpGetRiskHistory(int length,int interval) {

		List<Double> returner = new ArrayList<Double>();
		// for (int i = 0; i < length; i++) {
		// returner.add((double) Math.round((Math.random() * 5)));
		// }
		for (int i =length;i>= 0;i--){
			//the list is supposed to be read in an interleaved format, meaning x and y values alternating
			returner.add((double) (-i*interval));
			returner.add((double) getStepCount(getHoursBack((i+1)*interval), getHoursBack(i*interval)));
			
		}
		return returner;
	}

}
