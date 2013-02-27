package no.ntnu.stud.fallprevention;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	private int counter = 0;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		counter++;
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);	
		views.setTextViewText(R.id.update, Integer.toString(counter));
        appWidgetManager.updateAppWidget(appWidgetIds, views);
	}
} 