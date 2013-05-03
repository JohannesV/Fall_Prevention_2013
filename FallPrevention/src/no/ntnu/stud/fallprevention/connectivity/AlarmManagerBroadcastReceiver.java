
package no.ntnu.stud.fallprevention.connectivity;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import no.ntnu.stud.fallprevention.Constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";
    final static String TAG = "AlarmBroadcastReciever";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        Log.v(TAG, "onRecieve called from mainscreen!");
        // Acquire the lock
        wl.acquire();

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

        // You can do the processing here.
        // Bundle extras = intent.getExtras();
        // StringBuilder msgStr = new StringBuilder();
        //
        // if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
        // //Make sure this intent has been sent by the one-time timer button.
        // msgStr.append("One time Timer : ");
        // }
        // Format formatter = new SimpleDateFormat("hh:mm:ss a");
        // msgStr.append(formatter.format(new Date()));
        //
        // Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        // Release the lock
        wl.release();
    }

    public void SetAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {

        } else {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            intent.putExtra(ONE_TIME, Boolean.FALSE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            // After a short while
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    DateUtils.DAY_IN_MILLIS , pi);
        }

    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}
