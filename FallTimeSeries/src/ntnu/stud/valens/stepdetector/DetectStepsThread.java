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

import java.util.List;

import android.content.ContentValues;
import android.net.Uri;

/**
 * A separate thread called by StepMainService to calculate the step time stamps based on a time 
 * series of data. Performed in a separate thread to not interfere with the sensor input in 
 * StepMainService.
 * 
 * @author Elias
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
	public DetectStepsThread(List<Float> mTimeSeries, List<Long> mTimeStamps, StepMainService activity, double mean, double std) {
		this.mVectorLengths = mTimeSeries;
		this.mTimeStamps = mTimeStamps;
		this.activity = activity;
		mMean = mean;
		mStd = std;
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
	 * Stores the steps that have been found into the content provider
	 * 
	 * @param peakIndices - A list of which indices in the timeStamps list that correspond to peaks
	 */
	private void storeSteps(List<Integer> peakIndices) {
		for (Integer i : peakIndices) {
			pushToContentProvider(mTimeStamps.get(i));
		}
	}
	
	/**
	 * Connects to the content provider to store the time stamp of a single
	 * step.
	 * 
	 * @param Step
	 *            - The time stamp of the single step.
	 */
	public void pushToContentProvider(Long step) {
		Uri uri = Uri
				.parse("content://ntnu.stud.valens.contentprovider/raw_steps/");
		// Define the row to insert
		ContentValues rowToInsert = new ContentValues();
		rowToInsert.put( "timestamp",step);
		rowToInsert.put( "source", Values.TAG);
		// Insert row, hoping that everything works as expected.
		activity.getContentResolver().insert(uri, rowToInsert);
	}
}
