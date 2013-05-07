
package no.ntnu.stud.fallprevention.widget;

import java.sql.Timestamp;
import java.util.Calendar;

import no.ntnu.stud.fallprevention.Constants;
import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.activity.EventList;
import no.ntnu.stud.fallprevention.connectivity.ContentProviderHelper;
import no.ntnu.stud.fallprevention.connectivity.DatabaseHelper;
import no.ntnu.stud.fallprevention.datastructures.RiskStatus;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * This is the AppWidgetProvider, the class that handles creation of widgets in
 * android.
 * 
 * @author Elias
 */
public class WidgetProvider extends AppWidgetProvider {

    /**
     * Updates the widgets through their widgets Ids
     * 
     * @param context : contains the current program values to use for update,
     *            appWidgetManager: instance of AppWidgetManager for accessing
     *            the widget Ids, appWidgetsIds: Field of information about the
     *            widget Ids to use.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.v("VALENSwidget", "Start of update");

        // Display text
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        int eventListCount = new DatabaseHelper(context).dbGetEventList().size();
        Log.v("VALENSwidget", "eventListCount: " + eventListCount);

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.widget_layout, null);
        TextView textView = (TextView) layout
                .findViewById(R.id.widget_notification_text);

        // Update face based on state.
        RiskStatus status = new ContentProviderHelper(context).getRiskValue();

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
                    "Error thrown at WidgetProvider.java:89");
        }

        String mTemp = context.getString(R.string.notifcation_number).replaceAll("%1",
                String.valueOf(eventListCount));
        Log.v("VALENSwidget", mTemp);
        // Check whether we should create a new notification
        // checkForPush(context);

        // Redisplay image
        Log.v("VALENSwidget", "Update");
        Log.v("VALENSwidget", "Before: " + textView.getText().toString());
        textView.setText(mTemp);
        Log.v("VALENSwidget", "After: " + textView.getText().toString());

        views.setTextViewText(R.id.widget_notification_text, mTemp);
        Log.v("VALENSwidget", "Update of text");
        // drawable = context.getResources().getDrawable(
        // R.drawable.very_good_job);
        Bitmap bitmap = scaleDownBitmap(((BitmapDrawable) drawable).getBitmap(), 100, context);
        views.setBitmap(R.id.smiley, "setImageBitmap", bitmap);
        Log.v("VALENSwidget", "Generated bitmap");
        // Add onClick listener
        Log.v("VALENSwidget", "Generating intent");

        Intent clickIntent = new Intent(context, EventList.class);
        PendingIntent pendIntent = PendingIntent.getActivity(context,
                0, clickIntent, 0);
        views.setOnClickPendingIntent(R.id.smiley, pendIntent);
        Log.v("VALENSwidget", "Generated intent");

        // Display changes
        Log.v("VALENSwidget", "Attempting to update widget.");
//        Log.v("VALENSwidget",
//                java.util.Arrays.toString(appWidgetIds) + ", "
//                        + java.util.Arrays.toString(appWidgetManager.getAppWidgetIds(provider)));
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        Log.v("VALENSwidget", "Widget updated!");
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    /**
     * Checks if it is time to push the daily notification to the database.
     */
    @SuppressLint("NewApi")
    private void checkForPush(Context context) {
        Log.v("Main Screen", "Checking for push time");
        // Fetch current time and time stored in file
        Calendar current = Calendar.getInstance();
        Calendar last = Calendar.getInstance();

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Long lastTimestamp = sp.getLong("lastPushed", 0l);
        last.setTimeInMillis(lastTimestamp);

        if (DateUtils.DAY_IN_MILLIS < (current.getTimeInMillis() - last
                .getTimeInMillis())) {
            // More than a day has passed since the last message, so we'll make
            // a new one. To do so, we need to know the step count yesterday and
            // the day before that. So we first need to establish the
            // time stamps to find step count between..
            Calendar thisMorningC = Calendar.getInstance();
            thisMorningC.set(Calendar.HOUR, 5);
            thisMorningC.set(Calendar.SECOND, 0);
            thisMorningC.set(Calendar.MINUTE, 0);
            long thisMorning = thisMorningC.getTimeInMillis();
            long yesterday = thisMorning - DateUtils.DAY_IN_MILLIS;
            long dayBeforeYesterday = yesterday - DateUtils.DAY_IN_MILLIS;
            Timestamp yesterdayTs = new Timestamp(yesterday);
            Timestamp todayTs = new Timestamp(thisMorning);
            Timestamp dayBeforeTs = new Timestamp(dayBeforeYesterday);
            // Get step counts
            ContentProviderHelper cph = new ContentProviderHelper(context);
            int yesterdaySteps = cph.getStepCount(yesterdayTs, todayTs);
            int dayBeforeSteps = cph.getStepCount(dayBeforeTs, todayTs);
            // Finally, generate a message to be stored in the local DB
            DatabaseHelper dbh = new DatabaseHelper(context);
            if ((double) yesterdaySteps / (double) dayBeforeSteps < Constants.BAD_STEP_CHANGE) {
                dbh.dbAddEvent(1, yesterdaySteps, dayBeforeSteps);
            } else if ((double) yesterdaySteps / (double) dayBeforeSteps > Constants.GOOD_STEP_CHANGE) {
                dbh.dbAddEvent(0, yesterdaySteps, dayBeforeSteps);
            } else {
                dbh.dbAddEvent(2, yesterdaySteps, dayBeforeSteps);
            }
            // Record that the message that was created corresponds to the last
            // 24 hours before 5 this morning.
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("lastPushed", thisMorning);
            editor.commit();
        }
    }
}
