package no.ntnu.stud.fallprevention.widget;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.activity.EventList;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * This is the AppWidgetProvider, the class that handles creation of widgets in
 * android.
 * 
 * @author Elias
 * 
 */
public class WidgetProvider extends AppWidgetProvider {

	private int counter;

	/**
	 * Updates the widgets through their widgets Ids
	 * 
	 * @param context
	 *            : contains the current program values to use for update,
	 *            appWidgetManager: instance of AppWidgetManager for accessing
	 *            the widget Ids, appWidgetsIds: Field of information about the
	 *            widget Ids to use.
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
		counter = (int) (Math.random() * 100);
		// Display text
		Toast.makeText(context, "Counter: " + counter, Toast.LENGTH_LONG)
				.show();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout_no_messages);
        views.setTextViewText(R.id.textView1, "Counter: " + counter);
        // Add onClick listener
		Intent clickIntent = new Intent(context, EventList.class);
		PendingIntent pendIntent = PendingIntent.getActivity(context,
				0, clickIntent, 0);
		views.setOnClickPendingIntent(R.id.smileyButton, pendIntent);
		// Display changes
        appWidgetManager.updateAppWidget(appWidgetIds, views);
	}
}