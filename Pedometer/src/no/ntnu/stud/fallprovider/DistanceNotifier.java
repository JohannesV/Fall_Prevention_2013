/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package no.ntnu.stud.fallprovider;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Calculates and displays the distance walked.
 * 
 * @author Levente Bagi
 */
public class DistanceNotifier implements LocationListener {

	public interface Listener {
		public void valueChanged(float value);

		public void passValue();
	}

	private Listener mListener;

	static float mDistance = 0;

	PedometerSettings mSettings;
	Utils mUtils;

	boolean mIsMetric;
	float mStepLength;

	/**
	 * <code>MAX_REASONABLE_SPEED</code> is about 324 kilometer per hour or 201
	 * mile per hour.
	 */
	private static final int MAX_REASONABLE_SPEED = 90;

	/**
	 * <code>MAX_REASONABLE_ALTITUDECHANGE</code> between the last few waypoints
	 * and a new one the difference should be less then 200 meter.
	 */
	private static final int MAX_REASONABLE_ALTITUDECHANGE = 200;

	private static final String TAG = "OGT.GPSLoggerService";

	public static final String COMMAND = "nl.sogeti.android.gpstracker.extra.COMMAND";
	public static final int EXTRA_COMMAND_START = 0;
	public static final int EXTRA_COMMAND_PAUSE = 1;
	public static final int EXTRA_COMMAND_RESUME = 2;
	public static final int EXTRA_COMMAND_STOP = 3;

	/**
	 * If speeds should be checked to sane values
	 */
	private boolean mSpeedSanityCheck;

	private Location mPreviousLocation;
	private Vector<Location> mWeakLocations;
	private Queue<Double> mAltitudes;

	/**
	 * <code>mAcceptableAccuracy</code> indicates the maximum acceptable
	 * accuracy of a waypoint in meters.
	 */
	private float mMaxAcceptableAccuracy = 20;
	private Context mContext;

	public DistanceNotifier(Listener listener, PedometerSettings settings,
			Utils utils, Context context) {
		mListener = listener;
		mUtils = utils;
		mSettings = settings;
		mWeakLocations = new Vector<Location>(3);
		mAltitudes = new LinkedList<Double>();
		mContext = context;

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		mSpeedSanityCheck = sharedPreferences.getBoolean(
				Constants.SPEEDSANITYCHECK, true);
		sharedPreferences.getBoolean(Constants.BROADCAST_STREAM, false);

		reloadSettings();
	}

	public void setDistance(float distance) {
		mDistance = distance;
		notifyListener();
	}

	public void reloadSettings() {
		mIsMetric = mSettings.isMetric();
		mStepLength = mSettings.getStepLength();
		notifyListener();
	}

	private void notifyListener() {
		mListener.valueChanged(mDistance);
	}

	public void passValue() {
		// Callback of StepListener - Not implemented
	}

	@Override
	public void onLocationChanged(Location location) {
		Location filteredLocation = locationFilter(location);
		if (filteredLocation != null) {
			if (mPreviousLocation != null) {
				mDistance += mPreviousLocation.distanceTo(filteredLocation);
			}
			mPreviousLocation = location;
		}

		notifyListener();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 * Some GPS waypoints received are of to low a quality for tracking use.
	 * Here we filter those out.
	 * 
	 * @param proposedLocation
	 * @return either the (cleaned) original or null when unacceptable
	 */
	public Location locationFilter(Location proposedLocation) {
		// Do no include log wrong 0.0 lat 0.0 long, skip to next value in
		// while-loop
		if (proposedLocation != null
				&& (proposedLocation.getLatitude() == 0.0d || proposedLocation
						.getLongitude() == 0.0d)) {
			Log.w(TAG,
					"A wrong location was received, 0.0 latitude and 0.0 longitude... ");
			proposedLocation = null;
		}

		// Do not log a waypoint which is more inaccurate then is configured to
		// be acceptable
		if (proposedLocation != null
				&& proposedLocation.getAccuracy() > mMaxAcceptableAccuracy) {
			Log.w(TAG,
					String.format(
							"A weak location was received, lots of inaccuracy... (%f is more then max %f)",
							proposedLocation.getAccuracy(),
							mMaxAcceptableAccuracy));
			proposedLocation = addBadLocation(proposedLocation);
		}

		// Do not log a waypoint which might be on any side of the previous
		// waypoint
		if (proposedLocation != null
				&& mPreviousLocation != null
				&& proposedLocation.getAccuracy() > mPreviousLocation
						.distanceTo(proposedLocation)) {
			Log.w(TAG,
					String.format(
							"A weak location was received, not quite clear from the previous waypoint... (%f more then max %f)",
							proposedLocation.getAccuracy(),
							mPreviousLocation.distanceTo(proposedLocation)));
			proposedLocation = addBadLocation(proposedLocation);
		}

		// Speed checks, check if the proposed location could be reached from
		// the previous one in sane speed
		// Common to jump on network logging and sometimes jumps on Samsung
		// Galaxy S type of devices
		if (mSpeedSanityCheck && proposedLocation != null
				&& mPreviousLocation != null) {
			// To avoid near instant teleportation on network location or
			// glitches cause continent hopping
			float meters = proposedLocation.distanceTo(mPreviousLocation);
			long seconds = (proposedLocation.getTime() - mPreviousLocation
					.getTime()) / 1000L;
			float speed = meters / seconds;
			if (speed > MAX_REASONABLE_SPEED) {
				Log.w(TAG,
						"A strange location was received, a really high speed of "
								+ speed + " m/s, prob wrong...");
				proposedLocation = addBadLocation(proposedLocation);
				// // Might be a messed up Samsung Galaxy S GPS, reset the
				// logging
				// if (speed > 2 * MAX_REASONABLE_SPEED
				// && mPrecision != Constants.LOGGING_GLOBAL) {
				// Log.w(TAG,
				// "A strange location was received on GPS, reset the GPS listeners");
				// stopListening();
				// mLocationManager.removeGpsStatusListener(mStatusListener);
				// mLocationManager = (LocationManager)
				// mContext.getSystemService(Context.LOCATION_SERVICE);
				// }
			}
		}

		// Remove speed if not sane
		if (mSpeedSanityCheck && proposedLocation != null
				&& proposedLocation.getSpeed() > MAX_REASONABLE_SPEED) {
			Log.w(TAG, "A strange speed, a really high speed, prob wrong...");
			proposedLocation.removeSpeed();
		}

		// Remove altitude if not sane
		if (mSpeedSanityCheck && proposedLocation != null
				&& proposedLocation.hasAltitude()) {
			if (!addSaneAltitude(proposedLocation.getAltitude())) {
				Log.w(TAG,
						"A strange altitude, a really big difference, prob wrong...");
				proposedLocation.removeAltitude();
			}
		}
		// Older bad locations will not be needed
		if (proposedLocation != null) {
			mWeakLocations.clear();
		}
		return proposedLocation;
	}

	/**
	 * Store a bad location, when to many bad locations are stored the the
	 * storage is cleared and the least bad one is returned
	 * 
	 * @param location
	 *            bad location
	 * @return null when the bad location is stored or the least bad one if the
	 *         storage was full
	 */
	private Location addBadLocation(Location location) {
		mWeakLocations.add(location);
		if (mWeakLocations.size() < 3) {
			location = null;
		} else {
			Location best = mWeakLocations.lastElement();
			for (Location whimp : mWeakLocations) {
				if (whimp.hasAccuracy() && best.hasAccuracy()
						&& whimp.getAccuracy() < best.getAccuracy()) {
					best = whimp;
				} else {
					if (whimp.hasAccuracy() && !best.hasAccuracy()) {
						best = whimp;
					}
				}
			}
			synchronized (mWeakLocations) {
				mWeakLocations.clear();
			}
			location = best;
		}
		return location;
	}

	/**
	 * Builds a bit of knowledge about altitudes to expect and return if the
	 * added value is deemed sane.
	 * 
	 * @param altitude
	 * @return whether the altitude is considered sane
	 */
	private boolean addSaneAltitude(double altitude) {
		boolean sane = true;
		double avg = 0;
		int elements = 0;
		// Even insane altitude shifts increases alter perception
		mAltitudes.add(altitude);
		if (mAltitudes.size() > 3) {
			mAltitudes.poll();
		}
		for (Double alt : mAltitudes) {
			avg += alt;
			elements++;
		}
		avg = avg / elements;
		sane = Math.abs(altitude - avg) < MAX_REASONABLE_ALTITUDECHANGE;

		return sane;
	}
}
