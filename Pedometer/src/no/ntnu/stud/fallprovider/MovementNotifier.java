package no.ntnu.stud.fallprovider;

import java.util.Date;

import android.content.Context;

public class MovementNotifier implements StepListener {
	
	private static final String TAG = "no.ntnu.stud.fallprovider.MovementNotifier";
	
	private double mDistance = 0;
	private int mSteps = 0;
	private Date mTimeStart = now();
	private PedometerSettings mSettings;
	private Utils mUtils;
	private Context mContext;
	public static int TEN_MINUTES =  600;
	
	public MovementNotifier(PedometerSettings settings, Utils utils, Context context) {
        mUtils = utils;
        mSettings = settings;
        mContext = context;
        mTimeStart = now();
    }
	
	public void onStep() {
    	if(timeSpanLessThan(TEN_MINUTES)){
    		mSteps++;
    		mDistance = mSteps * mSettings.getStepLength();
    	}
    	else{
    		new DatabaseHelper(mContext).AddMovement(mSteps, mDistance, mTimeStart, now());
    		mSteps = 0;
    		mDistance = 0;
    		mTimeStart = now();
    	}
    }
    
    public void passValue() {
    	// Does nothing!
    }
    
    private long getUnixTimeStamp(Date date){
    	try{
    		return date.getTime() / 1000;
    	}
    	catch (Exception e) {
    		return 0;
		}
    }
    
    private Date now(){
    	return new Date();
    }
    
    private boolean timeSpanLessThan(int timespan){
    	return getUnixTimeStamp(now()) - getUnixTimeStamp(mTimeStart) > timespan;
    }
}
