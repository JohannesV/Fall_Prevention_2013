package no.ntnu.stud.fallprevention.widget;



import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.R.drawable;
import no.ntnu.stud.fallprevention.R.id;
import no.ntnu.stud.fallprevention.R.layout;
import no.ntnu.stud.fallprevention.activity.EventList;
import no.ntnu.stud.fallprevention.connectivity.DatabaseHelper;
import no.ntnu.stud.fallprevention.datastructures.RiskStatus;
/**
 * This class update the widget in a frenquency of 50 sec.
 * @author Tayfun
 */

public class WidgetUpdateService extends Service {
	
   	private final int WIDGET_UPDATE_FREQUENCY = 5000;
	
	@Override
	/**
	 * Updates the actual application context with the current application
	 * @param
	 * @return The current version of the method with current parameters.
	 */
	//TODO: Information to @param. Could not found.
	public int onStartCommand(Intent intent, int flags,int startId) {
        
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		Context context = this.getApplicationContext();
	    Timer timer = new Timer();
	    timer.schedule(new Updater(appWidgetManager, context), WIDGET_UPDATE_FREQUENCY, WIDGET_UPDATE_FREQUENCY);
	    
	    return super.onStartCommand(intent, flags, startId);
	}
	/**
	 * For the starting value
	 * @return null for making the default value
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
    /**
     * 
     * @author Tayfun
     *
     */
	private class Updater extends TimerTask {

		AppWidgetManager appWidgetManager;
		Context context;
		
		public Updater(AppWidgetManager appWidgetManager, Context context) {
			this.appWidgetManager = appWidgetManager;
			this.context = context;
		}
		
		@Override
		/**
		 * Executes the update options. Updates background for new messages and updates faces.
		 */
		public void run() {
		    ComponentName thisWidget = new ComponentName(getApplicationContext(), WidgetProvider.class);
		    // Field of all widgets to update them iterativly
		    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		    for (int widgetId : allWidgetIds) {
		    	RemoteViews views;
		    	// Change background if there are new messages
				if (new DatabaseHelper(context).dbHaveEvents()) {
					 views = new RemoteViews(context.getPackageName(), R.layout.widget_layout_w_messages);
				 }
				 else {
					 views = new RemoteViews(context.getPackageName(), R.layout.widget_layout_no_messages);
				 }
		    	
				 // Update face based on state
		    	RiskStatus status = new DatabaseHelper(context).dbGetStatus();
				Drawable drawable;
				if (status == RiskStatus.BAD_JOB) {
					drawable = context.getResources().getDrawable(R.drawable.bad_job);
				}
				else if (status == RiskStatus.NOT_SO_OK_JOB) {
					drawable = context.getResources().getDrawable(R.drawable.not_so_ok_job);
				}
				else if (status == RiskStatus.OK_JOB) {
					drawable = context.getResources().getDrawable(R.drawable.ok_job);
				}
				else if (status == RiskStatus.GOOD_JOB) {
					drawable = context.getResources().getDrawable(R.drawable.good_job);
				}
				else if (status == RiskStatus.VERY_GOOD_JOB) {
					drawable = context.getResources().getDrawable(R.drawable.very_good_job);
				}
				else {
					// Problem. This should never happen
					throw new RuntimeException("Error thrown at WidgetUpdateService.java:71");
				}
				
				Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
				
				views.setBitmap(R.id.smileyButton, "setImageBitmap", bitmap);
				 				 
				// Add onClickListener
				Intent clickIntent = new Intent(context, EventList.class);
				PendingIntent pendIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
				views.setOnClickPendingIntent(R.id.smileyButton, pendIntent);
				 
				appWidgetManager.updateAppWidget(widgetId, views);
		    }			
		}
	}
}
