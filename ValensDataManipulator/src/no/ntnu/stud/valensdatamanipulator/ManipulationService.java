package no.ntnu.stud.valensdatamanipulator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ManipulationService extends Service {

	public static final long GROUP_GAP_THRESHOLD = 10000;
	public static final long GROUP_SIZE_THRESHOLD = 10000;

	public void calculate() {
		ContentProviderHelper cph = new ContentProviderHelper(this);
		// Get the raw steps for the last 24hours.
		Timestamp now = ContentProviderHelper.getHoursBack(0);
		Timestamp yesterday = ContentProviderHelper.getHoursBack(24);
		List<List<Long>> rawSteps = cph.getRawSteps(yesterday, now);
		// Find the true steps steps. I.e. the steps from the source with the
		// most steps.
		List<Long> bestSource = new ArrayList<Long>();
		for (List<Long> stepList : rawSteps){
			if(stepList.size() > bestSource.size()){
				bestSource = stepList;
			}
		}
		// Store the true steps into the content provider
		cph.storeTrueSteps(bestSource);
		// Find the mean and std for the period
		// double[] meanAndStd = findStepIntervals(steps);

	}

	private double[] findStepIntervals(List<Long> steps) {
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
		if (tempGroup.get(tempGroup.size() - 1) - tempGroup.get(0) > GROUP_SIZE_THRESHOLD) {
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

	// We do not use any binders, so we just leave this method empty.
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Calculates the mean of a list of values.
	 * 
	 * @param values
	 *            - a list of numbers
	 * @return the mean
	 */
	public static double calculateMean(List<Long> values) {
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
	public static double calculateStd(List<Long> values, double mean) {
		double std = 0.0f;
		for (Long f : values) {
			std += (f - mean) * (f - mean);
		}
		std = std / values.size();
		return Math.sqrt(std);
	}
}
