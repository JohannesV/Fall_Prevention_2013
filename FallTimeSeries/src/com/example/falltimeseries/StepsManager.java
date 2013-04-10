package com.example.falltimeseries;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

/**
 * Class that maintains a list of steps for the applications. Groups the steps,
 * and handles the groups according to length: Discard the small ones and push
 * the long ones to the CP.
 * 
 * @author Elias Aamot
 * 
 */
public class StepsManager {

	private List<Long> mSteps;
	private StepMainService activity;

	public StepsManager(StepMainService activity) {
		mSteps = new ArrayList<Long>();
		this.activity = activity;
	}

	/**
	 * Adds newly calculated steps to the steps list. The method will figure out
	 * what to do with the steps, either discarding them, pushing them to the
	 * content provider, or retaining them until new steps appear.
	 * 
	 * @param newSteps
	 *            - the list of steps to store
	 */
	public void addSteps(List<Long> newSteps) {
		// Add steps to the list
		for (Long step : newSteps) {
			mSteps.add(step);
		}
		// Go through the list, looking for gaps larger than
		// STEP_GROUP_SEPARATOR, as this signals the end of a group of steps.
		int i = 1;
		while (i < mSteps.size()) {
			// If the gap is sufficiently large to imply that step[i] belongs to
			// another group...
			Log.v("SM", "I: " + i + ", mSteps.size()=" + mSteps.size());
			if ((mSteps.get(i) - mSteps.get(i - 1)) > Values.STEP_GROUP_SEPARATOR) {
				Log.v("SM", "mStep[i] = " + mSteps.get(i) + ", mStep[i-1] = "
						+ mSteps.get(i - 1));
				// Build a stepGroup out of the steps, and send them to another
				// method that will choose what to do with it.
				List<Long> stepGroup = new ArrayList<Long>();
				for (int k = 0; k < i; k++) {
					stepGroup.add(mSteps.get(k));
				}
				handleStepsGroup(stepGroup);
				// Start again in a new step list beginning at step[i].
				List<Long> uncheckedSteps = new ArrayList<Long>();
				for (int j = i; j < mSteps.size(); j++) {
					uncheckedSteps.add(mSteps.get(j));
				}
				mSteps = uncheckedSteps;
				i = 1;
			}
			// If the gap is small, then the new step is a part of the old
			// group. Keep looking.
			else {
				i++;
			}
		}
		// The last steps constitute a group, but we still don't know whether it
		// has been finished or not. We can check if STEP_GROUP_SEPARATOR time
		// has passed since the last time step. If this is the case, then the
		// group is complete, and we may either push or discard it.
		if (mSteps.size() > 0) {
			Long timeStamp = System.currentTimeMillis();
			if (timeStamp - mSteps.get(mSteps.size() - 1) > Values.STEP_GROUP_SEPARATOR) {
				handleStepsGroup(mSteps);
				mSteps = new ArrayList<Long>();
			}
		}
		// Otherwise, the group is incomplete, and we must wait for new data
		// before we can choose
		// to push or discard it.
	}

	/**
	 * Decides what to do with a single step group. If it is significant, it is
	 * pushed to the content provider. Otherwise, it is simply discarded.
	 * 
	 * @param stepGroup
	 *            - a list of steps that constitute a single group
	 */
	public void handleStepsGroup(List<Long> stepGroup) {
		// Look at the total length of the previous step block. If it is larger
		// than STEP_GROUP_MIN_DURATION, it is worth keeping, and store it in
		// the CP.
		if ((stepGroup.get(stepGroup.size() - 1) - stepGroup.get(0)) > Values.STEP_GROUP_MIN_DURATION) {
			// Push each individual step into the content provider
			for (Long step : stepGroup) {
				pushToContentProvider(step);
			}
		}
		// Otherwise, the group is insignificant, and we can discard it.
	}

	/**
	 * Connects to the content provider to store the time stamp of a single
	 * step.
	 * 
	 * @param Step
	 *            - The time stamp of the single step.
	 */
	public void pushToContentProvider(Long step) {
		Log.v("TOCP", "PUSHHIN!");
		Uri uri = Uri
				.parse("content://ntnu.stud.valens.contentprovider/raw_steps/");
		// Define the row to insert
		ContentValues rowToInsert = new ContentValues();
		rowToInsert.put(uri + "/raw_steps/timestamp/", step);
		rowToInsert.put(uri + "/raw_steps/source/", Values.TAG);
		// Insert row, hoping that everything works as expected.
		activity.getContentResolver().insert(uri, rowToInsert);
	}
}
