package no.ntnu.stud.fallprevention;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * This class calls the the method onUpdate to update the widgets
 * @author Tayfun
 *
 */
public class WidgetProvider extends AppWidgetProvider {
	
	/**
	 * Updates the widgets through their widgets Ids
	 * @param context: contains the current program values to use for update, 
	 *        appWidgetManager: instance of AppWidgetManager for accessing the widget Ids, 
	 *        appWidgetsIds: Field of information about the widget Ids to use.
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		
		//declarates a new instance of the actual content has to been updated
		Intent intent = new Intent(context.getApplicationContext(), WidgetUpdateService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		// starts the update on the intent has to been updated
		context.startService(intent);
	}
} 