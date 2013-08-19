/*************************************************************************
 * Copyright 2010 Jules White
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/
 * LICENSE-2.0 Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions
 * and limitations under the License.
 **************************************************************************/

package org.vt.smssec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	private static final String ENABLE_SMS_SEC = "EnableSMSSec";
	private static final String CMD = "cmd:";
	private static final String MESSAGES = "pdus";


	@Override
	public void onReceive(Context context, Intent intent) {

		Log.e("SMSSec", "Recv'd msg");
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		boolean enabled = prefs.getBoolean(ENABLE_SMS_SEC, true);

		if (!enabled){
			Log.e("SMSSec", "Not enabled");
			return;
		}

		// make sure the sms app doesn't get the message
		// and create a notification that the thief might
		// see
		Log.e("SMSSec", "Aborting broadcast...");
		this.abortBroadcast();

		// get the data associated with the intent
		Bundle bundle = intent.getExtras();

		// extract the list of sms messages from the data
		Object messages[] = (Object[]) bundle.get(MESSAGES);
		List<String> cmds = new ArrayList<String>();

		// iterate through the sms messages and look for any
		// commands that need to be executed
		for (int i = 0; i < messages.length; i++) {
			SmsMessage msg = SmsMessage.createFromPdu((byte[]) messages[i]);

			// do some checking to make sure that the sender has
			// permission to execute commands
			if (isPrivelegedNumber(msg.getOriginatingAddress())) {
				String body = msg.getMessageBody();

				Log.e("SMSSec", "body:"+body);
				// finally extract and add the command from the sms
				// body to the list of commands
				if (isCommand(body)) {
					String cmd = getCommand(body);
					
					Log.e("SMSSec", "It's a command!");
					cmds.add(cmd);
				}
				else {
					Log.e("SMSSec", "Looks like a normal sms...");
				}
			}
			else {
				Log.e("SMSSec", "Attempted access from unprivileged number");
			}

		}

		// if we didn't find any commands, we need
		// to cancel the abort so that the sms app
		// receives the message and the user (thief)
		// doesn't know that we are silently monitoring
		// sms messages
		if (cmds.size() == 0) {
			this.clearAbortBroadcast();
		} else {
			// ok, we got some commands that we need
			// to execute!

			for (String cmd : cmds) {

				if (cmd.startsWith("photo")) {
					PhotoCommand snap = new PhotoCommand(context);
					snap.speak();
				} else if (cmd.startsWith("say") || cmd.startsWith("taunt")) {

					int start = cmd.indexOf("\"");
					int end = cmd.indexOf("\"", start + 1);

					String msg = cmd.substring(start + 1, end);

					if (cmd.startsWith("say")) {
						TalkCommand talk = new TalkCommand(context, msg);
						talk.speak();
						
					} else {
						TauntCommand taunt = new TauntCommand(context, msg, Toast.LENGTH_LONG);
						taunt.sendTaunt();
					}
				}
			}

		}

	}

	private boolean isCommand(String msg) {
		return ("" + msg).startsWith(CMD);
	}

	private String getCommand(String msg) {
		return ("" + msg).substring(CMD.length());
	}

	private boolean isPrivelegedNumber(String num) {

		if (num.equals("5555555555")) {
			return true;
		}
		if (num.equals("5555555556")) {
			return true;
		}

		return false;
	}

}