package no.ntnu.stud.fallprevention.widget;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.activity.EventList;
import no.ntnu.stud.fallprevention.connectivity.ContentProviderHelper;
import no.ntnu.stud.fallprevention.connectivity.DatabaseHelper;
import no.ntnu.stud.fallprevention.datastructures.RiskStatus;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * This is the AppWidgetProvider, the class that handles creation of widgets in
 * android.
 * 
 * @author Elias
 * 
 */
public class WidgetProvider extends AppWidgetProvider {


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
		// Display text
		
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout_no_messages);
        
        int i = new DatabaseHelper(context).dbGetEventList().size();
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.widget_layout_no_messages, null);
		TextView textView = (TextView) layout
				.findViewById(R.id.textView1);
		
		// Update face based on state
		RiskStatus status = new ContentProviderHelper(context)
				.cpGetStatus(null);
		Drawable drawable;
		if (status == RiskStatus.BAD_JOB) {
			drawable = context.getResources().getDrawable(
					R.drawable.bad_job);
		} else if (status == RiskStatus.NOT_SO_OK_JOB) {
			drawable = context.getResources().getDrawable(
					R.drawable.not_so_ok_job);
		} else if (status == RiskStatus.OK_JOB) {
			drawable = context.getResources().getDrawable(
					R.drawable.ok_job);
		} else if (status == RiskStatus.GOOD_JOB) {
			drawable = context.getResources().getDrawable(
					R.drawable.good_job);
		} else if (status == RiskStatus.VERY_GOOD_JOB) {
			drawable = context.getResources().getDrawable(
					R.drawable.very_good_job);
		} else {
			// Problem. This should never happen
			throw new RuntimeException(
					"Error thrown at WidgetProvider.java:76");
		}
		
		String mTemp=textView.getText().toString().replaceAll("[0-9]+", String.valueOf(i));
		
        views.setTextViewText(R.id.textView1, mTemp);
        
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//		bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
//		Bitmap b = decodeScaledFile(f);
		final float scale = context.getResources().getDisplayMetrics().density;
		int p = (int) (110 * scale + 0.5f);

		Bitmap b2 = Bitmap.createScaledBitmap(bitmap, p, p, true);
		
		views.setBitmap(R.id.smileyButton, "setImageBitmap", b2);
        
        // Add onClick listener
		Intent clickIntent = new Intent(context, EventList.class);
		PendingIntent pendIntent = PendingIntent.getActivity(context,
				0, clickIntent, 0);
		views.setOnClickPendingIntent(R.id.smileyButton, pendIntent);
		// Display changes
        appWidgetManager.updateAppWidget(appWidgetIds, views);
	}
}