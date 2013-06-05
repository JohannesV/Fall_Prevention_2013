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
package ntnu.stud.valens.stepdetector;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A class that contain several of the static methods that are used during
 * calculation of steps.
 * 
 * @author Elias
 * 
 */
public class Methods {

	/**
	 * Smooths the time series data, using a floating window smoothing
	 * 
	 * @param data 
	 * 			- The time series of data to smooth
	 * @param window 
	 * 			- Window size of the smoothing window
	 * 
	 * @return The time series after smoothing
	 */
	public static List<Float> smooth(List<Float> data, int window) {
		List<Float> smoothed = new ArrayList<Float>();

		for (int i = window; i < (data.size() - window); i++) {
			float total = 0.0f;
			for (int j = -window; j < (window + 1); j++) {
				total += data.get(i + j);
			}
			float avg = (total / ((window * 2) + 1));
			smoothed.add(avg);
		}

		return smoothed;
	}

	/**
	 * Calculate the peak strength for every point in the time series
	 * 
	 * @param data 
	 * 			- The time series to calculate strength from
	 * 
	 * @return The series of peak strengths derived
	 */
	public static List<Float> calculatePeakStrengths(List<Float> data) {
		List<Float> peakStrengths = new ArrayList<Float>();

		// Iterate through the list to calculate the strength at every point.
		// Don't look at the first and last WINDOW_SIZE number of elements,
		// because peak values depend on the WINDOW_SIZE number of elements
		// in each direction.
		for (int i = Values.PEAK_STRENGTH_WINDOW; i < (data.size() - Values.PEAK_STRENGTH_WINDOW); i++) {
			float peakPointValue = data.get(i);
			float prePeakStrength = 0.0f;
			float postPeakStrength = 0.0f;

			// Calculate pre- and post-peak strengths simultaneously
			for (int j = 1; j < (Values.PEAK_STRENGTH_WINDOW + 1); j++) {
				prePeakStrength += (peakPointValue - data.get(i - j));
				postPeakStrength += (peakPointValue - data.get(i + j));
			}

			// Normalize and take the average of peak strength in both
			// directions
			prePeakStrength = (prePeakStrength / Values.PEAK_STRENGTH_WINDOW);
			postPeakStrength = (postPeakStrength / Values.PEAK_STRENGTH_WINDOW);
			float peakStrength = (prePeakStrength + postPeakStrength) / 2;

			peakStrengths.add(peakStrength);
		}

		return peakStrengths;
	}

	/**
	 * Find the set of indices that represent possible peaks based on the peak
	 * strength calculations.
	 * 
	 * Requires that mMean and mStd are calculated beforehand.
	 * 
	 * @param peakStrengths
	 *            - A series of peak strengths
	 * @param mean
	 *            - The mean value obtained during calibration
	 * @param std
	 *            - The STD value obtained during calibration
	 * 
	 * @return A list of indices representing potential peaks
	 */
	public static List<Integer> findPossiblePeaks(List<Float> peakStrengths,
			double mean, double std) {
		List<Integer> peakIndices = new ArrayList<Integer>();

		// Iterate through peak strength, and keep only those who are
		// sufficiently big. See documentation for details.
		for (int i = 0; i < peakStrengths.size(); i++) {
			if ((peakStrengths.get(i) - mean) > (std * Values.STD_THRESHOLD)) {
				peakIndices.add(i);
			}
		}

		return peakIndices;
	}

	/**
	 * Method that takes as input a list of potential peaks, and remove peaks if
	 * they are too close to each other.
	 * 
	 * @param peaks
	 *            - The indices of the potential peaks
	 * @param peakStrengths
	 *            - The list of calculated peak strengths for the time series
	 * @return A list of peak indices where no two peaks are too close
	 */
	public static List<Integer> removeClosePeaks(List<Integer> peaks,
			List<Float> peakStrengths) {
		// Remove all peaks closer to each other than WINDOW
		int i = 0;
		while (i < peaks.size() - 1) {
			// If too subsequent peaks are too close ...
			if (peaks.get(i + 1) - peaks.get(i) <= Values.PEAK_STRENGTH_WINDOW) {
				// ... remove the least significant
				if (peakStrengths.get(peaks.get(i + 1)) > peakStrengths
						.get(peaks.get(i))) {
					peaks.remove(i);
				} else {
					peaks.remove(i + 1);
				}
			}
			// If peaks are distant, move the counter one step ahead
			else {
				i++;
			}
		}
		return peaks;
	}

	/**
	 * Calculated the euclidian length of a vector
	 * 
	 * @param vector
	 *            any list of numbers, where each number will be interpreted as
	 *            vector length in a single direction.
	 * 
	 * @return The euclidian length
	 */
	public static float calculateVectorLength(float[] vector) {
		float sum = ((vector[0] * vector[0]) + (vector[1] * vector[1]) + (vector[2] * vector[2]));
		return (float) Math.sqrt((double) sum);
	}
	
	/**
	 * Calculates the mean of the positive values a list of values.
	 * 
	 * @param values - a list of numbers
	 * @return the mean
	 */
	public static double calculateMean(List<Float> values) {
		// Calculate mean only of values above zero
		double mean = 0.0f;
		for (Float f : values) {
			if (f>0) mean += f;
		}
		return mean / values.size();
	}
	
	/**
	 * Calculates the standard deviation of the positive values of a list of values
	 * 
	 * @param values - the list of values
	 * @param mean - the mean for the same list of numbers
	 * @return the standard deviation
	 */
	public static double calculateStd(List<Float> values, double mean) {
		// Calculate standard deviation only of values above zero
		double std = 0.0f;
		for (Float f : values) {
			if (f>0) std += (f-mean)*(f-mean);
		}
		std = std / values.size();
		return Math.sqrt(std);
	}
}
