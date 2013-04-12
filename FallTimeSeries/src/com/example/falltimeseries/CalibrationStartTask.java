package com.example.falltimeseries;

import java.util.TimerTask;

public class CalibrationStartTask extends TimerTask {

	private CalibrationActivity c;
	
	public CalibrationStartTask(CalibrationActivity c) {
		this.c = c;
	}
	
	@Override
	public void run() {
		// Play sound
		c.playSound();
		// Start getting input to calibrate
		CalibrationThread ct = new CalibrationThread(c);
		ct.run();
	}

}
