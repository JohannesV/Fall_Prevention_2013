/*******************************************************************************
 * Licensed to UbiCollab.org under one or more contributor
 * license agreements.  See the NOTICE file distributed 
 * with this work for additional information regarding
 * copyright ownership. UbiCollab.org licenses this file
 * to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package ntnu.stud.valens.contentprovider.calculations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * This class handles most of the work in deriving new data from data that
 * already exists in the content provider. In particular, it derives true steps
 * based on the most reliable step detector, and calculates gait parameters
 * (speed and variability).
 * 
 * @author Elias
 */
public class ManipulatorHelper implements Runnable {

	private static final String APP_TAG = "ntnu.stud.valens.contentprovider";

	public static final long GROUP_GAP_THRESHOLD = 10000;
	public static final long GROUP_SIZE_THRESHOLD = 10000;

	private Context context;
	
	public ManipulatorHelper(Context context) {
		this.context = context;
	}
	
	/**
	 * The calculation method handles the computation of gait parameters and
	 * finds the true steps. Fetches the data for the last 24 hours from the
	 * content provider. After calculation, it stores the information back into
	 * the proper tables in the content provider. This functions first finds
	 * groups of steps, and only if the step group is longer than a certain
	 * threshold it uses it to calculate gait speed and variability.
	 * 
	 * Will only calculate if no calculation has occurred in the same day, to
	 * avoid steps being counted multiple times.
	 * 
	 * @param context
	 *            - Any context object. Required for the ContentProviderHelper,
	 *            because Android for some reason requires a context to be able
	 *            to get access to content providers.
	 */
	public void run() {
		// Only update if no update has happened today.
		SharedPreferences prefs = context.getSharedPreferences(
				"ntnu.stud.valens.contentprovider", Context.MODE_PRIVATE);
		long lastUpdateTime = prefs.getLong("lastUpdateTime", 0);
		Log.v(APP_TAG, "LastTimeUpdate: " + lastUpdateTime);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(lastUpdateTime);
		Log.v(APP_TAG, "Last update day: " + c.get(Calendar.DATE));
		Log.v(APP_TAG, "Today: " + Calendar.getInstance().get(Calendar.DATE));
		if (!(c.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE))
				|| lastUpdateTime == 0) {
			ContentProviderHelper cph = new ContentProviderHelper(context);
			// Get the raw steps for the last 24hours.

			Timestamp now = ContentProviderHelper
					.getHoursBack(Calendar.HOUR - 4);
			Timestamp yesterday = ContentProviderHelper
					.getHoursBack(24 + Calendar.HOUR_OF_DAY - 4);
			List<List<Long>> rawSteps = cph.getRawSteps(yesterday, now);

			// Find the true steps steps. I.e. the steps from the source with
			// the most steps.

			Log.v(APP_TAG, "calculating now!");
			List<Long> bestSource = new ArrayList<Long>();
			for (List<Long> stepList : rawSteps) {
				if (stepList.size() > bestSource.size()) {

					bestSource = stepList;
				}
			}
			Log.v(APP_TAG, "bestSource.size: " + bestSource.size());
			// Store the true steps into the content provider
			cph.storeTrueSteps(bestSource);

			// Find the gait parameters for the true steps of the period and
			// store them in the content provider
			double[] gaitParameters = findGaitParameters(bestSource);
			Log.v(APP_TAG, "Gait parameter: " + gaitParameters[0] + ", "
					+ gaitParameters[1]);
			if (!(Double.isNaN(gaitParameters[0]) || Double
					.isNaN(gaitParameters[1]))) {
				cph.storeGaitParameters(gaitParameters, bestSource);
			}
			lastUpdateTime = System.currentTimeMillis();
			SharedPreferences.Editor editor = prefs.edit();
			editor.putLong("lastUpdateTime", lastUpdateTime);
			editor.commit();

		}		
	}

	/**
	 * Finds the gait parameters based on a list of steps, i.e. a step group.
	 * 
	 * @param steps
	 *            - The list of steps that gait parameters should be calculated
	 *            from.
	 * @return - An array of doubles containing two elements, the first being
	 *         the gait speed, the second being the gait variability.
	 */
	private double[] findGaitParameters(List<Long> steps) {
		// Stores intervals and the current step group temporarily
		List<Long> tempIntervals = new ArrayList<Long>();
		List<Long> tempGroup = new ArrayList<Long>();
		// Maintains current lists of mean and std
		double mean = 0d, std = 0d;
		int numberOfGroups = 0;

		for (int i = 0; i < steps.size() - 1; i++) {
			// If the current group is empty, this step is the beginning of a
			// new group
			if (tempGroup.isEmpty()) {
				tempGroup.add(steps.get(i));
			}
			// If the gap between the two steps are larger than a threshold, we
			// consider the old tempGroup as a single group, and the new step as
			// the beginning of a new one.
			else if (steps.get(i) - steps.get(i - 1) > GROUP_GAP_THRESHOLD) {
				// Look at the old step group, check if it is larger than
				// threshold.
				if (steps.get(i - 1) - tempGroup.get(0) > GROUP_SIZE_THRESHOLD) {
					// If it is, it is significant, and we store the intervals.
					for (int j = 1; j < tempGroup.size(); j++) {
						tempIntervals.add(tempGroup.get(j)
								- tempGroup.get(j - 1));
					}
					// Calculate mean and std for the intervals
					numberOfGroups++;
					double tempMean = calculateMean(tempIntervals);
					mean += tempMean;
					std += calculateStd(tempIntervals, tempMean);
					tempIntervals.clear();
				}
				// Then discard the group and start a new one.
				tempGroup.clear();
				tempGroup.add(steps.get(i));
			}
			// If the gap is smaller than the threshold, then the new step is a
			// part of the tempGroup.
			else {
				tempGroup.add(steps.get(i));
			}
		}

		// Finally, choose to either store the final group, or discard it,
		// depending on size
		if (tempGroup.size() > 0
				&& tempGroup.get(tempGroup.size() - 1) - tempGroup.get(0) > GROUP_SIZE_THRESHOLD) {
			for (int j = 1; j < tempGroup.size(); j++) {
				tempIntervals.add(tempGroup.get(j) - tempGroup.get(j - 1));
			}
			numberOfGroups++;
			double tempMean = calculateMean(tempIntervals);
			mean += tempMean;
			std += calculateStd(tempIntervals, tempMean);
		}

		// Now, find the mean of means, and mean of stds
		mean = mean / numberOfGroups;
		std = std / numberOfGroups;
		return new double[] { mean, std };
	}

	/**
	 * Calculates the mean of a list of values.
	 * 
	 * @param values
	 *            - a list of numbers
	 * @return the mean
	 */
	private static double calculateMean(List<Long> values) {
		double mean = 0.0f;
		for (Long f : values) {
			mean += f;
		}
		return mean / values.size();
	}

	/**
	 * Calculates the standard deviation of a list of values
	 * 
	 * @param values
	 *            - the list of values
	 * @param mean
	 *            - the mean for the same list of numbers
	 * @return the standard deviation
	 */
	private static double calculateStd(List<Long> values, double mean) {
		double std = 0.0f;
		for (Long f : values) {
			std += (f - mean) * (f - mean);
		}
		std = std / values.size();
		return Math.sqrt(std);
	}

}
