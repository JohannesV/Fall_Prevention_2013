package no.ntnu.stud.fallservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class Helper {
	private static final String TAG = "HelperClass";

	public static Date parseDate (String dateTime) {
		DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = iso8601Format.parse(dateTime);
		} catch (ParseException e) {
			Log.e(TAG, "Parsing ISO8601 datetime failed", e);
			return null;
		}
		
		return date;		
	}
}
