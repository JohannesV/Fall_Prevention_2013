package no.ntnu.stud.fallprevention.widget;

import no.ntnu.stud.fallprevention.R;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
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

	private int counter = 0;

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
		counter++;
		Toast.makeText(context, "Counter: " + counter, Toast.LENGTH_LONG)
				.show();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout_no_messages);
        views.setTextViewText(R.id.textView1, "Counter: " + counter);
        appWidgetManager.updateAppWidget(appWidgetIds, views);

		// declarates a new instance of the actual content has to been updated
		// Intent intent = new Intent(context.getApplicationContext(),
		// WidgetUpdateService.class);
		// intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		// starts the update on the intent has to been updated
		// Log.v("Widget", "Starting the service");
		// context.startService(intent);
	}
}