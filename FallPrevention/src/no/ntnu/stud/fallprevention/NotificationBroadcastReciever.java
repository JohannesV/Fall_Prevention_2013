package no.ntnu.stud.fallprevention;

import no.ntnu.stud.fallprevention.connectivity.ContentProviderHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationBroadcastReciever extends BroadcastReceiver {

private final String TAG="Notification reciever";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.v(TAG, "");
		// TODO Auto-generated method stub
	//	new ContentProviderHelper(arg0).pushNotification(status.getCode());

	}

}