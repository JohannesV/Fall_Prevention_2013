package com.example.falltimeseries;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.net.Uri;

/**
 * A separate thread called by StepMainService to calculate the step time stamps based on a time 
 * series of data. Performed in a separate thread to not interfere with the sensor input in 
 * StepMainService.
 * 
 * @author Elias Aamot
 *
 */
public class DetectStepsThread implements Runnable {	
	
	private List<Float> mVectorLengths;
	private List<Long> mTimeStamps;
	private StepMainService activity; 
	private double mMean, mStd;
	
	/**
	 * Constructs the new thread for calculating peaks
	 * 
	 * @param mTimeSeries - The time series of data
	 * @param mTimeStamps - The time stamps associated with the time series of data
	 * @param activity - The SetpMainService activity that called the thread
	 */
	public DetectStepsThread(List<Float> mTimeSeries, List<Long> mTimeStamps, StepMainService activity) {
		this.mVectorLengths = mTimeSeries;
		this.mTimeStamps = mTimeStamps;
		this.activity = activity;
	}

	/**
	 * Performs the running of the thread. Calculation follows the given steps:
	 *     1) Smoothing the time series data
	 *     2) Give each point a peak strength depending on the neighbouring data
	 *     3) Find mean and standard deviation
	 *     4) Find prospective peaks
	 *     5) Remove peaks that are too close to constituate individual peaks
	 * For a detailed description of the algorithm, see the documentation.
	 */
	@Override
	public void run() {
		// Calculate peak indices
		List<Float> smoothedData = smooth(mVectorLengths, Values.SMOOTHING_WINDOW);
		List<Float> peakStrengths = calculatePeakStrengths(smoothedData);
		// TODO: Calibration
		if (!activity.meanStdSet) {
			calculateMeanAndStd(peakStrengths);
		} else {
			mMean = activity.mMean;
			mStd = activity.mStd;
		}
		List<Integer> peakIndices = findPossiblePeaks(peakStrengths);
		peakIndices = removeClosePeaks(peakIndices, peakStrengths);
		storeSteps(peakIndices);		
	}

	/**
	 * Stores the steps that have been found into the StepsManager, which subsequently 
	 * will store them in the CP, discard them or retain them until more steps are pushed.
	 * 
	 * @param peakIndices - A list of which indices in the timeStamps list that correspond to peaks
	 */
	private void storeSteps(List<Integer> peakIndices) {
		List<Long> steps = new ArrayList<Long>();
		for (Integer i : peakIndices) {
			steps.add(mTimeStamps.get(i));
		}
		activity.getStepsManager().addSteps(steps);
	}

	/**
	 * Method that takes as input a list of potential peaks, and remove peaks if they are too close
	 * to each other.
	 * 
	 * @param peaks - The indices of the potential peaks
	 * @param peakStrengths - The list of calculated peak strengths for the time series
	 * @return A list of peak indices where no two peaks are too close 
	 */
	private List<Integer> removeClosePeaks(List<Integer> peaks, List<Float> peakStrengths) {
		// Remove all peaks closer to each other than WINDOW
		int i = 0;
		while (i < peaks.size()-1) {
			// If too subsequent peaks are too close ...
			if (peaks.get(i+1) - peaks.get(i) <= Values.WINDOW_SIZE) {
				// ... remove the least significant
				if (peakStrengths.get(peaks.get(i+1)) > peakStrengths.get(peaks.get(i))) {
					peaks.remove(i);
				}
				else {
					peaks.remove(i+1);
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
	 * Calculates the mean and standard deviation of a list of values,
	 * and stores it in the fields mMean and mStd.
	 * 
	 * @param a list of numbers
	 */
	private void calculateMeanAndStd(List<Float> values) {
		// Calculate mean only of values above zero
		mMean = 0.0f;
		for (Float f : values) {
			if (f>0) mMean += f;
		}
		mMean = mMean / values.size();
		
		// Calculate standard deviation only of values above zero
		mStd = 0.0f;
		for (Float f : values) {
			if (f>0) mStd += (f-mMean)*(f-mMean);
		}
		mStd = mStd / values.size();
		mStd = Math.sqrt(mStd);
		
		// Store this in the main activity as well
		activity.mMean = mMean;
		activity.mStd = mStd;
		activity.meanStdSet = true;
	}

	/**
	 * Find the set of indices that represent possible peaks based
	 * on the peak strength calculations.
	 * 
	 * Requires that mMean and mStd are calculated beforehand.
	 * 
	 * @param A series of peak strengths
	 * 
	 * @return A list of indices representing potential peaks
	 */
	private List<Integer> findPossiblePeaks(List<Float> peakStrengths) {
		List<Integer> peakIndices = new ArrayList<Integer>();
		
		// Iterate through peak strength, and keep only those who are 
		// sufficiently big. See documentation for details.
		for (int i = 0; i < peakStrengths.size(); i++) {
			if ((peakStrengths.get(i)-mMean) > (mStd*Values.STD_THRESHOLD)) {
				peakIndices.add(i);
			}
		}
		
		return peakIndices;
	}

	/**
	 * Calculate the peak strength for every point in the time series 
	 * 
	 * @param The time series to calculate strength from
	 * 
	 * @return The series of peak strengths derived
	 */
	private List<Float> calculatePeakStrengths(List<Float> data) {
		List<Float> peakStrengths = new ArrayList<Float>();

		// Iterate through the list to calculate the strength at every point.
		// Don't look at the first and last WINDOW_SIZE number of elements,
		// because peak values depend on the WINDOW_SIZE number of elements
		// in each direction.
		for (int i = Values.WINDOW_SIZE; i < (data.size() - Values.WINDOW_SIZE); i++) {
			float peakPointValue = data.get(i);
			float prePeakStrength = 0.0f;
			float postPeakStrength = 0.0f;

			// Calculate pre- and post-peak strengths simultaneously
			for (int j = 1; j < (Values.WINDOW_SIZE+1); j++) {
				prePeakStrength += (peakPointValue - data.get(i-j));
				postPeakStrength += (peakPointValue - data.get(i+j));
			}
			
			// Normalize and take the average of peak strength in both directions
			prePeakStrength = (prePeakStrength / Values.WINDOW_SIZE);
			postPeakStrength = (postPeakStrength / Values.WINDOW_SIZE);
			float peakStrength = (prePeakStrength+postPeakStrength)/2;

			peakStrengths.add(peakStrength);
		}

		return peakStrengths;
	}

	/**
	 * Smooths the time series data, using a floating window smoothing
	 * 
	 * @param The time series of data to smooth
	 * @param Window size of the smoothing window
	 * 
	 * @return The time series after smoothing
	 */
	private List<Float> smooth(List<Float> data, int window) {
		List<Float> smoothed = new ArrayList<Float>();
		
		for (int i = window; i<(data.size()-window); i++) {
			float total = 0.0f;
			for (int j = -window; j < (window+1); j++) {
				 total += data.get(i+j);
			}
			float avg = (total / ((window*2)+1));
			smoothed.add(avg);
		}
		
		return smoothed;
	}
}
