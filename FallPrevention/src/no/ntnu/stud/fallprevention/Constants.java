package no.ntnu.stud.fallprevention;

/**
 * Contains constants used elsewhere in the application
 * 
 * @author Johannes
 * 
 */
public class Constants {
	/**
	 * Definition of what is a significant change in step count, measures as the ratio of STEPS_NOW / STEPS_BEFORE
	 */
	public static final double GOOD_STEP_CHANGE = 1.10;
	public static final double BAD_STEP_CHANGE = 0.9;
	/**
	 * Definition of what is a good amount of steps regardless of what has been
	 * done before
	 */
	public static final int GOOD_STEPS_NUMBER = 3000;
	
    public static final double GOOD_SPEED_NUMBER = 1500;
    public static final double BAD_SPEED_NUMBER=3000;
    
    public static final double BAD_VARI_NUMBER=3000;
    public static final double GOOD_VARI_NUMBER=1000;
}
