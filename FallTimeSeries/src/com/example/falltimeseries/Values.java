package com.example.falltimeseries;

/**
 * Class that defines the constants used in the StepMainService and
 * DetectStepsThread.
 * 
 * @author Elias Aamot
 * 
 */
public class Values {

	// Label values, used only for identifying the service.
	public final static int MY_ID = 13377331;
	public static final String TAG = "no.ntnu.stud.valens.stepcounter";

	// Values that control the behaviour of the Step Detection Algorithm.
	// Modifying these values can improve the performance of the step
	// detector, but can also have detrimental effects, so read
	// the documentation before changing.
	public static final float STD_THRESHOLD = 0.8f;
	public static final int SMOOTHING_WINDOW = 5;
	public static final int WINDOW_SIZE = 10;
	public static final int DATA_STREAM_SIZE = 500;
	public static final long STEP_GROUP_SEPARATOR = (long) 10000.0;
	public static final long STEP_GROUP_MIN_DURATION = (long) 10000.0;

	// Derived values. Change the primitive values rather than changig these
	public static final int COMMIT_DATA_THRESHOLD = (SMOOTHING_WINDOW * 2)
			+ (WINDOW_SIZE * 2) + DATA_STREAM_SIZE;

}
