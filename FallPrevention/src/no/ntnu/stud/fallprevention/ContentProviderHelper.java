package no.ntnu.stud.fallprevention;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.widget.Toast;

public class ContentProviderHelper {
	static Timestamp sNow;
	static Timestamp sOneDayAgo;
	static Timestamp sTwoDaysAgo;
	static String sRecentStepsQuery;
	Context context;

	public ContentProviderHelper(Context context) {
		this.context = context;

		sNow = new Timestamp(System.currentTimeMillis());
		sOneDayAgo = new Timestamp(System.currentTimeMillis()
				- TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
		sRecentStepsQuery = "SELECT COUNT(timestamp) FROM raw_steps WHERE timestamp > "
				+ sOneDayAgo + " & timestamp < " + sNow + "; ";
	}

	/**
	 * Returns number of steps done the last 24 hours
	 * 
	 * @return
	 */
	public int getLastDayStepCount(Timestamp start, Timestamp stop) {
		// TODO: make use of strings from ValensDataProvider and call a query
		// with the correct arguments

		ContentResolver cr = context.getContentResolver();
		// cr.query(uri, projection, selection, selectionArgs, sortOrder)
		int returner;
		double random = new Random().nextDouble();
		// Toast.makeText(context, String.valueOf((int) random * 1000),
		// Toast.LENGTH_LONG).show();
		return (int) (random * 1000);
	}

	void refreshTimestamp() {
		sNow = new Timestamp(System.currentTimeMillis());
		Toast.makeText(context, String.valueOf(sNow), Toast.LENGTH_LONG).show();
		sOneDayAgo = getHoursBack(24);
		sTwoDaysAgo = getHoursBack(48);
		sRecentStepsQuery = "SELECT COUNT(timestamp) FROM raw_steps WHERE timestamp > "
				+ sOneDayAgo + " & timestamp < " + sNow + "; ";
	}

	Timestamp getHoursBack(int hours) {
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
	 * gait variability for particular time period
	 * 
	 * @param start
	 * @param stop
	 * @return
	 */
	public int getGaitVariability(Timestamp start, Timestamp stop) {
		return (Integer) null;
	}

	public RiskStatus cpGetStatus(RiskStatus prevStatus) {
		RiskStatus returner = RiskStatus.OK_JOB;

		double dayOne = getLastDayStepCount(getHoursBack(24), getHoursBack(0));
		double dayTwo = getLastDayStepCount(getHoursBack(48), getHoursBack(24));
		if (dayOne < dayTwo) {
			if (dayOne * 100 / dayTwo > 85) {
				returner = RiskStatus.OK_JOB;
			} else if (dayOne * 100 / dayTwo > 50) {
				returner = RiskStatus.NOT_SO_OK_JOB;
			} else {
				returner = RiskStatus.BAD_JOB;
			}

		} else if (dayOne > dayTwo) {
			if (dayTwo * 100 / dayOne > 85) {
				returner = RiskStatus.OK_JOB;
			} else if (dayTwo * 100 / dayOne > 50) {
				returner = RiskStatus.GOOD_JOB;
			} else {
				returner = RiskStatus.VERY_GOOD_JOB;
			}

		}
		Toast.makeText(context,
				String.valueOf(dayOne) + "\n" + String.valueOf(dayTwo),
				Toast.LENGTH_LONG).show();
		return returner;
	}

	List<Double> cpGetRiskHistory(int length) {
		List<Double> returner = new ArrayList<Double>();
		Uri uri = Uri.parse("content://ntnu.stud.valens.contentprovider");
		ContentProviderClient movementProvider = context.getContentResolver()
				.acquireContentProviderClient(uri);
		uri = Uri
				.parse("content://ntnu.stud.valens.contentprovider/raw_steps/");
		String[] projection = new String[] { "timestamp" };
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = "ID desc";
		try {
			Cursor cursor = movementProvider.query(uri, projection, selection,
					selectionArgs, sortOrder);
			if (!cursor.isNull(0)) {
				for (int i = cursor.getCount() - length; i < cursor.getCount(); i++) {
					if (i < 0) {
						i = 0;
					}
					cursor.moveToPosition(i);
					// double steps = Double.parseDouble(cursor.getString(0));
					// returner.add(steps);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return returner;
	}
}
