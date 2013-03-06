package no.ntnu.stud.fallservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FallService extends Service {

	private static final String TAG = "no.ntnu.stud.fallservice";

	private NotificationManager mNM;

	@Override
	public void onCreate() {
		Log.i(TAG, "[SERVICE] onCreate");
		super.onCreate();

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO do something useful
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO for communication return IBinder implementation
		return null;
	}

	/**
	 * Show a notification while this service is running.
	 */
	@SuppressWarnings("deprecation")
	private void showNotification() {
		CharSequence text = getText(R.string.app_name);
		Notification notification = new Notification();
		notification.flags = Notification.FLAG_NO_CLEAR
				| Notification.FLAG_ONGOING_EVENT;
		notification.setLatestEventInfo(this, text,
				getText(R.string.notification_subtitle), null);

		mNM.notify(R.string.app_name, notification);
	}
}
