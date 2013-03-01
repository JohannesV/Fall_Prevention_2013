package no.ntnu.stud.fallprevention;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider {
	
	private final String TAG = "WIDGET_PROVIDER";
	private int counter = 0;
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
	    // Chain up to the super class so the onEnabled, etc callbacks get dispatched
	    super.onReceive(context, intent);
	    // Handle a different Intent
	    Log.d(TAG, "onReceive()" + intent.getAction());

	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		counter++;
		Drawable drawable;
		RiskStatus status = new DatabaseHelper(context).dbGetStatus();
		//if (status == RiskStatus.BAD_JOB) {
		if (counter == 1) {
			drawable = context.getResources().getDrawable(R.drawable.bad_job);
		}
		else if (counter > 1) {
		//else if (status == RiskStatus.NOT_SO_OK_JOB) {
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
			// Problem
			drawable = null;
		}
		
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
		
		// get the handle on your widget
		 RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		 // replace the image
		 views.setBitmap(R.id.smileyButton, "setImageBitmap", bitmap);
		 appWidgetManager.updateAppWidget(appWidgetIds, views);
		 
		 // ADD A LISTENER
		 Intent intent = new Intent(context, EventList.class);
		 PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent, 0);
		 views.setOnClickPendingIntent(R.id.smileyButton, pendIntent);
		 
		 appWidgetManager.updateAppWidget(appWidgetIds, views);		 		
	}
} 