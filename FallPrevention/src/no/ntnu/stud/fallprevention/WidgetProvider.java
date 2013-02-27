package no.ntnu.stud.fallprevention;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	private int counter = 0;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Drawable drawable;
		RiskStatus status = new DatabaseHelper(context).dbGetStatus();
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
			// Problem
			drawable = null;
		}
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 200, 200, true));
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);	
		ImageButton imageButton = (ImageButton) views.findViewById(R.id.mainScreenSmileyImage);
		imageButton.setBackgroundDrawable(d);
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);	
		views.setTextViewText(R.id.update, Integer.toString(counter));
        appWidgetManager.updateAppWidget(appWidgetIds, views);
	}
} 