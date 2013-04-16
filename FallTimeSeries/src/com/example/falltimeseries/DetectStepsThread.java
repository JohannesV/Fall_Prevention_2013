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
	 *     3) Find prospective peaks
	 *     4) Remove peaks that are too close to constituate individual peaks
	 * For a detailed description of the algorithm, see the documentation.
	 */
	@Override
	public void run() {
		// Calculate peak indices
		List<Float> smoothedData = Methods.smooth(mVectorLengths, Values.SMOOTHING_WINDOW);
		List<Float> peakStrengths = Methods.calculatePeakStrengths(smoothedData);
		List<Integer> peakIndices = Methods.findPossiblePeaks(peakStrengths, mMean, mStd);
		peakIndices = Methods.removeClosePeaks(peakIndices, peakStrengths);
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
}
