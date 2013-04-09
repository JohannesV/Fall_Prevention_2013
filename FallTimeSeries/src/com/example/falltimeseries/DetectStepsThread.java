package com.example.falltimeseries;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

public class DetectStepsThread implements Runnable {

	public static final int SMOOTHING_WINDOW = 5;
	public static final int WINDOW_SIZE = 10;
	public static final int DATA_STREAM_SIZE = 100;
	public static final int COMMIT_DATA_THRESHOLD = (SMOOTHING_WINDOW * 2) + (WINDOW_SIZE * 2) + DATA_STREAM_SIZE;
	public static final float STD_THRESHOLD = 0.8f;
	
	private List<Float> mVectorLengths;
	private List<Long> mTimeStamps;
	private StepMainService activity; 
	private double mMean, mStd;
	
	public DetectStepsThread(List<Float> mVectorLengths, List<Long> mTimeStamps, StepMainService activity) {
		this.mVectorLengths = mVectorLengths;
		this.mTimeStamps = mTimeStamps;
		this.activity = activity;
	}
	
	@Override
	public void run() {
		List<Float> smoothedData = smooth(mVectorLengths, SMOOTHING_WINDOW);
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

	private void storeSteps(List<Integer> peakIndices) {
		for (Integer peak : peakIndices) {
			pushToContentProvider(mTimeStamps.get(peak));
		}
//		activity.update();
	}

	private List<Integer> removeClosePeaks(List<Integer> peaks, List<Float> peakStrengths) {
		// Remove all peaks closer to each other than WINDOW
		int i = 0;
		while (i < peaks.size()-1) {
			// If too subsequent peaks are too close ...
			if (peaks.get(i+1) - peaks.get(i) <= WINDOW_SIZE) {
				// ... remove the least significant
				if (peakStrengths.get(peaks.get(i+1)) > peakStrengths.get(peaks.get(i))) {
					peaks.remove(i);
				}
				else {
					peaks.remove(i+1);
				}
			}
			// If peaks are distance, move the counter one step ahead
			else {
				i++;
			}
		}
		return peaks;
	}

	private void calculateMeanAndStd(List<Float> values) {
		// Calculate mean of all values over zero
		mMean = 0.0f;
		for (Float f : values) {
			if (f>0) mMean += f;
		}
		mMean = mMean / values.size();
		// Calculate standard deviation of all values over zero
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

	private List<Integer> findPossiblePeaks(List<Float> peakStrengths) {
		List<Integer> peakIndices = new ArrayList<Integer>();
		
		// Add all those values 
		for (int i = 0; i < peakStrengths.size(); i++) {
			if ((peakStrengths.get(i)-mMean) > (mStd*STD_THRESHOLD)) {
				peakIndices.add(i);
			}
		}
		
		return peakIndices;
	}

	private List<Float> calculatePeakStrengths(List<Float> data) {
		List<Float> peakStrengths = new ArrayList<Float>();

		for (int i = WINDOW_SIZE; i < (data.size() - WINDOW_SIZE); i++) {
			float peakPointValue = data.get(i);
			float prePeakStrength = 0.0f;
			float postPeakStrength = 0.0f;

			// Calculate pre- and post-peak strengths simultaneously
			for (int j = 1; j < (WINDOW_SIZE+1); j++) {
				prePeakStrength += (peakPointValue - data.get(i-j));
				postPeakStrength += (peakPointValue - data.get(i+j));
			}
			// Normalize to averages
			prePeakStrength = (prePeakStrength / WINDOW_SIZE);
			postPeakStrength = (postPeakStrength / WINDOW_SIZE);
			float peakStrength = (prePeakStrength+postPeakStrength)/2;

			peakStrengths.add(peakStrength);
		}

		return peakStrengths;
	}

	// TODO: Has room for efficiency improvement
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


	/*
	 * Connect to CP and shit
	 * 
	 * TODO: use values from CP, not statics
	 */
	public void pushToContentProvider(Long step) {
		Log.v("Detect:","PUSH!!!!");
		Uri uri = Uri.parse("content://ntnu.stud.valens.contentprovider/raw_steps/");
		// Define the row to insert
		ContentValues rowToInsert = new ContentValues();
		rowToInsert.put(uri+"/raw_steps/timestamp/", step);
		rowToInsert.put(uri+"/raw_steps/source/", activity.tag);
		// Insert row, hoping that everything works as expected.
		activity.getContentResolver().insert(uri,rowToInsert);
	}
}
