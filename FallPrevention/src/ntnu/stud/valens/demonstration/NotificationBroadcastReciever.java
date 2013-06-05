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
package ntnu.stud.valens.demonstration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationBroadcastReciever extends BroadcastReceiver {
/**
 * TODO: explain what this does I'm not really sure but it seems like it is used for testing.
 */
private final String TAG="Notification reciever";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.v(TAG, "");
		// TODO Auto-generated method stub
	//	new ContentProviderHelper(arg0).pushNotification(status.getCode());

	}

}
