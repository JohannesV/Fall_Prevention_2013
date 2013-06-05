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
package ntnu.stud.valens.stepdetector.calibration;

import java.util.TimerTask;

/**
 * The main purpose of the CalibrationStartTask is to exist as a TimerTask.
 * Because Java requires that the function to be called by a Timer be
 * encapsulated in a TimerTask, we had to write a separate class for this.
 * 
 * The class exists so that the timer in CalibrationActivity can call run() it
 * at the end of a count down. The run() method plays a sound and then finally
 * starts calibration.
 * 
 * @author Elias
 * 
 */
public class CalibrationStartTask extends TimerTask {

	private CalibrationActivity c;

	/**
	 * Constructor.
	 * 
	 * @param c
	 *            - The CalibrationActivity that the Timer belongs to. This is
	 *            need to use the playSound() function in the
	 *            CalibrationActivity during run().
	 */
	public CalibrationStartTask(CalibrationActivity c) {
		this.c = c;
	}

	/**
	 * Called automatically by the system at the end of a Timer, if this
	 * TimerTask is associated to the Timer.
	 */
	@Override
	public void run() {
		// Play sound
		c.playSound();
		// Start getting input to calibrate
		CalibrationThread ct = new CalibrationThread(c);
		ct.run();
	}

}
