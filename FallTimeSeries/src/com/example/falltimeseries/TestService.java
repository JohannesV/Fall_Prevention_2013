package com.example.falltimeseries;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service {

	final static int myID = 13377331;
	
	public void onCreate() {
		Log.v("Service", "OnCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("Service","StartCommand");
		
		if (!intent.getBooleanExtra("stop", false)) {
			//The intent to launch when the user clicks the expanded notification
			Intent intento = new Intent(this, LaunchActivity.class);
			intento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intento, 0);
	
			//This constructor is deprecated. Use Notification.Builder instead
			Notification notice = new Notification(R.drawable.ic_launcher, "Ticker text", System.currentTimeMillis());
	
			//This method is deprecated. Use Notification.Builder instead.
			notice.setLatestEventInfo(this, "Title text", "Content text", pendIntent);
	
			notice.flags |= Notification.FLAG_NO_CLEAR;
			startForeground(myID, notice);
			
			return Service.START_NOT_STICKY;
		} else {
			stopForeground(true);
			return Service.START_NOT_STICKY;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}