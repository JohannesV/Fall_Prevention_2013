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
