/*******************************************************************************
 * Licensed to UbiCollab.org under one or more contributor
 * license agreements.  See the NOTICE file distributed 
 * with this work for additional information regarding
 * copyright ownership. UbiCollab.org licenses this file
 * to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package ntnu.stud.valens.demonstration.connectivity;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ntnu.stud.valens.demonstration.Constants;

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
        int dayBeforeSteps = cph.getStepCount(dayBeforeTs, yesterdayTs);
        
        //Get gait speed and variability
        double mGaitVariability=cph.getGaitVariability(yesterdayTs, todayTs);
        double mGaitSpeed= cph.getGaitSpeed(yesterdayTs, todayTs);
        
        // Generate a message to be stored in the local DB
        DatabaseHelper dbh = new DatabaseHelper(context);
        if ((double) yesterdaySteps / (double) dayBeforeSteps < Constants.BAD_STEP_CHANGE) {
            dbh.dbAddEvent(1, yesterdaySteps, dayBeforeSteps);
        } else if ((double) yesterdaySteps / (double) dayBeforeSteps > Constants.GOOD_STEP_CHANGE) {
            dbh.dbAddEvent(0, yesterdaySteps, dayBeforeSteps);
        } else {
            dbh.dbAddEvent(2, yesterdaySteps, dayBeforeSteps);
        }
        
        // In case of critical gait parameters push a warning message
        if (mGaitSpeed > Constants.BAD_SPEED_NUMBER || mGaitVariability > Constants.BAD_VARI_NUMBER) {
            dbh.dbAddEvent(3, 0, 0);
        }
        
        // Release the lock
        wl.release();
    }

    public void SetAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(Calendar.HOUR_OF_DAY, 05);
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
