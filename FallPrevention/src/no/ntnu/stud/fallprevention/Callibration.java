package no.ntnu.stud.fallprevention;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.SoundEffectConstants;

public class Callibration {
	public static final int CAL_TIME = 30000;
	public static final int WAIT_TIME = 10000;
	
	public void callibrate(final Context context){
		Thread t = new Thread(){		
			public void run(){
				wait(WAIT_TIME);
				MediaPlayer player = null;
			    player = MediaPlayer.create(context,R.Fall_Prevention_2013.beep);
			    player.start();
			    // calling the callibration app. For that we need to know if we shall access to the callibration app on the phones or to add a standard app
			    Intent intent = new Intent(Intent.ACTION_MAIN);     
			    intent.setClassName("com.example.package", "com.example.package.ActivityToStart");
			    startActivity(intent);
			    DevicePolicyManager lockingScreen;  // maybe in addition constructor = new DevicePolicyManager() 
			    lockingScreen.setMaximumTimeToLock(null, CAL_TIME)
			}t.start();
		}
	}
}
