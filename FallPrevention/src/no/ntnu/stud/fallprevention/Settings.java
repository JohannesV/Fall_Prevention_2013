package no.ntnu.stud.fallprevention;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Settings extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final Button button = (Button) findViewById(R.id.nameButton); // for changing name
		
	}
	
	
	
	public void selfDestruct(View P){
		Intent intent = new Intent(this, WriteName.class);
		startActivity(intent);
		// for changing name
	}
	
	//////////
	// Alarm 
	//////////
	
	// For setting the Level
	public void setLevel(View P){
		Intent intent = new Intent(this, SetLevel.class);
		startActivity(intent);
	}
	
	// For receiving the alarm class
	
	public class AlarmReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) 
		{   //Build pending intent from calling information to display Notification
		    PendingIntent Sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		    NotificationManager manager = (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		    Notification noti = new Notification(android.R.drawable.stat_notify_more, "LEVEL alarm", System.currentTimeMillis());
		    noti.setLatestEventInfo(context, "My Alarm", "OVER LEVEL..!!!", Sender);
		    noti.flags = Notification.FLAG_AUTO_CANCEL;
		    manager.notify(R.string.app_name, noti); 

		    //intent to call the activity which shows on ringing
		    Intent myIntent = new Intent(context, Alarmring.class);
		    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    context.startActivity(myIntent);

		    //display that alarm is ringing
		    Toast.makeText(context, "Alarm Ringing...!!!", Toast.LENGTH_LONG).show();
		}
	}
	
	
	//creating and assigning value to alarm manager class
    Intent AlarmIntent = new Intent(MainActivity.this, AlarmReciever.class);
    AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
    PendingIntent Sender = PendingIntent.getBroadcast(MainActivity.this, 0, AlarmIntent, 0);    
    AlmMgr.set(AlarmManager.RTC_WAKEUP, Alarm.getTimeInMillis(), Sender);
	
	public class OnBootReceiver extends BroadcastReceiver {
		public int wait=3000;  // 3 seconds
		  public void onReceive(Context context, Intent intent) {
			  AlarmManager mgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			  Intent i=new Intent(context, OnAlarmReceiver.class);
			  PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
			  
			  int setlevel = SetLevel.level;
			  int risklevel = 10; 
			if(setlevel > risklevel){
				  mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+wait, wait, pi);
			  }
		}
	}
		

	//////////
	//Clear History
	//////////
	
	
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
*/
}
