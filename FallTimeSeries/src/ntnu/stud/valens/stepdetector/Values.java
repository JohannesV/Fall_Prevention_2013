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
	
	// Values used for calibration
	public static final int CAL_TIME = 20000;
	public static final int CAL_WAIT = 10000;

	// Values that control the behaviour of the Step Detection Algorithm.
	// Modifying these values can improve the performance of the step
	// detector, but can also have detrimental effects, so read
	// the documentation before changing.
	public static final float STD_THRESHOLD = 0.8f;
	public static final int SMOOTHING_WINDOW = 5;
	public static final int PEAK_STRENGTH_WINDOW = 10;
	public static final int DATA_STREAM_SIZE = 500;
	public static final long STEP_GROUP_SEPARATOR = (long) 10000.0;
	public static final long STEP_GROUP_MIN_DURATION = (long) 10000.0;

	// Derived values. Change the primitive values rather than changig these
	public static final int COMMIT_DATA_THRESHOLD = (SMOOTHING_WINDOW * 2)
			+ (PEAK_STRENGTH_WINDOW * 2) + DATA_STREAM_SIZE;

}
