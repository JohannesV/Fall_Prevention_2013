package com.example.falltimeseries;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Class that maintains a list of steps for the applications. Groups the steps, and handles the
 * groups according to length: Discard the small ones and push the long ones to the CP.
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
	 * Adds newly calculated steps to the steps list. The method will figure out what to do with
	 * the steps, either discarding them, pushing them to the content provider, or retaining them
	 * until new steps appear.
	 * 
	 * @param The steps to store
	 */
	public void addSteps(List<Long> newSteps) {
		// Add steps to the list
		for (Long step : newSteps) {
			mSteps.add(step);
		}
		// Go through the list, looking for gaps larger than STEP_GROUP_SEPARATOR,
		// as this signals the end of a group of steps. 
		int i = 1;
		while (i < mSteps.size()) {
			// If the gap is sufficiently large to imply that step[i] belongs to another group...
			if ((mSteps.get(i) - mSteps.get(i-1)) > Values.STEP_GROUP_SEPARATOR) {
				// Look at the total length of the previous step block. If it is larger than
				// STEP_GROUP_MIN_DURATION, it is worth keeping, and store it in the CP.
				if ((mSteps.get(i-1) - mSteps.get(0)) > Values.STEP_GROUP_MIN_DURATION) {
					// Push each individual step into the content provider
					for (int k = 0; k < i; k++) {
						pushToContentProvider(mSteps.get(k));
					}
				}
				// Anyway, remove the last step group; either it has been stored already, or it 
				// has been deemed worthy of being discarded. Start again in a new step list
				// beginning at step[i].
				List<Long> uncheckedSteps = new ArrayList<Long>();
				for (int j = i; j < mSteps.size(); j++) {
					uncheckedSteps.add(mSteps.get(j));
				}
				mSteps = uncheckedSteps;
				i = 0;
			}
			// If the gap is small, then the new step is a part of the old group. Keep looking.
			else {
				i++;
			}
		}
		// We have run through the list. The last steps constitute a group, but we still don't know
		// whether it has been finished or not, so we'll keep these steps stores until addSteps is
		// called again.
	}
	
	/**
	 * Connects to the content provider to store the time stamp of a single step.
	 * 
	 * @param Step - The time stamp of the single step.
	 */
	public void pushToContentProvider(Long step) {
		Uri uri = Uri.parse("content://ntnu.stud.valens.contentprovider/raw_steps/");
		// Define the row to insert
		ContentValues rowToInsert = new ContentValues();
		rowToInsert.put(uri+"/raw_steps/timestamp/", step);
		rowToInsert.put(uri+"/raw_steps/source/", Values.TAG);
		// Insert row, hoping that everything works as expected.
		activity.getContentResolver().insert(uri,rowToInsert);
	}
}
